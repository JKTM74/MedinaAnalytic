package com.analytic.services;

import com.analytic.connectors.SheetReaderWriter;
import com.analytic.enums.SpecialitiesEnum;
import com.analytic.models.Doctor;
import com.analytic.models.ExcelLine;
import com.analytic.repositories.DoctorRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Расчет отделений.
 */
@Service
public class DepartmentsCalc {
    private final DoctorRepository doctorRepository;
    private final SpecialtyCalc specialtyCalc;
    private final SheetReaderWriter sheetReaderWriter;

    public DepartmentsCalc(DoctorRepository doctorRepository, SpecialtyCalc specialtyCalc, SheetReaderWriter sheetReaderWriter) {
        this.doctorRepository = doctorRepository;
        this.specialtyCalc = specialtyCalc;
        this.sheetReaderWriter = sheetReaderWriter;
    }

    /**
     * Метод собирает врачей нужного отделения и собирает все специальности из Excel файла.
     * Если отделение 2, отдельно собирает врачей, которые делают УЗИ, расчитыает строку УЗИ и добавляет в лист всех строк.
     * Затем всех врачей ассоциирует с листами(специальностями) Excel файла и запускает расчет всех специальностей.
     *
     * @param departmentNumber - номер отделения.
     * @return - строки для записи в Excel файл.
     */
    public List<ExcelLine> getExcelLines(int departmentNumber) {
        List<Doctor> doctors = doctorRepository.findDoctorsByDepartment(departmentNumber);

        doctors.forEach(Doctor::deleteSpaces);

        Map<String, Integer> sheets = getSheets(sheetReaderWriter.getSheetsFromExcel("ВП " + departmentNumber + " отделение.xlsx"));

        List<ExcelLine> excelLines = new ArrayList<>();

        if (departmentNumber == 2) {
            List<Doctor> uziDoctors = getUziUsers(doctors);
            excelLines.add(getUziLine(uziDoctors));
        }

        setCorrectSpecialtiesForUsers(doctors, sheets.keySet());

        excelLines.addAll(getLines(sheets, doctors, departmentNumber));

        return excelLines;
    }

    /**
     * Метод создаёт по каждой специальсноти(листу Excel файла) строку для вычислений.
     * Далее в стриме для каждой из них отбираются врачи этой специальности и по каждому из этих врачей происходит расчёт.
     *
     * @param sheets           - список листов
     * @param doctors          - врачи отделения
     * @param departmentNumber - номер отделения
     * @return список строк для записи в Excel файл.
     */
    private List<ExcelLine> getLines(Map<String, Integer> sheets, List<Doctor> doctors, int departmentNumber) {
        return sheets.entrySet().stream()
                .map(s ->
                        ExcelLine.builder()
                                .departmentNumber(departmentNumber)
                                .sheetName(s.getKey())
                                .sheetNumber(s.getValue())
                                .build())
                .peek((e) ->
                        doctors.stream()
                                .filter(d -> d.getSpecialty().equals(e.getSheetName()))
                                .forEach((d) -> specialtyCalc.setFieldsValues(d, e)))
                .collect(Collectors.toList());
    }

    /**
     * Метод отфильтровывает ненужные листы и возвращает все с которыми нужно работать.
     *
     * @param specialties - все листы из Excel файла.
     * @return мапа <Имя Листа, Номер Листа>
     */
    private Map<String, Integer> getSheets(Map<String, Integer> specialties) {
        return specialties.entrySet().stream()
                .filter(entry -> !entry.getKey().startsWith("Д ")
                        && !entry.getKey().toLowerCase().contains("свод")
                        && !entry.getKey().toLowerCase().contains("узи"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Метод ассоциирует врачей с специальностями в Excel файле с помощью
     * SpecialtiesEnum и исправляет врачам специальности в соответствии с Excel файлом.
     *
     * @param doctors     - врачи отделения
     * @param specialties - специальности
     */
    private void setCorrectSpecialtiesForUsers(List<Doctor> doctors, Set<String> specialties) {
        for (Doctor doctor : doctors) {
            boolean found = false;
            for (String specialty : specialties) {
                if (!found && specialty.toLowerCase().contains(doctor.getSpecialty().toLowerCase())) {
                    doctor.setSpecialty(specialty);
                    found = true;
                }
            }
            if (!found) {
                for (SpecialitiesEnum specialtyEnum : EnumSet.allOf(SpecialitiesEnum.class)) {
                    if (!found && specialtyEnum.anySpecialtyNames.contains(doctor.getSpecialty())) {
                        doctor.setSpecialty(specialtyEnum.name);
                        found = true;
                    }
                }
            }
        }
    }

    /**
     * Метод собирает всех ультразвукологов.
     *
     * @param doctors - врачи
     * @return список ультразвукологов.
     */
    private List<Doctor> getUziUsers(List<Doctor> doctors) {
        return doctors.stream()
                .filter((u) -> u.getSpecialty().toLowerCase().contains("узи") ||
                        u.getSpecialty().toLowerCase().contains("ультразвук"))
                .collect(Collectors.toList());
    }

    /**
     * Создаёт строку для листа УЗИ, вызывает setUziFieldsValues для расчетов её полей и возвращает эту строку.
     *
     * @param uziDoctors - ультразвукологи
     * @return строку для записи в Excel с расчитанными полями.
     */
    private ExcelLine getUziLine(List<Doctor> uziDoctors) {
        ExcelLine uziLine = ExcelLine.builder()
                .departmentNumber(2)
                .sheetNumber(10)
                .sheetName("УЗИ")
                .build();

        for (Doctor doctor : uziDoctors) {
            specialtyCalc.setUziFieldsValues(doctor, uziLine);
        }

        return uziLine;
    }
}
