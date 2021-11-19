package com.zupcc.marvelous;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients

@SpringBootApplication
public class MarvelousApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarvelousApplication.class, args);
    }

}
