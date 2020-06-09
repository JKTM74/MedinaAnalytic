package com.analitic;

import com.analitic.models.User;
import com.analitic.repositories.ServiceRepository;
import com.analitic.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class Main {
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;

    public Main(ServiceRepository serviceRepository, UserRepository userRepository) {
        this.serviceRepository = serviceRepository;
        this.userRepository = userRepository;
        startAnalytic();
    }

    private void startAnalytic(){
        List<User> users = userRepository.findUsersByDepartment(1);
        HashMap<String, Double> usersSum = new HashMap<>();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-yyyy");
        String date = simpleDateFormat.format(new Date(System.currentTimeMillis()));

        for (User user: users){
            Double priceSumWithoutCourse = serviceRepository.getSumByUserAndDateWithoutCourse(user.getUserFullName(), date);
            Double priceSumWithCourse = serviceRepository.getSumByUserAndDateWithCourse(user.getUserFullName(), date);
            priceSumWithCourse = priceSumWithCourse != null ? priceSumWithCourse : 0.0;
            priceSumWithoutCourse = priceSumWithoutCourse != null ? priceSumWithoutCourse : 0.0;

            double allSum = priceSumWithCourse + priceSumWithoutCourse;
            usersSum.put(user.getUserFullName(), allSum);
        }

        for (Map.Entry entry: usersSum.entrySet()){
            System.out.println(entry.getKey() + " - " + entry.getValue());
        }
    }
}