package com.jooseposkarehaver.booknfly.controller;

import com.jooseposkarehaver.booknfly.model.SeatingResponse;
import com.jooseposkarehaver.booknfly.service.SeatMapGenerator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mock/seating")
public class SeatController {

    @GetMapping("/map")
    public SeatingResponse getSeatMap(
            @RequestParam(name = "booking_id", required = false) String bookingId, // Matches `booking_id`
            @RequestParam(name = "numTickets", defaultValue = "1") int tickets,  // Matches `numTickets`
            @RequestParam(name = "extraLegroom", defaultValue = "false") boolean extraLegroom,
            @RequestParam(name = "windowSeats", defaultValue = "false") boolean windowSeats,
            @RequestParam(name = "groupSeating", defaultValue = "false") boolean groupSeating,
            @RequestParam(name = "closeToExit", defaultValue = "false") boolean closeToExit,
            @RequestParam(name = "seatingClass", defaultValue = "Economy") String seatingClass) {  // Matches `seatingClass`

        return SeatMapGenerator.generateSeatMap(bookingId, tickets, extraLegroom, windowSeats, groupSeating, closeToExit, seatingClass);
    }
}