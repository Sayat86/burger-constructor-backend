package com.example.burgerconstructorbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class BurgerConstructorBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BurgerConstructorBackendApplication.class, args);
    }
}
