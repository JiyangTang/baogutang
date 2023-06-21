package top.baogutang.admin.domain.req;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author n
 */
@Data
@ApiModel("用户登录req")
public class SysUserLoginReq implements Serializable {

    private static final long serialVersionUID = -8305880247606732166L;

    @ApiModelProperty(value = "邮箱")
    @NotBlank(message = "邮箱不能为空")
    private String email;

    @ApiModelProperty(value = "密码")
    @NotBlank(message = "密码不能为空")
    private String password;
}
