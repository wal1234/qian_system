package com.qian.system.controller;

import com.qian.system.domain.SysDictData;
import com.qian.system.domain.SysDictType;
import com.qian.common.annotation.Log;
import com.qian.system.common.core.controller.BaseController;
import com.qian.common.enums.system.BusinessType;
import com.qian.common.response.Response;
import com.qian.system.service.ISysDictDataService;
import com.qian.system.service.ISysDictTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典管理
 */
@Tag(name = "字典管理")
@RestController
@RequestMapping("/system/dict")
public class SysDictController extends BaseController {
    @Autowired
    private ISysDictTypeService dictTypeService;

    @Autowired
    private ISysDictDataService dictDataService;

    @Operation(summary = "获取字典类型列表")
    @PreAuthorize("@ss.hasPermi('system:dict:list')")
    @GetMapping("/type-list")
    public Response<List<SysDictType>> typeList(SysDictType dictType) {
        List<SysDictType> list = dictTypeService.selectDictTypeList(dictType);
        return Response.success(list);
    }

    @Operation(summary = "新增字典类型")
    @PreAuthorize("@ss.hasPermi('system:dict:add')")
    @Log(title = "字典类型", businessType = BusinessType.INSERT)
    @PostMapping("/type-add")
    public Response<Void> add(@RequestBody SysDictType dict) {
        if ("1".equals(dictTypeService.checkDictTypeUnique(dict))) {
            return Response.error("新增字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        dict.setCreateBy(getUsername());
        return toResponse(dictTypeService.insertDictType(dict));
    }

    @Operation(summary = "修改字典类型")
    @PreAuthorize("@ss.hasPermi('system:dict:edit')")
    @Log(title = "字典类型", businessType = BusinessType.UPDATE)
    @PutMapping("/type-update")
    public Response<Void> edit(@RequestBody SysDictType dict) {
        if ("1".equals(dictTypeService.checkDictTypeUnique(dict))) {
            return Response.error("修改字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        dict.setUpdateBy(getUsername());
        return toResponse(dictTypeService.updateDictType(dict));
    }

    @Operation(summary = "删除字典类型")
    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/type-delete/{dictIds}")
    public Response<Void> removeType(@PathVariable Long[] dictIds) {
        return toResponse(dictTypeService.deleteDictTypeByIds(dictIds));
    }

    @Operation(summary = "获取字典数据列表")
    @PreAuthorize("@ss.hasPermi('system:dict:list')")
    @GetMapping("/data-list")
    public Response<List<SysDictData>> dataList(SysDictData dictData) {
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        return Response.success(list);
    }

    @Operation(summary = "新增字典数据")
    @PreAuthorize("@ss.hasPermi('system:dict:add')")
    @Log(title = "字典数据", businessType = BusinessType.INSERT)
    @PostMapping("/data-add")
    public Response<Void> add(@RequestBody SysDictData dict) {
        dict.setCreateBy(getUsername());
        return toResponse(dictDataService.insertDictData(dict));
    }

    @Operation(summary = "修改字典数据")
    @PreAuthorize("@ss.hasPermi('system:dict:edit')")
    @Log(title = "字典数据", businessType = BusinessType.UPDATE)
    @PutMapping("/data-update")
    public Response<Void> edit(@RequestBody SysDictData dict) {
        dict.setUpdateBy(getUsername());
        return toResponse(dictDataService.updateDictData(dict));
    }

    @Operation(summary = "删除字典数据")
    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @Log(title = "字典数据", businessType = BusinessType.DELETE)
    @DeleteMapping("/data-delete/{dictCodes}")
    public Response<Void> removeData(@PathVariable Long[] dictCodes) {
        return toResponse(dictDataService.deleteDictDataByIds(dictCodes));
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