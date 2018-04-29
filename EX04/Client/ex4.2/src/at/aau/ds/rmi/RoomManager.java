package at.aau.ds.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RoomManager extends Remote {

    AvailableRooms checkAvailability(int arrivalDay, int departureDay) throws RemoteException;

    BookingConfirmation book(String guestName, int roomType, int arrivalDay, int departureDay) throws RemoteException;

    BookingSummary summary() throws RemoteException;
}
