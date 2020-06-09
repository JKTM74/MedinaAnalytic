package com.analitic.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "qdfsalesservices")
@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patientcalc")
    private String patientName;

    @Column(name = "PatientID")
    private long patientId;
}