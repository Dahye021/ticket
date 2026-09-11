package com.ticket.backend.controller;

import com.ticket.backend.dto.order.OrderRequest;
import com.ticket.backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/{ticketId}/orders")
    public ResponseEntity<String> purchase(
            @PathVariable Long ticketId,
            @RequestBody OrderRequest request,
            Authentication authentication
    ) {
        Long userId = (Long) authentication.getPrincipal();

    }
}
