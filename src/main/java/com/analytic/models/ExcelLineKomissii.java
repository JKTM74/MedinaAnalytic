package com.analytic.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExcelLineKomissii {
    private int sheetNumber;
    private String sheetName;

    private double streetSum;
    private int patientsCount;
    private int servicesCount;

    private double orgSum;

    public void setStreetVars(double streetSum, int patientsCount, int servicesCount) {
        this.streetSum = streetSum;
        this.patientsCount = patientsCount;
        this.servicesCount = servicesCount;
    }

    public void setOrgSum(double orgSum) {
        this.orgSum = orgSum;
    }
}
