package at.aau.ds;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;

public class DiningPhilosophers {

    private static final String RMI_REGISTRY_HOST = "localhost";
    private static final int RMI_REGISTRY_PORT = Registry.REGISTRY_PORT;
    private static final String RMI_NAME = "forks";

    private static final String[] PHILOSOPHERS = {
            "Aristotle",
            "Immanuel Kant",
            "Plato",
            "Confucius",
            "Sun Tzu"
    };

    public static void main(String[] args) throws InterruptedException {

        try {
            PhilosophersParty stub = (PhilosophersParty) LocateRegistry.getRegistry(RMI_REGISTRY_HOST, RMI_REGISTRY_PORT).lookup(RMI_NAME);
            stub.initialize(PHILOSOPHERS.length);
        } catch (Exception e) {
            System.out.println("Failed to initialize philosopher's party ...");
            e.printStackTrace();
            return;
        }

        // create philosophers
        Thread[] threads = new Thread[PHILOSOPHERS.length];
        for (int i = 0; i < PHILOSOPHERS.length; i++) {
            threads[i] = new Thread(new Philosopher(PHILOSOPHERS[i]));
        }

        // start the threads
        for(Thread thread : threads) {
            thread.start();
        }

        // and join all of them
        for(Thread thread : threads) {
            thread.join();
        }
    }

    private static class Philosopher implements Runnable {

        private static final int TOTAL_NUM_MEALS = 100;

        private static final int RANDOM_THINKING_TIME_MILLIS = 3000;
        private static final int RANDOM_EATING_TIME_MILLIS = 2000;
        private static final int RANDOM_OFFSET_MILLIS = 1000;

        private final String name;
        private final PhilosophersParty stub;
        private final Random rand = new Random();
        private int numMealsConsumed = 0;

        Philosopher(String name) {
            this.name = name;
            try {
                stub = (PhilosophersParty)LocateRegistry.getRegistry(RMI_REGISTRY_HOST, RMI_REGISTRY_PORT).lookup(RMI_NAME);
            } catch (Exception e) {
                throw  new IllegalStateException("RMI failed", e);
            }
        }

        @Override
        public void run() {

            // first of all join the party
            int philosopherId = 0;
            try {
                System.out.println("Philosopher " + name + " is trying to join the party ...");
                philosopherId = stub.join(name);
                System.out.println("Philosopher " + name + " successfully joined the party and got id " + philosopherId);
            } catch (RemoteException e) {
                System.out.println("Philosopher " + name + " failed to join the party");
                e.printStackTrace();
                return;
            }

            while (numMealsConsumed < TOTAL_NUM_MEALS) {
                System.out.println("Philosopher " + name + " is thinking - do not disturb");
                try {
                    Thread.sleep(rand.nextInt(RANDOM_THINKING_TIME_MILLIS) + RANDOM_OFFSET_MILLIS);
                } catch(InterruptedException e) {
                    System.out.println("Philosopher " + name + " was interrupted while thinking");
                    break;
                }
                System.out.println("Philosopher " + name + " finished thinking - and is hungry now");

                try {
                    stub.getForks(philosopherId);
                } catch (RemoteException e) {
                    System.out.println("Philosopher " + name + " failed to get forks");
                    e.printStackTrace();
                    return;
                }
                System.out.println("Philosopher " + name + " got forks and can start eating now");
                try {
                    Thread.sleep(rand.nextInt(RANDOM_EATING_TIME_MILLIS) + RANDOM_OFFSET_MILLIS);
                } catch(InterruptedException e) {
                    System.out.println("Philosopher " + name + " was interrupted while eating");
                }

                // increase number of meals
                numMealsConsumed += 1;
                System.out.println("Philosopher " + name + " is done with eating and releasing forks");

                try {
                    stub.returnForks(philosopherId);
                } catch (RemoteException e) {
                    System.out.println("Philosopher " + name + " failed to release forks");
                    e.printStackTrace();
                    return;
                }

                System.out.println("Philosopher " + name + " has released the forks");
            }
        }
    }
}
