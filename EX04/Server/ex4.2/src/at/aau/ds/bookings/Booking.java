package at.aau.ds.bookings;

public class Booking {

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

    public boolean isIntersecting(int startDay, int endDay) {

        if (endDay <= startDay) {
            throw new IllegalArgumentException("endDay <= startDay");
        }

        return endDay > arrivalDay && startDay < departureDay;
    }
}
