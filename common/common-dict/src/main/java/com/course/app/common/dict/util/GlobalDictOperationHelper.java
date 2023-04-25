package com.course.app.common.dict.util;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import com.course.app.common.core.object.ResponseResult;
import com.course.app.common.core.object.MyPageData;
import com.course.app.common.core.object.MyPageParam;
import com.course.app.common.core.util.MyModelUtil;
import com.course.app.common.core.util.MyPageUtil;
import com.course.app.common.dict.dto.GlobalDictDto;
import com.course.app.common.dict.model.GlobalDict;
import com.course.app.common.dict.service.GlobalDictService;
import com.course.app.common.dict.vo.GlobalDictVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 全局编码字典操作的通用帮助对象。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Slf4j
@Component
public class GlobalDictOperationHelper {

    @Autowired
    private GlobalDictService globalDictService;

    /**
     * 获取全部编码字典列表。
     *
     * @param globalDictDtoFilter 过滤对象。
     * @param pageParam           分页参数。
     * @return 字典的数据列表。
     */
    public ResponseResult<MyPageData<GlobalDictVo>> listAllGlobalDict(
            GlobalDictDto globalDictDtoFilter, MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        GlobalDict filter = MyModelUtil.copyTo(globalDictDtoFilter, GlobalDict.class);
        List<GlobalDict> dictList = globalDictService.getGlobalDictList(filter, null);
        List<GlobalDictVo> dictVoList = MyModelUtil.copyCollectionTo(dictList, GlobalDictVo.class);
        long totalCount = 0L;
        if (dictList instanceof Page) {
            totalCount = ((Page<GlobalDict>) dictList).getTotal();
        }
        return ResponseResult.success(MyPageUtil.makeResponseData(dictVoList, totalCount));
    }
}
