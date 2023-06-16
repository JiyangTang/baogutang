package top.baogutang.admin.schedule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zjiecode.wxpusher.client.bean.Message;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.baogutang.admin.domain.EdgeXAnnouncementsDto;
import top.baogutang.admin.services.IWxMsgPushService;
import top.baogutang.common.domain.Page;
import top.baogutang.common.domain.Results;
import top.baogutang.common.properties.WxMsgPushProperties;
import top.baogutang.common.utils.HttpUtils;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @description: EDGEX公告
 * @author: nikooh
 * @date: 2023/06/16 : 11:41
 */
@Slf4j
@Component
@RefreshScope
public class EdgeXNoticeSchedule {

    @Resource
    private IWxMsgPushService wxMsgPushService;

    @Resource
    private WxMsgPushProperties wxMsgPushProperties;

    /**
     * 每分钟查询edgeX公告
     */
    @Scheduled(cron = "0 0/1 * * * ? ")
    public void edgeXNotice() {
        Results<Page<EdgeXAnnouncementsDto>> results = HttpUtils.get("https://art-api.edge-x.cn/api/v1/art/announcements?pageNum=1&pageSize=1", new TypeReference<Results<Page<EdgeXAnnouncementsDto>>>() {
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
        EdgeXAnnouncementsDto announcementsDto = results.getData().getList().get(0);
        if (Math.abs(announcementsDto.getCreatedAt().getTime() - System.currentTimeMillis()) <= 60000) {
            wxMsgPushService.msgPush(Message.CONTENT_TYPE_HTML, announcementsDto.getTitle(), announcementsDto.getContent(), wxMsgPushProperties.getTopicIds());
        }
    }
}
