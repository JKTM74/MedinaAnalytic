package com.analytic.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Doctor {

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

    public void deleteSpaces(){
        if (userFullName != null) {
            userFullName = userFullName.trim();
        }
    }
}
