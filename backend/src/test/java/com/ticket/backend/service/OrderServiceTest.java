package com.ticket.backend.service;

import com.ticket.backend.domain.Orders;
import com.ticket.backend.domain.Tickets;
import com.ticket.backend.mapper.OrderMapper;
import com.ticket.backend.mapper.TicketMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private TicketMapper ticketMapper;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;


    // 티켓 2장 구매 시 주문 총액 기준으로 할인 계산
    @Test
    void purchaseMultipleTickets() {

        Tickets ticket = new Tickets();
        ticket.setTicketId(1L);
        ticket.setPrice(10001L);
        ticket.setDiscountRate(15);
        ticket.setRemainingQuantity(10);
        ticket.setPurchaseLimit(null);

        ticket.setSaleStartAt(LocalDateTime.now().minusDays(1));
        ticket.setSaleEndAt(LocalDateTime.now().plusDays(1));

        when(ticketMapper.findById(1L))
                .thenReturn(ticket);

        when(ticketMapper.decreaseStock(1L, 2))
                .thenReturn(1);

        orderService.purchase(1L, 1L, 2);

        ArgumentCaptor<Orders> captor =
                ArgumentCaptor.forClass(Orders.class);

        verify(orderMapper).insertOrder(captor.capture());

        Orders savedOrder = captor.getValue();

        assertEquals(1L, savedOrder.getUserId());
        assertEquals(1L, savedOrder.getTicketId());
        assertEquals(2, savedOrder.getQuantity());

        assertEquals(10001L, savedOrder.getPrice());

        // 10001 × 2 = 20002
        // 20002 × 15% = 3000.3 → 3000원
        assertEquals(3000L, savedOrder.getDiscountAmount());

        // 20002 - 3000
        assertEquals(17002L, savedOrder.getFinalAmount());

        assertEquals("PURCHASED", savedOrder.getOrderStatus());

        verify(ticketMapper).decreaseStock(1L, 2);
    }

    // 서로 다른 티켓은 각각의 상품 금액을 기준으로 할인 계산
    @Test
    void purchaseDifferentTicketsSeparately() {

        Tickets ticketA = new Tickets();
        ticketA.setTicketId(1L);
        ticketA.setPrice(10001L);
        ticketA.setDiscountRate(15);
        ticketA.setPurchaseLimit(null);
        ticketA.setSaleStartAt(LocalDateTime.now().minusDays(1));
        ticketA.setSaleEndAt(LocalDateTime.now().plusDays(1));

        Tickets ticketB = new Tickets();
        ticketB.setTicketId(2L);
        ticketB.setPrice(20001L);
        ticketB.setDiscountRate(10);
        ticketB.setPurchaseLimit(null);
        ticketB.setSaleStartAt(LocalDateTime.now().minusDays(1));
        ticketB.setSaleEndAt(LocalDateTime.now().plusDays(1));

        when(ticketMapper.findById(1L))
                .thenReturn(ticketA);

        when(ticketMapper.findById(2L))
                .thenReturn(ticketB);

        when(ticketMapper.decreaseStock(1L, 1))
                .thenReturn(1);

        when(ticketMapper.decreaseStock(2L, 1))
                .thenReturn(1);

        // 서로 다른 티켓 각각 1장 구매
        orderService.purchase(1L, 1L, 1);
        orderService.purchase(1L, 2L, 1);

        ArgumentCaptor<Orders> captor =
                ArgumentCaptor.forClass(Orders.class);

        verify(orderMapper, times(2))
                .insertOrder(captor.capture());

        Orders orderA = captor.getAllValues().get(0);
        Orders orderB = captor.getAllValues().get(1);

        // 티켓 A
        // 10001 × 15% = 1500.15 → 1500
        assertEquals(1500L, orderA.getDiscountAmount());
        assertEquals(8501L, orderA.getFinalAmount());

        // 티켓 B
        // 20001 × 10% = 2000.1 → 2000
        assertEquals(2000L, orderB.getDiscountAmount());
        assertEquals(18001L, orderB.getFinalAmount());
    }

    // 1인당 구매 제한 이내면 구매 성공
    @Test
    void purchaseWithinLimit() {

        Tickets ticket = new Tickets();
        ticket.setTicketId(1L);
        ticket.setPrice(10000L);
        ticket.setDiscountRate(10);
        ticket.setPurchaseLimit(3);
        ticket.setSaleStartAt(LocalDateTime.now().minusDays(1));
        ticket.setSaleEndAt(LocalDateTime.now().plusDays(1));

        when(ticketMapper.findById(1L))
                .thenReturn(ticket);

        when(orderMapper.getPurchasedQuantity(1L, 1L))
                .thenReturn(1);

        when(ticketMapper.decreaseStock(1L, 2))
                .thenReturn(1);

        orderService.purchase(1L, 1L, 2);

        verify(orderMapper).insertOrder(any(Orders.class));
        verify(ticketMapper).decreaseStock(1L, 2);
    }

    // 1인당 구매 제한을 초과하면 구매 실패
    @Test
    void purchaseOverLimit() {

        Tickets ticket = new Tickets();
        ticket.setTicketId(1L);
        ticket.setPrice(10000L);
        ticket.setDiscountRate(10);
        ticket.setPurchaseLimit(3);
        ticket.setSaleStartAt(LocalDateTime.now().minusDays(1));
        ticket.setSaleEndAt(LocalDateTime.now().plusDays(1));

        when(ticketMapper.findById(1L))
                .thenReturn(ticket);

        when(orderMapper.getPurchasedQuantity(1L, 1L))
                .thenReturn(2);

        assertThrows(
                IllegalArgumentException.class,
                () -> orderService.purchase(1L, 1L, 2)
        );

        verify(ticketMapper, never())
                .decreaseStock(anyLong(), anyInt());

        verify(orderMapper, never())
                .insertOrder(any());
    }
}