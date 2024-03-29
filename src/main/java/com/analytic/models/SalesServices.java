package com.analytic.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "qdfsalesservices")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SalesServices {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "servicescount")
    private int servicesCount;

    @Column(name = "patientscount")
    private int patientsCount;

    @Column(name = "sumprice")
    private double sumPrice;
}