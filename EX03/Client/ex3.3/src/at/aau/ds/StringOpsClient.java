package at.aau.ds;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.concurrent.TimeUnit;

public class StringOpsClient {

    private static final String FILENAME = "text.txt";

    public static void main(String[] args) {

        long start = System.nanoTime();

        StringOps ops;
        try {
            ops = (StringOps)Naming.lookup("rmi://localhost/StringOps");
        } catch (NotBoundException | RemoteException | MalformedURLException e) {
            System.out.println("Failed to lookup StringOps service" + e);
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {
            for(String line = br.readLine(); line != null; line = br.readLine()) {
                System.out.println(ops.uniqueReverse(line));
            }
        } catch (IOException e) {
            System.out.println("Something failed" + e);
            return;
        }

        long duration = System.nanoTime() - start;
        System.out.println("Duration: " + getDurationString(duration));
    }

    private static String getDurationString(double durationInNanoseconds) {

        if (durationInNanoseconds > TimeUnit.SECONDS.toNanos(10)) {
            return Double.toString(durationInNanoseconds / (1000.0 * 1000.0 * 1000.0)) + " s";
        } else if (durationInNanoseconds > TimeUnit.MILLISECONDS.toNanos(10)) {
            return Double.toString(durationInNanoseconds / (1000.0 * 1000.0)) + " ms";
        } else if (durationInNanoseconds > TimeUnit.MICROSECONDS.toNanos(10)) {
            return Double.toString(durationInNanoseconds / 1000.0) + " µs";
        } else {
            return Double.toString(durationInNanoseconds) + " ns";
        }
    }
}
