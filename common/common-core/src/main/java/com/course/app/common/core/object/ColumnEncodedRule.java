package com.course.app.common.core.object;

import lombok.Data;

/**
 * 编码字段的编码规则。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Data
public class ColumnEncodedRule {

    /**
     * 是否显示是计算并回显。
     */
    private Boolean calculateWhenView;

    /**
     * 前缀。
     */
    private String prefix;

    /**
     * 精确到DAYS/HOURS/MINUTES/SECONDS
     */
    private String precisionTo;

    /**
     * 中缀。
     */
    private String middle;

    /**
     * 流水序号的字符宽度，不足的前面补0。
     */
    private Integer idWidth;
}
