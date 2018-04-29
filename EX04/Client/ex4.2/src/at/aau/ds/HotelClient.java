package at.aau.ds;

import at.aau.ds.rmi.AvailableRooms;
import at.aau.ds.rmi.BookingConfirmation;
import at.aau.ds.rmi.BookingSummary;
import at.aau.ds.rmi.RoomManager;

import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class HotelClient {

    private static final String COMMAND_CHECK_AVAILABILITY = "checkAvailability";
    private static final String COMMAND_BOOK = "book";
    private static final String COMMAND_SUMMARY = "summary";

    private static final String RMI_REGISTRY_HOST = "localhost";
    private static final int RMI_REGISTRY_PORT = Registry.REGISTRY_PORT;
    private static final String RMI_NAME = "hotel";

    private static final int MIN_DAY = 1;
    private static final int MAX_DAY = 100;

    private HotelClient() {}

    private void summary() {

        RoomManager roomManager = locateRoomManager();
        if (roomManager != null) {
            try {
                BookingSummary summary = roomManager.summary();
                summary.print(new PrintWriter(System.out, true));
            } catch (RemoteException e) {
                System.out.println("Failed to query booking summary " + e);
                e.printStackTrace();
            }
        }
    }

    private void checkAvailability(int arrivalDay, int departureDay) {

        RoomManager roomManager = locateRoomManager();
        if (roomManager != null) {
            try {
                AvailableRooms availableRooms = roomManager.checkAvailability(arrivalDay, departureDay);
                availableRooms.print(new PrintWriter(System.out, true));
            } catch (RemoteException e) {
                System.out.println("Failed to query booking summary " + e);
                e.printStackTrace();
            }
        }
    }

    private void book(String guestName, int roomType, int arrivalDay, int departureDay) {
        RoomManager roomManager = locateRoomManager();
        if (roomManager != null) {
            try {
                BookingConfirmation bookingConfirmation = roomManager.book(guestName, roomType, arrivalDay, departureDay);
                if (bookingConfirmation.isBookingSuccessful()) {
                    System.out.println("Booking succeeded.");
                } else {
                    System.out.println("Booking was unsuccessful - alternative rooms are:");
                    bookingConfirmation.getAlternativeRooms().print(new PrintWriter(System.out, true));
                }
            } catch (RemoteException e) {
                System.out.println("Failed to query booking summary " + e);
                e.printStackTrace();
            }
        }
    }

    private RoomManager locateRoomManager() {

        try {
            Registry registry = LocateRegistry.getRegistry(RMI_REGISTRY_HOST, RMI_REGISTRY_PORT);
            return (RoomManager) registry.lookup(RMI_NAME);
        } catch (Exception e) {
            System.out.println("Failed to lookup RMI_NAME" + e);
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("Illegal number of arguments");
            printUsage();
            System.exit(1);
        }

        switch (args[0]) {
            case COMMAND_CHECK_AVAILABILITY:
                checkAvailability(args);
                break;
            case COMMAND_BOOK:
                book(args);
                break;
            case COMMAND_SUMMARY:
                summary(args);
                break;
            default:
                printUsage();
                System.exit(1);
                break;
        }
    }

    private static void summary(String[] args) {
        // no further args expected
        if (args.length != 1) {
            System.out.println("Illegal number of arguments");
            printUsage();
            System.exit(1);
        }

        new HotelClient().summary();
    }

    private static void checkAvailability(String[] args) {
        // arrival day and departure day expected
        if (args.length != 3) {
            System.out.println("Illegal number of arguments");
            printUsage();
            System.exit(1);
        }

        int arrivalDay = parseArrivalDay(args[1]);
        int departureDay = parseDepartureDay(args[2]);

        if (departureDay <= arrivalDay) {
            System.out.println("departureDay is less than or equal to arrivalDay");
            printUsage();
            System.exit(1);
        }

        new HotelClient().checkAvailability(arrivalDay, departureDay);
    }

    private static void book(String[] args) {
        // guest name, room type, arrival day and departure day expected
        if (args.length != 5) {
            System.out.println("Illegal number of arguments");
            printUsage();
            System.exit(1);
        }

        String guestName = args[1];
        if (guestName.trim().isEmpty()) {
            System.out.println("guestName must not be empty");
            printUsage();
            System.exit(1);
        }

        int roomType = 0;
        try {
            roomType = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            System.out.println("Room type is not a valid integer");
            printUsage();
            System.exit(1);
        }

        int arrivalDay = parseArrivalDay(args[3]);
        int departureDay = parseDepartureDay(args[4]);

        if (departureDay <= arrivalDay) {
            System.out.println("departureDay is less than or equal to arrivalDay");
            printUsage();
            System.exit(1);
        }

        new HotelClient().book(guestName, roomType, arrivalDay, departureDay);
    }

    private static int parseArrivalDay(String arrivalDayString) {
        int arrivalDay = 0;
        try {
            arrivalDay = Integer.parseInt(arrivalDayString);
        } catch (NumberFormatException e) {
            System.out.println("arrivalDay is not a valid integer.");
            printUsage();
            System.exit(1);
        }

        if (arrivalDay < MIN_DAY || arrivalDay > MAX_DAY) {
            System.out.println("arrivalDay is out of range [" + MIN_DAY + ", " + MAX_DAY + "]");
            printUsage();
            System.exit(1);
        }

        return arrivalDay;
    }

    private static int parseDepartureDay(String departureDayString) {
        int departureDay = 0;
        try {
            departureDay = Integer.parseInt(departureDayString);
        } catch (NumberFormatException e) {
            System.out.println("departureDay is not a valid integer.");
            printUsage();
            System.exit(1);
        }


        if (departureDay < MIN_DAY || departureDay > MAX_DAY) {
            System.out.println("departureDay is out of range [" + MIN_DAY + ", " + MAX_DAY + "]");
            printUsage();
            System.exit(1);
        }

        return departureDay;
    }

    private static void printUsage() {
        System.out.println("Usage: HotelClient <command> [ARGS]");
        System.out.println("Client for interacting with HotelServer");
        System.out.println();
        System.out.println("Valid <command>");
        System.out.println("\tcheckAvailability <arrivalDay> <departureDay>             Print list of available rooms");
        System.out.println("\tbook <guestName> <roomType> <arrivalDay> <departureDay>   Book room");
        System.out.println("\tsummary                                                   Print summary of bookings");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("\tHotelClient checkAvailability <arrivalDay> <departureDay>");
        System.out.println("\tHotelClient book <guestName> <roomType> <arrivalDay> <departureDay>");
        System.out.println("\tHotelClient summary");
    }
}
