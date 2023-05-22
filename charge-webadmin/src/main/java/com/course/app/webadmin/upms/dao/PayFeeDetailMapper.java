package com.course.app.webadmin.upms.dao;

import com.course.app.common.core.base.dao.BaseDaoMapper;
import com.course.app.webadmin.upms.model.PayFeeDetail;
import org.apache.ibatis.annotations.Param;

import java.util.*;

/**
 * 交易明细数据操作访问接口。
 *
 * @author 云翼
 * @date 2023-02-21
 */
public interface PayFeeDetailMapper extends BaseDaoMapper<PayFeeDetail> {

    /**
     * 批量插入对象列表。
     *
     * @param payFeeDetailList 新增对象列表。
     */
    void insertList(List<PayFeeDetail> payFeeDetailList);

    /**
     * 获取过滤后的对象列表。
     *
     * @param payFeeDetailFilter 主表过滤对象。
     * @param orderBy 排序字符串，order by从句的参数。
     * @return 对象列表。
     */
    List<PayFeeDetail> getPayFeeDetailList(
            @Param("payFeeDetailFilter") PayFeeDetail payFeeDetailFilter, @Param("orderBy") String orderBy);
}
