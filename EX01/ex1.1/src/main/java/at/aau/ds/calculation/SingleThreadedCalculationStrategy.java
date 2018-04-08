package at.aau.ds.calculation;

import at.aau.ds.DivisionResults;
import at.aau.ds.DivisorCalculator;
import at.aau.ds.IntegerRange;

public class SingleThreadedCalculationStrategy implements CalculationStrategy{

    private final IntegerRange range;

    public SingleThreadedCalculationStrategy(IntegerRange range) {
        this.range = range;
    }


    @Override
    public DivisionResults calculate() {

        DivisionResults results = new DivisionResults();

        for (int number : range) {

            DivisorCalculator.DivisorResult result = DivisorCalculator.calculateNumberOfDivisors(number);
            results.add(result);
        }

        return results;
    }

    @Override
    public String toString() {
        return "Single threaded calculation";
    }
}
