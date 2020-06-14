package com.analitic.services;

import com.analitic.models.User;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @Builder @AllArgsConstructor
public class Speciality {

    private int sheetNumber;

    private String name;

    private List<User> users = new ArrayList<>();

    public void addUser(User user){
        users.add(user);
    }

    @Override
    public String toString() {
        return sheetNumber + "--" + name;
    }
}
