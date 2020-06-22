package com.analitic.connectors;

import com.analitic.services.ExcelLine;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class SheetConnector {
    @Value(value = "${sheet-file-path}")
    private String filePath;

    public Map<String, Integer> getSpecialtiesFromExcel(int number) {
        Map<String, Integer> sheets = new HashMap<>();
        File file = new File(filePath + "ВП " + number + " отделение.xlsx");
        try (FileInputStream inputStream = new FileInputStream(file)){
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                sheets.put(workbook.getSheetName(i), i);
            }
            return sheets;
        } catch (IOException e) {
            e.getMessage();
            return null;
        }
    }

    public void writeToExcel(ExcelLine excelLine) {
        File file = new File(filePath + "ВП " + excelLine.getDepartmentNumber() + " отделение.xlsx");
        try (FileInputStream inputStream = new FileInputStream(file)) {

            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(excelLine.getSheetNumber());

            int rowNumber = 4;

            while (sheet.getRow(rowNumber) != null) {
                rowNumber++;
            }

            XSSFRow row = sheet.createRow(rowNumber);

            CellStyle cellStyle = workbook.createCellStyle();
            CreationHelper createHelper = workbook.getCreationHelper();
            cellStyle.setDataFormat(
                    createHelper.createDataFormat().getFormat("MM.yyyy"));

            cellStyle.setFillBackgroundColor(IndexedColors.AQUA.getIndex());
            cellStyle.setFillPattern(CellStyle.BIG_SPOTS);

            XSSFCell cell = row.createCell(0);
            cell.setCellValue(new Date());
            cell.setCellStyle(cellStyle);

            inputStream.close();

            FileOutputStream out = new FileOutputStream(file);

            workbook.write(out);
            System.out.println(rowNumber);
        } catch (IOException e) {
            e.getMessage();
        }
    }
}