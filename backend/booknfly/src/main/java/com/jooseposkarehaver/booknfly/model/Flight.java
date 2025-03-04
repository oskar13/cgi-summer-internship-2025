package com.jooseposkarehaver.booknfly.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;



@Data
@JsonIgnoreProperties(ignoreUnknown = true)

// Used for individual flight legs not whole flight results.
public class Flight {
    @JsonProperty("departure_airport")
    private Airport departureAirport;

    @JsonProperty("arrival_airport")
    private Airport arrivalAirport;

    private int duration;
    private String airplane;
    private String airline;

    @JsonProperty("airline_logo")
    private String airlineLogo;

    @JsonProperty("travel_class")
    private String travelClass;

    @JsonProperty("flight_number")
    private String flightNumber;

    private List<String> extensions;
    private String legroom;
    private boolean overnight;
}
