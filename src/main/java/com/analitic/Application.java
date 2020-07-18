package com.analitic;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.IOException;

@SpringBootApplication(scanBasePackages = {"com.analitic"})
@EntityScan("com.analitic.models")
@EnableJpaRepositories("com.analitic.repositories")
public class Application {

    public static void main(String[] args) throws IOException {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(Application.class);
        builder.headless(false);
        builder.run(args);
    }
}