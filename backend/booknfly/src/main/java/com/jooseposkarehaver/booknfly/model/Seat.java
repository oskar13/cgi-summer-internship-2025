package com.jooseposkarehaver.booknfly.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Seat {
    @JsonProperty("seat_number")
    private String seatNumber;

    @JsonProperty("row")
    private int row;

    @JsonProperty("seat_class")
    private String seatClass;

    @JsonProperty("features")
    private List<String> features;

    @JsonProperty("available")
    private boolean available;

    @JsonProperty("suggested")
    private boolean suggested;  // Seat suggested for reservation

    @JsonProperty("selected")
    private boolean selected;  // Seat suggested for reservation

}
