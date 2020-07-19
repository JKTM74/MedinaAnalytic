package com.analytic.connectors;

import com.analytic.models.ExcelLine;
import com.analytic.models.ExcelLineKomissii;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.*;
import org.openxmlformats.schemas.drawingml.x2006.chart.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Здесь происходит вся работа с Excel файлом.
 */
@PropertySource(ignoreResourceNotFound = true, value = "classpath:assignment1.properties")
@Component
public class SheetReaderWriter {
    @Value("${sheet-file-path}")
    private String filePath;
    private Date date;

    /**
     * Метод по имени файла открывает Excel файл и считывает все листы с их порядковыми номерами.
     *
     * @param fileName - имя файла
     * @return - мапу <Имя Листа, Порядковый Номер Листа В Файле>
     */
    public Map<String, Integer> getSheetsFromExcel(String fileName) {
        Map<String, Integer> sheets = new HashMap<>();
        File file = new File(filePath + fileName);
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

    /**
     * Запись комиссий в Excel файл.
     * Формирование гарфиков комиссий.
     *
     * @param excelLineKomissii строка комиссий
     */
    public void writeToExcel(ExcelLineKomissii excelLineKomissii) {
        File file = new File(filePath + "ЛИ_ФИ_процедурный_комиссии.xlsx");

        try {
            FileInputStream inputStream = new FileInputStream(file);
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            inputStream.close();

            XSSFSheet sheet = workbook.getSheet(excelLineKomissii.getSheetName());

            int rowNumber = getRowNumber(sheet);

            XSSFRow row = sheet.createRow(rowNumber);

            XSSFCellStyle cellDateStyle = getCellDateStyle(workbook);
            XSSFCellStyle decimalCellStyle = getCellDecimalStyle(workbook);
            XSSFCellStyle integerCellStyle = getCellIntegerStyle(workbook);

            XSSFCell cell = row.createCell(0);
            cell.setCellStyle(cellDateStyle);
            cell.setCellValue(date);

            rowNumber++;

            createKomissiiCells(row, rowNumber, excelLineKomissii, integerCellStyle, decimalCellStyle);

            drawCharts(workbook, excelLineKomissii.getSheetName(), rowNumber);

            FileOutputStream out = new FileOutputStream(file);
            workbook.write(out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Запись анализов и процедурного в ЛИ ФИ.
     * Формирование графиков для них.
     *
     * @param excelLine анализы или процедурный
     */
    public void writeToExcel(ExcelLine excelLine) {
        File file = new File(filePath + "ЛИ_ФИ_процедурный_комиссии.xlsx");

        try {
            FileInputStream inputStream = new FileInputStream(file);
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            inputStream.close();

            XSSFSheet sheet = workbook.getSheet(excelLine.getSheetName());

            int rowNumber = getRowNumber(sheet);

            XSSFRow row = sheet.createRow(rowNumber);

            XSSFCellStyle cellDateStyle = getCellDateStyle(workbook);
            XSSFCellStyle decimalCellStyle = getCellDecimalStyle(workbook);
            XSSFCellStyle integerCellStyle = getCellIntegerStyle(workbook);

            XSSFCell cell = row.createCell(0);
            cell.setCellStyle(cellDateStyle);
            cell.setCellValue(date);

            rowNumber++;

            createAllKlCells(row, rowNumber, excelLine, integerCellStyle, decimalCellStyle);

            createKLCells(row, rowNumber, excelLine, integerCellStyle, decimalCellStyle);

            createStreetCells(row, rowNumber, excelLine, integerCellStyle, decimalCellStyle);

            drawCharts(workbook, excelLine.getSheetName(), rowNumber);

            FileOutputStream out = new FileOutputStream(file);
            workbook.write(out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Запись строк для специальностей всех отделений.
     * Формирование графиков для них.
     * Расчет свода по отделению.
     *
     * @param excelLines
     */
    public void writeToExcel(List<ExcelLine> excelLines) {
        int departmentNumber = excelLines.get(0).getDepartmentNumber();
        File file = new File(filePath + "ВП " + departmentNumber + " отделение.xlsx");

        try {
            FileInputStream inputStream = new FileInputStream(file);
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            inputStream.close();

            for (ExcelLine excelLine : excelLines) {
                writeLineAndDrawCharts(workbook, excelLine);
            }

            calcDepartmentResume(workbook, departmentNumber);

            FileOutputStream out = new FileOutputStream(file);
            workbook.write(out);
            out.close();

        } catch (IOException e) {
            e.getMessage();
        }
    }

    /**
     * Подсчет свода отделения.
     * Находит последнюю строку на листе и копирует формулы ячеек, подменяя номер строки.
     *
     * @param workbook         - Excel файл
     * @param departmentNumber - номер отделения
     */
    private void calcDepartmentResume(XSSFWorkbook workbook, int departmentNumber) {
        XSSFSheet sheet = workbook.getSheet("Свод " + departmentNumber + " отделения");

        if (sheet != null) {
            int rowNumber = getRowNumber(sheet);
            XSSFRow oldRow = sheet.getRow(rowNumber - 1);
            XSSFRow newRow = sheet.createRow(rowNumber);

            int cellNum = 0;
            while (oldRow.cellIterator().hasNext()) {
                XSSFCell oldCell = oldRow.getCell(cellNum);
                if (oldCell == null) break;

                XSSFCell newCell = newRow.createCell(cellNum);

                if (oldCell.getCellType() == CellType.FORMULA) {
                    newCell.setCellFormula(oldCell.getCellFormula().replace(String.valueOf(rowNumber), String.valueOf(rowNumber + 1)));
                    newCell.setCellStyle(oldCell.getCellStyle());
                } else if (cellNum == 0) {
                    newCell.setCellValue(date);
                    newCell.setCellStyle(oldCell.getCellStyle());
                }

                cellNum++;
            }
            drawCharts(workbook, "Свод " + departmentNumber + " отделения", rowNumber + 1);
        }
    }

    /**
     * Создание строки.
     * Создание ячеек в строке.
     * Запись результатов в ячейки.
     * Запуск отрисовки графиков.
     *
     * @param workbook  = Excel файл
     * @param excelLine - строка
     */
    private void writeLineAndDrawCharts(XSSFWorkbook workbook, ExcelLine excelLine) {
        XSSFSheet sheet = workbook.getSheet(excelLine.getSheetName());

        int rowNumber = getRowNumber(sheet);

        XSSFRow row = sheet.createRow(rowNumber);

        XSSFCellStyle cellDateStyle = getCellDateStyle(workbook);
        XSSFCellStyle decimalCellStyle = getCellDecimalStyle(workbook);
        XSSFCellStyle integerCellStyle = getCellIntegerStyle(workbook);

        XSSFCell cell = row.createCell(0);
        cell.setCellStyle(cellDateStyle);
        cell.setCellValue(date);

        rowNumber++;

        createAllKlCells(row, rowNumber, excelLine, integerCellStyle, decimalCellStyle);

        createKLCells(row, rowNumber, excelLine, integerCellStyle, decimalCellStyle);

        createStreetCells(row, rowNumber, excelLine, integerCellStyle, decimalCellStyle);

        createResultCells(row, rowNumber, integerCellStyle, decimalCellStyle);

        drawCharts(workbook, excelLine.getSheetName(), rowNumber);
    }

    /**
     * Создаёт стиль для ячеек с датой
     *
     * @param workbook - книга
     * @return стиль для ячейки даты
     */
    private XSSFCellStyle getCellDateStyle(XSSFWorkbook workbook) {
        IndexedColorMap colorMap = workbook.getStylesSource().getIndexedColors();
        XSSFColor color = new XSSFColor(new java.awt.Color(153, 204, 255), colorMap);

        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(workbook.createDataFormat().getFormat("MMMM yyyy"));
        setCellColorAndStyle(cellStyle, color);

        return cellStyle;
    }

    /**
     * Создаёт стиль для ячеек с дробным значением
     *
     * @param workbook - книга
     * @return стиль для ячейки с дробным значением
     */
    private XSSFCellStyle getCellDecimalStyle(XSSFWorkbook workbook) {
        IndexedColorMap colorMap = workbook.getStylesSource().getIndexedColors();
        XSSFColor color = new XSSFColor(new java.awt.Color(255, 215, 181), colorMap);

        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(workbook.createDataFormat().getFormat("# ##0.00"));
        setCellColorAndStyle(cellStyle, color);

        return cellStyle;
    }

    /**
     * Создаёт стиль для ячеек с целым значением.
     *
     * @param workbook - книга
     * @return стиль для ячейки с целым значением
     */
    private XSSFCellStyle getCellIntegerStyle(XSSFWorkbook workbook) {
        IndexedColorMap colorMap = workbook.getStylesSource().getIndexedColors();
        XSSFColor color = new XSSFColor(new java.awt.Color(255, 215, 181), colorMap);

        XSSFCellStyle cellStyle = workbook.createCellStyle();
        setCellColorAndStyle(cellStyle, color);

        return cellStyle;
    }

    /**
     * Задает фон ячейки и границы
     *
     * @param cellStyle - стиль который нужно настроить
     * @param color     - цвет который нужно применить к стилю
     */
    private void setCellColorAndStyle(XSSFCellStyle cellStyle, XSSFColor color) {
        cellStyle.setFillForegroundColor(color);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
    }

    /**
     * Отрисовка графиков.
     * Получает все графики нужного листа, для каждого из них и для каждой
     * кривой меняет последнюю цифру на номер строки где все расчеты.
     *
     * @param workbook  - книга
     * @param sheetName - имя листа
     * @param rowNumber - номер строки
     */
    private void drawCharts(XSSFWorkbook workbook, String sheetName, int rowNumber) {
        XSSFSheet sheet = workbook.getSheet("Д " + sheetName);

        XSSFDrawing drawing = sheet.createDrawingPatriarch();
        List<XSSFChart> charts = drawing.getCharts();

        for (XSSFChart chart : charts) {
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

    /**
     * Вызывает методы, которые создают ячейки с нужными стилями, значениями/формулами, порядковыми номерами.
     * Все методы ниже аналогичны.
     *
     * @param row              - строка
     * @param rowNumber        - номер строки
     * @param excelLine        - строка с данными
     * @param integerCellStyle - стиль для ячейки с целым значением
     * @param decimalCellStyle - стиль для ячейки с дробным значением
     */
    private void createAllKlCells(XSSFRow row, int rowNumber, ExcelLine excelLine, XSSFCellStyle integerCellStyle, XSSFCellStyle decimalCellStyle) {
        createDecimalCellWithValue(1, excelLine.getAllKLSum(), row, decimalCellStyle);
        createIntegerCellWithValue(2, excelLine.getAllKLPatientsCount(), row, integerCellStyle);
        createDecimalCellWithFormula(3, getDivisionFormula("B", "C", rowNumber), row, decimalCellStyle);
        createIntegerCellWithValue(4, excelLine.getAllKLServicesCount(), row, integerCellStyle);
        createDecimalCellWithFormula(5, getDivisionFormula("E", "C", rowNumber), row, decimalCellStyle);
    }

    private void createKLCells(XSSFRow row, int rowNumber, ExcelLine excelLine, XSSFCellStyle integerCellStyle, XSSFCellStyle decimalCellStyle) {
        createDecimalCellWithValue(6, excelLine.getKLSum(), row, decimalCellStyle);
        createIntegerCellWithValue(7, excelLine.getKLPatientsCount(), row, integerCellStyle);
        createDecimalCellWithFormula(8, getDivisionFormula("G", "H", rowNumber), row, decimalCellStyle);
        createIntegerCellWithValue(9, excelLine.getKLServicesCount(), row, integerCellStyle);
        createDecimalCellWithFormula(10, getDivisionFormula("J", "H", rowNumber), row, decimalCellStyle);
    }

    private void createStreetCells(XSSFRow row, int rowNumber, ExcelLine excelLine, XSSFCellStyle integerCellStyle, XSSFCellStyle decimalCellStyle) {
        createDecimalCellWithValue(11, excelLine.getStreetSum(), row, decimalCellStyle);
        createIntegerCellWithValue(12, excelLine.getStreetPatientsCount(), row, integerCellStyle);
        createDecimalCellWithFormula(13, getDivisionFormula("L", "M", rowNumber), row, decimalCellStyle);
        createIntegerCellWithValue(14, excelLine.getStreetServicesCount(), row, integerCellStyle);
        createDecimalCellWithFormula(15, getDivisionFormula("O", "M", rowNumber), row, decimalCellStyle);
    }

    private void createResultCells(XSSFRow row, int rowNumber, XSSFCellStyle integerCellStyle, XSSFCellStyle decimalCellStyle) {
        createDecimalCellWithFormula(16, "B" + rowNumber + " + L" + rowNumber, row, decimalCellStyle);
        createIntegerCellWithFormula(17, "C" + rowNumber + " + M" + rowNumber, row, integerCellStyle);
        createDecimalCellWithFormula(18, "Q" + rowNumber + " + R" + rowNumber, row, decimalCellStyle);
        createIntegerCellWithFormula(19, "E" + rowNumber + " + O" + rowNumber, row, integerCellStyle);
        createDecimalCellWithFormula(20, getDivisionFormula("T", "R", rowNumber), row, decimalCellStyle);
    }

    private void createKomissiiCells(XSSFRow row, int rowNumber, ExcelLineKomissii excelLineKomissii, XSSFCellStyle integerCellStyle, XSSFCellStyle decimalCellStyle) {
        createDecimalCellWithValue(1, excelLineKomissii.getStreetSum(), row, decimalCellStyle);
        createIntegerCellWithValue(2, excelLineKomissii.getPatientsCount(), row, integerCellStyle);
        createDecimalCellWithFormula(3, getDivisionFormula("B", "C", rowNumber), row, decimalCellStyle);
        createIntegerCellWithValue(4, excelLineKomissii.getServicesCount(), row, integerCellStyle);
        createDecimalCellWithFormula(5, getDivisionFormula("E", "C", rowNumber), row, decimalCellStyle);
        createDecimalCellWithValue(6, excelLineKomissii.getOrgSum(), row, decimalCellStyle);
        createDecimalCellWithFormula(7, "B" + rowNumber + " + G" + rowNumber, row, decimalCellStyle);
    }

    /**
     * Создает ячейки применяя нужный стиль, устанавливая нужный порядковый номер ячейки, устанавливая нужное значение.
     * Все методы ниже аналогичны.
     *
     * @param columnIndex      - порядковый номер ячейки
     * @param value            - значение
     * @param row              - строка
     * @param decimalCellStyle - стиль ячейки
     */
    private void createDecimalCellWithValue(int columnIndex, double value, XSSFRow row, XSSFCellStyle decimalCellStyle) {
        XSSFCell cell;
        cell = row.createCell(columnIndex);
        cell.setCellStyle(decimalCellStyle);
        cell.setCellValue(value);
    }

    private void createDecimalCellWithFormula(int columnIndex, String formula, XSSFRow row, XSSFCellStyle decimalCellStyle) {
        XSSFCell cell;
        cell = row.createCell(columnIndex);
        cell.setCellStyle(decimalCellStyle);
        cell.setCellFormula(formula);
    }

    private void createIntegerCellWithValue(int columnIndex, double value, XSSFRow row, XSSFCellStyle decimalCellStyle) {
        XSSFCell cell;
        cell = row.createCell(columnIndex);
        cell.setCellStyle(decimalCellStyle);
        cell.setCellValue(value);
    }

    private void createIntegerCellWithFormula(int columnIndex, String formula, XSSFRow row, XSSFCellStyle decimalCellStyle) {
        XSSFCell cell;
        cell = row.createCell(columnIndex);
        cell.setCellStyle(decimalCellStyle);
        cell.setCellFormula(formula);
    }

    /**
     * Метод для получения последней строки листа
     *
     * @param sheet - лист
     * @return номер последней строки листа
     */
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

    /**
     * Создаёт формулу деления значений из двух ячеек
     *
     * @param dividend  - делимое
     * @param divider   - делитель
     * @param rowNumber - порядковый номер строки
     * @return формула для ячейки
     */
    private String getDivisionFormula(String dividend, String divider, int rowNumber) {
        return "IF(" + divider + rowNumber + "<>0," + dividend + rowNumber + "/" + divider + rowNumber + ",0.00)";
    }

    /**
     * Устанавливает дату из гуя
     *
     * @param date - дата
     */
    public void setDate(Date date) {
        this.date = date;
    }
}