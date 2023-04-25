package com.course.app.common.flow.constant;

/**
 * 工作流中的常量数据。
 *
 * @author 云翼
 * @date 2023-02-21
 */
public class FlowConstant {

    /**
     * 标识流程实例启动用户的变量名。
     */
    public static final String START_USER_NAME_VAR = "${startUserName}";

    /**
     * 流程实例发起人变量名。
     */
    public static final String PROC_INSTANCE_INITIATOR_VAR = "initiator";

    /**
     * 流程实例中发起人用户的变量名。
     */
    public static final String PROC_INSTANCE_START_USER_NAME_VAR = "startUserName";

    /**
     * 流程任务的指定人变量。
     */
    public static final String TASK_APPOINTED_ASSIGNEE_VAR = "appointedAssignee";

    /**
     * 操作类型变量。
     */
    public static final String OPERATION_TYPE_VAR = "operationType";

    /**
     * 多任务拒绝数量变量。
     */
    public static final String MULTI_REFUSE_COUNT_VAR = "multiRefuseCount";

    /**
     * 多任务同意数量变量。
     */
    public static final String MULTI_AGREE_COUNT_VAR = "multiAgreeCount";

    /**
     * 多任务弃权数量变量。
     */
    public static final String MULTI_ABSTAIN_COUNT_VAR = "multiAbstainCount";

    /**
     * 会签发起任务。
     */
    public static final String MULTI_SIGN_START_TASK_VAR = "multiSignStartTask";

    /**
     * 会签任务总数量。
     */
    public static final String MULTI_SIGN_NUM_OF_INSTANCES_VAR = "multiNumOfInstances";

    /**
     * 会签任务执行的批次Id。
     */
    public static final String MULTI_SIGN_TASK_EXECUTION_ID_VAR = "taskExecutionId";

    /**
     * 多实例实例数量变量。
     */
    public static final String NUMBER_OF_INSTANCES_VAR = "nrOfInstances";

    /**
     * 多实例已完成实例数量变量。
     */
    public static final String NUMBER_OF_COMPLETED_INSTANCES_VAR = "nrOfCompletedInstances";

    /**
     * 多任务指派人列表变量。
     */
    public static final String MULTI_ASSIGNEE_LIST_VAR = "assigneeList";

    /**
     * 上级部门领导审批变量。
     */
    public static final String GROUP_TYPE_UP_DEPT_POST_LEADER_VAR = "upDeptPostLeader";

    /**
     * 本部门领导审批变量。
     */
    public static final String GROUP_TYPE_DEPT_POST_LEADER_VAR = "deptPostLeader";

    /**
     * 所有部门岗位审批变量。
     */
    public static final String GROUP_TYPE_ALL_DEPT_POST_VAR = "allDeptPost";

    /**
     * 本部门岗位审批变量。
     */
    public static final String GROUP_TYPE_SELF_DEPT_POST_VAR = "selfDeptPost";

    /**
     * 同级部门岗位审批变量。
     */
    public static final String GROUP_TYPE_SIBLING_DEPT_POST_VAR = "siblingDeptPost";

    /**
     * 上级部门岗位审批变量。
     */
    public static final String GROUP_TYPE_UP_DEPT_POST_VAR = "upDeptPost";

    /**
     * 任意部门关联的岗位审批变量。
     */
    public static final String GROUP_TYPE_DEPT_POST_VAR = "deptPost";

    /**
     * 指定角色分组变量。
     */
    public static final String GROUP_TYPE_ROLE_VAR = "role";

    /**
     * 指定部门分组变量。
     */
    public static final String GROUP_TYPE_DEPT_VAR = "dept";

    /**
     * 指定用户分组变量。
     */
    public static final String GROUP_TYPE_USER_VAR = "user";

    /**
     * 指定审批人。
     */
    public static final String GROUP_TYPE_ASSIGNEE = "ASSIGNEE";

    /**
     * 岗位。
     */
    public static final String GROUP_TYPE_POST = "POST";

    /**
     * 上级部门领导审批。
     */
    public static final String GROUP_TYPE_UP_DEPT_POST_LEADER = "UP_DEPT_POST_LEADER";

    /**
     * 本部门岗位领导审批。
     */
    public static final String GROUP_TYPE_DEPT_POST_LEADER = "DEPT_POST_LEADER";

    /**
     * 本部门岗位前缀。
     */
    public static final String SELF_DEPT_POST_PREFIX = "SELF_DEPT_";

    /**
     * 上级部门岗位前缀。
     */
    public static final String UP_DEPT_POST_PREFIX = "UP_DEPT_";

    /**
     * 同级部门岗位前缀。
     */
    public static final String SIBLING_DEPT_POST_PREFIX = "SIBLING_DEPT_";

    /**
     * 当前流程实例所有任务的抄送数据前缀。
     */
    public static final String COPY_DATA_MAP_PREFIX = "copyDataMap_";

    /**
     * 作为临时变量存入任务变量JSONObject对象时的key。
     */
    public static final String COPY_DATA_KEY = "copyDataKey";

    /**
     * 流程中业务快照数据中，主表数据的Key。
     */
    public static final String MASTER_DATA_KEY = "masterData";

    /**
     * 流程中业务快照数据中，关联从表数据的Key。
     */
    public static final String SLAVE_DATA_KEY = "slaveData";

    /**
     * 流程任务的最近更新状态的Key。
     */
    public static final String LATEST_APPROVAL_STATUS_KEY = "latestApprovalStatus";

    /**
     * 流程用户任务待办之前的通知类型的Key。
     */
    public static final String USER_TASK_NOTIFY_TYPES_KEY = "flowNotifyTypeList";

    /**
     * 私有构造函数，明确标识该常量类的作用。
     */
    private FlowConstant() {
    }
}
