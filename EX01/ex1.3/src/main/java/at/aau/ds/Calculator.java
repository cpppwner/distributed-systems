package at.aau.ds;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Calculator {

    private static final IntegerRange RANGE = new IntegerRange(1, 100000);

    private final int numThreads;

    public Calculator(int numThreads) {
        this.numThreads = numThreads;
    }

    public DivisionResults calculate() {

        // fill up the queue containing the input numbers
        ConcurrentLinkedQueue<Integer> inputQueue = new ConcurrentLinkedQueue<>();
        for (int number : RANGE) {
            inputQueue.add(number);
        }

        // create queue storing the results
        LinkedBlockingQueue<DivisorCalculator.DivisorResult> resultsQueue = new LinkedBlockingQueue<>();

        // create the thread pool
        List<CalculationThread> threads = new ArrayList<>(numThreads);
        for (int i = 0; i < numThreads; i++) {
            threads.add(new CalculationThread(inputQueue, resultsQueue));
        }

        // start threads
        threads.forEach(Thread::start);

        // collect results
        DivisionResults results = new DivisionResults();
        for (int i = 0; i < RANGE.getLength(); i++) {
            try {
                results.add(resultsQueue.take());
            } catch (InterruptedException e) {
                // intentionally left empty
            }
        }

        return results;
    }

    public static void main(String[] args) {

        System.out.println("Calculator with results queue ...");

        int numThreads = ArgumentParser.parseNumThreads(args);
        long start = System.nanoTime();
        Calculator calculator = new Calculator(numThreads);
        DivisionResults results = calculator.calculate();
        long duration = System.nanoTime() - start;


        System.out.println(results.toString());
        System.out.println("Calculating with " + numThreads + "  took " + DurationUtility.getDurationString(duration) + ".");
    }
}
