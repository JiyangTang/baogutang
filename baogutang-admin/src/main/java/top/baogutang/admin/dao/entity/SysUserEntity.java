package top.baogutang.admin.dao.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author developer
 * @version 1.0
 * @description: 用户实体
 * @date 2021/11/9 14:33
 */
@Data
@TableName("t_sys_user")
public class SysUserEntity implements Serializable {

    private static final long serialVersionUID = 390648011771249275L;


    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @ApiModelProperty(value = "创建人")
    private Long creator;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新人")
    private Long updater;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 删除标记，0是正常，1逻辑删除
     */
    @TableLogic
    private Integer deleted;

    /**
     * 名字
     */
    private String username;

    /**
     * email
     */
    private String email;

    /**
     * mobile
     */
    private String mobile;

    private String password;

    /**
     * 是否启用（0:启用，1:禁用）
     */
    private Integer enableFlag;

}
