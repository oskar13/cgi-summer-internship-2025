package com.jooseposkarehaver.booknfly.service;

import com.jooseposkarehaver.booknfly.model.Seat;

import java.util.*;
import java.util.stream.Collectors;

public class SeatMapGenerator {

    public static List<Seat> generateSeatMap(double occupancy, int tickets, boolean extraLegroom, boolean windowSeats, boolean groupSeating, boolean closeToExit) {
        List<Seat> seatMap = new ArrayList<>();
        Random random = new Random();

        // Generate the airplane seating plan
        for (int row = 1; row <= 25; row++) {
            String seatClass = determineSeatClass(row);

            for (char seat = 'A'; seat <= 'F'; seat++) {
                List<String> features = new ArrayList<>();

                if (row <= 6 || (row >= 14 && (seat == 'A' || seat == 'F'))) {
                    features.add("extra_legroom");
                }
                if (seat == 'A' || seat == 'F') {
                    features.add("window_seat");
                }
                if (row == 7 || row == 13 || row == 14 || row == 25) {
                    features.add("close_to_exit");
                }

                // Determine if the seat should be randomly occupied
                boolean occupied = random.nextDouble() < occupancy;

                Seat newSeat = new Seat(
                        row + String.valueOf(seat),
                        row,
                        seatClass,
                        features,
                        !occupied,  // Available if not occupied
                        false,  // Not suggested yet
                        null   // No messages yet
                );

                seatMap.add(newSeat);
            }
        }

        // Suggest seats based on preferences
        suggestSeats(seatMap, tickets, extraLegroom, windowSeats, groupSeating, closeToExit);

        return seatMap;
    }

    private static String determineSeatClass(int row) {
        if (row <= 2) return "First Class";
        if (row <= 6) return "Business";
        if (row <= 13) return "Premium Economy";
        return "Economy";
    }

    private static void suggestSeats(List<Seat> seatMap, int tickets, boolean extraLegroom, boolean windowSeats, boolean groupSeating, boolean closeToExit) {
        List<Seat> availableSeats = seatMap.stream()
                .filter(Seat::isAvailable)
                .collect(Collectors.toList());

        List<Seat> matchingSeats = new ArrayList<>();

        for (Seat seat : availableSeats) {
            boolean matches = true;

            if (extraLegroom && !seat.getFeatures().contains("extra_legroom")) matches = false;
            if (windowSeats && !seat.getFeatures().contains("window_seat")) matches = false;
            if (closeToExit && !seat.getFeatures().contains("close_to_exit")) matches = false;

            if (matches) matchingSeats.add(seat);
        }

        // If we need group seating, try to pick tickets from the same row
        if (groupSeating) {
            Map<Integer, List<Seat>> seatsByRow = matchingSeats.stream()
                    .collect(Collectors.groupingBy(Seat::getRow));

            for (List<Seat> rowSeats : seatsByRow.values()) {
                if (rowSeats.size() >= tickets) {
                    for (int i = 0; i < tickets; i++) {
                        rowSeats.get(i).setSuggested(true);
                    }
                    return;
                }
            }
        }

        // Otherwise, just pick any matching seats
        for (int i = 0; i < tickets && i < matchingSeats.size(); i++) {
            matchingSeats.get(i).setSuggested(true);
        }

        // If not enough seats were found, return a message
        if (matchingSeats.size() < tickets) {
            for (Seat seat : seatMap) {
                if (seat.isAvailable() && !seat.isSuggested()) {
                    seat.setMessage("Not enough seats available matching all criteria.");
                }
            }
        }
    }
}
