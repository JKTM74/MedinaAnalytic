package com.analitic.services;

import com.analitic.connectors.SheetConnector;
import com.analitic.models.ExcelLine;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Main {
    private final Department department;
    private final SheetConnector sheetConnector;
    private final LiFiCalc liFiCalc;

    // если меняются отделения, править тут
    private static final int[] DEPARTMENT_NUMBERS = {1, 2, 3};

    public Main(Department department, SheetConnector sheetConnector, LiFiCalc liFiCalc) {
        this.department = department;
        this.sheetConnector = sheetConnector;
        this.liFiCalc = liFiCalc;
        startAnalytic();
    }

    private void startAnalytic() {

        List<List<ExcelLine>> excelLines = Arrays.stream(DEPARTMENT_NUMBERS)
                .mapToObj(department::getExcelLines)
                .collect(Collectors.toList());

        excelLines.stream().forEach(sheetConnector::writeToExcel);

        liFiCalc.calc();
    }
}

/*TODO: * Сделать ENUM для ассоциаций юзеров по специальностям которых не ясно куда отнести относительно шита. +
        * Написать методы и запросы для сбора аналитики из БД по каждому отделению. +
        * Написать методы для формирования строк в шитах и вбросах этих строк. +-
        * Придумать как запускать файл не по расписанию, а по кнопке из МИСа и собирать аналитику по дате которую выберет пользователь.
        * Реализовать настройку через конфиг. +-
        * Хорошо было бы добавить логирование.
        * Хорошо было бы добавить гуй.
*/