package com.app.ecom.controller;

import com.app.ecom.dto.OrderResponse;
import com.app.ecom.model.Order;
import com.app.ecom.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/api/orders")
    public ResponseEntity<OrderResponse> createOrder(@RequestHeader("X-User-Id") String userId) {
        return orderService.createorder(userId)
                .map(orderResponse -> new ResponseEntity<>(orderResponse, HttpStatus.CREATED))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }
}
