package com.course.app.webadmin.upms.dao;

import com.course.app.common.core.base.dao.BaseDaoMapper;
import com.course.app.webadmin.upms.model.PrePaymentFeeDetail;
import org.apache.ibatis.annotations.Param;

import java.util.*;

/**
 * 预缴费明细数据操作访问接口。
 *
 * @author 云翼
 * @date 2023-02-21
 */
public interface PrePaymentFeeDetailMapper extends BaseDaoMapper<PrePaymentFeeDetail> {

    /**
     * 批量插入对象列表。
     *
     * @param prePaymentFeeDetailList 新增对象列表。
     */
    void insertList(List<PrePaymentFeeDetail> prePaymentFeeDetailList);

    /**
     * 获取过滤后的对象列表。
     *
     * @param prePaymentFeeDetailFilter 主表过滤对象。
     * @param orderBy 排序字符串，order by从句的参数。
     * @return 对象列表。
     */
    List<PrePaymentFeeDetail> getPrePaymentFeeDetailList(
            @Param("prePaymentFeeDetailFilter") PrePaymentFeeDetail prePaymentFeeDetailFilter, @Param("orderBy") String orderBy);
}
