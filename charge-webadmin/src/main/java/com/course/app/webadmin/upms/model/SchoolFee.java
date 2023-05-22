package com.course.app.webadmin.upms.model;

import com.baomidou.mybatisplus.annotation.*;
import com.course.app.common.core.base.model.BaseModel;
import com.course.app.common.core.base.mapper.BaseModelMapper;
import com.course.app.webadmin.upms.vo.SchoolFeeVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * SchoolFee实体对象。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "school_fee")
public class SchoolFee extends BaseModel {

    /**
     * 主键Id。
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 生源地。
     */
    private String sourceAddress;

    /**
     * 分数区间。
     */
    private String scoreRange;

    /**
     * 规则描述。
     */
    private String ruleDesc;

    /**
     * 费用。
     */
    private Integer fee;

    /**
     * 逻辑删除标记字段(1: 正常 -1: 已删除)。
     */
    @TableLogic
    private Integer deletedFlag;

    @Mapper
    public interface SchoolFeeModelMapper extends BaseModelMapper<SchoolFeeVo, SchoolFee> {
    }
    public static final SchoolFeeModelMapper INSTANCE = Mappers.getMapper(SchoolFeeModelMapper.class);
}
