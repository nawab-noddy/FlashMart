package com.inventory.FlashMart.dto;

import lombok.Data;

@Data
public class OrderEvent {

    private String userId;

    private String productId;
}
