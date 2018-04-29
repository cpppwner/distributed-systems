package at.aau.ds.rmi;

import java.io.Serializable;

public class BookingConfirmation implements Serializable {

    private final boolean bookingSucceeded;

    private final AvailableRooms alternativeRooms;

    public BookingConfirmation(boolean bookingSucceeded, AvailableRooms alternativeRooms) {
        this.bookingSucceeded = bookingSucceeded;
        this.alternativeRooms = alternativeRooms;
    }

    public boolean isBookingSuccessful() {
        return bookingSucceeded;
    }

    public AvailableRooms getAlternativeRooms() {
        return alternativeRooms;
    }
}
