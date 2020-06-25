package com.analitic.connectors;

import com.analitic.models.ExcelLine;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.*;
import org.openxmlformats.schemas.drawingml.x2006.chart.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

            XSSFCellStyle cellDateStyle = getCellDateStyle(workbook);
            XSSFCellStyle decimalCellStyle = getCellDecimalStyle(workbook);
            XSSFCellStyle integerCellStyle = getCellIntegerStyle(workbook);

            XSSFCell cell = row.createCell(0);
            cell.setCellStyle(cellDateStyle);
            cell.setCellValue(new Date());

            rowNumber++;

            createAllKlCells(row, rowNumber, excelLine, integerCellStyle, decimalCellStyle);

            createKLCells(row, rowNumber, excelLine, integerCellStyle, decimalCellStyle);

            createStreetCells(row, rowNumber, excelLine, integerCellStyle, decimalCellStyle);

            createResultCells(row, rowNumber, integerCellStyle, decimalCellStyle);

            inputStream.close();

            drawCharts(workbook, excelLine.getSheetName(), rowNumber);

            FileOutputStream out = new FileOutputStream(file);
            workbook.write(out);
            out.close();

        } catch (IOException e) {
            e.getMessage();
        }
    }

    private XSSFCellStyle getCellDateStyle(XSSFWorkbook workbook) {
        IndexedColorMap colorMap = workbook.getStylesSource().getIndexedColors();
        XSSFColor color = new XSSFColor(new java.awt.Color(153,204,255), colorMap);

        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(workbook.createDataFormat().getFormat("MMMM yyyy"));
        cellStyle.setFillForegroundColor(color);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);

        return cellStyle;
    }

    private XSSFCellStyle getCellDecimalStyle(XSSFWorkbook workbook) {
        IndexedColorMap colorMap = workbook.getStylesSource().getIndexedColors();
        XSSFColor color = new XSSFColor(new java.awt.Color(255, 215, 181), colorMap);

        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00"));
        cellStyle.setFillForegroundColor(color);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);

        return cellStyle;
    }

    private XSSFCellStyle getCellIntegerStyle(XSSFWorkbook workbook) {
        IndexedColorMap colorMap = workbook.getStylesSource().getIndexedColors();
        XSSFColor color = new XSSFColor(new java.awt.Color(255, 215, 181), colorMap);

        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(color);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);

        return cellStyle;
    }

    private void drawCharts(XSSFWorkbook workbook, String sheetName, int rowNumber) {
        XSSFSheet sheet = workbook.getSheet("Д " + sheetName);

        XSSFDrawing drawing = sheet.createDrawingPatriarch();
        List<XSSFChart> charts = drawing.getCharts();

        for (XSSFChart chart: charts) {
            CTChart ctChart = chart.getCTChart();
            CTPlotArea ctPlotArea = ctChart.getPlotArea();

            CTLineChart ctLineChart = ctPlotArea.getLineChartArray(0);
            List<CTLineSer> ctLineSears = ctLineChart.getSerList();

            for (CTLineSer ctLineSer : ctLineSears) {
                CTAxDataSource cttAxDataSource = ctLineSer.getCat();
                CTNumDataSource dataSource = ctLineSer.getVal();
                String y = dataSource.getNumRef().getF();
                String x = cttAxDataSource.getStrRef().getF();

                String newX = x.substring(0, x.length() - 2) + rowNumber;
                String newY = y.substring(0, y.length() - 2) + rowNumber;

                dataSource.getNumRef().setF(newY);
                cttAxDataSource.getStrRef().setF(newX);
            }
        }
    }

    private void createResultCells(XSSFRow row, int rowNumber, XSSFCellStyle integerCellStyle, XSSFCellStyle decimalCellStyle) {
        XSSFCell cell;

        cell = row.createCell(16);
        cell.setCellStyle(decimalCellStyle);
        cell.setCellFormula("B" + rowNumber + " + L" + rowNumber);

        cell = row.createCell(17);
        cell.setCellStyle(integerCellStyle);
        cell.setCellFormula("C" + rowNumber + " + M" + rowNumber);

        cell = row.createCell(18);
        cell.setCellStyle(decimalCellStyle);
        cell.setCellFormula(getDivisionFormula("Q", "R", rowNumber));

        cell = row.createCell(19);
        cell.setCellStyle(integerCellStyle);
        cell.setCellFormula("E" + rowNumber + " + O" + rowNumber);

        cell = row.createCell(20);
        cell.setCellStyle(decimalCellStyle);
        cell.setCellFormula(getDivisionFormula("T", "R", rowNumber));
    }

    private void createStreetCells(XSSFRow row, int rowNumber, ExcelLine excelLine, XSSFCellStyle integerCellStyle, XSSFCellStyle decimalCellStyle) {
        XSSFCell cell;

        cell = row.createCell(11);
        cell.setCellStyle(decimalCellStyle);
        cell.setCellValue(excelLine.getStreetSum());

        cell = row.createCell(12);
        cell.setCellStyle(integerCellStyle);
        cell.setCellValue(excelLine.getStreetPatientsCount());

        cell = row.createCell(13);
        cell.setCellStyle(decimalCellStyle);
        cell.setCellFormula(getDivisionFormula("L", "M", rowNumber));

        cell = row.createCell(14);
        cell.setCellStyle(integerCellStyle);
        cell.setCellValue(excelLine.getStreetServicesCount());

        cell = row.createCell(15);
        cell.setCellStyle(decimalCellStyle);
        cell.setCellFormula(getDivisionFormula("O", "M", rowNumber));
    }

    private void createKLCells(XSSFRow row, int rowNumber, ExcelLine excelLine, XSSFCellStyle integerCellStyle, XSSFCellStyle decimalCellStyle) {
        XSSFCell cell;

        cell = row.createCell(6);
        cell.setCellStyle(decimalCellStyle);
        cell.setCellValue(excelLine.getKLSum());

        cell = row.createCell(7);
        cell.setCellStyle(integerCellStyle);
        cell.setCellValue(excelLine.getKLPatientsCount());

        cell = row.createCell(8);
        cell.setCellStyle(decimalCellStyle);
        cell.setCellFormula(getDivisionFormula("G", "H", rowNumber));

        cell = row.createCell(9);
        cell.setCellStyle(integerCellStyle);
        cell.setCellValue(excelLine.getKLServicesCount());

        cell = row.createCell(10);
        cell.setCellStyle(decimalCellStyle);
        cell.setCellFormula(getDivisionFormula("J", "H", rowNumber));
    }

    private void createAllKlCells(XSSFRow row, int rowNumber, ExcelLine excelLine, XSSFCellStyle integerCellStyle, XSSFCellStyle decimalCellStyle) {
        XSSFCell cell;

        cell = row.createCell(1);
        cell.setCellStyle(decimalCellStyle);
        cell.setCellValue(excelLine.getAllKLSum());

        cell = row.createCell(2);
        cell.setCellStyle(integerCellStyle);
        cell.setCellValue(excelLine.getAllKLPatientsCount());

        cell = row.createCell(3);
        cell.setCellStyle(decimalCellStyle);
        cell.setCellFormula(getDivisionFormula("B", "C", rowNumber));

        cell = row.createCell(4);
        cell.setCellStyle(integerCellStyle);
        cell.setCellValue(excelLine.getAllKLServicesCount());

        cell = row.createCell(5);
        cell.setCellStyle(decimalCellStyle);
        cell.setCellFormula(getDivisionFormula("E", "C", rowNumber));
    }

    private int getRowNumber(XSSFSheet sheet) {
        int rowNumber = 0;

        while (sheet.getRow(rowNumber) != null) {
            XSSFCell cell = sheet.getRow(rowNumber).getCell(0);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                rowNumber++;
            } else {
                break;
            }
        }
        return rowNumber;
    }

    private String getDivisionFormula(String dividend, String divider, int rowNumber) {
        return "IF(" + divider + rowNumber + "<>0," + dividend + rowNumber + "/" + divider + rowNumber + ",0.00)";
    }
}