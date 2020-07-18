package com.analitic.models;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor @AllArgsConstructor @Getter @Setter
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
}
