package com.analitic.connectors;

import com.analitic.services.ExcelLine;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
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

        try {
            Workbook workbook = new XSSFWorkbook(new FileInputStream(filePath + "ВП " + number + " отделение.xlsx"));

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
        String fullFilePath = filePath + "ВП " + excelLine.getDepartmentNumber() + " отделение.xlsx";
        try (Workbook workbook = WorkbookFactory.create(new File(fullFilePath));) {
            Sheet sheet = workbook.getSheetAt(excelLine.getSheetNumber());

            int rowNumber = 4;

            while (sheet.getRow(rowNumber) != null) {
                rowNumber++;
            }
            rowNumber++;

            Row row = sheet.createRow(rowNumber);
            Cell test = row.createCell(0);
            test.setCellValue("sadasd");
            workbook.write(new FileOutputStream(fullFilePath));
            System.out.println(rowNumber);
        } catch (IOException | InvalidFormatException e) {
            e.getMessage();
        }
    }
}