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
            return assignBestMatchingSeat(availableSeats, extraLegroom, windowSeats, closeToExit);
        }

        // Group seating logic
        if (groupSeating) {
            return assignGroupSeatsWithPreferences(availableSeats, tickets, extraLegroom, windowSeats, closeToExit);
        }

        // If not group seating, assign best available individual seats
        return assignBestIndividualSeats(availableSeats, tickets, extraLegroom, windowSeats, closeToExit);
    }


    private static SeatingMessage assignBestMatchingSeat(List<Seat> availableSeats, boolean extraLegroom, boolean windowSeats, boolean closeToExit) {
        for (Seat seat : availableSeats) {
            if ((extraLegroom && seat.getFeatures().contains("extra_legroom")) ||
                    (windowSeats && seat.getFeatures().contains("window_seat")) ||
                    (closeToExit && seat.getFeatures().contains("close_to_exit"))) {
                seat.setSuggested(true);
                return null;
            }
        }

        // If no perfect match, assign first available seat
        availableSeats.get(0).setSuggested(true);
        if (!extraLegroom && !windowSeats && !closeToExit) {
            return null; // No options were selected, no need to send a message.
        }
        return new SeatingMessage("warning", "No seats fully matched your preferences, but a seat was assigned.");
    }

    // Idea behind this is that when searching group seating recommendations, we try to match preferences for one seat, then put other seats nearby.
    private static SeatingMessage assignGroupSeatsWithPreferences(List<Seat> availableSeats, int tickets, boolean extraLegroom, boolean windowSeats, boolean closeToExit) {

        // Find the best starting seat

        Seat bestStartingSeat = null;

        for (Seat seat : availableSeats) {
            boolean matchesAll = (!extraLegroom || seat.getFeatures().contains("extra_legroom")) &&
                    (!windowSeats || seat.getFeatures().contains("window_seat")) &&
                    (!closeToExit || seat.getFeatures().contains("close_to_exit"));

            if (matchesAll) {
                bestStartingSeat = seat;
                break;  // Stop at the first perfect match
            }
        }

        //  If no perfect match, pick any available seat
        if (bestStartingSeat == null) {
            bestStartingSeat = availableSeats.get(0);
        }

        // Mark the starting seat as suggested
        bestStartingSeat.setSuggested(true);
        List<Seat> selectedSeats = new ArrayList<>();
        selectedSeats.add(bestStartingSeat);

        // Try to find other seats in the same row or nearby
        Seat finalBestStartingSeat = bestStartingSeat;
        List<Seat> sameRowSeats = availableSeats.stream()
                .filter(seat -> seat.getRow() == finalBestStartingSeat.getRow() && seat != finalBestStartingSeat)
                .collect(Collectors.toList());

        for (Seat seat : sameRowSeats) {
            if (selectedSeats.size() < tickets) {
                seat.setSuggested(true);
                selectedSeats.add(seat);
            }
        }

        // If not enough seats in the same row, assign nearby seats
        for (Seat seat : availableSeats) {
            if (!selectedSeats.contains(seat) && selectedSeats.size() < tickets) {
                seat.setSuggested(true);
                selectedSeats.add(seat);
            }
        }

        // Check if all tickets were assigned
        if (selectedSeats.size() < tickets) {
            return new SeatingMessage("warning", "Not enough seats matching all preferences, but closest seats were assigned.");
        }

        return null;
    }



    private static SeatingMessage assignBestIndividualSeats(List<Seat> availableSeats, int tickets, boolean extraLegroom, boolean windowSeats, boolean closeToExit) {
        List<Seat> selectedSeats = new ArrayList<>();

        for (Seat seat : availableSeats) {
            if (selectedSeats.size() < tickets &&
            ((extraLegroom && seat.getFeatures().contains("extra_legroom")) ||
            (windowSeats && seat.getFeatures().contains("window_seat")) ||
            (closeToExit && seat.getFeatures().contains("close_to_exit")))) {
                seat.setSuggested(true);
                selectedSeats.add(seat);
            }
        }

        // If not enough seats match preferences, assign the next available ones
        for (Seat seat : availableSeats) {
            if (selectedSeats.size() < tickets && !selectedSeats.contains(seat)) {
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
