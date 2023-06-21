package top.baogutang.admin.services.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.baogutang.admin.dao.entity.SysUserEntity;
import top.baogutang.admin.dao.mapper.SysUserMapper;
import top.baogutang.admin.domain.req.SysUserLoginReq;
import top.baogutang.admin.domain.req.SysUserRegisterReq;
import top.baogutang.admin.services.ISysUserService;
import top.baogutang.common.annotation.RequestLimit;
import top.baogutang.common.constants.ErrorCodeEnum;
import top.baogutang.common.exceptions.BusinessException;

import javax.annotation.Resource;

import java.util.Objects;

import static top.baogutang.common.constants.CacheConstant.ENABLE;
import static top.baogutang.common.constants.CacheConstant.RANDOM_IMAGE_CACHE_KEY;

/**
 * @description:
 * @author: nikooh
 * @date: 2023/06/19 : 12:14
 */
@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUserEntity> implements ISysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private PasswordEncoder passwordEncoder;


    @Override
    @RequestLimit
    @Transactional(rollbackFor = Exception.class)
    public void register(SysUserRegisterReq sysUserRegisterReq) {
        String cacheKey = String.format(RANDOM_IMAGE_CACHE_KEY, sysUserRegisterReq.getCheckKey());
        Object cacheCaptcha = redisTemplate.opsForValue().get(cacheKey);
        if (Objects.isNull(cacheCaptcha)) {
            throw new BusinessException(ErrorCodeEnum.CAPTCHA_NOT_EXISTS);
        }
        if (!sysUserRegisterReq.getCaptcha().equalsIgnoreCase((String) cacheCaptcha)) {
            throw new BusinessException(ErrorCodeEnum.CAPTCHA_INCORRECT);
        }
        // 查询邮箱手机号是否已占用
        SysUserEntity sysUser = baseMapper.selectByEmailOrMobile(sysUserRegisterReq.getEmail(), sysUserRegisterReq.getMobile());
        if (Objects.nonNull(sysUser)) {
            throw new BusinessException(ErrorCodeEnum.CURRENT_USER_EXISTS);
        }
        // 可以进行注册
        SysUserEntity userEntity = new SysUserEntity();
        userEntity.setId(IdWorker.getId());
        userEntity.setUsername(sysUserRegisterReq.getEmail());
        userEntity.setEmail(sysUserRegisterReq.getEmail());
        userEntity.setMobile(sysUserRegisterReq.getMobile());
        userEntity.setPassword(passwordEncoder.encode(sysUserRegisterReq.getPassword()));
        saveOrUpdate(userEntity);
    }

    @Override
    public SaTokenInfo login(SysUserLoginReq sysUserLoginReq) {
        String password = sysUserLoginReq.getPassword();
        SysUserEntity userEntity = this.findByEmail(sysUserLoginReq.getEmail());
        if (userEntity == null) {
            throw new BusinessException(ErrorCodeEnum.E_BIZ_DIGITAL_ART_USER_NOT_EXISTS);
        }
        boolean userVerify = passwordEncoder.matches(password, userEntity.getPassword());
        if (!userVerify) {
            throw new BusinessException(ErrorCodeEnum.PASSWORD_ERROR);
        }
        // Sa-Token
        StpUtil.login(userEntity.getId());
        return StpUtil.getTokenInfo();
    }

    @Override
    public void logout() {
        StpUtil.logout();
    }


    private SysUserEntity findByEmail(String email) {
        Wrapper<SysUserEntity> wrapper = Wrappers.<SysUserEntity>query()
                .lambda()
                .eq(SysUserEntity::getEmail, email)
                .eq(SysUserEntity::getEnableFlag, ENABLE);
        return this.getOne(wrapper);
    }
}
