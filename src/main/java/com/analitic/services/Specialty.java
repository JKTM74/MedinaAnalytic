package com.analitic.services;

import com.analitic.models.ExcelLine;
import com.analitic.models.SalesServices;
import com.analitic.models.User;
import com.analitic.repositories.SalesServicesRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.SimpleDateFormat;

@Service
public class Specialty {

    private final SalesServicesRepository salesServicesRepository;

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-yyyy");
    private final String date = simpleDateFormat.format(new Date(System.currentTimeMillis()));

    public Specialty(SalesServicesRepository salesServicesRepository) {
        this.salesServicesRepository = salesServicesRepository;
    }

    public void setFieldsValues(User user, ExcelLine excelLine) {
        calcAllKl(user, excelLine);
        calcKl(user, excelLine);
        calcStreet(user, excelLine);
    }

    public void setUziFieldsValues(User user, ExcelLine excelLine) {
        calcUziAll(user, excelLine);
        calcUziKl(user, excelLine);
        calcUziStreet(user, excelLine);
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

    private void calcUziAll(User user, ExcelLine excelLine){
        SalesServices personalUziServices = salesServicesRepository.getPersonalUziServices(user.getUserFullName(), date);
        SalesServices directedUziServices = salesServicesRepository.getDirectedUziServices(user.getUserFullName(), date);

        int patientsCount = personalUziServices.getPatientsCount() + directedUziServices.getPatientsCount();
        int servicesCount = personalUziServices.getServicesCount() + directedUziServices.getServicesCount();
        double allKlSum = personalUziServices.getSumPrice() + directedUziServices.getSumPrice();

        excelLine.setAllKLVars(patientsCount, servicesCount, allKlSum);
    }

    private void calcUziKl(User user, ExcelLine excelLine) {
        SalesServices personalUziServices = salesServicesRepository.getPersonalUziServices(user.getUserFullName(), date);

        excelLine.setKLVars(personalUziServices.getPatientsCount(), personalUziServices.getServicesCount(), personalUziServices.getSumPrice());
    }

    private void calcUziStreet(User user, ExcelLine excelLine) {
        SalesServices streetServices = salesServicesRepository.getStreetUziServices(user.getUserFullName(), date);

        excelLine.setStreetVars(streetServices.getPatientsCount(), streetServices.getServicesCount(), streetServices.getSumPrice());
    }
}
