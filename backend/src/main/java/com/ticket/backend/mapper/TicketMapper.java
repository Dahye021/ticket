package com.ticket.backend.mapper;

import com.ticket.backend.domain.Tickets;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TicketMapper {

    List<Tickets> findAll();            //전체 티켓 조회
    Tickets findById(Long ticketId);    //티켓 상세 조회
}
