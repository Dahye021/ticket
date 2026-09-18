package com.ticket.backend.service;

import com.ticket.backend.domain.Orders;
import com.ticket.backend.domain.Tickets;
import com.ticket.backend.mapper.OrderMapper;
import com.ticket.backend.mapper.TicketMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final TicketMapper ticketMapper;
    private final OrderMapper orderMapper;

    //티켓 구매
    @Transactional
    public void purchase(Long userId, Long ticketId, Integer quantity) {

        //구매 수량 확인
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("구매 수량을 확인해주세요.");
        }

        //티켓 조회
        Tickets tickets = ticketMapper.findById(ticketId);

        if (tickets == null) {
            throw new IllegalArgumentException("티켓이 존재하지 않습니다.");
        }

        LocalDateTime now = LocalDateTime.now();

        //티켓 판매 기간 확인
        if (now.isBefore(tickets.getSaleStartAt()) || now.isAfter(tickets.getSaleEndAt())) {
            throw new IllegalArgumentException("현재 판매 중인 티켓이 아닙니다.");
        }

        //금액 계산
        long price = tickets.getPrice();
        int discountRate = tickets.getDiscountRate() == null ? 0 : tickets.getDiscountRate();

        long originalAmount = price * quantity;
        long discountAmount = originalAmount * discountRate / 100;
        long finalAmount = originalAmount - discountAmount;

        //1인당 구매 제한 확인
        if (tickets.getPurchaseLimit() != null) {
            int purchasedQuantity = orderMapper.getPurchasedQuantity(userId, ticketId);

            if (purchasedQuantity + quantity > tickets.getPurchaseLimit()) {
                throw new IllegalArgumentException("1인당 구매 제한 수량을 초과했습니다.");
            }
        }

        //재고 차감
        int result = ticketMapper.decreaseStock(ticketId, quantity);

        if (result == 0) {
            throw new IllegalArgumentException("티켓 재고가 부족합니다.");
        }

        //주문 생성
        Orders orders = new Orders();
        orders.setUserId(userId);
        orders.setTicketId(ticketId);
        orders.setQuantity(quantity);
        orders.setPrice(price);
        orders.setDiscountRate(tickets.getDiscountRate());
        orders.setDiscountAmount(discountAmount);
        orders.setFinalAmount(finalAmount);
        orders.setOrderStatus("PURCHASED");
        orders.setOrderedAt(now);
        orders.setCanceledAt(null);

        orderMapper.insertOrder(orders);
    }

    //주문 목록 조회
    public List<Orders> getMyOrders(Long userId) {
        return orderMapper.findByUserId(userId);
    }

    //주문 상세 조회
    public Orders getOrder(Long userId, Long orderId) {
        Orders orders = orderMapper.findByOrderId(orderId, userId);

        if (orders == null) {
            throw new IllegalArgumentException("주문을 찾울 수 없습니다.");
        }
        return orders;
    }

    //주문 취소
    @Transactional
    public void cancel(Long userId, Long orderId) {

        //주문 본인 주문인지 확인
        Orders orders = orderMapper.findByOrderId(orderId, userId);

        if (orders == null) {
            throw new IllegalArgumentException("주문을 찾을 수 없습니다.");
        }

        //주문 상태 변경
        int result = orderMapper.cancelOrder(orderId, userId);

        if (result == 0) {
            throw new IllegalArgumentException("이미 취소된 주문입니다.");
        }

        //티켓 재고 복구
        ticketMapper.increaseStock(
                orders.getTicketId(),
                orders.getQuantity()
        );
    }
}
