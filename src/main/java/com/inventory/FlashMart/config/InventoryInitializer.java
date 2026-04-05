package com.inventory.FlashMart.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class InventoryInitializer implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        StringRedisTemplate
    }
}
