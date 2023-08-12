package com.hongtai.nat.server.core;


import com.hongtai.nat.server.core.bootstrap.Starter;
import com.hongtai.nat.common.core.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 *
 */
@Component
@Slf4j
public class ServerRunner implements CommandLineRunner {


    @Override
    public void run(String... args) throws Exception {
        log.info("server start");
        Map<String, Starter> beansMap = SpringUtil.getBeansMapOfType(Starter.class);
        for (Map.Entry<String, Starter> starterEntry : beansMap.entrySet()) {
            String name = starterEntry.getKey();
            Starter starter = starterEntry.getValue();
            try {
                starter.start();
            } catch (Exception e) {
                log.error("start [{}] start error", name, e);
            }
        }
        log.info("server start complete");
    }
}
