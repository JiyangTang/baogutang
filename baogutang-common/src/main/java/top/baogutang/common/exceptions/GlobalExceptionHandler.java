package top.baogutang.common.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.baogutang.common.domain.Results;

import java.util.List;

/**
 * @description: 全局异常处理
 * @author: nikooh
 * @date: 2023/06/15 : 12:23
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    public GlobalExceptionHandler() {
        //
    }

    @ExceptionHandler({Throwable.class})
    public Results<Object> handleException(Throwable e) {
        log.error("请求发生错误，错误信息:{}", e.getMessage(), e);
        return Results.failed("出错啦，请稍后再试哟～");
    }


    @ExceptionHandler({Exception.class})
    public Results<Object> handleException(Exception e) {
        log.error("请求发生错误，错误信息:", e);
        return Results.failed("服务器出错啦，请稍后再试哟～");
    }

    @ExceptionHandler({ClientAbortException.class})
    public Results<Object> handleClientAbortException() {
        log.error("ClientAbortException: java.io.IOException: Broken pipe: {}", "客户端链接断开");
        return Results.failed(300, "服务器出错啦，请稍后再试哟～");
    }

    @ExceptionHandler({IllegalArgumentException.class, MissingServletRequestParameterException.class, HttpMediaTypeNotSupportedException.class, HttpRequestMethodNotSupportedException.class})
    public Results<Object> handleMultiException(Exception e) {
        log.error("请求发生错误，错误信息:{}", e.getMessage(), e);
        return Results.failed(300, e.getMessage());
    }

    @ExceptionHandler({BusinessException.class})
    public Results<Object> businessException(BusinessException e) {
        log.error("请求发生错误，code:{},message:{}", e.getCode(), e.getMessage());
        return Results.failed(e.getCode(), e.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Results<Object> parameterExceptionHandler(MethodArgumentNotValidException e) {
        BindingResult exceptions = e.getBindingResult();
        if (exceptions.hasErrors()) {
            List<ObjectError> errors = exceptions.getAllErrors();
            if (!errors.isEmpty()) {
                FieldError fieldError = (FieldError) errors.get(0);
                return Results.failed(301, fieldError.getDefaultMessage());
            }
        }
        return Results.failed(301, e.getMessage());
    }
}
