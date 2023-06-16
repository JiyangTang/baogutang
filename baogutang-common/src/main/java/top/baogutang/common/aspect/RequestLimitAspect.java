package top.baogutang.common.aspect;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.baogutang.common.annotation.RequestLimit;
import top.baogutang.common.domain.Results;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: nikooh
 * @date: 2023/06/15 : 12:05
 */
@Slf4j
@Aspect
@Component
public class RequestLimitAspect {


    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Pointcut("@annotation(top.baogutang.common.annotation.RequestLimit)")
    public void limit() {
    }

    @Around("limit()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String ip = getIpAddress(request);
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        RequestLimit requestLimit = method.getAnnotation(RequestLimit.class);
        if (requestLimit != null) {
            String key = requestLimit.key();
            if (StringUtils.isEmpty(key)) {
                String className = method.getDeclaringClass().getName();
                String name = method.getName();
                String args = Arrays.toString(point.getArgs());
                String ipKey = String.format("%s#%s#%s", className, name, args);
                int hashCode = Math.abs(ipKey.hashCode());
                key = String.format("%s_%d", ip, hashCode);
            }
            if (requestLimit.includeToken()) {
                String token = request.getHeader("authorization");
                if (token != null) {
                    key = key.concat("_").concat(DigestUtils.md5Hex(token));
                }
            }
            long timeout = requestLimit.timeout();
            TimeUnit timeUnit = requestLimit.timeUnit();
            if (timeout < 0) {
                //过期时间5s
                timeout = 5;
            }
            Boolean lock = redisTemplate.opsForValue().setIfAbsent(key, "1", timeout, timeUnit);
            if (!Boolean.TRUE.equals(lock)) {
                return Results.failed("正在赶来中，请稍后再试～");
            }
        }
        return point.proceed();
    }

    /**
     * 获取请求用户的IP地址
     *
     * @param request request
     * @return return
     */
    public String getIpAddress(HttpServletRequest request) {

        String ipAddresses = request.getHeader("x-forwarded-for");

        ipAddresses = getString(request, ipAddresses);

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //X-Real-IP：nginx服务代理
            ipAddresses = request.getHeader("X-Real-IP");
        }

        //有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
        if (ipAddresses != null && ipAddresses.length() != 0) {
            ipAddresses = ipAddresses.split(",")[0];
        }

        //还是不能获取到，最后再通过request.getRemoteAddr();获取
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getRemoteAddr();
        }

        return ipAddresses;
    }

    public static String getString(HttpServletRequest request, String ipAddresses) {
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //Proxy-Client-IP：apache 服务代理
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //WL-Proxy-Client-IP：weblogic 服务代理
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //HTTP_CLIENT_IP：有些代理服务器
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }
        return ipAddresses;
    }
}
