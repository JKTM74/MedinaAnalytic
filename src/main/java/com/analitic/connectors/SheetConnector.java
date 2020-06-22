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
                if (sheet.getRow(rowNumber).getCell(0) != null) {
                    rowNumber++;
                } else{
                    break;
                }
            }

            XSSFRow row = sheet.createRow(rowNumber);

            CellStyle cellStyle = workbook.createCellStyle();
            CreationHelper createHelper = workbook.getCreationHelper();
            cellStyle.setDataFormat(
                    createHelper.createDataFormat().getFormat("MMMM yyyy"));

            XSSFCell cell = row.createCell(0);
            cell.setCellValue(new Date());
            cell.setCellStyle(cellStyle);

            rowNumber++;
            row.createCell(1).setCellValue(excelLine.getAllKLSum());
            row.createCell(2).setCellValue(excelLine.getAllKLPatientsCount());
            row.createCell(3).setCellFormula(getCellFormula("B", "C", rowNumber));
            row.createCell(4).setCellValue(excelLine.getAllKLServicesCount());
            row.createCell(5).setCellFormula(getCellFormula("E", "C", rowNumber));
            row.createCell(6).setCellValue(excelLine.getKLSum());
            row.createCell(7).setCellValue(excelLine.getKLPatientsCount());
            row.createCell(8).setCellFormula(getCellFormula("G", "H", rowNumber));
            row.createCell(9).setCellValue(excelLine.getKLServicesCount());
            row.createCell(10).setCellFormula(getCellFormula("J", "H", rowNumber));
            row.createCell(11).setCellValue(excelLine.getStreetSum());
            row.createCell(12).setCellValue(excelLine.getStreetPatientsCount());
            row.createCell(13).setCellFormula(getCellFormula("L", "M", rowNumber));
            row.createCell(14).setCellValue(excelLine.getStreetServicesCount());
            row.createCell(15).setCellFormula(getCellFormula("O", "M", rowNumber));
            row.createCell(16).setCellFormula(getCellFormula("B", "L", rowNumber));
            row.createCell(17).setCellFormula("C" + rowNumber + "+ M" + rowNumber);
            row.createCell(18).setCellFormula(getCellFormula("Q", "R", rowNumber));
            row.createCell(19).setCellFormula("E" + rowNumber + "+ O" + rowNumber);
            row.createCell(20).setCellFormula(getCellFormula("T", "R", rowNumber));

            inputStream.close();

            FileOutputStream out = new FileOutputStream(file);

            workbook.write(out);
            System.out.println(rowNumber);
        } catch (IOException e) {
            e.getMessage();
        }
    }

    private String getCellFormula(String dividend, String divider, int rowNumber){
        return "IF(" + divider + rowNumber + "<>0," + dividend + rowNumber + "/" + divider + rowNumber + ",0)";
    }
}