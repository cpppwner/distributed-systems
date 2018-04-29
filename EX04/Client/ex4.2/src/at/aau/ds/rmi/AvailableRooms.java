package at.aau.ds.rmi;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AvailableRooms implements Serializable  {

    private final Map<Integer, AvailableRoomType> availableRoomTypes = new HashMap<>();
    private final int arrivalDay;
    private final int departureDay;

    public AvailableRooms(int arrivalDay, int departureDay) {
        this.arrivalDay = arrivalDay;
        this.departureDay = departureDay;
    }

    public void addAvailableRoomType(int type, int numRoomsAvailable, int totalPrice) {

        // check that the same type has not been added yet
        if (availableRoomTypes.containsKey(type)) {
            throw new IllegalStateException("roomType has already been added");
        }

        availableRoomTypes.put(type, new AvailableRoomType(type, numRoomsAvailable, totalPrice));
    }

    public List<AvailableRoomType> getAvailableRoomTypes() {

        return availableRoomTypes.values().stream()
                .sorted(Comparator.comparing(AvailableRoomType::getType))
                .collect(Collectors.toList());
    }

    public void print(PrintWriter writer) {

        writer.println("Available rooms (arrivalDay=" + arrivalDay + "; departureDay=" + departureDay + ")");
        for (AvailableRoomType availableRoomTypes :  getAvailableRoomTypes()) {
            availableRoomTypes.print(writer);
        }
    }

    public static class AvailableRoomType implements Serializable {

        private final int type;

        private final int numAvailable;

        private final int totalPrice;

        public AvailableRoomType(int type, int numAvailable, int totalPrice) {
            this.type = type;
            this.numAvailable = numAvailable;
            this.totalPrice = totalPrice;
        }

        public int getType() {
            return type;
        }

        public int getNumAvailable() {
            return numAvailable;
        }

        public int getTotalPrice() {
            return totalPrice;
        }

        public void print(PrintWriter writer) {
            writer.println("\ttype=" + getType() + "; #rooms available=" + getNumAvailable() + "; total price=" + getTotalPrice());
        }
    }
}
