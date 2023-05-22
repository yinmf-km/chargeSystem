package com.course.app.webadmin.upms.model;

import com.baomidou.mybatisplus.annotation.*;
import com.course.app.common.core.base.mapper.BaseModelMapper;
import com.course.app.webadmin.upms.vo.PrePaymentFeeDetailVo;
import lombok.Data;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Date;

/**
 * PrePaymentFeeDetail实体对象。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Data
@TableName(value = "pre_payment_fee_detail")
public class PrePaymentFeeDetail {

    /**
     * 交易编号。
     */
    @TableId(value = "pre_pay_mer_tran_no")
    private Long prePayMerTranNo;

    /**
     * 缴费学生。
     */
    private Long studentId;

    /**
     * 缴费学生绑定手机号码。
     */
    private String phoneNum;

    /**
     * 缴费金额。
     */
    private String tradeAmount;

    /**
     * 缴费时间。
     */
    private Date transTime;

    /**
     * 缴费途径。
     */
    private String tranScene;

    @Mapper
    public interface PrePaymentFeeDetailModelMapper extends BaseModelMapper<PrePaymentFeeDetailVo, PrePaymentFeeDetail> {
    }
    public static final PrePaymentFeeDetailModelMapper INSTANCE = Mappers.getMapper(PrePaymentFeeDetailModelMapper.class);
}
