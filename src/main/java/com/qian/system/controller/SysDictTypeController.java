package com.qian.system.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.qian.common.annotation.Log;
import com.qian.system.common.core.controller.BaseController;
import com.qian.common.response.Response;
import com.qian.system.domain.SysDictType;
import com.qian.common.enums.system.BusinessType;
import com.qian.system.service.ISysDictTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 字典类型
 */
@Tag(name = "字典类型管理")
@RestController
@RequestMapping("/system/dict/type")
public class SysDictTypeController extends BaseController {
    @Autowired
    private ISysDictTypeService dictTypeService;

    @Operation(summary = "获取字典类型列表")
    @PreAuthorize("@ss.hasPermi('system:dict:list')")
    @GetMapping("/all")
    public Response<List<SysDictType>> list(SysDictType dictType) {
        List<SysDictType> list = dictTypeService.selectDictTypeList(dictType);
        return Response.success(list);
    }

    @Operation(summary = "导出字典类型列表")
    @PreAuthorize("@ss.hasPermi('system:dict:export')")
    @Log(title = "字典类型", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public Response<Void> export(SysDictType dictType) {
        List<SysDictType> list = dictTypeService.selectDictTypeList(dictType);
        // TODO: 实现导出逻辑
        return Response.success();
    }

    @Operation(summary = "根据字典类型编号获取详细信息")
    @PreAuthorize("@ss.hasPermi('system:dict:query')")
    @GetMapping(value = "/{dictId}")
    public Response<SysDictType> getInfo(@PathVariable Long dictId) {
        return Response.success(dictTypeService.selectDictTypeById(dictId));
    }

    @Operation(summary = "新增字典类型")
    @PreAuthorize("@ss.hasPermi('system:dict:add')")
    @Log(title = "字典类型", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public Response<Void> add(@Validated @RequestBody SysDictType dict) {
        if ("1".equals(dictTypeService.checkDictTypeUnique(dict))) {
            return Response.error("新增字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        dict.setCreateBy(getUsername());
        return toResponse(dictTypeService.insertDictType(dict));
    }

    @Operation(summary = "修改字典类型")
    @PreAuthorize("@ss.hasPermi('system:dict:edit')")
    @Log(title = "字典类型", businessType = BusinessType.UPDATE)
    @PutMapping("/update")
    public Response<Void> edit(@Validated @RequestBody SysDictType dict) {
        if ("1".equals(dictTypeService.checkDictTypeUnique(dict))) {
            return Response.error("修改字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        dict.setUpdateBy(getUsername());
        return toResponse(dictTypeService.updateDictType(dict));
    }

    @Operation(summary = "删除字典类型")
    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/remove/{dictIds}")
    public Response<Void> remove(@PathVariable Long[] dictIds) {
        return toResponse(dictTypeService.deleteDictTypeByIds(dictIds));
    }

    @Operation(summary = "刷新字典缓存")
    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @Log(title = "字典类型", businessType = BusinessType.CLEAN)
    @DeleteMapping("/refreshCache")
    public Response<Void> refreshCache() {
        dictTypeService.clearDictCache();
        return Response.success();
    }

    @Operation(summary = "获取字典选择框列表")
    @GetMapping("/optionselect")
    public Response<List<SysDictType>> optionselect() {
        return Response.success(dictTypeService.selectDictTypeAll());
    }

    /**
     * 响应返回结果
     * 
     * @param rows 影响行数
     * @return 操作结果
     */
    private Response<Void> toResponse(int rows) {
        return rows > 0 ? Response.success() : Response.error("操作失败");
    }
} 