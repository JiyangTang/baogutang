package top.baogutang.admin.domain.req;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @description: 注册请求参数
 * @author: nikooh
 * @date: 2023/06/19 : 12:11
 */
@Data
public class SysUserRegisterReq implements Serializable {

    private static final long serialVersionUID = -8279662824540042891L;

    /**
     * 注册邮箱
     */
    @NotBlank(message = "邮箱不能为空")
    private String email;

    /**
     * 注册手机号
     */
    @NotBlank(message = "手机不能为空")
    private String mobile;

    /**
     * 账户密码
     */
    @NotBlank(message = "密码不能为空")
    @Length(min = 6, max = 10, message = "密码长度请设置在6～10位之间")
    private String password;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    private String captcha;

    /**
     * 验证码key
     */
    @NotBlank(message = "验证码key不能为空")
    private String checkKey;
}
