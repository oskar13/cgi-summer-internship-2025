package com.jooseposkarehaver.booknfly.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jooseposkarehaver.booknfly.model.FlightSearchRequest;
import com.jooseposkarehaver.booknfly.model.FlightSearchResponse;
import com.jooseposkarehaver.booknfly.model.SeatingOptions;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class FlightService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String apiKey;

    public FlightService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.apiKey = System.getenv("SERPAPI_KEY");
    }

    public FlightSearchResponse searchFlights(FlightSearchRequest request) {
        String apiUrl = "https://serpapi.com/search"; // Replace with the real API URL

        // Build the URL with query parameters
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("api_key", apiKey)
                .queryParam("engine", "google_flights")
                .queryParam("hl", "en")
                .queryParam("gl", "ee")
                .queryParam("currency", "EUR")
                .queryParam("departure_id", request.getOrigin()) // Maps to "departure_id"
                .queryParam("arrival_id", request.getDestination()) // Maps to "arrival_id"
                .queryParam("outbound_date", request.getOutboundDate());

        // Add optional parameters if present
        if (request.getReturnDate() != null && !request.getReturnDate().isEmpty()) {
            uriBuilder.queryParam("return_date", request.getReturnDate());
        }
        if (request.getMaxPrice() != null) {
            uriBuilder.queryParam("max_price", request.getMaxPrice());
        }
        if (request.getMaxPrice() != null) {
            uriBuilder.queryParam("adults", request.getAdults());
        }
        if (request.getChildren() != null) {
            uriBuilder.queryParam("children", request.getChildren());
        }
        if (request.getType() != null) {
            uriBuilder.queryParam("type", request.getType());
        }

        // Build final URL
        String finalUrl = uriBuilder.toUriString();

        // Send GET request
        ResponseEntity<String> responseEntity;
        try {
            responseEntity = restTemplate.getForEntity(finalUrl, String.class);
        } catch (Exception e) {
            throw new RuntimeException("Error while calling external API", e);
        }

        // Parse JSON response
        try {
            JsonNode root = objectMapper.readTree(responseEntity.getBody());
            FlightSearchResponse flightResponse = objectMapper.treeToValue(root, FlightSearchResponse.class);

            // Populate seating options manually from request
            flightResponse.setSeatingOptions(extractSeatingOptions(request));

            return flightResponse;
        } catch (Exception e) {
            throw new RuntimeException("Error parsing flight search response", e);
        }
    }

    private SeatingOptions extractSeatingOptions(FlightSearchRequest request) {
        SeatingOptions seatingOptions = new SeatingOptions();
        seatingOptions.setExtraLegroom(request.getExtraLegroom());
        seatingOptions.setWindowSeats(request.getWindowSeats());
        seatingOptions.setGroupSeating(request.getGroupSeating());
        seatingOptions.setCloseToExit(request.getCloseToExit());
        return seatingOptions;
    }
}
