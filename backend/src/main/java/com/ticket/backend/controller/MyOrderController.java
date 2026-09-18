package com.ticket.backend.controller;

import com.ticket.backend.domain.Orders;
import com.ticket.backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.Authenticator;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class MyOrderController {
    private final OrderService orderService;

    //주문 조회
    @GetMapping("/me")
    public ResponseEntity<List<Orders>> getMyOrders(
            Authentication authentication
    ) {
        Long userId = (Long) authentication.getPrincipal();
        List<Orders> orders = orderService.getMyOrders(userId);
        return ResponseEntity.ok(orders);
    }

    //주문 상세 조회
    @GetMapping("/{orderId}")
    public ResponseEntity<Orders> getOrder(
            @PathVariable Long orderId,
            Authentication authentication
    ) {
        Long userId = (Long) authentication.getPrincipal();

        Orders orders = orderService.getOrder(userId, orderId);

        return ResponseEntity.ok(orders);
    }
}
