package com.course.app.webadmin.upms.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * PrePaymentFeeDetailVO视图对象。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@ApiModel("PrePaymentFeeDetailVO视图对象")
@Data
public class PrePaymentFeeDetailVo {

    /**
     * 交易编号。
     */
    @ApiModelProperty(value = "交易编号")
    private Long prePayMerTranNo;

    /**
     * 缴费学生。
     */
    @ApiModelProperty(value = "缴费学生")
    private Long studentId;

    /**
     * 缴费学生绑定手机号码。
     */
    @ApiModelProperty(value = "缴费学生绑定手机号码")
    private String phoneNum;

    /**
     * 缴费金额。
     */
    @ApiModelProperty(value = "缴费金额")
    private String tradeAmount;

    /**
     * 缴费时间。
     */
    @ApiModelProperty(value = "缴费时间")
    private Date transTime;

    /**
     * 缴费途径。
     */
    @ApiModelProperty(value = "缴费途径")
    private String tranScene;
}
