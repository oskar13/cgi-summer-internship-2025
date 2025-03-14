package com.jooseposkarehaver.booknfly.controller;


import com.jooseposkarehaver.booknfly.model.FlightSearchRequest;
import com.jooseposkarehaver.booknfly.model.FlightSearchResponse;
import com.jooseposkarehaver.booknfly.service.FlightService;
import com.jooseposkarehaver.booknfly.service.MockFlightService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class FlightController {

    private final MockFlightService mockFlightService;
    private final FlightService flightSearchService; // Real API Service

    public FlightController(MockFlightService mockFlightService, FlightService flightSearchService) {
        this.mockFlightService = mockFlightService;
        this.flightSearchService = flightSearchService;
    }

    // Mock API - Available at /api/mock/flights
    @PostMapping("/mock/flights")
    public FlightSearchResponse getMockFlights(@Valid @RequestBody FlightSearchRequest request) {
        return mockFlightService.generateMockFlights(request);
    }

    // Real API - Available at /api/flights
    @PostMapping("/flights")
    public FlightSearchResponse getRealFlights(@Valid @RequestBody FlightSearchRequest request) {
        return flightSearchService.searchFlights(request);
    }
}