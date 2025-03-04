package com.jooseposkarehaver.booknfly.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarbonEmissions {

    // The amount of carbon emissions of the flight, in grams
    @JsonProperty("this_flight")
    private int thisFlight;

    // Typical amount of carbon emissions for the route, in grams
    @JsonProperty("typical_for_this_route")
    private int typicalForThisRoute;

    // The carbon emissions difference between the flight and typical value, in percent
    @JsonProperty("difference_percent")
    private int differencePercent;
}