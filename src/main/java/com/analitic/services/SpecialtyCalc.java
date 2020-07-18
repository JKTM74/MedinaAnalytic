package com.analitic.services;

import com.analitic.models.ExcelLine;
import com.analitic.models.SalesServices;
import com.analitic.models.Doctor;
import com.analitic.repositories.SalesServicesRepository;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Расчет специальности.
 */
@Service
public class SpecialtyCalc {
    private final SalesServicesRepository salesServicesRepository;
    private String date;

    public SpecialtyCalc(SalesServicesRepository salesServicesRepository) {
        this.salesServicesRepository = salesServicesRepository;
    }

    /**
     * Вычисление полей любого врача, кроме ультразвукологов.
     * @param doctor - врач
     * @param excelLine - строка специальности.
     */
    public void setFieldsValues(Doctor doctor, ExcelLine excelLine) {
        calcAllKl(doctor, excelLine);
        calcKl(doctor, excelLine);
        calcStreet(doctor, excelLine);
    }

    /**
     * Вычисление полей для ультразвукологов.
     * @param doctor - ультразвуколог
     * @param excelLine - строка УЗИ.
     */
    public void setUziFieldsValues(Doctor doctor, ExcelLine excelLine) {
        calcUziAll(doctor, excelLine);
        calcUziKl(doctor, excelLine);
        calcUziStreet(doctor, excelLine);
    }

    /**
     * Метод получает информацию по собственным и направленным услугам (Все КЛ) из БД.
     * Вызывает setAllKlVars у строки для суммирования результатов и сохранения в переменных строки.
     * @param doctor - врач
     * @param excelLine - строка специальности.
     */
    private void calcAllKl(Doctor doctor, ExcelLine excelLine) {
        SalesServices personalServices = salesServicesRepository.getPersonalServices(doctor.getUserFullName(), date);
        SalesServices directedServices = salesServicesRepository.getDirectedServices(doctor.getUserFullName(), date);

        int patientsCount = personalServices.getPatientsCount() + directedServices.getPatientsCount();
        int servicesCount = personalServices.getServicesCount() + directedServices.getServicesCount();
        double allKlSum = personalServices.getSumPrice() + directedServices.getSumPrice();

        excelLine.setAllKLVars(patientsCount, servicesCount, allKlSum);
    }

    /**
     * Метод получает информацию по собственным услугам (КЛ) из БД.
     * Вызывает setAllKlVars у строки для суммирования результатов и сохранения в переменных строки.
     * @param doctor - врач
     * @param excelLine - строка специальности.
     */
    private void calcKl(Doctor doctor, ExcelLine excelLine) {
        SalesServices personalServices = salesServicesRepository.getPersonalServices(doctor.getUserFullName(), date);

        excelLine.setKLVars(personalServices.getPatientsCount(), personalServices.getServicesCount(), personalServices.getSumPrice());
    }

    /**
     * Метод получает информацию по услугам с улицы из БД.
     * Вызывает setAllKlVars у строки для суммирования результатов и сохранения в переменных строки.
     * @param doctor - врач
     * @param excelLine - строка специальности.
     */
    private void calcStreet(Doctor doctor, ExcelLine excelLine) {
        SalesServices streetServices = salesServicesRepository.getStreetServices(doctor.getUserFullName(), date);

        excelLine.setStreetVars(streetServices.getPatientsCount(), streetServices.getServicesCount(), streetServices.getSumPrice());
    }

    /**
     * Метод получает информацию по собственным и направленным услугам (Все КЛ) из БД для УЗИ.
     * Вызывает setAllKlVars у строки для суммирования результатов и сохранения в переменных строки.
     * @param doctor - врач
     * @param excelLine - строка специальности.
     */
    private void calcUziAll(Doctor doctor, ExcelLine excelLine){
        SalesServices personalUziServices = salesServicesRepository.getPersonalUziServices(doctor.getUserFullName(), date);
        SalesServices directedUziServices = salesServicesRepository.getDirectedUziServices(doctor.getUserFullName(), date);

        int patientsCount = personalUziServices.getPatientsCount() + directedUziServices.getPatientsCount();
        int servicesCount = personalUziServices.getServicesCount() + directedUziServices.getServicesCount();
        double allKlSum = personalUziServices.getSumPrice() + directedUziServices.getSumPrice();

        excelLine.setAllKLVars(patientsCount, servicesCount, allKlSum);
    }

    /**
     * Метод получает информацию по собственным (КЛ) из БД для УЗИ.
     * Вызывает setAllKlVars у строки для суммирования результатов и сохранения в переменных строки.
     * @param doctor - врач
     * @param excelLine - строка специальности.
     */
    private void calcUziKl(Doctor doctor, ExcelLine excelLine) {
        SalesServices personalUziServices = salesServicesRepository.getPersonalUziServices(doctor.getUserFullName(), date);

        excelLine.setKLVars(personalUziServices.getPatientsCount(), personalUziServices.getServicesCount(), personalUziServices.getSumPrice());
    }

    /**
     * Метод получает информацию по услугам с улицы из БД для УЗИ.
     * Вызывает setAllKlVars у строки для суммирования результатов и сохранения в переменных строки.
     * @param doctor - врач
     * @param excelLine - строка специальности.
     */
    private void calcUziStreet(Doctor doctor, ExcelLine excelLine) {
        SalesServices streetServices = salesServicesRepository.getStreetUziServices(doctor.getUserFullName(), date);

        excelLine.setStreetVars(streetServices.getPatientsCount(), streetServices.getServicesCount(), streetServices.getSumPrice());
    }

    /**
     * Устанавливает дату из гуя для запросов к БД.
     * @param date дата с гуя
     */
    public void setDate(Date date) {
        this.date = new SimpleDateFormat("MM-yyyy").format(date);
    }
}
