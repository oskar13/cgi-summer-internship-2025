package com.jooseposkarehaver.booknfly.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlightItinerary {

    private List<Flight> flights;  // Individual flight legs
    private List<Layover> layovers;

    @JsonProperty("total_duration")
    private int totalDuration;

    @JsonProperty("carbon_emissions")
    private CarbonEmissions carbonEmissions;

    private int price;
    private String type;

    @JsonProperty("airline_logo")
    private String airlineLogo;

    private List<String> extensions;

    @JsonProperty("departure_token")
    private String departureToken;

    @JsonProperty("booking_token")
    private String bookingToken;
}
