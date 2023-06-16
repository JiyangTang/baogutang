package top.baogutang.common.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @description:
 * @author: nikooh
 * @date: 2023/06/16 : 12:28
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Page<T> implements Serializable {

    private static final long serialVersionUID = 7521438130006292396L;

    private Integer totalCount;

    private Integer pageSize;

    private Integer totalPage;

    private Integer currPage;

    private List<T> list;
}
