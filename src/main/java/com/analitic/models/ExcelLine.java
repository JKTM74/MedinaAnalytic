package com.analitic.models;

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

    public void setAllKLVars(int allKLPatientsCount, int allKLServicesCount, double allKLSum){
        this.allKLPatientsCount += allKLPatientsCount;
        this.allKLServicesCount += allKLServicesCount;
        this.allKLSum += allKLSum;
    }

    public void setKLVars(int KLPatientsCount, int KLServicesCount, double KLSum){
        this.KLPatientsCount += KLPatientsCount;
        this.KLServicesCount += KLServicesCount;
        this.KLSum += KLSum;
    }

    public void setStreetVars(int streetPatientsCount, int streetServicesCount, double streetSum){
        this.streetPatientsCount += streetPatientsCount;
        this.streetServicesCount += streetServicesCount;
        this.streetSum += streetSum;
    }

    @Override
    public String toString() {
        return "ExcelLine{" +
                "departmentNumber=" + departmentNumber +
                ", sheetNumber=" + sheetNumber +
                ", sheetName='" + sheetName + '\'' +
                ", allKLSum=" + allKLSum +
                ", allKLPatientsCount=" + allKLPatientsCount +
                ", allKLServicesCount=" + allKLServicesCount +
                ", KLSum=" + KLSum +
                ", KLPatientsCount=" + KLPatientsCount +
                ", KLServicesCount=" + KLServicesCount +
                ", streetSum=" + streetSum +
                ", streetPatientsCount=" + streetPatientsCount +
                ", streetServicesCount=" + streetServicesCount +
                '}';
    }
}
