package com.course.app.common.core.object;

import java.util.LinkedList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分页数据的应答返回对象。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyPageData<T> {
    /**
     * 数据列表。
     */
    private List<T> dataList;
    /**
     * 数据总数量。
     */
    private Long totalCount;

    /**
     * 为了保持前端的数据格式兼容性，在没有数据的时候，需要返回空分页对象。
     * @return 空分页对象。
     */
    public static <T> MyPageData<T> emptyPageData() {
        return new MyPageData<>(new LinkedList<>(), 0L);
    }
}
