package top.baogutang.common.aspect;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.baogutang.common.annotation.VerifyRequired;
import top.baogutang.common.components.TokenComponent;
import top.baogutang.common.constants.CacheConstant;
import top.baogutang.common.domain.JwtBody;
import top.baogutang.common.domain.Results;
import top.baogutang.common.domain.TokenCodeEnum;
import top.baogutang.common.exceptions.BusinessException;
import top.baogutang.common.utils.UserThreadLocal;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

import static top.baogutang.common.constants.ErrorCodeEnum.E_BIZ_NEED_VERIFY;
import static top.baogutang.common.constants.JwtConstant.AUTHORIZATION;

/**
 * @description:
 * @author: nikooh
 * @date: 2023/06/15 : 12:07
 */
@Slf4j
@Aspect
@Component
public class VerifyRequiredAspect {


    @Resource
    private TokenComponent tokenComponent;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Pointcut("@annotation(top.baogutang.common.annotation.VerifyRequired)")
    public void verify() {
    }

    @Around("verify()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        VerifyRequired verifyRequired = method.getAnnotation(VerifyRequired.class);
        if (verifyRequired != null) {
            String token = request.getHeader(AUTHORIZATION);
            if (StringUtils.isBlank(token)) {
                throw new BusinessException(
                        TokenCodeEnum.AUTH_TOKEN_EMPTY.getCode(), TokenCodeEnum.AUTH_TOKEN_EMPTY.getMessage());
            }
            JwtBody body = tokenComponent.parseToken(token);
            Long userId = body.getId();
            UserThreadLocal.set(userId);
            if (Objects.isNull(redisTemplate.opsForValue().get(CacheConstant.USER_VERIFY_KEY_PREFIX + userId))) {
                return Results.failed(E_BIZ_NEED_VERIFY.getCode(), E_BIZ_NEED_VERIFY.getMsg());
            }
            Object val = redisTemplate.opsForValue().get(CacheConstant.TOKEN + userId);
            if (Objects.isNull(val)) {
                redisTemplate.opsForValue().set(CacheConstant.TOKEN + userId, token);
            } else {
                //比对token、不相同则判断token生产时间，取最新的覆盖
                String redisToken = String.valueOf(val);
                if (!token.equals(redisToken)) {
                    JwtBody redisBody = tokenComponent.parseToken(redisToken);
                    if (body.getIat() > redisBody.getIat()) {
                        log.info("VerifyNewTokenCover,currentBody:{} redisBody:{}", JSON.toJSONString(body), JSON.toJSONString(redisBody));
                        redisTemplate.opsForValue().set(CacheConstant.TOKEN + userId, token);
                    } else {
                        log.info("FoundOldToken,currentBody:{} redisBody:{}", JSON.toJSONString(body), JSON.toJSONString(redisBody));
                        throw new BusinessException(
                                TokenCodeEnum.AUTH_FAILED.getCode(), TokenCodeEnum.AUTH_FAILED.getMessage());
                    }
                }
            }

        }
        return point.proceed();
    }

    @After("verify()")
    public void after() {
        UserThreadLocal.remove();
    }

}
