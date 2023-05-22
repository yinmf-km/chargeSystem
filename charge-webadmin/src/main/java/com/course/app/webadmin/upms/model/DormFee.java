package com.course.app.webadmin.upms.model;

import com.baomidou.mybatisplus.annotation.*;
import com.course.app.common.core.base.model.BaseModel;
import com.course.app.common.core.base.mapper.BaseModelMapper;
import com.course.app.webadmin.upms.vo.DormFeeVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * DormFee实体对象。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "dorm_fee")
public class DormFee extends BaseModel {

    /**
     * 主键Id。
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 宿舍类型。
     */
    private String dormType;

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
    public interface DormFeeModelMapper extends BaseModelMapper<DormFeeVo, DormFee> {
    }
    public static final DormFeeModelMapper INSTANCE = Mappers.getMapper(DormFeeModelMapper.class);
}
