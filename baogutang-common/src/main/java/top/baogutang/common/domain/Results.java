package top.baogutang.common.domain;

import io.swagger.annotations.ApiModelProperty;
import org.slf4j.MDC;
import top.baogutang.common.constants.ErrorCodeEnum;

import java.io.Serializable;

/**
 * @description:
 * @author: nikooh
 * @date: 2023/06/15 : 11:54
 */
public class Results<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final int SUCCESS_CODE = 200;
    public static final int FAIL_CODE = 300;
    public static final String SUCCESS_MSG = "success";
    public static final int PARAM_ILLEGAL_CODE = 301;

    @ApiModelProperty("响应码,200为请求成功;其他异常均为业务异常")
    private int code;
    @ApiModelProperty("响应信息")
    private String msg;
    @ApiModelProperty("业务数据")
    private T data;
    @ApiModelProperty("全局链路请求id")
    private String rid;

    public Results() {
    }

    public Results(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Results(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> Results<T> ok() {
        return restResult(null, 200, (String) null);
    }

    public static <T> Results<T> ok(T data) {
        return restResult(data, SUCCESS_CODE, SUCCESS_MSG);
    }

    public static <T> Results<T> ok(T data, int i, String msg) {
        return restResult(data, 200, (String) null);
    }


    public static <T> Results<T> ok(T data, String msg) {
        return restResult(data, 200, msg);
    }

    public static <T> Results<T> result(T data, int code, String msg) {
        return restResult(data, code, msg);
    }

    public static <T> Results<T> failed() {
        return restResult(null, 300, null);
    }

    public static <T> Results<T> failed(String msg) {
        return restResult(null, 300, msg);
    }

    public static <T> Results<T> failed(int code, String msg) {
        return restResult(null, code, msg);
    }


    public static <T> Results<T> failed(T data) {
        return restResult(data, 300, (String) null);
    }

    public static <T> Results<T> failed(T data, String msg) {
        return restResult(data, 300, msg);
    }

    public static <T> Results<T> restResult(T data, int code, String msg) {
        Results<T> r = new Results<>();
        r.setCode(code);
        r.setData(data);
        r.setMsg(msg);
        String rid = MDC.get("X-Request-Id");
        r.setRid(rid);
        return r;
    }

    public static <T> Results<T> failed(ErrorCodeEnum errorCodeEnum) {
        return restResult(null, errorCodeEnum.getCode(), errorCodeEnum.getMsg());
    }

    public Boolean isSuccess() {
        return this.code == SUCCESS_CODE;
    }

    public static <T> boolean isSuccess(Results<T> response) {
        return response != null && response.code == SUCCESS_CODE;
    }


    public void setErrorCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static <T> Results.ResultsBuilder<T> builder() {
        return new Results.ResultsBuilder();
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public T getData() {
        return this.data;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public void setCode(final int code) {
        this.code = code;
    }

    public void setMsg(final String msg) {
        this.msg = msg;
    }

    public void setData(final T data) {
        this.data = data;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Results;
    }


    @Override
    public String toString() {
        return "Results(code=" + this.getCode() + ", msg=" + this.getMsg() + ", data=" + this.getData() + ")";
    }

    public static class ResultsBuilder<T> {
        private int code;
        private String msg;
        private T data;

        ResultsBuilder() {
        }

        public Results.ResultsBuilder<T> code(final int code) {
            this.code = code;
            return this;
        }

        public Results.ResultsBuilder<T> msg(final String msg) {
            this.msg = msg;
            return this;
        }

        public Results.ResultsBuilder<T> data(final T data) {
            this.data = data;
            return this;
        }

        public Results<T> build() {
            return new Results(this.code, this.msg, this.data);
        }

        @Override
        public String toString() {
            return "Results.ResultsBuilder(code=" + this.code + ", msg=" + this.msg + ", data=" + this.data + ")";
        }
    }
}
