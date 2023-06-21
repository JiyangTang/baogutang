package top.baogutang.admin.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.baogutang.admin.dao.entity.SysUserEntity;

/**
 * @author developer
 * @version 1.0
 * @description: Mapper映射接口
 * @date 2021/11/9 14:34
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUserEntity> {


    /**
     * 根据邮箱或手机号查询
     *
     * @param email  邮箱
     * @param mobile 手机号
     * @return 用户信息
     */
    SysUserEntity selectByEmailOrMobile(@Param("email") String email, @Param("mobile") String mobile);
}