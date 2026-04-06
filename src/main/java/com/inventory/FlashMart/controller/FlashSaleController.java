package com.inventory.FlashMart.controller;

import com.inventory.FlashMart.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/flash-sale")
public class FlashSaleController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/buy")
    public ResponseEntity<?>


}
