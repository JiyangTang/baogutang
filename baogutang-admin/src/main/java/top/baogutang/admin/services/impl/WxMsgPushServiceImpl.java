package top.baogutang.admin.services.impl;

import com.alibaba.fastjson.JSON;
import com.zjiecode.wxpusher.client.WxPusher;
import com.zjiecode.wxpusher.client.bean.Message;
import com.zjiecode.wxpusher.client.bean.MessageResult;
import com.zjiecode.wxpusher.client.bean.Result;
import com.zjiecode.wxpusher.client.bean.WxUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import top.baogutang.admin.services.IWxMsgPushService;
import com.zjiecode.wxpusher.client.bean.Page;
import top.baogutang.common.exceptions.BusinessException;
import top.baogutang.common.properties.WxMsgPushProperties;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: nikooh
 * @date: 2023/06/16 : 10:27
 */
@Slf4j
@RefreshScope
@Service
public class WxMsgPushServiceImpl implements IWxMsgPushService {

    @Resource
    private WxMsgPushProperties wxMsgPushProperties;

    @Override
    public Boolean msgPush(Integer msgType, String summary, String msgContent, Set<Long> topicIdSet) {
        Message message = new Message();
        Set<String> userUidSet = this.queryUids(wxMsgPushProperties.getAppToken());
        if (CollectionUtils.isEmpty(userUidSet)) {
            return Boolean.TRUE;
        }
        message.setUids(userUidSet);
        message.setAppToken(wxMsgPushProperties.getAppToken());
        message.setSummary(summary);
        message.setContentType(msgType);
        message.setContent(msgContent);
        Result<List<MessageResult>> result = null;
        try {
            result = WxPusher.send(message);
        } catch (Exception e) {
            log.error(">>>>>>>>>>推送消息异常：{}<<<<<<<<<<", e.getMessage(), e);
            return Boolean.FALSE;
        }
        log.info(">>>>>>>>>>消息推送结果：{}<<<<<<<<<<", JSON.toJSONString(result));
        if (Objects.nonNull(result) && result.isSuccess()) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private Set<String> queryUids(String appToken) {
        //分页查询全部用户
        Result<Page<WxUser>> wxUsersResult = null;
        try {
            wxUsersResult = WxPusher.queryWxUser(appToken, 1, Integer.MAX_VALUE);
        } catch (Exception e) {
            log.error(">>>>>>>>>>查询用户信息失败：{}<<<<<<<<<<", e.getMessage(), e);
            throw new BusinessException("查询用户信息失败");
        }
        log.info(">>>>>>>>>>查询用户列表结果：{}<<<<<<<<<<", JSON.toJSONString(wxUsersResult));
        if (Objects.nonNull(wxUsersResult) && Objects.nonNull(wxUsersResult.getData()) && CollectionUtils.isNotEmpty(wxUsersResult.getData().getRecords())) {
            return wxUsersResult.getData().getRecords().stream()
                    .map(WxUser::getUid)
                    .collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }
}
