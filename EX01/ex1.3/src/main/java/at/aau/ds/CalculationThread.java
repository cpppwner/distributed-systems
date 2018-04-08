package at.aau.ds;

import at.aau.ds.DivisionResults;
import at.aau.ds.DivisorCalculator;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class CalculationThread extends Thread {

    private final ConcurrentLinkedQueue<Integer> numbers;
    private final LinkedBlockingQueue<DivisorCalculator.DivisorResult> results;

    public CalculationThread(ConcurrentLinkedQueue<Integer> numbers, LinkedBlockingQueue<DivisorCalculator.DivisorResult> results) {
        this.numbers = numbers;
        this.results = results;
    }

    @Override
    public void run() {

        Integer number = numbers.poll();
        while (number != null) {

            DivisorCalculator.DivisorResult result = DivisorCalculator.calculateNumberOfDivisors(number);
            try {
                results.put(result);
            } catch (InterruptedException e) {
                // intentionally left empty
            }

            number = numbers.poll();
        }
    }
}
