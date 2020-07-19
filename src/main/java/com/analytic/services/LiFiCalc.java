package com.analytic.services;

import com.analytic.connectors.SheetReaderWriter;
import com.analytic.models.ExcelLine;
import com.analytic.models.ExcelLineKomissii;
import com.analytic.models.SalesServices;
import com.analytic.repositories.SalesServicesRepository;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Расчет Лабораторных и Функциональных исследований.
 */
@Service
public class LiFiCalc {
    private final SheetReaderWriter sheetReaderWriter;
    private final SalesServicesRepository salesServicesRepository;

    private String date;

    public LiFiCalc(SheetReaderWriter sheetReaderWriter, SalesServicesRepository salesServicesRepository) {
        this.sheetReaderWriter = sheetReaderWriter;
        this.salesServicesRepository = salesServicesRepository;
    }

    /**
     * Запуск расчетов по анализам, процедурному кабинету, комиссиям.
     * Запись результатов в Excel файл.
     */
    public void calc() {
        Map<String, Integer> sheets = sheetReaderWriter.getSheetsFromExcel("ЛИ_ФИ_процедурный_комиссии.xlsx");

        ExcelLine analyzesLine = getAnalyzesExcelLine(sheets.get("Анализы"));
        ExcelLine procedurniyLine = getProcedurniyExcelLine(sheets.get("Процедурный"));
        ExcelLineKomissii excelLineKomissii = getKomissiiExcelLine(sheets.get("Комиссии"));

        sheetReaderWriter.writeToExcel(analyzesLine);
        sheetReaderWriter.writeToExcel(procedurniyLine);
        sheetReaderWriter.writeToExcel(excelLineKomissii);
    }

    /**
     * Создание строки для анализов, запуск вычислений.
     *
     * @param sheetNumber - номер Excel листа
     * @return стркоа готовая для записи в Excel файл.
     */
    private ExcelLine getAnalyzesExcelLine(Integer sheetNumber) {
        ExcelLine analyzesLine = ExcelLine.builder()
                .departmentNumber(0)
                .sheetName("Анализы")
                .sheetNumber(sheetNumber)
                .build();

        calcAnalyzes(analyzesLine);

        return analyzesLine;
    }

    /**
     * Создание строки для процедурного кабинета, запуск вычислений.
     *
     * @param sheetNumber - номер Excel листа
     * @return стркоа готовая для записи в Excel файл.
     */
    private ExcelLine getProcedurniyExcelLine(Integer sheetNumber) {
        ExcelLine procedurniyLine = ExcelLine.builder()
                .departmentNumber(0)
                .sheetName("Процедурный")
                .sheetNumber(sheetNumber)
                .build();

        calcProcedurniy(procedurniyLine);

        return procedurniyLine;
    }

    /**
     * Создание строки для комиссий, запуск вычислений.
     *
     * @param sheetNumber - номер Excel листа
     * @return стркоа готовая для записи в Excel файл.
     */
    private ExcelLineKomissii getKomissiiExcelLine(Integer sheetNumber) {
        ExcelLineKomissii komissiiLine = ExcelLineKomissii.builder()
                .sheetName("Комиссии")
                .sheetNumber(sheetNumber)
                .build();

        calcKomissii(komissiiLine);

        return komissiiLine;
    }

    /**
     * Получение данных из БД по анализам и добавление их в строку для записи.
     *
     * @param analyzesLine - строка анализов
     */
    private void calcAnalyzes(ExcelLine analyzesLine) {
        SalesServices allAnalyzes = salesServicesRepository.getAllAnalyzes(date);
        SalesServices allKlAnalyzes = salesServicesRepository.getAllKlAnalyzes(date);
        SalesServices streetAnalyzes = salesServicesRepository.getStreetAnalyzes(date);

        analyzesLine.setAllKLVars(allAnalyzes.getPatientsCount(), allAnalyzes.getServicesCount(), allAnalyzes.getSumPrice());
        analyzesLine.setKLVars(allKlAnalyzes.getPatientsCount(), allKlAnalyzes.getServicesCount(), allKlAnalyzes.getSumPrice());
        analyzesLine.setStreetVars(streetAnalyzes.getPatientsCount(), streetAnalyzes.getServicesCount(), streetAnalyzes.getSumPrice());
    }

    /**
     * Получение данных из БД по процедурному кабинету и добавление их в строку для записи.
     *
     * @param procedurniyLine - строка процедурного кабинета
     */
    private void calcProcedurniy(ExcelLine procedurniyLine) {
        SalesServices allProcedurniy = salesServicesRepository.getAllProcedurniy(date);
        SalesServices allKlProcedurniy = salesServicesRepository.getAllKlProcedurniy(date);
        SalesServices streetProcedurniy = salesServicesRepository.getStreetProcedurniy(date);

        procedurniyLine.setAllKLVars(allProcedurniy.getPatientsCount(), allProcedurniy.getServicesCount(), allProcedurniy.getSumPrice());
        procedurniyLine.setKLVars(allKlProcedurniy.getPatientsCount(), allKlProcedurniy.getServicesCount(), allKlProcedurniy.getSumPrice());
        procedurniyLine.setStreetVars(streetProcedurniy.getPatientsCount(), streetProcedurniy.getServicesCount(), streetProcedurniy.getSumPrice());
    }

    /**
     * Получение данных из БД по комиссиям и добавление их в строку для записи.
     *
     * @param komissiiLine - строка комиссий
     */
    private void calcKomissii(ExcelLineKomissii komissiiLine) {
        SalesServices streetKomissii = salesServicesRepository.getStreetKomissii(date);
        Double orgSum = salesServicesRepository.getKomissiiOrg(date);

        komissiiLine.setStreetVars(streetKomissii.getSumPrice(), streetKomissii.getPatientsCount(), streetKomissii.getServicesCount());

        komissiiLine.setOrgSum(orgSum);
    }

    /**
     * Устанавливает дату из гуя для запросов к БД.
     *
     * @param date дата с гуя
     */
    public void setDate(Date date) {
        this.date = new SimpleDateFormat("MM-yyyy").format(date);
    }
}
