package at.aau.ds;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Calculator {

    private static final IntegerRange RANGE = new IntegerRange(1, 100000);

    private final int numThreads;

    public Calculator(int numThreads) {
        this.numThreads = numThreads;
    }

    public DivisionResults calculate() {

        // fill up the queue containing the input numbers
        ConcurrentLinkedQueue<Integer> numbers = new ConcurrentLinkedQueue<>();
        for (int number : RANGE) {
            numbers.add(number);
        }

        // create the thread pool
        List<CalculationThread> threads = new ArrayList<>(numThreads);
        for (int i = 0; i < numThreads; i++) {
            threads.add(new CalculationThread(numbers));
        }

        // start threads
        threads.forEach(Thread::start);

        // wait for all of them to be finished
        for (CalculationThread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                // intentionally left empty
            }
        }

        // collect final results
        DivisionResults results = new DivisionResults();
        for (CalculationThread thread : threads) {
            results.merge(thread.getResults());
        }

        return results;
    }


    public static void main(String[] args) {

        System.out.println("Calculator without results queue ...");

        int numThreads = ArgumentParser.parseNumThreads(args);

        long start = System.nanoTime();
        Calculator calculator = new Calculator(numThreads);
        DivisionResults results = calculator.calculate();

        long duration = System.nanoTime() - start;

        System.out.println(results.toString());
        System.out.println("Calculating with " + numThreads + "  took " + DurationUtility.getDurationString(duration) + ".");
    }
}
