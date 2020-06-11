package com.analitic.services;

import com.analitic.models.User;
import lombok.*;

import java.util.List;

@Setter @Getter @AllArgsConstructor @NoArgsConstructor @Builder
public class Department {
    private int number;

    private List<User> users;

    private List <String> specialties;

    public Department(int i) {
    }
}
