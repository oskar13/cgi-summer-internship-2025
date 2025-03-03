package com.jooseposkarehaver.booknfly.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlightSearchResponse {

    @JsonProperty("best_flights")
    private List<BestFlight> bestFlights;

    @JsonProperty("other_flights")
    private List<BestFlight> otherFlights; // Same structure as bestFlights
}