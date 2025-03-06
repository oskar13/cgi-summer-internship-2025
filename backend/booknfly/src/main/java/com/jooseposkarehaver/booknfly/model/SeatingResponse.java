package com.jooseposkarehaver.booknfly.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatResponse {
    @JsonProperty("seats")
    private List<Seat> seats;

    @JsonProperty("message")
    private SeatingMessage message;
}