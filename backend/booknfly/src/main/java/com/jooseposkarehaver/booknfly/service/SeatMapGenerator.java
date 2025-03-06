package com.jooseposkarehaver.booknfly.service;

import com.jooseposkarehaver.booknfly.model.Seat;
import com.jooseposkarehaver.booknfly.model.SeatingMessage;
import com.jooseposkarehaver.booknfly.model.SeatingResponse;

import java.util.*;
import java.util.stream.Collectors;

public class SeatMapGenerator {


    public static SeatingResponse generateSeatMap(String bookingId, int tickets, boolean extraLegroom, boolean windowSeats, boolean groupSeating, boolean closeToExit, String seatingClass) {
        // Use a hash of the booking ID to create a deterministic randomness
        long seed = (bookingId != null) ? bookingId.hashCode() : "123456789".hashCode();
        Random random = new Random(seed);

        List<Seat> seatMap = new ArrayList<>();

        // Generate a seat layout
        for (int row = 1; row <= 25; row++) {
            String seatClass = determineSeatClass(row);

            for (char seat = 'A'; seat <= 'F'; seat++) {
                List<String> features = new ArrayList<>();
                if (row <= 6 || (row >= 14 && (seat == 'A' || seat == 'F'))) features.add("extra_legroom");
                if (seat == 'A' || seat == 'F') features.add("window_seat");
                if (row == 7 || row == 13 || row == 14 || row == 25) features.add("close_to_exit");

                boolean occupied = random.nextDouble() < 0.3; // 30% of seats occupied

                Seat newSeat = new Seat(row + String.valueOf(seat), row, seatClass, features, !occupied,  // Available if not occupied
                        false, // Not suggested yet,
                        false // Not selected yet.
                );

                seatMap.add(newSeat);
            }
        }

        // Find and suggest seats
        SeatingMessage seatMessage = suggestSeats(seatMap, tickets, seatingClass , extraLegroom, windowSeats, groupSeating, closeToExit);

        return new SeatingResponse(seatMap, seatMessage);
    }

    private static String determineSeatClass(int row) {
        if (row <= 2) return "First Class";
        if (row <= 6) return "Business";
        if (row <= 13) return "Premium Economy";
        return "Economy";
    }


    private static SeatingMessage suggestSeats(List<Seat> seatMap, int tickets, String seatingClass, boolean extraLegroom, boolean windowSeats, boolean groupSeating, boolean closeToExit) {
        // Filter available seats by seating class
        List<Seat> availableSeats = seatMap.stream()
                .filter(Seat::isAvailable)
                .filter(seat -> seat.getSeatClass().equals(seatingClass))
                .collect(Collectors.toList());

        if (availableSeats.isEmpty()) {
            return new SeatingMessage("error", "No available seats in the selected seating class.");
        }

        // Solo ticket case - prioritize preferences
        if (tickets == 1) {
            for (Seat seat : availableSeats) {
                boolean matches = true;
                if (extraLegroom && !seat.getFeatures().contains("extra_legroom")) matches = false;
                if (windowSeats && !seat.getFeatures().contains("window_seat")) matches = false;
                if (closeToExit && !seat.getFeatures().contains("close_to_exit")) matches = false;

                if (matches) {
                    seat.setSuggested(true);
                    return null; // No message needed
                }
            }


            availableSeats.get(0).setSuggested(true);
            return new SeatingMessage("warning", "No seats fully matched your preferences, but a seat was assigned.");
        }

        // Group seating case
        if (groupSeating) {
            Map<Integer, List<Seat>> seatsByRow = availableSeats.stream().collect(Collectors.groupingBy(Seat::getRow));

            for (List<Seat> rowSeats : seatsByRow.values()) {
                if (rowSeats.size() >= tickets) {
                    for (int i = 0; i < tickets; i++) {
                        rowSeats.get(i).setSuggested(true);
                    }
                    return null;
                }
            }

            // If not enough seats in a single row, break up the group
            List<Seat> assignedSeats = new ArrayList<>();
            for (Seat seat : availableSeats) {
                if (assignedSeats.size() < tickets) {
                    seat.setSuggested(true);
                    assignedSeats.add(seat);
                }
            }

            if (assignedSeats.size() < tickets) {
                return new SeatingMessage("warning", "Not enough seats in the same row, but seats were assigned close together.");
            }
            return new SeatingMessage("info", "Seats assigned in multiple rows due to limited availability.");
        }

        // If group seating is off, just find best available seats
        List<Seat> selectedSeats = new ArrayList<>();
        for (Seat seat : availableSeats) {
            if (selectedSeats.size() < tickets) {
                seat.setSuggested(true);
                selectedSeats.add(seat);
            }
        }

        if (selectedSeats.size() < tickets) {
            return new SeatingMessage("warning", "Not enough seats available matching all criteria.");
        }

        return null;
    }




}
