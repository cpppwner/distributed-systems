package at.aau.ds.operations;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Operation extends Remote {
    int execute(int ... operands) throws RemoteException, OperationException;
}
