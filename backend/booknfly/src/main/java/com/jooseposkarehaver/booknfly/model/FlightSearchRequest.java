package com.jooseposkarehaver.booknfly.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.*;
import lombok.Data;


import java.util.List;
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlightSearchRequest {

    // Named departure_id on SerpApi
    @NotBlank(message = "Missing `origin` parameter.")
    private String origin;

    // Named arrival_id on SerpApi
    @NotBlank(message = "Missing `destination` parameter.")
    private String destination;

    @NotNull(message = "Missing `type` parameter.")
    @Min(value = 1, message = "`type` must be either 1 (Round trip) or 2 (One way).")
    @Max(value = 2, message = "`type` must be either 1 (Round trip) or 2 (One way).")
    private Integer type = 2; // 1 - Round trip (not implemented yet), 2 - One way

    @NotNull(message = "Missing `adults` parameter.")
    @Min(value = 0, message = "`adults` cannot be negative.")
    private Integer adults = 1;

    @NotNull(message = "Missing `children` parameter.")
    @Min(value = 0, message = "`children` cannot be negative.")
    private Integer children = 0;

    @JsonProperty("outbound_date")
    @NotBlank(message = "Missing `outbound_date` parameter.")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "`outbound_date` must be in YYYY-MM-DD format.")
    private String outboundDate; // YYYY-MM-DD

    @JsonProperty("return_date")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "`return_date` must be in YYYY-MM-DD format.")
    private String returnDate; // YYYY-MM-DD (not used for now)

    @Pattern(regexp = "^[1-4]$", message = "`travel_class` must be one of: 1 (Economy), 2 (Premium Economy), 3 (Business), 4 (First).")
    @JsonProperty("travel_class")
    private String travelClass = "1"; // 1 - Economy, 2 - Premium Economy, 3 - Business, 4 - First

    @NotNull(message = "Missing `max_price` parameter.")
    @Min(value = 0, message = "Must be a positive integer")
    @JsonProperty("max_price")
    private Integer maxPrice;

    @NotNull(message = "Missing `max_duration` parameter.")
    @Min(value = 0, message = "Must be a positive integer")
    @JsonProperty("max_duration")
    private Integer maxDuration; // Maximum flight duration in minutes

    @NotNull(message = "Missing `stops` parameter.") // Ensures it's not null
    @Min(value = 0, message = "`stops` must be between 0 and 3.")
    @Max(value = 3, message = "`stops` must be between 0 and 3.")
    private Integer stops = 0; // 0 - Any stops, 1 - Nonstop, 2 - 1 stop, 3 - 2 stops

    @NotNull(message = "Missing `min_avg_legroom` parameter.")
    @Min(value = 50, message = "Must be over 50")
    @JsonProperty("min_avg_legroom")
    private Integer minAvgLegroom = 50; // Minimum average legroom in cm


    //Seationg Options

    // Seating finder
    @JsonProperty("extra_legroom")
    private Boolean extraLegroom = false;

    // Seating finder
    @JsonProperty("window_seats")
    private Boolean windowSeats = false;

    // Seating finder
    @JsonProperty("group_seating")
    private Boolean groupSeating = false;

    // Seating finder
    @JsonProperty("close_to_exit")
    private Boolean closeToExit = false;
}
