package com.course.app.common.core.object;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Mybatis Mapper.xml中所需的分组条件对象。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Data
@AllArgsConstructor
public class MyGroupCriteria {

    /**
     * GROUP BY 从句后面的参数。
     */
    private String groupBy;
    /**
     * SELECT 从句后面的分组显示字段。
     */
    private String groupSelect;
}
