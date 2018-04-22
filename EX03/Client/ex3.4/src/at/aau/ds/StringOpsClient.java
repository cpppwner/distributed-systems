package at.aau.ds;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StringOpsClient {

    private static final Pattern LINE_PATTERN = Pattern.compile("^(\\d+):.*$");
    private static final String FILENAME = "text.txt";

    public static void main(String[] args) {

        final LinkedBlockingQueue<String> results = new LinkedBlockingQueue<>();
        final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 2);

        long start = System.nanoTime();

        StringOps ops;
        try {
            ops = (StringOps)Naming.lookup("rmi://localhost/StringOpsSafe");
        } catch (NotBoundException | RemoteException | MalformedURLException e) {
            System.out.println("Failed to lookup StringOps service" + e);
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {
            for(String line = br.readLine(); line != null; line = br.readLine()) {
                final String currentLine = line;
                executorService.submit(() -> {
                    String result = ops.uniqueReverse(currentLine);
                    System.out.println(result);
                    results.add(result);
                });
            }
        } catch (IOException e) {
            System.out.println("Something failed " + e);
            return;
        }

        // wait for termination
        executorService.shutdown();
        while (!executorService.isTerminated()) {
            try {
                executorService.awaitTermination(1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                System.out.println("Terminated ...");
                return;
            }
        }

        // ensure all patterns are unique
        Set<Integer> resultIds = new HashSet<>();
        for (String line : results) {
            Matcher matcher = LINE_PATTERN.matcher(line);
            if (matcher.matches()) {
                resultIds.add(Integer.parseInt(matcher.group(1)));
            }
        }

        if (resultIds.size() != 5000) {
            System.out.println("Something is wrong - expected 5000 distinct number, got " + resultIds.size() + " instead.");
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
