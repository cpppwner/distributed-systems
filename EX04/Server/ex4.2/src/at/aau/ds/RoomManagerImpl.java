package at.aau.ds;

import at.aau.ds.availability.AvailabilityChecker;
import at.aau.ds.bookings.Booking;
import at.aau.ds.bookings.Bookings;
import at.aau.ds.rmi.AvailableRooms;
import at.aau.ds.rmi.BookingConfirmation;
import at.aau.ds.rmi.BookingSummary;
import at.aau.ds.rmi.RoomManager;
import at.aau.ds.rooms.RoomInformation;
import at.aau.ds.rooms.Rooms;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.concurrent.locks.StampedLock;

public class RoomManagerImpl extends UnicastRemoteObject implements RoomManager {

    private final Rooms rooms = new Rooms();
    private final Bookings bookings = new Bookings();
    private final StampedLock stampedLock = new StampedLock();

    RoomManagerImpl() throws RemoteException {
        super();

        initializeRooms();
    }

    private void initializeRooms() {
        rooms.addRoomInformation(new RoomInformation(0, 10, 55));
        rooms.addRoomInformation(new RoomInformation(1, 20, 75));
        rooms.addRoomInformation(new RoomInformation(2, 5, 90));
        rooms.addRoomInformation(new RoomInformation(3, 3, 130));
        rooms.addRoomInformation(new RoomInformation(4, 2, 250));
    }

    @Override
    public AvailableRooms checkAvailability(int arrivalDay, int departureDay) throws RemoteException {

        validateArrivalAndDepartureDay(arrivalDay, departureDay);

        long stamp = stampedLock.readLock();
        Map<Integer, Integer> roomTypeNumAvailableMap;
        try {
            roomTypeNumAvailableMap = new AvailabilityChecker(rooms, bookings).getAvailableRooms(arrivalDay, departureDay);
        } finally {
            stampedLock.unlockRead(stamp);
        }

        AvailableRooms availableRooms = new AvailableRooms(arrivalDay, departureDay);
        for (Map.Entry<Integer, Integer> entry : roomTypeNumAvailableMap.entrySet()) {
            availableRooms.addAvailableRoomType(entry.getKey(), entry.getValue(), rooms.computePrice(entry.getKey(), arrivalDay, departureDay));
        }

        return availableRooms;
    }

    @Override
    public BookingConfirmation book(String guestName, int roomType, int arrivalDay, int departureDay) throws RemoteException {

        if (guestName == null || guestName.trim().isEmpty()) {
            throw new IllegalArgumentException("guestName not given");
        }
        if (!rooms.isValidRoomType(roomType)) {
            throw new IllegalArgumentException("roomType is invalid");
        }
        validateArrivalAndDepartureDay(arrivalDay, departureDay);

        long stamp = stampedLock.writeLock();
        boolean bookingConfirmed = false;
        try {
            // check if given room type is available within
            if (new AvailabilityChecker(rooms, bookings).isAvailable(roomType, arrivalDay, departureDay)) {
                bookings.addBooking(new Booking(guestName, arrivalDay, departureDay, roomType, rooms.computePrice(roomType, arrivalDay, departureDay)));
                bookingConfirmed = true;
            }
        } finally {
            stampedLock.unlockWrite(stamp);
        }

        if (bookingConfirmed) {
            return new BookingConfirmation(true, null);
        } else {
            return new BookingConfirmation(false, checkAvailability(arrivalDay, departureDay));
        }
    }

    @Override
    public BookingSummary summary() throws RemoteException {

        long stamp = stampedLock.readLock();
        BookingSummary result = new BookingSummary();
        try {

            for (Booking booking : bookings.getBookings()) {
                result.addBooking(new BookingSummary.Booking(booking.getGuestName(),
                        booking.getArrivalDay(),
                        booking.getDepartureDay(),
                        booking.getRoomType(),
                        booking.getTotalPrice()));
            }

        } finally {
            stampedLock.unlockRead(stamp);
        }

        return result;
    }

    private void validateArrivalAndDepartureDay(int arrivalDay, int departureDay) {
        if (arrivalDay < 1 || arrivalDay > 100) {
            throw new IllegalArgumentException("arrivalDay is < 1 or > 100");
        }
        if (departureDay < 1 || departureDay > 100) {
            throw new IllegalArgumentException("departureDay is < 1 or > 100");
        }
        if (departureDay <= arrivalDay) {
            throw new IllegalArgumentException("departureDay is <= arrivalDay");
        }
    }
}
