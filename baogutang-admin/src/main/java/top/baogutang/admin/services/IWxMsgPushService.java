package top.baogutang.admin.services;


import java.util.Set;

/**
 * @description: 微信消息推送service
 * @author: nikooh
 * @date: 2023/06/16 : 10:27
 */
public interface IWxMsgPushService {

    /**
     * 微信消息推送
     *
     * @param msgType    消息类型
     * @param summary    消息主题
     * @param msgContent 消息内容
     * @param topicIdSet 消息模版id
     * @return 推送结果
     */
    Boolean msgPush(Integer msgType, String summary, String msgContent, Set<Long> topicIdSet);

}
