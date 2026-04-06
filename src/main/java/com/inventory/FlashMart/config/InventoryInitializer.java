package com.inventory.FlashMart.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class InventoryInitializer implements CommandLineRunner {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Override
    public void run(String... args) throws Exception {
        stringRedisTemplate.opsForValue().set("flashmart:inventory:iphone","1000");
    }
}
