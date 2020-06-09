package com.analitic.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class User {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userfullname")
    String userFullName;

    @Column(name = "title")
    String title;

    @Column(name = "otdel")
    String department;

    @Override
    public String toString() {
        return "User{" +
                "userFullName='" + userFullName + '\'' +
                ", title='" + title + '\'' +
                ", department=" + department +
                '}';
    }
}
