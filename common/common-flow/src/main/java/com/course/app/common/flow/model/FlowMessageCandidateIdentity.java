package com.course.app.common.flow.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 流程任务消息的候选身份实体对象。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Data
@TableName(value = "zz_flow_msg_candidate_identity")
public class FlowMessageCandidateIdentity {

    /**
     * 主键Id。
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 任务消息Id。
     */
    @TableField(value = "message_id")
    private Long messageId;

    /**
     * 候选身份类型。
     */
    @TableField(value = "candidate_type")
    private String candidateType;

    /**
     * 候选身份Id。
     */
    @TableField(value = "candidate_id")
    private String candidateId;
}
