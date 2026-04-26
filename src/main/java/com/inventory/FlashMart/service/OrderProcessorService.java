package com.inventory.FlashMart.service;

import com.inventory.FlashMart.dto.OrderEvent;
import com.inventory.FlashMart.entity.Order;
import com.inventory.FlashMart.repository.OrderRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderProcessorService {

    @Autowired
    OrderRepository orderRepository;

    @RabbitListener(queues = "order.queue")
    public void processOrder(OrderEvent event){

        Order order = new Order();

        order.setUserId(event.getUserId());
        order.setProductId(event.getProductId());
        orderRepository.save(order);
    }

}
