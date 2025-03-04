package com.jooseposkarehaver.booknfly.controller;


import com.jooseposkarehaver.booknfly.model.FlightSearchRequest;
import com.jooseposkarehaver.booknfly.model.FlightSearchResponse;
import com.jooseposkarehaver.booknfly.service.MockFlightService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mock")
public class FlightController {

    private final MockFlightService mockFlightService;

    public FlightController(MockFlightService mockFlightService) {
        this.mockFlightService = mockFlightService;
    }

    @PostMapping("/flights")
    public FlightSearchResponse getMockFlights(@Valid @RequestBody FlightSearchRequest request) {
        return mockFlightService.generateMockFlights(request);
    }
}