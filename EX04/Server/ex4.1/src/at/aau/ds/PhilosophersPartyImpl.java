package at.aau.ds;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class PhilosophersPartyImpl extends UnicastRemoteObject implements PhilosophersParty {


    /** Store name of owning philosopher (not id). */
    private int[] forks;
    /** Binary semaphores for synchronization */
    private Semaphore[] semaphores;

    /** Countdown latch used to wait for philosophers */
    private CountDownLatch countDownLatch;
    /** Philosopher id counter */
    private final AtomicInteger philosopherIdCounter = new AtomicInteger(0);
    /** philosopher's name.*/
    private String[] philosophers;


    PhilosophersPartyImpl() throws RemoteException {
        super();
    }

    @Override
    public void initialize(int numPhilosophers) throws RemoteException {

        forks = new int[numPhilosophers];
        semaphores = new Semaphore[numPhilosophers];
        philosophers = new String[numPhilosophers];
        for (int i = 0; i < numPhilosophers; i++) {
            forks[i] = -1;
            semaphores[i] = new Semaphore(1);
            philosophers[i] = null;
            philosopherIdCounter.set(0);
        }
        countDownLatch = new CountDownLatch(numPhilosophers);

    }

    @Override
    public int join(String philosopherName) throws RemoteException {

        System.out.println(philosopherName + " is joining the party.");
        int id = philosopherIdCounter.getAndIncrement();
        philosophers[id] = philosopherName;
        countDownLatch.countDown();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            System.out.println("Philosopher's party was interrupted.");
        }

        return id;
    }

    @Override
    public void getForks(int philosopherId) throws RemoteException {

        if (philosopherId < 0 || philosopherId > philosophers.length) {
            throw new IllegalArgumentException("philosopherId " + philosopherId + " is out of range.");
        }

        System.out.println("Philosopher " + philosophers[philosopherId] + "(" + philosopherId + ") is trying to get forks ...");

        Random rand = new Random();

        while (true) {

            // flip a coin to determine whether to try left or right coin
            int randomValue = rand.nextInt(2);

            // try to acquire the fork
            int forkOne = (philosopherId + randomValue) % philosophers.length;
            try {
                semaphores[forkOne].acquire();
            } catch (InterruptedException e) {
                System.out.println("Philosopher " + philosophers[philosopherId] + " was interrupted while getting forks ...");
                // nothing to release here - just bail out
                break;
            }

            if (forks[forkOne] == -1) {
                // fork not taken, try to get the second fork
                int forkTwo = (philosopherId + (1 - randomValue)) % philosophers.length;
                try {
                    semaphores[forkTwo].acquire();
                } catch (InterruptedException e) {
                    System.out.println("Philosopher " + philosophers[philosopherId] + " was interrupted while getting forks ...");
                    // release the semaphore the philosopher is currently holding and bail out
                    semaphores[forkOne].release();
                    break;
                }

                if (forks[forkTwo] == -1) {
                    // both forks are available - take them
                    forks[forkOne] = philosopherId;
                    forks[forkTwo] = philosopherId;

                    System.out.println("Philosopher " + philosophers[philosopherId] + "(" + philosopherId + ") can start using forks " + forkOne + " " + forkTwo);

                    semaphores[forkOne].release();
                    semaphores[forkTwo].release();

                    // done - break out of loop
                    break;
                }

                // second fork is taken - therefore semaphore needs to be released
                // +retry by staying in loop
                semaphores[forkTwo].release();
            }

            // first fork is taken - therefore semaphore needs to be released
            // +retry by staying in loop
            semaphores[forkOne].release();
        }
    }

    @Override
    public void returnForks(int philosopherId) throws RemoteException {

        if (philosopherId < 0 || philosopherId > philosophers.length) {
            throw new IllegalArgumentException("philosopherId " + philosopherId + " is out of range.");
        }

        int forkOne = philosopherId;
        int forkTwo = (philosopherId + 1) % philosophers.length;

        // acquire the semaphores
        try {
            semaphores[forkOne].acquire();
        } catch (InterruptedException e) {
            System.out.println("Philosopher " + philosophers[philosopherId] + " was interrupted while returning forks.");
            return;
        }

        try {
            semaphores[forkTwo].acquire();
        } catch (InterruptedException e) {
            System.out.println("Philosopher " + philosophers[philosopherId] + " was interrupted while returning forks.");
            semaphores[forkOne].release();
            return;
        }

        if (forks[forkOne] != philosopherId || forks[forkTwo] != philosopherId) {
            // the philosopher does not hold all forks??
            System.out.println("Not all forks held by philosopher");
            semaphores[forkOne].release();
            semaphores[forkTwo].release();
            return;
        }

        forks[forkOne] = -1;
        forks[forkTwo] = -1;

        System.out.println("Philosopher " + philosophers[philosopherId] + "(" + philosopherId + ") has returned forks " + forkOne + " " + forkTwo);

        semaphores[forkOne].release();
        semaphores[forkTwo].release();
    }
}
