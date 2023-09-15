package com.hongtai.nat.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.hongtai.nat")
public class NatServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(NatServerApplication.class);
    }

}
