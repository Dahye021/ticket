package com.ticket.backend.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Tickets {
    private Long ticketId;                         //티켓 ID
    private String ticketName;                     //티켓 이름
    private String venue;                           //공연 장소
    private LocalDateTime saleStartAt;            //판매 시작 시각
    private LocalDateTime saleEndAt;              //판매 종료 시각
    private LocalDateTime validStartAt;           //유효 시작 시각
    private LocalDateTime validEndAt;             //유효 종료 시각
    private Integer totalQuantity;                 //전체 등록 수량
    private Integer remainingQuantity;             //남은 수량
    private Integer purchaseLimit;                 //인당 구매 제한 수량
    private Long price;                             //티켓 정가
    private Integer discountRate;                  //할인율
}