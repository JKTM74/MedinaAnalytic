package com.analitic.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "qdfsalesservices")
@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class SalesServices {

    @Column(name = "servicescount")
    private int servicesCount;

    @Column(name = "patientscount")
    private int patientsCount;

    @Column(name = "sumprice")
    private double sumPrice;
}