package com.learnavro.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.learnavro.dto.CoffeeOrderDTO;
import com.learnavro.dto.CoffeeOrderUpdateDTO;
import com.learnavro.service.CoffeeOrderService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1/coffee_orders")
@Slf4j
public class CoffeeOrderController {
    private CoffeeOrderService coffeeOrderService;

    public CoffeeOrderController(CoffeeOrderService coffeeOrderService) {
        this.coffeeOrderService = coffeeOrderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)// 201 - Created will be sent back as response
    public CoffeeOrderDTO newOrder(@Valid @RequestBody CoffeeOrderDTO coffeeOrderDTO){
        log.info("Received the request for an order {}", coffeeOrderDTO);
//        return coffeeOrderDTO;
        return coffeeOrderService.newOrder(coffeeOrderDTO);
    }

    @PutMapping("/{order_id}")
    public CoffeeOrderUpdateDTO updateOrder(@PathVariable("order_id") String orderId, @Valid @RequestBody CoffeeOrderUpdateDTO coffeeOrderUpdateDTO){
        log.info("Received the PUT request for an order {}", coffeeOrderUpdateDTO);
        return coffeeOrderService.updateOrder(orderId, coffeeOrderUpdateDTO);
    }
}
