package com.course.app.webadmin.upms.dao;

import com.course.app.common.core.base.dao.BaseDaoMapper;
import com.course.app.webadmin.upms.model.CollegeScore;
import org.apache.ibatis.annotations.Param;

import java.util.*;

/**
 * 高考成绩信息数据操作访问接口。
 *
 * @author 云翼
 * @date 2023-02-21
 */
public interface CollegeScoreMapper extends BaseDaoMapper<CollegeScore> {

    /**
     * 批量插入对象列表。
     *
     * @param collegeScoreList 新增对象列表。
     */
    void insertList(List<CollegeScore> collegeScoreList);

    /**
     * 获取过滤后的对象列表。
     *
     * @param collegeScoreFilter 主表过滤对象。
     * @param orderBy 排序字符串，order by从句的参数。
     * @return 对象列表。
     */
    List<CollegeScore> getCollegeScoreList(
            @Param("collegeScoreFilter") CollegeScore collegeScoreFilter, @Param("orderBy") String orderBy);
}
