package com.ecom.house.controller;

import com.ecom.house.service.OrderService;
import com.ecom.house.entity.Order;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/{accountId}/order")
    @Operation(summary = "Create an order", description = "Creates an order for given account id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order created successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Order addOrder(@PathVariable Long accountId, @RequestBody Order order) {
        return orderService.addOrder(accountId, order);
    }

    @GetMapping("/{accountId}/order")
    @Operation(summary = "Find the orders", description = "Returns list of orders for given account id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order created successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public List<Order> getOrders(@PathVariable Long accountId) {
        return orderService.getOrders(accountId);
    }

    @GetMapping("/{businessAccountId}/{subAccountId}/order")
    @Operation(summary = "Find the orders", description = "Returns list of orders for given account id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order found successfully"),
            @ApiResponse(responseCode = "404", description = "Account mapping not found or Inactive"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Order>> getOrdersForSubAccount(@PathVariable Long businessAccountId, @PathVariable Long subAccountId) {
        return orderService.getOrdersForSubAccount(businessAccountId, subAccountId);
    }
}
