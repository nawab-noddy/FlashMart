package com.inventory.FlashMart.service;

import com.inventory.FlashMart.config.RabbitMQConfig;
import com.inventory.FlashMart.dto.OrderEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    public boolean deductStock(){
        long updatedInventory = stringRedisTemplate.opsForValue().decrement("flashmart:inventory:iphone");

        if (updatedInventory < 0){
            stringRedisTemplate.opsForValue().increment("flashmart:inventory:iphone");
        }

        if(updatedInventory >= 0) {
            OrderEvent event = new OrderEvent();
            event.setUserId("User1234");
            event.setProductId("iPhone-Flash-Sale");
            rabbitTemplate.convertAndSend(
                    "order.exchange",
                    "order.routing.key",
                    event
            );
            return true;
        }

        return false;
    }
}
