package com.analitic.connectors;

import com.analitic.services.ExcelLine;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
        File file = new File(filePath + "ВП " + excelLine.getDepartmentNumber() + " отделение.xlsx");
        try (FileInputStream inputStream = new FileInputStream(file)) {

            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(excelLine.getSheetNumber());

            int rowNumber = 4;

            while (sheet.getRow(rowNumber) != null) {
                rowNumber++;
            }
            rowNumber++;

            System.out.println(rowNumber);
        } catch (IOException e) {
            e.getMessage();
        }
    }
}