package top.baogutang.admin.config;

import cn.dev33.satoken.exception.NotLoginException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.baogutang.common.domain.Results;

/**
 * @description: 全局异常处理
 * @author: nikooh
 * @date: 2023/06/15 : 12:23
 */
@Slf4j
@RestControllerAdvice
public class SysGlobalExceptionHandler {


    public SysGlobalExceptionHandler() {
        //
    }


    @ExceptionHandler({NotLoginException.class})
    public Results<Object> businessNotLoginException(NotLoginException e) {
        log.error("请求发生错误，code:{},message:{}", e.getCode(), e.getMessage());
        return Results.failed(e.getCode(), e.getMessage());
    }


}
