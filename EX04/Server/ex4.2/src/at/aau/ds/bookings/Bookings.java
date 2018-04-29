package at.aau.ds.bookings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Bookings {

    private final List<Booking> bookings = new ArrayList<>();

    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    public List<Booking> getIntersectingBookings(int startDay, int endDay) {

        return bookings.stream()
                .filter(booking -> booking.isIntersecting(startDay, endDay))
                .collect(Collectors.toList());
    }

    public List<Booking> getIntersectingBookings(int roomType, int startDay, int endDay) {

        return bookings.stream()
                .filter(booking -> booking.isIntersecting(startDay, endDay) && booking.getRoomType() == roomType)
                .collect(Collectors.toList());
    }

    public List<Booking> getBookings() {

        return Collections.unmodifiableList(bookings);
    }
}
