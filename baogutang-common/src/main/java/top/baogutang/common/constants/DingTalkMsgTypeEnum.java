package top.baogutang.common.constants;

/**
 * @description: 钉钉消息类型枚举
 * @author: nikooh
 * @date: 2023/04/07 : 13:41
 */
public enum DingTalkMsgTypeEnum {

    /**
     * 文本消息
     */
    TEXT("text"),

    /**
     * 图片消息
     */
    IMAGE("image"),

    /**
     * 语音消息
     */
    VOICE("voice"),

    /**
     * 文件消息
     */
    FILE("file"),

    /**
     * 链接消息
     */
    LINK("link"),

    /**
     * OA消息
     */
    OA("oa"),

    /**
     * markdown消息
     */
    MARKDOWN("markdown"),

    /**
     * 卡片消息
     */
    ACTION_CARD("action_card"),

    ;

    private final String type;

    DingTalkMsgTypeEnum(String type) {
        this.type = type;
    }


    public String getType() {
        return type;
    }
}
