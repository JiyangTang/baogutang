package top.baogutang.common.constants;

/**
 * @description: 钉钉服务相关常量
 * @author: nikooh
 * @date: 2023/06/21 : 16:53
 */
public class DingTalkServerConstants {

    private DingTalkServerConstants() {
        // private constructor
    }

    /**
     * token
     */
    public static final String ACC_TOKEN = "https://oapi.dingtalk.com/gettoken";

    /**
     * 群组创建
     */
    public static final String CHAT_CREATE = "https://oapi.dingtalk.com/chat/create";

    /**
     * 群组修改
     */
    public static final String CHAT_UPDATE = "https://oapi.dingtalk.com/chat/update";

    /**
     * 工作通知
     */
    public static final String CORP_CONVERSATION_ASYNC_SEND_V2 = "https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2";

    /**
     * 部门下员工
     */
    public static final String USER_LIST = "https://oapi.dingtalk.com/topapi/v2/user/list";

    /**
     * 部门列表
     */
    public static final String DEPARTMENT_LIST = "https://oapi.dingtalk.com/department/list";

    /**
     * 用户详情
     */
    public static final String USER_INFO = "https://oapi.dingtalk.com/user/getuserinfo";

    /**
     * 手机号搜索userId
     */
    public static final String USER_ID_BY_MOBILE = "https://oapi.dingtalk.com/topapi/v2/user/getbymobile";
}
