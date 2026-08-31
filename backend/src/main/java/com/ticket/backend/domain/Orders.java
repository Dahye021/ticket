package com.ticket.backend.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Orders {
    private Long orderId;
    private Long userId;
    private Long ticketId;
    private Integer quantity;
    private Long price;
    private Integer discountRate;
    private Long discountAmount;
    private Long finalAmount;
    private String orderStatus;
    private LocalDateTime orderedAt;
    private LocalDateTime canceledAt;
}