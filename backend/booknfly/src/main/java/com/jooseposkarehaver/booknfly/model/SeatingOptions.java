package com.jooseposkarehaver.booknfly.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SeatingOptions {

    @JsonProperty("extra_legroom")
    private Boolean extraLegroom;

    @JsonProperty("window_seats")
    private Boolean windowSeats;

    @JsonProperty("group_seating")
    private Boolean groupSeating;

    @JsonProperty("close_to_exit")
    private Boolean closeToExit;

    @JsonProperty("seating_class")
    private Integer seatingClass = 1;
}