package top.baogutang.admin.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description:
 * @author: nikooh
 * @date: 2023/06/16 : 12:09
 */
@Data
public class EdgeXAnnouncementsDto implements Serializable {

    private static final long serialVersionUID = 2069054276884655439L;

    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 类型
     */
    private String type;

    /**
     * 封面
     */
    private String cover;

    /**
     * 创作者
     */
    private String creator;

    /**
     * 内容
     */
    private String content;

    /**
     * 创建时间
     */
    private Date createdAt;
}
