package com.example.lostandfoundbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.example.lostandfoundbackend.*")
public class LostAndFoundBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(LostAndFoundBackendApplication.class, args);
    }

}
