package top.baogutang.common.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description:
 * @author: nikooh
 * @date: 2023/06/15 : 12:00
 */
@Getter
@AllArgsConstructor
public enum TokenCodeEnum {
    /**
     * 未获取到token
     */
    AUTH_TOKEN_EMPTY(-200, "未获取到token"),
    AUTH_FAILED(-200, "无权限"),
    AUTH_TIME_OUT(-200, "token已过期"),
    AUTH_TOKEN_ILLEGAL(-200, "Token不合法");

    public int code;

    public String message;
}
