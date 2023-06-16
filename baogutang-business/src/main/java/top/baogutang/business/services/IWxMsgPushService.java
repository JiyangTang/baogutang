package top.baogutang.business.services;


/**
 * @description: 微信消息推送service
 * @author: nikooh
 * @date: 2023/06/16 : 10:27
 */
public interface IWxMsgPushService {

    /**
     * 创建一个带参数的二维码，用户扫码的时候，回调里面会携带二维码的参数.
     *
     * @return 二维码
     */
    String getOrCode();

}
