package top.baogutang.admin.domain.res;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author n
 * @date 2021/11/21
 */
@Data
@ApiModel("用户token信息")
public class SysUserInfo implements Serializable {

    private static final long serialVersionUID = -2577609323497270414L;

    @ApiModelProperty(value = "token（加在请求头里）")
    private String accessToken;

    private String tokenType;

    @ApiModelProperty(value = "token失效后根据refresh_token刷新")
    private String refreshToken;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "用户邮箱")
    private String email;

    @ApiModelProperty(value = "用户手机号")
    private String mobile;
}
