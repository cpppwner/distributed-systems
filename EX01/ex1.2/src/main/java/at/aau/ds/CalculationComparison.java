package at.aau.ds;

public class CalculationComparison {

    private static final int NUM_REPETITIONS = 10;
    private static final int MAX_NUM_THREADS = 20;

    public static void main(String[] args) {

        for (int numThreads = 1; numThreads <= MAX_NUM_THREADS; numThreads++) {
            measure(numThreads);
        }
    }

    private static void measure(int numThreads) {

        long totalDuration = 0;

        for (int repetition = 0; repetition < NUM_REPETITIONS; repetition++) {
            long start = System.nanoTime();

            Calculator calculator = new Calculator(numThreads);
            calculator.calculate();

            long end = System.nanoTime();

            long duration = end - start;
            totalDuration += duration;
        }

        double averageDurationNs = ((double)totalDuration / (double)NUM_REPETITIONS);

        System.out.println("Calculation with " + numThreads + " thread(s) took " + DurationUtility.getDurationString(averageDurationNs) + " in average (" + NUM_REPETITIONS + " repetitions)");
    }
}
