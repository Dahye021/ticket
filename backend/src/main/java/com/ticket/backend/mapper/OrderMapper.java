package com.ticket.backend.mapper;

import com.ticket.backend.domain.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

@Mapper
public interface OrderMapper {
    void insertOrder(Orders orders);          //주문 저장

    //티켓 구매 수량 조회
    int getPurchasedQuantity(
            @Param("userId") Long userId,
            @Param("ticketId") Long ticketId
    );
}
