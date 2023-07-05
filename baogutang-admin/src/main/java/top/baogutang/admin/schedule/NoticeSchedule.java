package top.baogutang.admin.schedule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.baogutang.admin.domain.AnnouncementsDto;
import top.baogutang.admin.services.IWxMsgPushService;
import top.baogutang.admin.utils.DingTalkMsgPushUtils;
import top.baogutang.common.domain.Page;
import top.baogutang.common.domain.Results;
import top.baogutang.common.properties.WxMsgPushProperties;
import top.baogutang.common.utils.OkHttpUtil;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static top.baogutang.common.constants.CacheConstant.MSG_PUSH_PREFIX_KEY;

/**
 * @description: EDGEX公告
 * @author: nikooh
 * @date: 2023/06/16 : 11:41
 */
@Slf4j
@Component
@RefreshScope
public class NoticeSchedule {

    @Resource
    private IWxMsgPushService wxMsgPushService;

    @Resource
    private WxMsgPushProperties wxMsgPushProperties;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private DingTalkMsgPushUtils dingTalkMsgPushUtils;

    @Value("${dingtalk.baogutang.agentId}")
    private String agentId;

    private static final String EDGE_X_REQUEST_URL = "https://art-api.edge-x.cn/api/v1/art/announcements?pageNum=1&pageSize=1";
    private static final String EDGE_REQUEST_URL = "https://art-api.heishiapp.com/api/v1/art/announcements?pageNum=1&pageSize=1";

    /**
     * 每分钟查询edgeX公告
     */
    @Scheduled(cron = "0 0/1 * * * ? ")
    public void edgeXNotice() {
        Results<Page<AnnouncementsDto>> results = OkHttpUtil.get(EDGE_X_REQUEST_URL, null, null, new TypeReference<Results<Page<AnnouncementsDto>>>() {
        });
        log.info(">>>>>>>>>>请求获取edgeX公告返回数据：{}<<<<<<<<<<", JSON.toJSONString(results));
        if (Objects.isNull(results)) {
            return;
        }
        if (!Boolean.TRUE.equals(results.isSuccess())) {
            return;
        }
        if (Objects.isNull(results.getData()) || CollectionUtils.isEmpty(results.getData().getList())) {
            return;
        }
        AnnouncementsDto announcementsDto = results.getData().getList().get(0);
        String cacheKey = String.format(MSG_PUSH_PREFIX_KEY, "edge_x", announcementsDto.getId());

        Boolean result = redisTemplate.opsForValue().setIfAbsent(cacheKey, 1, 5, TimeUnit.DAYS);
        if (Boolean.TRUE.equals(result)) {
            String content = "# EDGE-X-" + announcementsDto.getTitle() + "\n\n![](" + announcementsDto.getCover() + ")\n\n> 点击下方链接查看更多详情：\n\n[查看详情](" + "https://app.edge-x.cn/#/noticeDetail?noticeId=" + announcementsDto.getId() + ")";
            dingTalkMsgPushUtils.robotMarkdownMsgPush(announcementsDto.getTitle(), content);
//            wxMsgPushService.msgPush(Message.CONTENT_TYPE_HTML, announcementsDto.getTitle(), announcementsDto.getContent(), wxMsgPushProperties.getTopicIds());
        }
    }

    /**
     * 每分钟查询edge公告
     */
    @Scheduled(cron = "0 0/1 * * * ? ")
    public void edgeNotice() {
        Results<Page<AnnouncementsDto>> results = OkHttpUtil.get(EDGE_REQUEST_URL, null, null, new TypeReference<Results<Page<AnnouncementsDto>>>() {
        });

        log.info(">>>>>>>>>>请求获取edge公告返回数据：{}<<<<<<<<<<", JSON.toJSONString(results));
        if (Objects.isNull(results)) {
            return;
        }
        if (!Boolean.TRUE.equals(results.isSuccess())) {
            return;
        }
        if (Objects.isNull(results.getData()) || CollectionUtils.isEmpty(results.getData().getList())) {
            return;
        }
        AnnouncementsDto announcementsDto = results.getData().getList().get(0);
        String cacheKey = String.format(MSG_PUSH_PREFIX_KEY, "edge", announcementsDto.getId());

        Boolean result = redisTemplate.opsForValue().setIfAbsent(cacheKey, 1, 5, TimeUnit.DAYS);
        if (Boolean.TRUE.equals(result)) {
            String content = "# EDGE-" + announcementsDto.getTitle() + "\n\n![](" + announcementsDto.getCover() + ")\n\n> 点击下方链接查看更多详情：\n\n[查看详情](" + "https://activities-h5.heishiapp.com/#/noticeDetail?noticeId=" + announcementsDto.getId() + ")";
            dingTalkMsgPushUtils.robotMarkdownMsgPush(announcementsDto.getTitle(), content);
//            wxMsgPushService.msgPush(Message.CONTENT_TYPE_HTML, announcementsDto.getTitle(), announcementsDto.getContent(), wxMsgPushProperties.getTopicIds());
        }
    }

}
