package com.qian.system.common.utils.poi;

import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Excel工具类
 */
public class ExcelUtil<T> {
    private Class<T> clazz;

    public ExcelUtil(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * 导出Excel
     * 
     * @param list 导出数据集合
     * @param sheetName 工作表的名称
     * @return 结果
     */
    public String exportExcel(List<T> list, String sheetName) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(sheetName);
            // 创建标题行
            Row headerRow = sheet.createRow(0);
            Field[] fields = clazz.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(fields[i].getName());
            }
            // 填充数据
            for (int i = 0; i < list.size(); i++) {
                Row row = sheet.createRow(i + 1);
                T item = list.get(i);
                for (int j = 0; j < fields.length; j++) {
                    fields[j].setAccessible(true);
                    Cell cell = row.createCell(j);
                    cell.setCellValue(String.valueOf(fields[j].get(item)));
                }
            }
            // 写入到输出流
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toString();
        } catch (IOException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
} 