package com.analitic;

import com.analitic.repositories.ServiceRepository;
import org.springframework.stereotype.Controller;

@Controller
public class MainApi {
    private final ServiceRepository serviceRepository;

    public MainApi(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }
}
