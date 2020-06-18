package com.analitic.services;

import com.analitic.repositories.SalesServicesRepository;
import com.analitic.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Main {
    private final UserRepository userRepository;
    private final SalesServicesRepository salesServicesRepository;
    private final Department department;

    private static final int[] DEPARTMENT_NUMBERS = {1, 2, 3}; // если меняются отделения, править тут

    public Main(UserRepository userRepository, SalesServicesRepository salesServicesRepository, Department department) {
        this.userRepository = userRepository;
        this.salesServicesRepository = salesServicesRepository;
        this.department = department;
        startAnalytic();
    }

    private void startAnalytic() {

        List<List<ExcelLine>> excelLines = Arrays.stream(DEPARTMENT_NUMBERS)
                .mapToObj(department::getExcelLines)
                .collect(Collectors.toList());

        excelLines.stream()
                .flatMap(List::stream)
                .forEach(System.out::println);
    }
}

/*TODO: * Сделать ENUM для ассоциаций юзеров по специальностям которых не ясно куда отнести относительно шита. +
        * Написать методы и запросы для сбора аналитики из БД по каждому отделению.
        * Написать методы для формирования строк в шитах и вбросах этих строк.
        * Придумать как запускать файл не по расписанию, а по кнопке из МИСа и собирать аналитику по дате которую выберет пользователь.
        * Реализовать настройку через конфиг.
        * Хорошо было бы добавить логирование.
        * Хорошо было бы добавить гуй.
*/