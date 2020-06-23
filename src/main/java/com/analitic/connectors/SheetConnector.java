package com.analitic.connectors;

import com.analitic.services.ExcelLine;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
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
        try (FileInputStream inputStream = new FileInputStream(file)) {
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

        try {
            FileInputStream inputStream = new FileInputStream(file);
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(excelLine.getSheetNumber());

            int rowNumber = getRowNumber(sheet);

            XSSFRow row = sheet.createRow(rowNumber);

            CellStyle cellDateStyle = workbook.createCellStyle();
            cellDateStyle.setDataFormat(workbook.createDataFormat().getFormat("MMMM yyyy"));

            XSSFCellStyle decimalCellStyle = workbook.createCellStyle();
            decimalCellStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00"));

            XSSFCell cell = row.createCell(0);
            cell.setCellStyle(cellDateStyle);
            cell.setCellValue(new Date());

            rowNumber++;

            createAllKlCells(row, rowNumber, excelLine, decimalCellStyle);

            createKLCells(row, rowNumber, excelLine, decimalCellStyle);

            createStreetCells(row, rowNumber, excelLine, decimalCellStyle);

            createResultCells(row, rowNumber, decimalCellStyle);

            inputStream.close();

            FileOutputStream out = new FileOutputStream(file);

            workbook.write(out);

        } catch (IOException e) {
            e.getMessage();
        }
    }

    private void createResultCells(XSSFRow row, int rowNumber, XSSFCellStyle decimalCellStyle) {
        XSSFCell cell;

        cell = row.createCell(16);
        cell.setCellStyle(decimalCellStyle);
        cell.setCellFormula("B" + rowNumber + "+ L" + rowNumber);

        row.createCell(17).setCellFormula("C" + rowNumber + "+ M" + rowNumber);

        cell = row.createCell(18);
        cell.setCellStyle(decimalCellStyle);
        cell.setCellFormula(getCellFormula("Q", "R", rowNumber));

        row.createCell(19).setCellFormula("E" + rowNumber + "+ O" + rowNumber);

        cell = row.createCell(20);
        cell.setCellStyle(decimalCellStyle);
        cell.setCellFormula(getCellFormula("T", "R", rowNumber));
    }

    private void createStreetCells(XSSFRow row, int rowNumber, ExcelLine excelLine, XSSFCellStyle decimalCellStyle) {
        XSSFCell cell;

        row.createCell(11).setCellValue(excelLine.getStreetSum());

        row.createCell(12).setCellValue(excelLine.getStreetPatientsCount());

        cell = row.createCell(13);
        cell.setCellStyle(decimalCellStyle);
        cell.setCellFormula(getCellFormula("L", "M", rowNumber));

        row.createCell(14).setCellValue(excelLine.getStreetServicesCount());

        cell = row.createCell(15);
        cell.setCellStyle(decimalCellStyle);
        cell.setCellFormula(getCellFormula("O", "M", rowNumber));
    }

    private void createKLCells(XSSFRow row, int rowNumber, ExcelLine excelLine, XSSFCellStyle decimalCellStyle) {
        XSSFCell cell;

        row.createCell(6).setCellValue(excelLine.getKLSum());

        row.createCell(7).setCellValue(excelLine.getKLPatientsCount());

        cell = row.createCell(8);
        cell.setCellStyle(decimalCellStyle);
        cell.setCellFormula(getCellFormula("G", "H", rowNumber));

        row.createCell(9).setCellValue(excelLine.getKLServicesCount());

        cell = row.createCell(10);
        cell.setCellStyle(decimalCellStyle);
        cell.setCellFormula(getCellFormula("J", "H", rowNumber));
    }

    private void createAllKlCells(XSSFRow row, int rowNumber, ExcelLine excelLine, XSSFCellStyle decimalCellStyle) {
        XSSFCell cell;

        row.createCell(1).setCellValue(excelLine.getAllKLSum());

        row.createCell(2).setCellValue(excelLine.getAllKLPatientsCount());

        cell = row.createCell(3);
        cell.setCellStyle(decimalCellStyle);
        cell.setCellFormula(getCellFormula("B", "C", rowNumber));

        row.createCell(4).setCellValue(excelLine.getAllKLServicesCount());

        cell = row.createCell(5);
        cell.setCellStyle(decimalCellStyle);
        cell.setCellFormula(getCellFormula("E", "C", rowNumber));
    }

    private int getRowNumber(XSSFSheet sheet) {
        int rowNumber = 0;

        while (sheet.getRow(rowNumber) != null) {
            XSSFCell cell = sheet.getRow(rowNumber).getCell(0);
            if (cell != null && cell.getCellType() != XSSFCell.CELL_TYPE_BLANK) {
                rowNumber++;
            } else {
                break;
            }
        }
        return rowNumber;
    }

    private String getCellFormula(String dividend, String divider, int rowNumber) {
        return "IF(" + divider + rowNumber + "<>0," + dividend + rowNumber + "/" + divider + rowNumber + ",0)";
    }
}