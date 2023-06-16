package top.baogutang.common.constants;

/**
 * @description:
 * @author: nikooh
 * @date: 2023/06/15 : 11:48
 */
public enum ErrorCodeEnum {
    /**
     * 数据不存在
     */
    E_BIZ_ERROR(80800000, "请稍后重试～"),
    E_BIZ_ALREADY_VERIFIED(81010001, "您已经完成认证了哦～"),
    E_BIZ_ID_NO_ALREADY_VERIFIED(81010002, "该身份证已被认证哦～"),
    E_PARAM_ID_NO_INCORRECT(81010003, "您输入的身份证有误哦～"),
    E_PARAM_MOBILE_INCORRECT(81010004, "您输入的手机号有误哦～"),
    E_PARAM_MSG_INCORRECT(81010005, "短信验证码不正确哟～"),
    E_BIZ_MSG_SEND_FAIL(81010006, "请求频繁哟～"),
    E_BIZ_NEED_VERIFY(81010007, "请完成实名认证～"),

    E_BIZ_DIGITAL_ART_DELAY_ORDER_PROCESS_ERROR(81010015, "订单已超时哦～"),
    E_BIZ_DIGITAL_ART_CREATE_ORDER_ERROR(81010016, "创建订单失败"),
    E_BIZ_DIGITAL_ART_CREATE_PAYMENT_UPDATE_ORDER_ERROR(81010017, "创建支付数据更新订单失败"),
    E_BIZ_DIGITAL_ART_STORAGE_DATA_NOT_EXISTS(81010018, "库存不存在哦～"),
    E_BIZ_DIGITAL_ART_STORAGE_EXISTS_PENDING_ORDER(81010019, "当前库存存在进行中订单哦～"),
    E_BIZ_DIGITAL_ART_STORAGE_EXISTS_NICKNAME(81010020, "当前昵称已存在"),
    E_BIZ_DIGITAL_ART_ORDER_NOT_EXISTS(81010022, "订单不存在哦～"),
    E_BIZ_DIGITAL_ART_ORDER_ALREADY_PAID(81010023, "订单已支付哦～"),
    E_BIZ_DIGITAL_ART_ACCOUNT_NOT_EXISTS(81010015, "账户信息不存在，请先绑定支付宝～"),
    E_BIZ_DIGITAL_ART_ACCOUNT_STATE_ERROR(81010016, "账户状态异常，请联系官方～"),
    E_BIZ_DIGITAL_ART_USER_NOT_EXISTS(81010017, "用户信息不存在哦～"),
    E_BIZ_DIGITAL_ART_MSG_CODE_NOT_EXISTS(81010018, "验证码已失效，请重新发送～"),
    E_BIZ_DIGITAL_ART_MSG_CODE_NOT_VALID(81010019, "验证码错误，请重新输入～"),
    E_BIZ_DIGITAL_ART_MOBILE_EMAIL_NOT_AVAILABLE(81010020, "仅能填写手机号或邮箱哦~"),
    E_BIZ_DIGITAL_ART_PASSWORD_NOT_VALID(81010022, "支付密码只能是数字哦～"),
    E_BIZ_DIGITAL_ART_PAY_PASSWORD_NOT_SET(81010023, "支付密码还未设置哦～"),
    E_BIZ_DIGITAL_ART_PAY_PASSWORD_FAIL(81010024, "支付密码错误，请重新输入~"),
    E_BIZ_DIGITAL_ART_PAY_PASSWORD_FAILS(81010025, "输入错误次数过多，请稍后再试~"),
    E_BIZ_DIGITAL_ART_PAY_ACCOUNT_NOT_BOUND(81010026, "请先绑定支付宝~"),
    E_BIZ_DIGITAL_ART_ACCOUNT_BALANCE_NOT_ENOUGH(81010027, "账户余额不足~"),
    E_BIZ_DIGITAL_ART_CASH_TOO_MANY_TIMES(81010028, "超过最大提现次数，请明天再来~"),
    E_BIZ_DIGITAL_ART_CASH_TOO_MANY_AMOUNT(81010029, "当日累计提现已超过最大提现金额~"),
    E_BIZ_DIGITAL_ART_CASH_ERROR(81010030, "提现失败，请稍后重试~"),
    E_BIZ_DIGITAL_ART_TRANS_ERROR(81010031, "交易失败，请稍后重试~"),
    E_BIZ_DIGITAL_ART_PASSWORD_LENGTH_NOT_VALID(81010033, "请输入六位支付密码～"),
    E_BIZ_DIGITAL_ART_EXCHANGE_MALL_OUT_OF_STOCK(81010040, "好遗憾，抢光了"),
    E_BIZ_DIGITAL_ART_GOODS_NOT_ENOUGH_STORAGE(81010048, "库存不足，请稍后重试~"),
    E_BIZ_DIGITAL_ART_VOUCHER_NOT_EXISTS(81010050, "兑换码不存在~"),
    E_BIZ_DIGITAL_ART_VOUCHER_ALREADY_USED(81010051, "兑换码已兑换~"),
    E_BIZ_DIGITAL_ART_VOUCHER_USER_HOLD_GOODS_NOT_ENOUGH(81010052, "暂无兑换资格"),
    E_BIZ_DIGITAL_ART_GOODS_LIMIT_BUY(81010053, "您已经达到购买上限"),

    USER_NOT_EXITS(81010015, "用户不存在"),
    PASSWORD_ERROR(81010016, "密码错误"),
    NO_PERMISSION(81010017, "账号无权限"),
    E_BIZ_SIGN_ERROR(81011023, "签名错误~"),
    E_BIZ_NOT_COLLAB_GOODS_ERROR(81011024, "该商品不支持此操作~"),
    E_BIZ_MSG_SELL_LOCK(81011032, "请勿频繁操作～"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限"),
    AUTHORIZATION_HEADER_IS_EMPTY(600, "请求头中的token为空"),

    DATA_IS_NULL(81011033, "数据为空");;

    private final int code;
    private final String msg;

    ErrorCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
