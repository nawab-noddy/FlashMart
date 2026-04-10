package com.inventory.FlashMart.service;

import com.inventory.FlashMart.config.RabbitMQConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RabbitMQConfig rabbitMQConfig;

    public boolean deductStock(){
        long updatedInventory = stringRedisTemplate.opsForValue().decrement("flashmart:inventory:iphone");

        if (updatedInventory < 0){
            stringRedisTemplate.opsForValue().increment("flashmart:inventory:iphone");
        }

        return updatedInventory >= 0;
    }
}
