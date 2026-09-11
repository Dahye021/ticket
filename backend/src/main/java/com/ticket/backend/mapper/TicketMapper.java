package com.ticket.backend.mapper;

import com.ticket.backend.domain.Tickets;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TicketMapper {

    List<Tickets> findAll();                                    //전체 티켓 조회
    Tickets findById(Long ticketId);                            //티켓 상세 조회

    //티켓 재고 차감 (파라미터 두개여서 @Parram 사용)
    int decreaseStock(
            @Param("ticketId") Long ticketId,
            @Param("quantity") Integer quantity
    );
}
