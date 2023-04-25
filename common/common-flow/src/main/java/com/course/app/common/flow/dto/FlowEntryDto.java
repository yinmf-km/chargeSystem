package com.course.app.common.flow.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.course.app.common.core.validator.ConstDictRef;
import com.course.app.common.core.validator.UpdateGroup;
import com.course.app.common.flow.model.constant.FlowBindFormType;
import com.course.app.common.flow.model.constant.FlowEntryStatus;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 流程的Dto对象。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@ApiModel("流程的Dto对象")
@Data
public class FlowEntryDto {

    /**
     * 主键Id。
     */
    @ApiModelProperty(value = "主键Id")
    @NotNull(message = "数据验证失败，主键不能为空！", groups = {UpdateGroup.class})
    private Long entryId;

    /**
     * 流程名称。
     */
    @ApiModelProperty(value = "流程名称")
    @NotBlank(message = "数据验证失败，流程名称不能为空！")
    private String processDefinitionName;

    /**
     * 流程标识Key。
     */
    @ApiModelProperty(value = "流程标识Key")
    @NotBlank(message = "数据验证失败，流程标识Key不能为空！")
    private String processDefinitionKey;

    /**
     * 流程分类。
     */
    @ApiModelProperty(value = "流程分类")
    @NotNull(message = "数据验证失败，流程分类不能为空！")
    private Long categoryId;

    /**
     * 流程状态。
     */
    @ApiModelProperty(value = "流程状态")
    @ConstDictRef(constDictClass = FlowEntryStatus.class, message = "数据验证失败，工作流状态为无效值！")
    private Integer status;

    /**
     * 流程定义的xml。
     */
    @ApiModelProperty(value = "流程定义的xml")
    private String bpmnXml;

    /**
     * 绑定表单类型。
     */
    @ApiModelProperty(value = "绑定表单类型")
    @ConstDictRef(constDictClass = FlowBindFormType.class, message = "数据验证失败，工作流绑定表单类型为无效值！")
    @NotNull(message = "数据验证失败，工作流绑定表单类型不能为空！")
    private Integer bindFormType;

    /**
     * 在线表单的页面Id。
     */
    @ApiModelProperty(value = "在线表单的页面Id")
    private Long pageId;

    /**
     * 在线表单的缺省路由名称。
     */
    @ApiModelProperty(value = "在线表单的缺省路由名称")
    private String defaultRouterName;

    /**
     * 在线表单Id。
     */
    @ApiModelProperty(value = "在线表单Id")
    private Long defaultFormId;

    /**
     * 工单表编码字段的编码规则，如果为空则不计算工单编码。
     */
    @ApiModelProperty(value = "工单表编码字段的编码规则")
    private String encodedRule;

    /**
     * 流程的自定义扩展数据(JSON格式)。
     */
    @ApiModelProperty(value = "流程的自定义扩展数据")
    private String extensionData;
}
