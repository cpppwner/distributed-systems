package at.aau.ds;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface StringOps extends Remote {
    String uniqueReverse(String input) throws RemoteException;
}
