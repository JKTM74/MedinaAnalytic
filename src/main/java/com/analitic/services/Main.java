package com.analitic.services;


import com.analitic.connectors.SheetReaderWriter;
import com.analitic.models.ExcelLine;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Исток здесь.
 */
@Service
public class Main {
    private final DepartmentsCalc departmentsCalc;
    private final SpecialtyCalc specialtyCalc;
    private final SheetReaderWriter sheetReaderWriter;
    private final LiFiCalc liFiCalc;

    // если меняются отделения, править тут
    private static final int[] DEPARTMENT_NUMBERS = {1, 2, 3};


    public Main(DepartmentsCalc departmentsCalc, SpecialtyCalc specialtyCalc, SheetReaderWriter sheetReaderWriter, LiFiCalc liFiCalc) {
        this.departmentsCalc = departmentsCalc;
        this.specialtyCalc = specialtyCalc;
        this.sheetReaderWriter = sheetReaderWriter;
        this.liFiCalc = liFiCalc;
    }

    /**
     * Метод вызывается по кнопке гуя и запрускает все расчеты.
     * Собирает лист листов ExcelLine по каждому отделению и отправляет их на запись.
     * Отдельно расчитывает ЛИ_ФИ.
     * @param date - приходит с гуя и с помошью метода setDate прокидываю в остальные классы
     */
    public void startAnalytic(Date date) {
        setDate(date);

        List<List<ExcelLine>> excelLines = Arrays.stream(DEPARTMENT_NUMBERS)
                .mapToObj(departmentsCalc::getExcelLines)
                .collect(Collectors.toList());

        excelLines.stream().forEach(sheetReaderWriter::writeToExcel);

        liFiCalc.calc();
    }

    /**
     * Прокидываю дату из гуя дальше в другие классы.
     * @param date
     */
    private void setDate(Date date){
        liFiCalc.setDate(date);
        sheetReaderWriter.setDate(date);
        specialtyCalc.setDate(date);
    }
}

/*TODO: * Сделать ENUM для ассоциаций юзеров по специальностям которых не ясно куда отнести относительно шита. +
 * Написать методы и запросы для сбора аналитики из БД по каждому отделению. +
 * Написать методы для формирования строк в шитах и вбросах этих строк. +
 * Придумать как запускать файл не по расписанию, а по кнопке из МИСа и собирать аналитику по дате которую выберет пользователь. +
 * Реализовать настройку через конфиг. +-
 * Хорошо было бы добавить логирование. -
 * Хорошо было бы добавить гуй. +
 */