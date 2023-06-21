package top.baogutang.admin.services;

import cn.dev33.satoken.stp.SaTokenInfo;
import top.baogutang.admin.domain.req.SysUserLoginReq;
import top.baogutang.admin.domain.req.SysUserRegisterReq;
import top.baogutang.admin.domain.res.SysUserInfo;

/**
 * @description:
 * @author: nikooh
 * @date: 2023/06/19 : 12:13
 */
public interface ISysUserService {

    /**
     * 后台系统用户注册
     *
     * @param sysUserRegisterReq 注册请求参数
     */
    void register(SysUserRegisterReq sysUserRegisterReq);

    /**
     * 登陆接口
     *
     * @param sysUserLoginReq 登陆请求参数
     * @return 登陆用户信息
     */
    SaTokenInfo login(SysUserLoginReq sysUserLoginReq);

    /**
     * 登出接口
     */
    void logout();
}
