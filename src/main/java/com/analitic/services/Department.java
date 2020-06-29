package com.analitic.services;

import com.analitic.connectors.SheetConnector;
import com.analitic.enums.SpecialitiesEnum;
import com.analitic.models.ExcelLine;
import com.analitic.models.User;
import com.analitic.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class Department {
    private final UserRepository userRepository;
    private final Specialty specialty;
    private final SheetConnector sheetConnector;

    public Department(UserRepository userRepository, Specialty specialty, SheetConnector sheetConnector) {
        this.userRepository = userRepository;
        this.specialty = specialty;
        this.sheetConnector = sheetConnector;
    }

    public List<ExcelLine> getExcelLines(int departmentNumber) {
        List<User> users = userRepository.findUsersByDepartment(departmentNumber);

        Map<String, Integer> sheets = getSheets(sheetConnector.getSheetsFromExcel(departmentNumber));

        List<ExcelLine> excelLines = new ArrayList<>();

        if (departmentNumber == 2){
            List<User> uziUsers = getUziUsers(users);
            excelLines.add(getUziLine(uziUsers));
        }

        setCorrectSpecialtiesForUsers(users, sheets.keySet());

        excelLines.addAll(getLines(sheets, users, departmentNumber));

        return excelLines;
    }

    private List<ExcelLine> getLines(Map<String, Integer> sheets, List<User> users, int departmentNumber){
        return sheets.entrySet().stream()
                .map(s ->
                        ExcelLine.builder()
                            .departmentNumber(departmentNumber)
                            .sheetName(s.getKey())
                            .sheetNumber(s.getValue())
                            .build())
                .peek( (e) ->
                        users.stream()
                        .filter(u -> u.getSpecialty().equals(e.getSheetName()))
                        .forEach((u) -> specialty.setFieldsValues(u, e)))
                .collect(Collectors.toList());
    }

    private Map<String, Integer> getSheets(Map<String, Integer> specialties){
        return specialties.entrySet().stream()
                .filter(entry -> !entry.getKey().startsWith("Д ")
                        && !entry.getKey().toLowerCase().contains("свод")
                        && !entry.getKey().toLowerCase().contains("узи"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private void setCorrectSpecialtiesForUsers(List<User> users, Set<String> specialties) {
        for (User user : users) {
            boolean found = false;
            for (String specialty : specialties) {
                if (!found && specialty.toLowerCase().contains(user.getSpecialty().toLowerCase())) {
                    user.setSpecialty(specialty);
                    found = true;
                }
            }
            if (!found) {
                for (SpecialitiesEnum specialtyEnum : EnumSet.allOf(SpecialitiesEnum.class)) {
                    if (!found && specialtyEnum.anySpecialtyNames.contains(user.getSpecialty())) {
                        user.setSpecialty(specialtyEnum.name);
                        found = true;
                    }
                }
            }
        }
    }

    private List<User> getUziUsers(List<User> users){
        return users.stream()
                .filter((u) -> u.getSpecialty().toLowerCase().contains("узи") ||
                        u.getSpecialty().toLowerCase().contains("ультразвук"))
                .collect(Collectors.toList());
    }

    private ExcelLine getUziLine(List<User> users){
        ExcelLine uziLine = ExcelLine.builder()
                .departmentNumber(2)
                .sheetNumber(10)
                .sheetName("УЗИ")
                .build();

        for (User user: users){
            specialty.setUziFieldsValues(user, uziLine);
        }

        return uziLine;
    }
}
