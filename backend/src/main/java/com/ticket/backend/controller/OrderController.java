package com.ticket.backend.controller;

import com.ticket.backend.dto.order.OrderRequest;
import com.ticket.backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    //티켓 구매
    @PostMapping("/{ticketId}/orders")
    public ResponseEntity<String> purchase(
            @PathVariable Long ticketId,
            @RequestBody OrderRequest request,
            Authentication authentication
    ) {
        Long userId = (Long) authentication.getPrincipal();

        orderService.purchase(
                userId,
                ticketId,
                request.getQuantity()
        );

        return ResponseEntity.ok("티켓 구매가 완료되었습니다.");
    }

    //주문 취소
    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<String> cancel(
            @PathVariable Long orderId,
            Authentication authentication
    ) {
        Long userId = (Long) authentication.getPrincipal();

        orderService.cancel(userId, orderId);

        return ResponseEntity.ok("주문이 취소되었습니다.");
    }
}
