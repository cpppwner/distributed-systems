package at.aau.ds.calculation;

import at.aau.ds.DivisionResults;
import at.aau.ds.DivisorCalculator;
import at.aau.ds.IntegerRange;

import java.util.List;

public class MultiThreadedCalculationStrategy implements CalculationStrategy {

    private final List<IntegerRange> ranges;
    private final CalculationThread[] threads;

    public MultiThreadedCalculationStrategy(int numThreads, IntegerRange range) {
        threads = new CalculationThread[numThreads];
        ranges = range.divide(numThreads);
    }

    @Override
    public DivisionResults calculate() {

        initializeThreads();
        startCalculation();
        waitForCalculationCompletion();

        DivisionResults results = new DivisionResults();
        for (CalculationThread thread : threads) {
            results.merge(thread.results);
        }

        return results;
    }

    private void initializeThreads() {
        for (int threadIndex = 0; threadIndex < threads.length; threadIndex++) {
            threads[threadIndex] = new CalculationThread(ranges.get(threadIndex));
        }
    }

    private void startCalculation() {
        for (Thread thread : threads) {
            thread.start();
        }
    }

    private void waitForCalculationCompletion() {
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                // intentionally left empty
            }
        }
    }

    @Override
    public String toString() {
        return "Multi threaded calculation with " + threads.length + " threads";
    }

    private static final class CalculationThread extends Thread {

        private final IntegerRange range;
        private final DivisionResults results = new DivisionResults();

        CalculationThread(IntegerRange range) {
            this.range = range;
        }

        @Override
        public void run() {
            for (int number : range) {
                DivisorCalculator.DivisorResult result = DivisorCalculator.calculateNumberOfDivisors(number);
                results.add(result);
            }
        }
    }
}
