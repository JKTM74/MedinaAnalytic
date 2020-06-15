package com.analitic.connectors;

import lombok.Getter;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Getter
public class SheetConnector {
    private final String filePath = "\\\\192.168.1.10\\Server\\Общие папки\\Автоматизация\\Анализ направлений\\Новая аналитика тест\\"; //TODO: вынести в конфиг

    private Workbook workbook;

    public SheetConnector(int number) throws IOException, InvalidFormatException {
        workbook = WorkbookFactory.create(new File(filePath + "ВП " + number + " отделение.xlsx"));
    }

    public Map<String, Integer> getSpecialtiesFromExcel(){

        Map<String, Integer> sheets = new HashMap<>();
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            sheets.put(workbook.getSheetName(i), i);
        }
        return sheets;
    }
}