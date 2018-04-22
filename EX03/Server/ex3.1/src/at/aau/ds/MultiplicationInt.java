package at.aau.ds;

        import java.rmi.Remote;
        import java.rmi.RemoteException;

public interface MultiplicationInt extends Remote {
    int mull(int a, int b) throws RemoteException;
}
