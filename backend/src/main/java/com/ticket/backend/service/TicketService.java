package com.ticket.backend.service;

import com.ticket.backend.domain.Tickets;
import com.ticket.backend.mapper.TicketMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketMapper ticketMapper;

    //티켓 전체 조회
    public List<Tickets> getTickets() {
        return ticketMapper.findAll();
    }

    //티켓 상세 조회
    public Tickets getTicket(Long ticketId) {
        Tickets ticket = ticketMapper.findById(ticketId);

        if (ticket == null) {
            throw new IllegalArgumentException("존재하지 않는 티켓입니다.");
        } return ticket;
    }
}
