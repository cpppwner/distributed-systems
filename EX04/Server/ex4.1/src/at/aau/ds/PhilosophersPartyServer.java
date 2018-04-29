package at.aau.ds;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class PhilosophersPartyServer {
    private static final int RMI_REGISTRY_PORT = Registry.REGISTRY_PORT;
    private static final String RMI_NAME = "forks";

    public static void main(String[] args) {

        // try startup the registry
        Registry registry = null;
        try {
            registry = LocateRegistry.createRegistry(RMI_REGISTRY_PORT);
        } catch (RemoteException e) {
            System.out.println("Failed to create registry - trying to locate registry.");
            try {
                registry = LocateRegistry.getRegistry(RMI_REGISTRY_PORT);
            } catch (RemoteException e1) {
                System.out.println("Failed to locate registry at port " + RMI_REGISTRY_PORT + ".");
                System.exit(1);
            }
        }

        try {
            registry.rebind(RMI_NAME, new PhilosophersPartyImpl());
        } catch (RemoteException e) {
            System.out.println("Failed to bind forks");
            System.exit(1);
        }

        System.out.println("Philosophers party server is up and running ...");
    }
}
