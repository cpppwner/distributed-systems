package at.aau.ds;

import at.aau.ds.calculation.CalculationStrategy;
import at.aau.ds.calculation.MultiThreadedCalculationStrategy;
import at.aau.ds.calculation.SingleThreadedCalculationStrategy;

public class StrategyComparison {

    private static final IntegerRange RANGE = new IntegerRange(1, 100000);

    private static final int NUM_REPETITIONS = 10;
    private static final int MAX_NUM_THREADS = 20;

    public static void main(String[] args) {

        measure(new SingleThreadedCalculationStrategy(RANGE));
        for (int numThreads = 2; numThreads <= MAX_NUM_THREADS; numThreads++) {
            measure(new MultiThreadedCalculationStrategy(numThreads, RANGE));
        }
    }

    private static void measure(CalculationStrategy strategy) {

        long totalDuration = 0;

        for (int repetition = 0; repetition < NUM_REPETITIONS; repetition++) {
            long start = System.nanoTime();
            strategy.calculate();
            long end = System.nanoTime();

            long duration = end - start;
            totalDuration += duration;
        }

        double averageDurationNs = ((double)totalDuration / (double)NUM_REPETITIONS);

        System.out.println(strategy.toString() + ": took " + DurationUtility.getDurationString(averageDurationNs) + " in average (" + NUM_REPETITIONS + " repetitions)");
    }
}
