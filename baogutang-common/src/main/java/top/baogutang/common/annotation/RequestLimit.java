package top.baogutang.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: nikooh
 * @date: 2023/06/15 : 12:04
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequestLimit {

    /**
     * 指定时间内不可重复提交,单位秒
     */
    long timeout() default 1;

    /**
     * 时间单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 指定key
     */
    String key() default "";

    /**
     * 是否限制单个人的， 包含token
     */
    boolean includeToken() default true;
}
