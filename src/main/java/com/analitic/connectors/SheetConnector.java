package com.analitic.connectors;

import lombok.Getter;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Getter
public class SheetConnector {
    private final String filePath = "C:\\Аналитика\\"; //TODO: вынести в конфиг

    private Workbook workbook;

    private List<String> specialities;

    public SheetConnector(int number) throws IOException, InvalidFormatException {
        workbook = WorkbookFactory.create(new File(filePath + "ВП " + number + " отделение.xlsx"));
        specialities = initSpecialitiesArray(workbook);
    }

    private List<String> initSpecialitiesArray(Workbook workbook){

        List<String> sheetNames = new ArrayList<>();
        for (int i=0; i<workbook.getNumberOfSheets(); i++) {
            sheetNames.add( workbook.getSheetName(i) );
        }
        return sheetNames;
    }


}
