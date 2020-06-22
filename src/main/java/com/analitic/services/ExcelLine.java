package com.analitic.services;

import lombok.*;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class ExcelLine {
    private int departmentNumber;

    private int sheetNumber;
    private String sheetName;

    private double allKLSum;
    private int allKLPatientsCount;
    private int allKLServicesCount;

    private double KLSum;
    private int KLPatientsCount;
    private int KLServicesCount;

    private double streetSum;
    private int streetPatientsCount;
    private int streetServicesCount;

    void setAllKLVars(int allKLPatientsCount, int allKLServicesCount, double allKLSum){
        this.allKLPatientsCount += allKLPatientsCount;
        this.allKLServicesCount += allKLServicesCount;
        this.allKLSum += allKLSum;
    }

    void setKLVars(int KLPatientsCount, int KLServicesCount, double KLSum){
        this.KLPatientsCount += KLPatientsCount;
        this.KLServicesCount += KLServicesCount;
        this.KLSum += KLSum;
    }

    void setStreetVars(int streetPatientsCount, int streetServicesCount, double streetSum){
        this.streetPatientsCount += streetPatientsCount;
        this.streetServicesCount += streetServicesCount;
        this.streetSum += streetSum;
    }
}
