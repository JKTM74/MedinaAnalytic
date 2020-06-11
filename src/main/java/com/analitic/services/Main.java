package com.analitic.services;

import com.analitic.connectors.SheetConnector;
import com.analitic.models.User;
import com.analitic.repositories.ServiceRepository;
import com.analitic.repositories.UserRepository;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class Main {
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;

    private static final int[] DEPARTMENT_NUMBERS = {1, 2, 3}; // если меняются отделения, править тут

    public Main(ServiceRepository serviceRepository, UserRepository userRepository) {
        this.serviceRepository = serviceRepository;
        this.userRepository = userRepository;
        startAnalytic();
    }

    private void startAnalytic() {

        List<Department> department = Arrays.stream(DEPARTMENT_NUMBERS)
                .mapToObj(num ->
                        Department.builder()
                                .number(num).build()).collect(Collectors.toList());

        department.forEach(this::departmentAnalyse);



        List<User> users = userRepository.findUsersByDepartment(1);
        HashMap<String, Double> usersSum = new HashMap<>();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-yyyy");
        String date = simpleDateFormat.format(new Date(System.currentTimeMillis()));

        for (User user : users) {
            Double priceSumWithoutCourse = serviceRepository.getSumByUserAndDateWithoutCourse(user.getUserFullName(), date);
            Double priceSumWithCourse = serviceRepository.getSumByUserAndDateWithCourse(user.getUserFullName(), date);
            priceSumWithCourse = priceSumWithCourse != null ? priceSumWithCourse : 0.0;
            priceSumWithoutCourse = priceSumWithoutCourse != null ? priceSumWithoutCourse : 0.0;

            double allSum = priceSumWithCourse + priceSumWithoutCourse;
            usersSum.put(user.getUserFullName(), allSum);
        }

        for (Map.Entry entry : usersSum.entrySet()) {
            System.out.println(entry.getKey() + " - " + entry.getValue());
        }
    }

    private void departmentAnalyse(Department department)  {
        SheetConnector sheetConnector = null;
        try {
            sheetConnector = new SheetConnector(department.getNumber());
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }

        List<String> spec = sheetConnector.getSpecialities();

        spec.forEach(System.out::println);
    }
}