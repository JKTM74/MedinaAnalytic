package com.analitic.services;

import com.analitic.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Setter @Getter @AllArgsConstructor @Builder
public class Department {
    private int number;

    private List<User> users;

    private List <Speciality> specialties;

    public void setSpecialtiesFromExcel(Map<String, Integer> specialties) {
        this.specialties = specialties.entrySet().stream()
                .filter(s -> !s.getKey().substring(0, 2).equals("Д "))
                .map(s ->
                        Speciality.builder()
                                .sheetNumber(s.getValue())
                                .name(s.getKey())
                                .build())
                .collect(Collectors.toList());
    }

    public void associateUsersWithSpecialties(){
        for (User user: users){
            boolean isAssociated = false;
            for (Speciality speciality: specialties){
                if (speciality.getName().toLowerCase().contains(user.getSpecialty().toLowerCase()) && !isAssociated){
                    speciality.addUser(user);
                    isAssociated = true;
                }
            }
            if (!isAssociated){
            }
        }
    }
}
