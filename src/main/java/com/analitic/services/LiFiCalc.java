package com.analitic.services;

import com.analitic.connectors.SheetConnector;
import com.analitic.models.ExcelLine;
import com.analitic.repositories.SalesServicesRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class LiFiCalc {
    private final SheetConnector sheetConnector;
    private final SalesServicesRepository salesServicesRepository;

    public LiFiCalc(SheetConnector sheetConnector, SalesServicesRepository salesServicesRepository) {
        this.sheetConnector = sheetConnector;
        this.salesServicesRepository = salesServicesRepository;
    }

    public List<ExcelLine> getExcelLines(){

        Map<String, Integer> sheets = sheetConnector.getSheetsFromExcel("ЛИ_ФИ_процедурный_комиссии.xlsx");

        List<ExcelLine> excelLines = new ArrayList<>();



        return excelLines;
    }
}
