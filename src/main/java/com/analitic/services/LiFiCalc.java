package com.analitic.services;

import com.analitic.connectors.SheetConnector;
import com.analitic.models.ExcelLine;
import com.analitic.models.ExcelLineKomissii;
import com.analitic.models.SalesServices;
import com.analitic.repositories.SalesServicesRepository;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Map;

@Service
public class LiFiCalc {
    private final SheetConnector sheetConnector;
    private final SalesServicesRepository salesServicesRepository;

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-yyyy");
    private final String date = simpleDateFormat.format(java.sql.Date.valueOf("2020-06-01"));


    public LiFiCalc(SheetConnector sheetConnector, SalesServicesRepository salesServicesRepository) {
        this.sheetConnector = sheetConnector;
        this.salesServicesRepository = salesServicesRepository;
    }

    public void calc(){
        Map<String, Integer> sheets = sheetConnector.getSheetsFromExcel("ЛИ_ФИ_процедурный_комиссии.xlsx");

        ExcelLine analyzesLine = getAnalyzesExcelLine(sheets.get("Анализы"));
        ExcelLine procedurniyLine = getProcedurniyExcelLine(sheets.get("Процедурный"));
        ExcelLineKomissii excelLineKomissii = getKomissiiExcelLine(sheets.get("Комиссии"));

        sheetConnector.writeToExcel(analyzesLine);
        sheetConnector.writeToExcel(procedurniyLine);
        sheetConnector.writeToExcel(excelLineKomissii);
    }

    private ExcelLine getAnalyzesExcelLine(Integer sheetNumber) {
        ExcelLine analyzesLine = ExcelLine.builder()
                                    .departmentNumber(0)
                                    .sheetName("Анализы")
                                    .sheetNumber(sheetNumber)
                                .build();

        calcAnalyzes(analyzesLine);

        return analyzesLine;
    }

    private ExcelLine getProcedurniyExcelLine(Integer sheetNumber) {
        ExcelLine procedurniyLine = ExcelLine.builder()
                .departmentNumber(0)
                .sheetName("Процедурный")
                .sheetNumber(sheetNumber)
                .build();

        calcProcedurniy(procedurniyLine);

        return procedurniyLine;
    }

    private ExcelLineKomissii getKomissiiExcelLine(Integer sheetNumber) {
        ExcelLineKomissii komissiiLine = ExcelLineKomissii.builder()
                .sheetName("Комиссии")
                .sheetNumber(sheetNumber)
                .build();

        calcKomissii(komissiiLine);

        return komissiiLine;
    }

    private void calcAnalyzes(ExcelLine analyzesLine) {
        SalesServices allAnalyzes = salesServicesRepository.getAllAnalyzes(date);
        SalesServices allKlAnalyzes = salesServicesRepository.getAllKlAnalyzes(date);
        SalesServices streetAnalyzes = salesServicesRepository.getStreetAnalyzes(date);

        analyzesLine.setAllKLVars(allAnalyzes.getPatientsCount(), allAnalyzes.getServicesCount(), allAnalyzes.getSumPrice());
        analyzesLine.setKLVars(allKlAnalyzes.getPatientsCount(), allKlAnalyzes.getServicesCount(), allKlAnalyzes.getSumPrice());
        analyzesLine.setStreetVars(streetAnalyzes.getPatientsCount(), streetAnalyzes.getServicesCount(), streetAnalyzes.getSumPrice());
    }

    private void calcProcedurniy(ExcelLine procedurniyLine) {
        SalesServices allProcedurniy = salesServicesRepository.getAllProcedurniy(date);
        SalesServices allKlProcedurniy = salesServicesRepository.getAllKlProcedurniy(date);
        SalesServices streetProcedurniy = salesServicesRepository.getStreetProcedurniy(date);

        procedurniyLine.setAllKLVars(allProcedurniy.getPatientsCount(), allProcedurniy.getServicesCount(), allProcedurniy.getSumPrice());
        procedurniyLine.setKLVars(allKlProcedurniy.getPatientsCount(), allKlProcedurniy.getServicesCount(), allKlProcedurniy.getSumPrice());
        procedurniyLine.setStreetVars(streetProcedurniy.getPatientsCount(), streetProcedurniy.getServicesCount(), streetProcedurniy.getSumPrice());
    }

    private void calcKomissii(ExcelLineKomissii komissiiLine) {
        SalesServices streetKomissii = salesServicesRepository.getStreetKomissii(date);
        Double orgSum = salesServicesRepository.getKomissiiOrg(date);

        komissiiLine.setStreetVars(streetKomissii.getSumPrice(), streetKomissii.getPatientsCount(), streetKomissii.getServicesCount());

        komissiiLine.setOrgSum(orgSum);
    }
}
