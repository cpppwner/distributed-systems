package at.aau.ds;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

public class Multi extends UnicastRemoteObject implements MultiplicationInt {

    public Multi() throws RemoteException {
    }

    public Multi(int port) throws RemoteException {
        super(port);
    }

    public Multi(int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
        super(port, csf, ssf);
    }

    @Override
    public int mull(int a, int b) throws RemoteException {
        return a * b;
    }
}
