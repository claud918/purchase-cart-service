package com.example.cart.controller;

import com.example.cart.model.OrderRequest;
import com.example.cart.model.OrderResponse;
import com.example.cart.service.OrderService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public OrderResponse createOrder(@RequestBody OrderRequest request) {
        return orderService.calculateOrder(request.getOrder());
    }

}