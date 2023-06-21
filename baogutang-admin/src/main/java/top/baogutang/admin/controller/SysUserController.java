package top.baogutang.admin.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.hutool.core.lang.PatternPool;
import cn.hutool.core.lang.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.baogutang.admin.domain.req.SysUserLoginReq;
import top.baogutang.admin.domain.req.SysUserRegisterReq;
import top.baogutang.admin.services.ISysUserService;
import top.baogutang.common.domain.Results;

import javax.annotation.Resource;

/**
 * @description: 后台用户控制器
 * @author: nikooh
 * @date: 2023/06/19 : 11:57
 */
@Slf4j
@RestController
@RequestMapping("api/v1/sysUser")
public class SysUserController {

    @Resource
    private ISysUserService sysUserService;

    /**
     * 后台系统用户注册
     *
     * @param sysUserRegisterReq 注册请求参数
     * @return 注册结果
     */
    @PostMapping("register")
    public Results<Void> register(@Validated @RequestBody SysUserRegisterReq sysUserRegisterReq) {
        if (!Validator.isMatchRegex(PatternPool.EMAIL, sysUserRegisterReq.getEmail())) {
            return Results.failed("邮箱格式不正确！");
        }
        if (!Validator.isMatchRegex(PatternPool.MOBILE, sysUserRegisterReq.getMobile())) {
            return Results.failed("手机号格式不正确！");
        }
        sysUserService.register(sysUserRegisterReq);
        return Results.ok();
    }

    /**
     * 登陆接口
     *
     * @param sysUserLoginReq 登陆请求参数
     * @return 登陆用户信息
     */
    @PostMapping("/login")
    public Results<SaTokenInfo> login(@RequestBody SysUserLoginReq sysUserLoginReq) {
        return Results.ok(sysUserService.login(sysUserLoginReq));
    }

    /**
     * 登出接口
     */
    @PostMapping("/logout")
    @SaCheckLogin
    public Results<Void> logout() {
        sysUserService.logout();
        return Results.ok();
    }


}
