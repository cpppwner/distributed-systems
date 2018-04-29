package at.aau.ds.rooms;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Rooms {

    private final Map<Integer, RoomInformation> roomInformationMap = new HashMap<>();



    public void addRoomInformation(RoomInformation roomInformation) {

        if (roomInformationMap.containsKey(roomInformation.getType())) {
            throw new IllegalStateException("RoomInformation for type " + roomInformation.getType() + " has been added already");
        }

        roomInformationMap.put(roomInformation.getType(), roomInformation);
    }


    public Set<Integer> getAllRoomTypes() {

        return roomInformationMap.keySet();
    }

    public int getNumberOfAvailableRoomsForType(int roomType) {

        RoomInformation information = roomInformationMap.get(roomType);
        if (information == null) {
            throw new IllegalArgumentException("RoomInformation for type " + roomType + " is not available");
        }

        return information.getNumAvailable();
    }

    public boolean isValidRoomType(int roomType) {

        return roomInformationMap.containsKey(roomType);
    }

    public int computePrice(Integer roomType, int arrivalDay, int departureDay) {

        RoomInformation roomInformation = roomInformationMap.get(roomType);
        if (roomInformation == null) {
            throw new IllegalArgumentException("roomType " + roomType + " is invalid.");
        }

        int numberOfDays = departureDay - arrivalDay;
        return roomInformation.getPricePerNight() * numberOfDays;
    }
}
