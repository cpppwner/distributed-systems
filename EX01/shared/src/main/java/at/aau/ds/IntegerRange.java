package at.aau.ds;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * Class representing a range of integer numbers with both bounds included.
 */
public final class IntegerRange implements Iterable<Integer> {

    private final int lowerBound;
    private final int upperBound;

    /**
     * Creates a new integer range with given lower and upper bound.
     *
     * @param lowerBound The lower bound of the range (included).
     * @param upperBound The upper bound of the range (included).
     * @throws IllegalArgumentException If {@code lowerBound} is greater than {@code upperBound}.
     */
    public IntegerRange(int lowerBound, int upperBound) {
        if (lowerBound > upperBound) {
            throw new IllegalArgumentException("upperBound > lowerBound");
        }
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public int getLowerBound() {
        return lowerBound;
    }

    public int getUpperBound() {
        return upperBound;
    }

    public int getLength() {
        return getUpperBound() - getLowerBound() + 1; // plus one since upper bound is included
    }

    public List<IntegerRange> divide(int numberOfParts) {
        if (numberOfParts <= 0) {
            throw new IllegalArgumentException("numberOfPars must be greater than 0");
        }

        List<IntegerRange> result = new ArrayList<>(numberOfParts);

        int lowerBound = getLowerBound();
        int remainingLength = getLength();

        while(numberOfParts > 0) {
            int partLength = remainingLength / numberOfParts;
            result.add(new IntegerRange(lowerBound, lowerBound + partLength - 1));
            lowerBound += partLength;
            remainingLength = getUpperBound() - lowerBound + 1;
            numberOfParts -= 1;
        }

        return result;
    }

    @Override
    public Iterator<Integer> iterator() {
        return IntStream.rangeClosed(lowerBound, upperBound).iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IntegerRange integers = (IntegerRange) o;
        return (lowerBound == integers.lowerBound) &&
                (upperBound == integers.upperBound);
    }

    @Override
    public int hashCode() {

        return Objects.hash(lowerBound, upperBound);
    }
}
