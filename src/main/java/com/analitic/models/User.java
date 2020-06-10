package com.analitic.models;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class User {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userfullname")
    private String userFullName;

    @Column(name = "title")
    private String specialty;

    @Column(name = "otdel")
    private String department;

    @Override
    public String toString() {
        return "User{" +
                "userFullName='" + userFullName + '\'' +
                ", title='" + specialty + '\'' +
                ", department=" + department +
                '}';
    }
}
