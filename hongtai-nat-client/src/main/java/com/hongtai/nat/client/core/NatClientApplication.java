package com.hongtai.nat.client.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.hongtai.nat")
public class NatClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(NatClientApplication.class);
    }

}
