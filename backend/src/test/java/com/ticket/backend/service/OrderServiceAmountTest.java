//package com.ticket.backend.service;
//
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//class OrderServiceAmountTest {
//
//    // 할인 계산이 정확히 나누어 떨어지는 경우
//    @Test
//    void calculateExactDiscount() {
//
//        long[] amounts =
//                OrderService.calculateAmounts(10000, 2, 10);
//
//        assertEquals(20000, amounts[0]); // 할인 전 총액
//        assertEquals(2000, amounts[1]);  // 할인 금액
//        assertEquals(18000, amounts[2]); // 최종 결제 금액
//    }
//
//    // 할인 계산 시 1원 미만 금액은 버리는 경우
//    @Test
//    void calculateDiscountWithTruncation() {
//
//        long[] amounts =
//                OrderService.calculateAmounts(10001, 1, 15);
//
//        assertEquals(10001, amounts[0]); // 할인 전 총액
//        assertEquals(1500, amounts[1]);  // 1500.15원 -> 1500원
//        assertEquals(8501, amounts[2]);  // 최종 결제 금액
//    }
//
//    // 여러 장 구매 시 주문 총액에 할인율을 적용
//    @Test
//    void calculateDiscountAfterQuantityApplied() {
//
//        long[] amounts =
//                OrderService.calculateAmounts(10001, 2, 15);
//
//        assertEquals(20002, amounts[0]); // 10001 × 2
//        assertEquals(3000, amounts[1]);  // 20002 × 15% = 3000.3 -> 3000
//        assertEquals(17002, amounts[2]);
//    }
//
//    // 서로 다른 티켓은 각 상품 단위로 할인 계산
//    @Test
//    void calculateDiscountPerTicket() {
//
//        long[] ticketA =
//                OrderService.calculateAmounts(10001, 1, 15);
//
//        long[] ticketB =
//                OrderService.calculateAmounts(20001, 1, 10);
//
//        assertEquals(1500, ticketA[1]);
//        assertEquals(8501, ticketA[2]);
//
//        assertEquals(2000, ticketB[1]);
//        assertEquals(18001, ticketB[2]);
//    }
//}