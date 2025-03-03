package com.jooseposkarehaver.booknfly.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarbonEmissions {

    @JsonProperty("this_flight")
    private int thisFlight;

    @JsonProperty("typical_for_this_route")
    private int typicalForThisRoute;

    @JsonProperty("difference_percent")
    private int differencePercent;
}