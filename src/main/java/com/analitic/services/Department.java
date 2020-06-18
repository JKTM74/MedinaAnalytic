package com.analitic.services;

import com.analitic.connectors.SheetConnector;
import com.analitic.enums.SpecialitiesEnum;
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
        List<ExcelLine> excelLines = new ArrayList<>();

        Map<String, Integer> sheets = sheetConnector.getSpecialtiesFromExcel(departmentNumber).entrySet().stream()
                .filter(entry -> !entry.getKey().startsWith("Д ")
                        && !entry.getKey().contains("свод"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        setCorrectUsersSpecialty(users, sheets.keySet());

        for (Map.Entry entry : sheets.entrySet()) {
            ExcelLine excelLine = new ExcelLine();

            excelLine.setSheetConnector(sheetConnector);
            excelLine.setSheetName((String) entry.getKey());
            excelLine.setSheetNumber((Integer) entry.getValue());

            List<User> usersBySpecialty = users.stream()
                    .filter(u -> u.getSpecialty().equals(excelLine.getSheetName()))
                    .collect(Collectors.toList());

            for (User user : usersBySpecialty) {
                specialty.setFieldsValues(user, excelLine);
            }
            excelLines.add(excelLine);
        }
        return excelLines;
    }

    public void setCorrectUsersSpecialty(List<User> users, Set<String> specialties) {
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
}
