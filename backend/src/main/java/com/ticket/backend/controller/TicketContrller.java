package com.ticket.backend.controller;

import com.ticket.backend.domain.Tickets;
import com.ticket.backend.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketContrller {
    private final TicketService ticketService;

    //티켓 전체 조회
    @GetMapping
    public ResponseEntity<List<Tickets>> getTickets() {
        List<Tickets> tickets = ticketService.getTickets();
        return ResponseEntity.ok(tickets);
    }

    //티켓 상세 조회
    @GetMapping("/{ticketId}")
    public ResponseEntity<Tickets> getTicket(
            @PathVariable Long ticketId         //티켓 아이디를 넣어주는 역할
    ) {
        Tickets tickets = ticketService.getTicket(ticketId);
        return ResponseEntity.ok(tickets);
    }
}

