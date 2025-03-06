package com.jooseposkarehaver.booknfly.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatingMessage {
    @JsonProperty("status")
    private String status; // "success", "warning", or "error"

    @JsonProperty("text")
    private String text;
}
