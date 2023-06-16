package top.baogutang.common.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.baogutang.common.domain.Results;

import javax.servlet.http.HttpServletRequest;

/**
 * @description:
 * @author: nikooh
 * @date: 2023/06/15 : 12:16
 */
@Slf4j
@Aspect
@Component
public class LogAspect {


    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void logPointCut() {
        //pointCut

    }

    @Around("logPointCut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        Object result;
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        assert sra != null;
        HttpServletRequest request = sra.getRequest();
        String requestId = MDC.get("X-Request-Id");
        long startMills = System.currentTimeMillis();
        long cost = 0;
        try {
            result = pjp.proceed();
            cost = System.currentTimeMillis() - startMills;
            log.info("请求结束！本次请求耗时:{},url: {}, method: {}, params: {},user:{}, token: {},  响应结果:{}",
                    cost, request.getRequestURL().toString(),
                    request.getMethod(), pjp.getArgs(), JSON.toJSONString(request.getAttribute("user")), request.getHeader("authorization"),
                    JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect));
            if (result instanceof Results) {
                Results r = (Results) result;
                r.setRid(requestId);
            }
        } catch (Exception e) {
            log.error("请求异常！！！本次请求耗时:{},error:{},url: {}, method: {}, params: {},user:{}, token: {}", cost, e, request.getRequestURL().toString(),
                    request.getMethod(), pjp.getArgs(), JSON.toJSONString(request.getAttribute("user")), request.getHeader("token"));
            throw e;
        }
        return result;
    }

}
