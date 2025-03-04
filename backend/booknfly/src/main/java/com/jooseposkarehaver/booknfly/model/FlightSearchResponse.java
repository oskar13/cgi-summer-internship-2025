package com.jooseposkarehaver.booknfly.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlightSearchResponse {

    @JsonProperty("best_flights")
    private List<FlightItinerary> bestFlights;

    @JsonProperty("other_flights")
    private List<FlightItinerary> otherFlights; // Same structure as best_flights

    @JsonProperty("seating_options")
    private SeatingOptions seatingOptions;
}