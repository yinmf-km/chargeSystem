package com.course.app.webadmin.upms.dto;

import com.course.app.common.core.validator.UpdateGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

import java.util.Date;

/**
 * PrePaymentFeeDetailDto对象。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@ApiModel("PrePaymentFeeDetailDto对象")
@Data
public class PrePaymentFeeDetailDto {

    /**
     * 交易编号。
     */
    @ApiModelProperty(value = "交易编号", required = true)
    @NotNull(message = "数据验证失败，交易编号不能为空！", groups = {UpdateGroup.class})
    private Long prePayMerTranNo;

    /**
     * 缴费学生。
     */
    @ApiModelProperty(value = "缴费学生", required = true)
    @NotNull(message = "数据验证失败，缴费学生不能为空！")
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
    @ApiModelProperty(value = "缴费时间", required = true)
    @NotNull(message = "数据验证失败，缴费时间不能为空！")
    private Date transTime;

    /**
     * 缴费途径。
     */
    @ApiModelProperty(value = "缴费途径", required = true)
    @NotBlank(message = "数据验证失败，缴费途径不能为空！")
    private String tranScene;
}
