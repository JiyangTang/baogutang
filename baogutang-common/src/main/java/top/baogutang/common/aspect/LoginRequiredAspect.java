package top.baogutang.common.aspect;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.baogutang.common.annotation.LoginRequired;
import top.baogutang.common.components.TokenComponent;
import top.baogutang.common.constants.CacheConstant;
import top.baogutang.common.domain.JwtBody;
import top.baogutang.common.domain.TokenCodeEnum;
import top.baogutang.common.exceptions.BusinessException;
import top.baogutang.common.utils.UserThreadLocal;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

import static top.baogutang.common.constants.JwtConstant.AUTHORIZATION;

/**
 * @description:
 * @author: nikooh
 * @date: 2023/06/15 : 11:32
 */
@Slf4j
@Aspect
@Component
public class LoginRequiredAspect {

    @Resource
    private TokenComponent tokenComponent;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Pointcut("@annotation(top.baogutang.common.annotation.LoginRequired)")
    public void point() {
    }

    @Before(value = "point() && @annotation(loginRequired)")
    public void verifyTokenForClass(LoginRequired loginRequired) {
        if (loginRequired.required()) {
            checkToken();
        } else {
            checkTokenWithOutRequired();
        }
    }

    @After("point()")
    public void after() {
        UserThreadLocal.remove();
    }

    private void checkToken() {

        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.isNull(requestAttributes)) {
            return;
        }
        HttpServletRequest request = requestAttributes.getRequest();
        String token = request.getHeader(AUTHORIZATION);
        if (StringUtils.isEmpty(token)) {
            throw new BusinessException(
                    TokenCodeEnum.AUTH_TOKEN_EMPTY.getCode(), TokenCodeEnum.AUTH_TOKEN_EMPTY.getMessage());
        }

        log.info("request url:{},method:{}", request.getRequestURL(), request.getMethod());
        JwtBody body = tokenComponent.parseToken(token);
        Long userId = body.getId();
        Object val = redisTemplate.opsForValue().get(CacheConstant.TOKEN + userId);
        if (Objects.isNull(val)) {
            redisTemplate.opsForValue().set(CacheConstant.TOKEN + userId, token);
        } else {
            //比对token、不相同则判断token生产时间，取最新的覆盖
            String redisToken = String.valueOf(val);
            if (!token.equals(redisToken)) {
                JwtBody redisBody = tokenComponent.parseToken(redisToken);
                if (body.getIat() > redisBody.getIat()) {
                    log.info("newTokenCover,currentBody:{} redisBody:{}", JSON.toJSONString(body), JSON.toJSONString(redisBody));
                    redisTemplate.opsForValue().set(CacheConstant.TOKEN + userId, token);
                } else {
                    log.info("FoundOldToken,currentBody:{} redisBody:{}", JSON.toJSONString(body), JSON.toJSONString(redisBody));
                    throw new BusinessException(
                            TokenCodeEnum.AUTH_FAILED.getCode(), TokenCodeEnum.AUTH_FAILED.getMessage());
                }
            }
        }
        UserThreadLocal.set(body.getId());
    }

    private void checkTokenWithOutRequired() {
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.isNull(requestAttributes)) {
            return;
        }
        HttpServletRequest request = requestAttributes.getRequest();
        String token = request.getHeader(AUTHORIZATION);
        if (StringUtils.isNotEmpty(token)) {
            log.info("request url:{},method:{}", request.getRequestURL(), request.getMethod());
            JwtBody body = tokenComponent.parseToken(token);
            UserThreadLocal.set(body.getId());
        }
    }

}
