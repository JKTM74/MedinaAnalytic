package com.analitic.services;

import com.analitic.enums.SpecialitiesEnum;
import com.analitic.models.User;
import com.analitic.repositories.SalesServicesRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class Department {
    private int number;

    private List<User> users;

    private List<Specialty> specialties;

    public void setSpecialtiesFromExcel(Map<String, Integer> specialties) {
        this.specialties = specialties.entrySet().stream()
                .filter(s -> !s.getKey().startsWith("Д ")
                        && !s.getKey().toLowerCase().contains("свод"))
                .map(s ->
                        Specialty.builder()
                                .sheetNumber(s.getValue())
                                .name(s.getKey())
                                .build())
                .collect(Collectors.toList());
    }

    public void connectUserToSpeciality() {
        for (User user : users) {
            boolean connected = false;
            for (Specialty specialty : specialties) {
                if (specialty.getName().toLowerCase().contains(user.getSpecialty().toLowerCase()) && !connected) {
                    specialty.addUser(user);
                    connected = true;
                }
            }
            if (!connected) {
                for (SpecialitiesEnum specialtyEnum : EnumSet.allOf(SpecialitiesEnum.class)) {
                    if (specialtyEnum.anySpecialtyNames.contains(user.getSpecialty()) && !connected) {
                        Specialty specialty = specialties.stream()
                                .filter(s -> s.getName().equals(specialtyEnum.name))
                                .findAny()
                                .orElse(null);
                        connected = true;
                        if (specialty != null) {
                            specialty.addUser(user);
                        }
                    }
                }
            }
            if (!connected) System.out.println(user.getUserFullName() + " не смог подобрать специальность");
        }
    }

    public void analyzeSpecialties(SalesServicesRepository salesServicesRepository){
        specialties.stream().forEach((s) -> s.calculateFields(salesServicesRepository));
    }
}
