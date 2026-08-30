package com.ticket.backend.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class tickets {
    private Long ticket_id;                         //티켓 ID
    private String ticket_name;                     //티켓 이름
    private String venue;                           //공연 장소
    private LocalDateTime sale_start_at;            //판매 시작 시각
    private LocalDateTime sale_end_at;              //판매 종료 시각
    private LocalDateTime valid_start_at;           //유효 시작 시각
    private LocalDateTime valid_end_at;             //유효 종료 시각
    private Integer total_quantity;                 //전체 등록 수량
    private Integer remaining_quantity;             //남은 수량
    private Integer purchase_limit;                 //인당 구매 제한 수량
    private Long price;                             //티켓 정가
    private Integer discount_rate;                  //할인율
}