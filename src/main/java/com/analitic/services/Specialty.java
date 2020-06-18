package com.analitic.services;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.analitic.models.SalesServices;
import com.analitic.models.User;
import com.analitic.repositories.SalesServicesRepository;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class Specialty {
    @Autowired
    private SalesServicesRepository salesServicesRepository;

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-yyyy");
    private final String date = simpleDateFormat.format(new Date(System.currentTimeMillis()));

    public void setFieldsValues(User user, ExcelLine excelLine) {
        calcAllKl(user, excelLine);
        calcKl(user, excelLine);
        calcStreet(user, excelLine);
    }

    private void calcAllKl(User user, ExcelLine excelLine) {
        SalesServices personalServices = salesServicesRepository.getPersonalServices(user.getUserFullName(), date);
        SalesServices directedServices = salesServicesRepository.getDirectedServices(user.getUserFullName(), date);

        int patientsCount = personalServices.getPatientsCount() + directedServices.getPatientsCount();
        int servicesCount = personalServices.getServicesCount() + directedServices.getServicesCount();
        double allKlSum = personalServices.getSumPrice() + directedServices.getSumPrice();

        excelLine.setAllKLVars(patientsCount, servicesCount, allKlSum);
    }

    private void calcKl(User user, ExcelLine excelLine) {
        SalesServices personalServices = salesServicesRepository.getPersonalServices(user.getUserFullName(), date);

        excelLine.setKLVars(personalServices.getPatientsCount(), personalServices.getServicesCount(), personalServices.getSumPrice());
    }

    private void calcStreet(User user, ExcelLine excelLine) {
        SalesServices streetServices = salesServicesRepository.getStreetServices(user.getUserFullName(), date);

        excelLine.setStreetVars(streetServices.getPatientsCount(), streetServices.getServicesCount(), streetServices.getSumPrice());
    }
}
