package at.aau.ds.operations;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;

public class Addition extends UnicastRemoteObject implements Operation {

    public Addition() throws RemoteException {
        super();
    }

    @Override
    public int execute(int... operands) throws RemoteException, OperationException {
        if (operands.length < 2) {
            throw new OperationException("At least two operands are required");
        }

        return Arrays.stream(operands).sum();
    }
}
