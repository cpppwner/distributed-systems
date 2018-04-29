package at.aau.ds.rmi;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BookingSummary implements Serializable {

    private final List<Booking> bookings = new ArrayList<>();

    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    public List<Booking> getBookings() {
        return Collections.unmodifiableList(bookings);
    }

    public void print(PrintWriter writer) {

        getBookings().stream()
                .sorted(Comparator.comparing(Booking::getArrivalDay)
                        .thenComparing(Booking::getDepartureDay)
                        .thenComparing(Booking::getRoomType))
                .forEach(booking -> booking.print(writer));

    }

    public static final class Booking implements Serializable {

        private final String guestName;
        private final int arrivalDay;
        private final int departureDay;
        private final int roomType;
        private final int totalPrice;

        public Booking(String guestName, int arrivalDay, int departureDay, int roomType, int totalPrice) {
            this.guestName = guestName;
            this.arrivalDay = arrivalDay;
            this.departureDay = departureDay;
            this.roomType = roomType;
            this.totalPrice = totalPrice;
        }

        public String getGuestName() {
            return guestName;
        }

        public int getArrivalDay() {
            return arrivalDay;
        }

        public int getDepartureDay() {
            return departureDay;
        }

        public int getRoomType() {
            return roomType;
        }

        public int getTotalPrice() {
            return totalPrice;
        }

        public void print(PrintWriter writer) {
            writer.println("Booking: guest name=" + getGuestName() + "; "
                    + "arrival day = " + getArrivalDay() + "; "
                    + "departure day = " + getDepartureDay() + "; "
                    + "room type = " + getRoomType() + "; "
                    + "total price = " + getTotalPrice());
        }
    }
}
