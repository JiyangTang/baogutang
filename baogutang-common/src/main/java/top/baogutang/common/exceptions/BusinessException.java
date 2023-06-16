package top.baogutang.common.exceptions;

import top.baogutang.common.constants.ErrorCodeEnum;
import top.baogutang.common.domain.Results;

/**
 * @description: 业务异常
 * @author: nikooh
 * @date: 2023/06/15 : 11:48
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = -8485514816508612287L;

    private int code = Results.FAIL_CODE;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }


    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(ErrorCodeEnum errorCodeEnum) {
        super(errorCodeEnum.getMsg());
        this.code = errorCodeEnum.getCode();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
