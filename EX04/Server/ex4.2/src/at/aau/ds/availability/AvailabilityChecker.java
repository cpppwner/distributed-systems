package at.aau.ds.availability;

import at.aau.ds.bookings.Booking;
import at.aau.ds.bookings.Bookings;
import at.aau.ds.rooms.Rooms;

import java.util.*;

public class AvailabilityChecker {

    private final Rooms rooms;
    private final Bookings bookings;

    public AvailabilityChecker(Rooms rooms, Bookings bookings) {
        this.rooms = rooms;
        this.bookings = bookings;
    }

    public Map<Integer, Integer> getAvailableRooms(int arrivalDay, int departureDay) {

        // construct a map {room type -> {day -> num available rooms}}
        Set<Integer> roomTypes = rooms.getAllRoomTypes();
        Map<Integer, Map<Integer, Integer>> roomTypeAvailabilityMap = new HashMap<>();
        for (int roomType : roomTypes) {
            roomTypeAvailabilityMap.put(roomType, createAvailabilityDaysMap(arrivalDay, departureDay, roomType));
        }

        // reduce availability for all confirmed bookings
        List<Booking> intersectingBooking = bookings.getIntersectingBookings(arrivalDay, departureDay);
        for (Booking booking : intersectingBooking) {
            for (int day = Math.max(arrivalDay, booking.getArrivalDay());
                 day < Math.min(departureDay, booking.getDepartureDay());
                 day++) {

                int numRoomsAvailable = roomTypeAvailabilityMap.get(booking.getRoomType()).get(day);
                if (numRoomsAvailable <= 0) {
                    throw new IllegalStateException("Internal bookings error");
                }

                // decrease number of available rooms for given type and day
                numRoomsAvailable -= 1;

                // store the decreased number of rooms
                roomTypeAvailabilityMap.get(booking.getRoomType()).put(day, numRoomsAvailable);
            }
        }

        // construct the available rooms map
        Map<Integer, Integer> result = new HashMap<>();
        for (int roomType : roomTypeAvailabilityMap.keySet()) {
            Optional<Integer> minAvailable = roomTypeAvailabilityMap.get(roomType).values()
                    .stream()
                    .min(Integer::compare);

            if (minAvailable.isPresent() && minAvailable.get() > 0) {
                // at least one is available
                result.put(roomType, minAvailable.get());
            }
        }

        return result;
    }

    public boolean isAvailable(int roomType, int arrivalDay, int departureDay) {

        // build up availability map
        Map<Integer, Integer> availabilityDaysMap = createAvailabilityDaysMap(arrivalDay, departureDay, roomType);

        // reduce availability by confirmed bookings
        List<Booking> intersectingBooking = bookings.getIntersectingBookings(roomType, arrivalDay, departureDay);
        for (Booking booking : intersectingBooking) {
            for (int day = Math.max(arrivalDay, booking.getArrivalDay());
                 day < Math.min(departureDay, booking.getDepartureDay());
                 day++) {

                int numRoomsAvailable = availabilityDaysMap.get(day);
                if (numRoomsAvailable <= 0) {
                    throw new IllegalStateException("Internal bookings error");
                }

                // decrease number of available rooms for given type and day
                numRoomsAvailable -= 1;

                // store the decreased number of rooms
                availabilityDaysMap.put(day, numRoomsAvailable);
            }
        }

        Optional<Integer> minAvailable = availabilityDaysMap.values().stream().min(Integer::compare);
        return minAvailable.isPresent() && minAvailable.get() > 0;
    }


    private Map<Integer, Integer> createAvailabilityDaysMap(int arrivalDay, int departureDay, int roomType) {

        int numAvailableRooms = rooms.getNumberOfAvailableRoomsForType(roomType);
        Map<Integer, Integer> availabilityDaysMap = new HashMap<>();
        for (int day = arrivalDay; day < departureDay; day++) {
            availabilityDaysMap.put(day, numAvailableRooms);
        }

        return availabilityDaysMap;
    }
}
