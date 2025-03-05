package com.jooseposkarehaver.booknfly.service;


import com.jooseposkarehaver.booknfly.model.*;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class MockFlightService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final Random RANDOM = new Random();

    // Sample airport data for mock requests
    private static final List<String> AIRPORTS = List.of("TLL", "BER", "LHR", "CDG", "FRA", "AMS", "MAD", "BCN", "IST", "ZRH", "MUC", "VIE");
    private static final List<String> AIRLINES = List.of("Mock Airlines", "Air Test", "Demo Airways", "Sample Jet");
    private static final List<String> AIRPLANES = List.of("Boeing 737", "Airbus A320", "Boeing 777", "Airbus A350");

    public FlightSearchResponse generateMockFlights(FlightSearchRequest request) {


        FlightSearchResponse response = new FlightSearchResponse();

        List<FlightItinerary> itineraries = generateMockItineraries(request);
        response.setBestFlights(itineraries.subList(0, Math.min(2, itineraries.size())));
        response.setOtherFlights(itineraries.subList(Math.min(2, itineraries.size()), itineraries.size()));

        // Map seating preferences
        SeatingOptions seatingOptions = new SeatingOptions();
        seatingOptions.setExtraLegroom(request.getExtraLegroom());
        seatingOptions.setWindowSeats(request.getWindowSeats());
        seatingOptions.setGroupSeating(request.getGroupSeating());
        seatingOptions.setCloseToExit(request.getCloseToExit());

        response.setSeatingOptions(seatingOptions);


        return response;
    }

    private List<FlightItinerary> generateMockItineraries(FlightSearchRequest request) {
        List<FlightItinerary> itineraries = new ArrayList<>();

        int count = RANDOM.nextInt(3) + 2; // Generate 2 to 5 itineraries
        for (int i = 0; i < count; i++) {
            itineraries.add(generateMockItinerary(request));
        }

        return itineraries;
    }

    private FlightItinerary generateMockItinerary(FlightSearchRequest request) {
        List<Flight> flights = new ArrayList<>();
        int totalDuration = 0;

        // Define departure and arrival locations
        String origin = request.getOrigin() != null ? request.getOrigin() : getRandomAirport();
        String destination = request.getDestination() != null ? request.getDestination() : getRandomAirportExcluding(origin);

        // Random flight time within given outbound date or next 30 days
        LocalDateTime departureTime = request.getOutboundDate() != null ?
                LocalDateTime.parse(request.getOutboundDate() + " 06:00", FORMATTER) :
                generateRandomDepartureTime();

        int flightCount = getFlightLegsBasedOnStops(request.getStops());

        for (int i = 0; i < flightCount; i++) {
            int flightDuration = 60 + RANDOM.nextInt(180); // 1 to 3 hours
            LocalDateTime arrivalTime = departureTime.plusMinutes(flightDuration);

            Flight flight = new Flight();
            flight.setDepartureAirport(new Airport(origin, origin, departureTime.format(FORMATTER)));
            flight.setArrivalAirport(new Airport(destination, destination, arrivalTime.format(FORMATTER)));
            flight.setDuration(flightDuration);
            flight.setAirplane(getRandomItem(AIRPLANES));
            flight.setAirline(getRandomItem(AIRLINES));
            flight.setAirlineLogo("https://via.placeholder.com/50");
            flight.setTravelClass(request.getTravelClass());
            flight.setFlightNumber("MK" + (100 + RANDOM.nextInt(900)));
            //flight.setExtensions(getRandomExtensions(request.getExtraOptions()));
            flight.setLegroom(getLegroomForClass(flight.getTravelClass(),request.getMinAvgLegroom()));
            flight.setOvernight(departureTime.getDayOfMonth() != arrivalTime.getDayOfMonth());

            flights.add(flight);
            totalDuration += flightDuration;
            departureTime = arrivalTime.plusMinutes(60); // 1-hour layover
        }

        int price = generatePrice(request.getMaxPrice(), flights.size());

        FlightItinerary itinerary = new FlightItinerary();
        itinerary.setFlights(flights);
        itinerary.setTotalDuration(totalDuration);
        itinerary.setPrice(price);

        return itinerary;
    }

    private String getRandomAirport() {
        return getRandomItem(AIRPORTS);
    }

    private String getRandomAirportExcluding(String exclude) {
        return AIRPORTS.stream().filter(a -> !a.equals(exclude)).findFirst().orElse("LAX");
    }


    private List<String> getRandomExtensions(List<String> extraOptions) {
        List<String> availableOptions = List.of("WiFi", "Power Outlet", "Extra Legroom", "Window Seat", "Group Seating", "Close to Exit");
        if (extraOptions == null || extraOptions.isEmpty()) {
            return availableOptions.subList(0, RANDOM.nextInt(3) + 1);
        }
        return extraOptions;
    }

    private String getLegroomForClass(String travelClass, Integer minLegroom) {
        Random random = new Random();

        // Base legroom values for different classes
        int baseLegroom = switch (travelClass) {
            case "4" -> 110; // First Class
            case "3" -> 90;  // Business
            case "2" -> 80;  // Premium Economy
            default -> 70;   // Economy
        };

        // Ensure the base legroom is at least minLegroom
        int finalLegroom = Math.max(baseLegroom, minLegroom);

        // Add random variation (+0 to 3 cm)
        finalLegroom += random.nextInt(4);

        return finalLegroom + " cm";
    }

    private int generatePrice(Integer maxPrice, int numFlights) {
        int basePrice = 150 + RANDOM.nextInt(300);
        int finalPrice = basePrice * numFlights;
        return (maxPrice != null && finalPrice > maxPrice) ? maxPrice : finalPrice;
    }

    private LocalDateTime generateRandomDepartureTime() {
        return LocalDateTime.now().plusDays(RANDOM.nextInt(30))
                .withHour(6 + RANDOM.nextInt(12))
                .withMinute(RANDOM.nextBoolean() ? 0 : 30);
    }

    private int getFlightLegsBasedOnStops(Integer stops) {
        return switch (stops) {
            case 1 -> 1; // Nonstop only
            case 2 -> RANDOM.nextBoolean() ? 1 : 2; // 1 stop or fewer
            case 3 -> RANDOM.nextInt(3) + 1; // Up to 2 stops
            default -> RANDOM.nextBoolean() ? 1 : 2; // Default (random)
        };
    }

    private <T> T getRandomItem(List<T> list) {
        return list.get(RANDOM.nextInt(list.size()));
    }
}
