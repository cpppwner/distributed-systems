package at.aau.ds.rooms;

public class RoomInformation {

    private final int type;
    private final int numAvailable;
    private final int pricePerNight;

    public RoomInformation(int type, int numAvailable, int pricePerNight) {
        this.type = type;
        this.numAvailable = numAvailable;
        this.pricePerNight = pricePerNight;
    }

    public int getType() {
        return type;
    }

    public int getNumAvailable() {
        return numAvailable;
    }

    public int getPricePerNight() {
        return pricePerNight;
    }
}
