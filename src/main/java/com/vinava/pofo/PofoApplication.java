package com.vinava.pofo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PofoApplication {

    public static void main(String[] args) {
        SpringApplication.run(PofoApplication.class, args);
    }

}
