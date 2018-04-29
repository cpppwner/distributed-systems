package at.aau.ds;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PhilosophersParty extends Remote {

    /**
     * Initialize the dining philosophers.
     *
     * <p>
     *     This must be called before anything else and can only be called once.
     * </p>
     *
     * @param numPhilosophers The number of philosophers.
     * @throws RemoteException
     */
    void initialize(int numPhilosophers) throws RemoteException;

    /**
     * Let a philosopher join.
     *
     * @param philosopherName The philosopher's name.
     * @return Returns a unique identifier for the philosopher, which must be used in subsequent calls.
     * @throws RemoteException
     */
    int join(String philosopherName) throws RemoteException;

    /**
     * Get forks.
     *
     * @param philosopherId Unique identifier as returned by {@link #join(String)}
     * @return
     * @throws RemoteException
     */
    void getForks(int philosopherId) throws RemoteException;

    /**
     * Release forks.
     *
     * @param philosopherId Unique identifier as returned by {@link #join(String)}
     * @return
     * @throws RemoteException
     */
    void returnForks(int philosopherId) throws RemoteException;
}
