package ru.razymov.catharsis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CatharsisApplication {

    public static void main(String[] args) {
        SpringApplication.run(CatharsisApplication.class, args);
    }

}
