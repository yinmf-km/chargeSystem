package com.course.app.webadmin.upms.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.TypeReference;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import com.course.app.webadmin.upms.dto.SysMenuDto;
import com.course.app.webadmin.upms.vo.SysMenuVo;
import com.course.app.webadmin.upms.model.SysMenu;
import com.course.app.webadmin.upms.model.SysDataPerm;
import com.course.app.webadmin.upms.model.constant.SysMenuType;
import com.course.app.webadmin.upms.service.SysMenuService;
import com.course.app.webadmin.upms.service.SysDataPermService;
import com.course.app.common.core.constant.ErrorCodeEnum;
import com.course.app.common.core.object.*;
import com.course.app.common.core.util.*;
import com.course.app.common.core.validator.UpdateGroup;
import com.course.app.common.core.annotation.MyRequestBody;
import com.course.app.common.log.annotation.OperationLog;
import com.course.app.common.log.model.constant.SysOperationLogType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单管理接口控制器类。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Api(tags = "菜单管理接口")
@Slf4j
@RestController
@RequestMapping("/admin/upms/sysMenu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private SysDataPermService sysDataPermService;

    /**
     * 添加新菜单操作。
     *
     * @param sysMenuDto           新菜单对象。
     * @param permCodeIdListString 与当前菜单Id绑定的权限Id列表，多个权限之间逗号分隔。
     * @return 应答结果对象，包含新增菜单的主键Id。
     */
    @ApiOperationSupport(ignoreParameters = {"sysMenuDto.menuId"})
    @OperationLog(type = SysOperationLogType.ADD)
    @PostMapping("/add")
    public ResponseResult<Long> add(
            @MyRequestBody SysMenuDto sysMenuDto, @MyRequestBody String permCodeIdListString) {
        String errorMessage = MyCommonUtil.getModelValidationError(sysMenuDto);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        SysMenu sysMenu = MyModelUtil.copyTo(sysMenuDto, SysMenu.class);
        if (sysMenu.getParentId() != null) {
            SysMenu parentSysMenu = sysMenuService.getById(sysMenu.getParentId());
            if (parentSysMenu == null) {
                errorMessage = "数据验证失败，关联的父菜单不存在！";
                return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
            }
            if (parentSysMenu.getOnlineFormId() != null) {
                errorMessage = "数据验证失败，不能为动态表单菜单添加子菜单！";
                return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
            }
        }
        CallResult result = sysMenuService.verifyRelatedData(sysMenu, null, permCodeIdListString);
        if (!result.isSuccess()) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, result.getErrorMessage());
        }
        Set<Long> permCodeIdSet = null;
        if (result.getData() != null) {
            permCodeIdSet = result.getData().getObject("permCodeIdSet", new TypeReference<Set<Long>>(){});
        }
        sysMenuService.saveNew(sysMenu, permCodeIdSet);
        return ResponseResult.success(sysMenu.getMenuId());
    }

    /**
     * 更新菜单数据操作。
     *
     * @param sysMenuDto           更新菜单对象。
     * @param permCodeIdListString 与当前菜单Id绑定的权限Id列表，多个权限之间逗号分隔。
     * @return 应答结果对象。
     */
    @OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/update")
    public ResponseResult<Void> update(
            @MyRequestBody SysMenuDto sysMenuDto, @MyRequestBody String permCodeIdListString) {
        String errorMessage = MyCommonUtil.getModelValidationError(sysMenuDto, Default.class, UpdateGroup.class);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        SysMenu originalSysMenu = sysMenuService.getById(sysMenuDto.getMenuId());
        if (originalSysMenu == null) {
            errorMessage = "数据验证失败，当前菜单并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        SysMenu sysMenu = MyModelUtil.copyTo(sysMenuDto, SysMenu.class);
        if (ObjectUtil.notEqual(originalSysMenu.getOnlineFormId(), sysMenu.getOnlineFormId())) {
            if (originalSysMenu.getOnlineFormId() == null) {
                errorMessage = "数据验证失败，不能为当前菜单添加在线表单Id属性！";
                return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
            }
            if (sysMenu.getOnlineFormId() == null) {
                errorMessage = "数据验证失败，不能去掉当前菜单的在线表单Id属性！";
                return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
            }
        }
        if (originalSysMenu.getOnlineFormId() != null
                && originalSysMenu.getMenuType().equals(SysMenuType.TYPE_BUTTON)) {
            errorMessage = "数据验证失败，在线表单的内置菜单不能编辑！";
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        CallResult result = sysMenuService.verifyRelatedData(sysMenu, originalSysMenu, permCodeIdListString);
        if (!result.isSuccess()) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, result.getErrorMessage());
        }
        Set<Long> permCodeIdSet = null;
        if (result.getData() != null) {
            permCodeIdSet = result.getData().getObject("permCodeIdSet", new TypeReference<Set<Long>>(){});
        }
        if (!sysMenuService.update(sysMenu, originalSysMenu, permCodeIdSet)) {
            errorMessage = "数据验证失败，当前权限字并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        return ResponseResult.success();
    }

    /**
     * 删除指定菜单操作。
     *
     * @param menuId 指定菜单主键Id。
     * @return 应答结果对象。
     */
    @OperationLog(type = SysOperationLogType.DELETE)
    @PostMapping("/delete")
    public ResponseResult<Void> delete(@MyRequestBody Long menuId) {
        if (MyCommonUtil.existBlankArgument(menuId)) {
            return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
        }
        String errorMessage;
        SysMenu menu = sysMenuService.getById(menuId);
        if (menu == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        if (menu.getOnlineFormId() != null && menu.getMenuType().equals(SysMenuType.TYPE_BUTTON)) {
            errorMessage = "数据验证失败，在线表单的内置菜单不能删除！";
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        // 对于在线表单，无需进行子菜单的验证，而是在删除的时候，连同子菜单一起删除。
        if (menu.getOnlineFormId() == null && sysMenuService.hasChildren(menuId)) {
            errorMessage = "数据验证失败，当前菜单存在下级菜单！";
            return ResponseResult.error(ErrorCodeEnum.HAS_CHILDREN_DATA, errorMessage);
        }
        List<SysDataPerm> dataPermList = sysDataPermService.getSysDataPermListByMenuId(menuId);
        if (CollUtil.isNotEmpty(dataPermList)) {
            SysDataPerm dataPerm = dataPermList.get(0);
            errorMessage = "数据验证失败，当前菜单正在被数据权限 [" + dataPerm.getDataPermName() + "] 引用，不能直接删除！";
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        if (!sysMenuService.remove(menu)) {
            errorMessage = "数据操作失败，菜单不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        return ResponseResult.success();
    }

    /**
     * 获取全部菜单列表。
     *
     * @return 应答结果对象，包含全部菜单数据列表。
     */
    @PostMapping("/list")
    public ResponseResult<List<SysMenuVo>> list() {
        List<SysMenu> sysMenuList = sysMenuService.getAllListByOrder("showOrder");
        return ResponseResult.success(MyModelUtil.copyCollectionTo(sysMenuList, SysMenuVo.class));
    }

    /**
     * 查看指定菜单数据详情。
     *
     * @param menuId 指定菜单主键Id。
     * @return 应答结果对象，包含菜单详情。
     */
    @GetMapping("/view")
    public ResponseResult<SysMenuVo> view(@RequestParam Long menuId) {
        if (MyCommonUtil.existBlankArgument(menuId)) {
            return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
        }
        SysMenu sysMenu = sysMenuService.getByIdWithRelation(menuId, MyRelationParam.full());
        if (sysMenu == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        SysMenuVo sysMenuVo = MyModelUtil.copyTo(sysMenu, SysMenuVo.class);
        return ResponseResult.success(sysMenuVo);
    }

    /**
     * 以字典形式返回目录和菜单类型的菜单管理数据集合。字典的键值为[menuId, menuName]。
     * 白名单接口，登录用户均可访问。
     *
     * @return 应答结果对象，包含的数据为 List<Map<String, String>>，map中包含两条记录，key的值分别是id和name，value对应具体数据。
     */
    @GetMapping("/listMenuDict")
    public ResponseResult<List<Map<String, Object>>> listMenuDict() {
        Collection<SysMenu> resultList = sysMenuService.getAllListByOrder("showOrder");
        resultList = resultList.stream()
                .filter(m -> m.getMenuType() <= SysMenuType.TYPE_MENU).collect(Collectors.toList());
        return ResponseResult.success(
                MyCommonUtil.toDictDataList(resultList, SysMenu::getMenuId, SysMenu::getMenuName, SysMenu::getParentId));
    }

    /**
     * 查询菜单的权限资源地址列表。同时返回详细的分配路径。
     *
     * @param menuId 菜单Id。
     * @param url    权限资源地址过滤条件。
     * @return 应答对象，包含从菜单到权限资源的权限分配路径信息的查询结果列表。
     */
    @GetMapping("/listSysPermWithDetail")
    public ResponseResult<List<Map<String, Object>>> listSysPermWithDetail(Long menuId, String url) {
        if (MyCommonUtil.isBlankOrNull(menuId)) {
            return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
        }
        return ResponseResult.success(sysMenuService.getSysPermListWithDetail(menuId, url));
    }

    /**
     * 查询菜单的用户列表。同时返回详细的分配路径。
     *
     * @param menuId    菜单Id。
     * @param loginName 登录名。
     * @return 应答对象，包含从菜单到用户的完整权限分配路径信息的查询结果列表。
     */
    @GetMapping("/listSysUserWithDetail")
    public ResponseResult<List<Map<String, Object>>> listSysUserWithDetail(Long menuId, String loginName) {
        if (MyCommonUtil.isBlankOrNull(menuId)) {
            return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
        }
        return ResponseResult.success(sysMenuService.getSysUserListWithDetail(menuId, loginName));
    }
}
