package com.hongtai.nat.client.core;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ClientStarter implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        // 启动客户端TODO
        if(args.length==0){
            throw new IllegalArgumentException("client need to config a licenseKey");
        }

    }
}
