package com.analitic.services;

import com.analitic.models.User;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @Builder @AllArgsConstructor
public class Speciality {

    private int sheetNumber;

    private String name;

    private List<User> users;


    public void addUser(User user){
        if(users == null){
            users = new ArrayList<>();
        }
        users.add(user);
    }

    @Override
    public String toString() {
        return sheetNumber + "--" + name;
    }
}
