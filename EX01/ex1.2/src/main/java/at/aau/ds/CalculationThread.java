package at.aau.ds;

import java.util.concurrent.ConcurrentLinkedQueue;

public class CalculationThread extends Thread {

    private final ConcurrentLinkedQueue<Integer> numbers;
    private final DivisionResults results = new DivisionResults();

    public CalculationThread(ConcurrentLinkedQueue<Integer> numbers) {
        this.numbers = numbers;
    }

    public DivisionResults getResults() {
        return results;
    }

    @Override
    public void run() {

        Integer number = numbers.poll();
        while (number != null) {

            DivisorCalculator.DivisorResult result = DivisorCalculator.calculateNumberOfDivisors(number);
            results.add(result);

            number = numbers.poll();
        }
    }
}
