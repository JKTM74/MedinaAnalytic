package com.analitic.services;

import com.analitic.connectors.SheetConnector;
import com.analitic.repositories.UserRepository;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Main {
    private final UserRepository userRepository;

    private static final int[] DEPARTMENT_NUMBERS = {1, 2, 3}; // если меняются отделения, править тут

    public Main( UserRepository userRepository) {
        this.userRepository = userRepository;
        startAnalytic();
    }

    private void startAnalytic() {

        List<Department> department = Arrays.stream(DEPARTMENT_NUMBERS)
                .mapToObj(num ->
                        Department.builder()
                                .number(num).build()).collect(Collectors.toList());

        department.forEach(this::departmentAnalyse);
    }

    private void departmentAnalyse(Department department) {

        try {
            SheetConnector sheetConnector = new SheetConnector(department.getNumber());

            department.setSpecialtiesFromExcel(sheetConnector.getSpecialtiesFromExcel());

            department.setUsers(userRepository.findUsersByDepartment(department.getNumber()));

            department.connectUserToSpeciality();

            department.analyzeSpecialties();

        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }
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