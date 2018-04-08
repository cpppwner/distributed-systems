package at.aau.ds;

import at.aau.ds.calculation.CalculationStrategy;
import at.aau.ds.calculation.MultiThreadedCalculationStrategy;
import at.aau.ds.calculation.SingleThreadedCalculationStrategy;

public class Calculator {

    private static final IntegerRange RANGE = new IntegerRange(1, 100000);

    public static void main(String[] args) {

        int numThreads = ArgumentParser.parseNumThreads(args);
        long start = System.nanoTime();
        CalculationStrategy strategy = numThreads > 1
                ? new MultiThreadedCalculationStrategy(numThreads, RANGE)
                : new SingleThreadedCalculationStrategy(RANGE);

        DivisionResults results = strategy.calculate();
        long duration = System.nanoTime() - start;

        System.out.println(results.toString());
        System.out.println("Calculating with " + numThreads + "  took " + DurationUtility.getDurationString(duration) + ".");
    }
}
