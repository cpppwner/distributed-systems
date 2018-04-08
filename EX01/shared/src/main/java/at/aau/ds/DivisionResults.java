package at.aau.ds;

import java.util.LinkedList;
import java.util.stream.Collectors;

public class DivisionResults {

    private final LinkedList<DivisorCalculator.DivisorResult> results = new LinkedList<>();


    public void add(DivisorCalculator.DivisorResult result) {

        if (results.isEmpty()) {
            results.add(result);
        } else {
            int cmp = results.getFirst().compareTo(result);
            if (cmp < 0) {
                results.clear();
                results.add(result);
            } else if (cmp == 0) {
                // same number of divisors
                results.add(result);
            }
        }
    }

    @Override
    public String toString() {
        if (results.size() > 1) {
            return String.format("Result: %s have %d number of divisors",
                    results.stream().map(r -> Integer.toString(r.getNumber())).collect(Collectors.joining(", ")),
                    results.get(0).getNumberOfDivisors());
        } else {
            return String.format("Result: %s has %d number of divisors",
                    results.get(0).getNumber(),
                    results.get(0).getNumberOfDivisors());
        }
    }

    public void merge(DivisionResults results) {

        results.results.forEach(this::add);
    }
}
