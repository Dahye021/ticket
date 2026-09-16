package com.ticket.backend.mapper;

import com.ticket.backend.domain.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

@Mapper
public interface OrderMapper {
    void insertOrder(Orders orders);          //주문 저장

    //티켓 구매 수량 조회
    int getPurchasedQuantity(
            @Param("userId") Long userId,
            @Param("ticketId") Long ticketId
    );

    //사용자 주문 목록 조회
    List<Orders> findByUserId(
            @Param("userId") Long userId
    );

    //주문 상세 조회
    Orders findByOrderId(
            @Param("orderId") Long orderId,
            @Param("userId") Long userId
    );

    //주문 취소
    int cancelOrder(
      @Param("orderId") Long orderId,
      @Param("userId") Long userId
    );
}
