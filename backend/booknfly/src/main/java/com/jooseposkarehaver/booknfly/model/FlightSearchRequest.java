package com.jooseposkarehaver.booknfly.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


import java.util.List;
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlightSearchRequest {

    @NotBlank(message = "Missing `origin` parameter.")
    private String origin;

    @NotBlank(message = "Missing `destination` parameter.")
    private String destination;
    private Integer type; // 1 - Round trip (not implemented yet), 2 - One way
    private Integer adults = 1;
    private Integer children = 0;
    @JsonProperty("outbound_date")
    private String outboundDate; // YYYY-MM-DD
    @JsonProperty("return_date")
    private String returnDate; // YYYY-MM-DD (not used for now)
    @JsonProperty("travel_class")
    private String travelClass = "1"; // 1 - Economy, 2 - Premium Economy, 3 - Business, 4 - First
    @JsonProperty("max_price")
    private Integer maxPrice;
    @JsonProperty("max_duration")
    private Integer maxDuration; // Maximum flight duration in minutes
    private Integer stops = 0; // 0 - Any stops, 1 - Nonstop, 2 - 1 stop, 3 - 2 stops
    @JsonProperty("min_avg_legroom")
    private Integer minAvgLegroom = 0; // Minimum average legroom in cm
    @JsonProperty("extra_options")
    private List<String> extraOptions; // Extra seat preferences like ['extra_legroom', 'window_seats', etc.]
}
