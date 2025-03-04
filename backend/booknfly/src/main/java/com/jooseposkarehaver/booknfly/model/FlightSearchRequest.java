package com.jooseposkarehaver.booknfly.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.*;
import lombok.Data;


import java.util.List;
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlightSearchRequest {

    @NotBlank(message = "Missing `origin` parameter.")
    private String origin;

    @NotBlank(message = "Missing `destination` parameter.")
    private String destination;

    private Integer type = 2; // 1 - Round trip (not implemented yet), 2 - One way
    private Integer adults = 1;
    private Integer children = 0;

    @JsonProperty("outbound_date")
    @NotBlank(message = "Missing `outbound_date` parameter.")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "`outbound_date` must be in YYYY-MM-DD format.")
    private String outboundDate; // YYYY-MM-DD

    @JsonProperty("return_date")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "`return_date` must be in YYYY-MM-DD format.")
    private String returnDate; // YYYY-MM-DD (not used for now)

    @JsonProperty("travel_class")
    private String travelClass = "1"; // 1 - Economy, 2 - Premium Economy, 3 - Business, 4 - First

    @JsonProperty("max_price")
    private Integer maxPrice;

    @JsonProperty("max_duration")
    private Integer maxDuration; // Maximum flight duration in minutes

    @NotNull(message = "Missing `stops` parameter.") // Ensures it's not null
    @Min(value = 0, message = "`stops` must be between 0 and 3.")
    @Max(value = 3, message = "`stops` must be between 0 and 3.")
    private Integer stops = 0; // 0 - Any stops, 1 - Nonstop, 2 - 1 stop, 3 - 2 stops

    @Min(value = 0, message = "Must be over 50")
    @JsonProperty("min_avg_legroom")
    private Integer minAvgLegroom = 50; // Minimum average legroom in cm

    @JsonProperty("extra_legroom")
    private Boolean extraLegroom = false;

    @JsonProperty("window_seats")
    private Boolean windowSeats = false;

    @JsonProperty("group_seating")
    private Boolean groupSeating = false;

    @JsonProperty("close_to_exit")
    private Boolean closeToExit = false;
}
