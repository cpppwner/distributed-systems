package at.aau.ds;

import java.util.Objects;

/**
 * Class for calculating the number of divisors for a given positive integer.
 */
public class DivisorCalculator {

    /**
     * Calculate the number of divisors using a brute-force approach.
     *
     * @param number The number for which to calculate the number of divisors.
     * @return Number of divisors, which is a number greater than or equal to 1.
     * @throws IllegalArgumentException If {@code number} is less than 1.
     */
    public static DivisorResult calculateNumberOfDivisors(int number) {
        if (number < 1) {
            throw new IllegalArgumentException("number is less than 1");
        }

        int numberOfDivisors = 0;
        for (int i = 1; i <= number; i++) {
            if ((number % i) == 0) {
                numberOfDivisors += 1;
            }
        }

        return new DivisorResult(number, numberOfDivisors);
    }

    public static class DivisorResult implements Comparable<DivisorResult> {

        private final int number;
        private final int numberOfDivisors;

        DivisorResult(int number, int numberOfDivisors) {
            this.number = number;
            this.numberOfDivisors = numberOfDivisors;
        }

        public int getNumber() {
            return number;
        }

        public int getNumberOfDivisors() {
            return numberOfDivisors;
        }

        @Override
        public int compareTo(DivisorResult o) {
            return Integer.compare(this.getNumberOfDivisors(), o.getNumberOfDivisors());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            DivisorResult that = (DivisorResult) o;
            return (number == that.number) &&
                    (numberOfDivisors == that.numberOfDivisors);
        }

        @Override
        public int hashCode() {

            return Objects.hash(number, numberOfDivisors);
        }
    }
}
