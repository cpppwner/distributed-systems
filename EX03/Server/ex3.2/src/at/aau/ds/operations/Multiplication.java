package at.aau.ds.operations;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Multiplication extends UnicastRemoteObject implements Operation {

    public Multiplication() throws RemoteException {
        super();
    }

    @Override
    public int execute(int... operands) throws RemoteException, OperationException {
        if (operands.length < 2) {
            throw new OperationException("At least two operands are required");
        }

        int result = operands[0];
        for (int i = 1; i < operands.length; i++) {
            result *= operands[i];
        }

        return result;
    }
}
