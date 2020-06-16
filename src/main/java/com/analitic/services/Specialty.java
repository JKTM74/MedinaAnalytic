package com.analitic.services;

import com.analitic.models.SalesServices;
import com.analitic.models.User;
import com.analitic.repositories.SalesServicesRepository;
import lombok.*;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @Builder @AllArgsConstructor
public class Specialty {
    private final SalesServicesRepository salesServicesRepository;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-yyyy");
    private final String date = simpleDateFormat.format(new Date(System.currentTimeMillis()));

    private int sheetNumber;

    private String name;

    private List<User> users;

    private double allKLSum;
    private int allKLPatientsCount;
    private int allKLServicesCount;

    private double KLSum;
    private int KLPatientsCount;
    private int KLServicesCount;

    private double streetSum;
    private int streetPatientsCount;
    private int streetServicesCount;

    public void addUser(User user){
        if(users == null){
            users = new ArrayList<>();
        }
        users.add(user);
    }

    public void calculateFields(){
        for (User user: users){
            calcAllKl(user);
            calcKl(user);
            calcStreet(user);
        }
    }

    private void calcAllKl(User user) {
        SalesServices personalServices = salesServicesRepository.getPersonalServices(user.getUserFullName(), date);
        SalesServices directedServices = salesServicesRepository.getDirectedServices(user.getUserFullName(), date);

        int patientsCount = personalServices.getPatientsCount() + directedServices.getPatientsCount();
        int servicesCount = personalServices.getServicesCount() + directedServices.getServicesCount();
        double allKlSum = personalServices.getSumPrice() + directedServices.getSumPrice();

        setAllKLVars(patientsCount, servicesCount, allKlSum);
    }

    private void calcKl(User user) {
        SalesServices personalServices = salesServicesRepository.getPersonalServices(user.getUserFullName(), date);

        setKLVars(personalServices.getPatientsCount(), personalServices.getServicesCount(), personalServices.getSumPrice());
    }

    private void calcStreet(User user) {
        SalesServices streetServices = salesServicesRepository.getStreetServices(user.getUserFullName(), date);

        setStreetVars(streetServices.getPatientsCount(), streetServices.getServicesCount(), streetServices.getSumPrice());
    }

    private void setAllKLVars(int allKLPatientsCount, int allKLServicesCount, double allKLSum){
        this.allKLPatientsCount += allKLPatientsCount;
        this.allKLServicesCount += allKLServicesCount;
        this.allKLSum += allKLSum;
    }

    private void setKLVars(int KLPatientsCount, int KLServicesCount, double KLSum){
        this.KLPatientsCount += KLPatientsCount;
        this.KLServicesCount += KLServicesCount;
        this.KLSum += KLSum;
    }

    private void setStreetVars(int streetPatientsCount, int streetServicesCount, double streetSum){
        this.streetPatientsCount += streetPatientsCount;
        this.streetServicesCount += streetServicesCount;
        this.streetSum += streetSum;
    }

    @Override
    public String toString() {
        return sheetNumber + "--" + name + "\n" + users.toString() ;
    }
}
