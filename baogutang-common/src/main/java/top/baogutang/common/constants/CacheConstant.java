package top.baogutang.common.constants;

/**
 * @description: 缓存常量
 * @author: nikooh
 * @date: 2023/06/15 : 11:28
 */
public class CacheConstant {

    /**
     * 实名认证完成
     */
    public static final String USER_VERIFY_KEY_PREFIX = "top:baogutang:user:verify:";

    public static final String TOKEN = "top:baogutang:user:token:";

    /**
     * 微信消息推送获取临时二维码缓存key
     */
    public static final String WX_MSG_PUSH_QR_CODE_PREFIX = "top:baogutang:wx:msg_push:%s:";

    /**
     * 随机验证码图片key
     */
    public static final String RANDOM_IMAGE_CACHE_KEY = "top:baogutang:random_image:%s:";

    public static final String SYS_TOKEN = "top:baogutang:sys:user:token:";


    /**
     * 消息推送
     */
    public static final String MSG_PUSH_PREFIX_KEY = "top:baogutang:msg:push:%d:";


    public static final Integer ENABLE = 0;
}
