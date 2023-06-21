package top.baogutang.admin.services.impl;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.baogutang.admin.services.ICommonService;
import top.baogutang.common.constants.ErrorCodeEnum;
import top.baogutang.common.exceptions.BusinessException;
import top.baogutang.common.utils.RandImageUtil;

import javax.annotation.Resource;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static top.baogutang.common.constants.CacheConstant.RANDOM_IMAGE_CACHE_KEY;

/**
 * @description:
 * @author: nikooh
 * @date: 2023/06/19 : 12:20
 */
@Slf4j
@Service
public class CommonServiceImpl implements ICommonService {

    /**
     * 验证码
     */
    private static final String BASE_CHECK_CODES = "qwertyuipkjhgfdsazxcvbnmQWERTYUPKJHGFDSAZXCVBNM23456789";

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public String randomImage(String key) {
        String captcha = RandomUtil.randomString(BASE_CHECK_CODES, 4);
        String cacheKey = String.format(RANDOM_IMAGE_CACHE_KEY, key);
        redisTemplate.opsForValue().set(cacheKey, captcha, 5, TimeUnit.MINUTES);
        try {
            return RandImageUtil.generate(captcha);
        } catch (IOException e) {
            log.error(">>>>>>>>>>获取图片验证码失败：{}<<<<<<<<<<", e.getMessage(), e);
            throw new BusinessException(ErrorCodeEnum.E_BIZ_ERROR);
        }
    }
}
