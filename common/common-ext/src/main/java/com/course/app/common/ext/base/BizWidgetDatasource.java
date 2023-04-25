package com.course.app.common.ext.base;

import cn.hutool.core.collection.CollUtil;
import com.course.app.common.core.object.MyOrderParam;
import com.course.app.common.core.object.MyPageData;
import com.course.app.common.core.object.MyPageParam;

import java.util.List;
import java.util.Map;

/**
 * 业务组件获取数据的数据源接口。
 * 如果业务服务集成了common-ext组件，可以通过实现该接口的方式，为BizWidgetController访问提供数据。
 * 对于没有集成common-ext组件的服务，可以通过http方式，为BizWidgetController访问提供数据。
 *
 * @author 云翼
 * @date 2023-02-21
 */
public interface BizWidgetDatasource {

    /**
     * 获取指定通用业务组件的数据。
     *
     * @param filter     过滤参数。不同的数据源参数不同。这里我们以键值对的方式传递。
     * @param orderParam 排序参数。
     * @param pageParam  分页参数。
     * @return 查询后的分页数据列表。
     */
    MyPageData<Map<String, Object>> getDataList(
            Map<String, Object> filter, MyOrderParam orderParam, MyPageParam pageParam);

    /**
     * 获取指定主键Id的数据对象。
     *
     * @param id 主键Id。
     * @return 指定主键Id的数据对象。
     */
    default Map<String, Object> getDataById(String id) {
        List<Map<String, Object>> dataList = this.getDataListByIds(CollUtil.newArrayList(id));
        return CollUtil.isEmpty(dataList) ? null : dataList.get(0);
    }

    /**
     * 获取指定主键Id的数据对象。
     *
     * @param ids 主键Id。
     * @return 指定主键Id的数据对象。
     */
    List<Map<String, Object>> getDataListByIds(List<String> ids);
}
