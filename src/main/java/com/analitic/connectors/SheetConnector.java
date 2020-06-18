package com.analitic.connectors;

import lombok.Getter;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class SheetConnector {
    @Value(value = "${sheet-file-path}")
    private String filePath;

    public Map<String, Integer> getSpecialtiesFromExcel(int number){
        Map<String, Integer> sheets = new HashMap<>();

        try {
            Workbook workbook = WorkbookFactory.create(new File(filePath + "ВП " + number + " отделение.xlsx"));
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                sheets.put(workbook.getSheetName(i), i);
            }
            return sheets;
        } catch (IOException | InvalidFormatException e) {
            e.getMessage();
            return null;
        }
    }
}