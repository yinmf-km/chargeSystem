package com.course.app.common.ext.controller;

import com.alibaba.fastjson.JSONObject;
import com.course.app.common.core.object.*;
import com.course.app.common.ext.util.BizWidgetDatasourceExtHelper;
import com.course.app.common.core.annotation.MyRequestBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 业务组件获取数据的访问接口。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Slf4j
@RestController
@RequestMapping("/admin/commonext/bizwidget")
public class BizWidgetController {

    @Autowired
    private BizWidgetDatasourceExtHelper bizWidgetDatasourceExtHelper;

    @PostMapping("/list")
    public ResponseResult<MyPageData<Map<String, Object>>> list(
            @MyRequestBody(required = true) String widgetType,
            @MyRequestBody JSONObject filter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        String appCode = TokenData.takeFromRequest().getAppCode();
        MyPageData<Map<String, Object>> pageData =
                bizWidgetDatasourceExtHelper.getDataList(appCode, widgetType, filter, orderParam, pageParam);
        return ResponseResult.success(pageData);
    }

    @PostMapping("/view")
    public ResponseResult<List<Map<String, Object>>> view(
            @MyRequestBody(required = true) String widgetType,
            @MyRequestBody(required = true) String ids) {
        String appCode = TokenData.takeFromRequest().getAppCode();
        List<Map<String, Object>> dataMapList =
                bizWidgetDatasourceExtHelper.getDataListByIds(appCode, widgetType, ids);
        return ResponseResult.success(dataMapList);
    }
}
