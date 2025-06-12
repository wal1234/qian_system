package com.qian.system.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.qian.common.annotation.Log;
import com.qian.system.common.core.controller.BaseController;
import com.qian.common.response.Response;
import com.qian.system.domain.entity.SysDictData;
import com.qian.system.service.ISysDictDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 字典数据
 */
@Tag(name = "字典数据管理")
@RestController
@RequestMapping("/system/dict/data")
public class SysDictDataController extends BaseController {
    @Autowired
    private ISysDictDataService dictDataService;

    @Operation(summary = "获取字典数据列表")
    @PreAuthorize("@ss.hasPermi('system:dict:list')")
    @GetMapping("/list/all")
    public Response<List<SysDictData>> list(SysDictData dictData) {
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        return Response.success(list);
    }

    @Operation(summary = "导出字典数据列表")
    @PreAuthorize("@ss.hasPermi('system:dict:export')")
    @Log(title = "字典数据", businessType = "EXPORT")
    @GetMapping("/export")
    public Response<Void> export(SysDictData dictData) {
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        // TODO: 实现导出逻辑
        return Response.success();
    }

    @Operation(summary = "根据字典数据编号获取详细信息")
    @PreAuthorize("@ss.hasPermi('system:dict:query')")
    @GetMapping(value = "/{dictCode}")
    public Response<SysDictData> getInfo(@PathVariable Long dictCode) {
        return Response.success(dictDataService.selectDictDataById(dictCode));
    }

    @Operation(summary = "根据字典类型查询字典数据信息")
    @GetMapping(value = "/type/{dictType}")
    public Response<List<SysDictData>> dictType(@PathVariable String dictType) {
        return Response.success(dictDataService.selectDictDataByType(dictType));
    }

    @Operation(summary = "新增字典数据")
    @PreAuthorize("@ss.hasPermi('system:dict:add')")
    @Log(title = "字典数据", businessType = "INSERT")
    @PostMapping("/add")
    public Response<Void> add(@Validated @RequestBody SysDictData dict) {
        dict.setCreateBy(getUsername());
        return toResponse(dictDataService.insertDictData(dict));
    }

    @Operation(summary = "修改字典数据")
    @PreAuthorize("@ss.hasPermi('system:dict:edit')")
    @Log(title = "字典数据", businessType = "UPDATE")
    @PutMapping("/update")
    public Response<Void> edit(@Validated @RequestBody SysDictData dict) {
        dict.setUpdateBy(getUsername());
        return toResponse(dictDataService.updateDictData(dict));
    }

    @Operation(summary = "删除字典数据")
    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @Log(title = "字典数据", businessType = "DELETE")
    @DeleteMapping("/remove/{dictCodes}")
    public Response<Void> remove(@PathVariable Long[] dictCodes) {
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