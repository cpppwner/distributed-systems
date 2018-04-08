package at.aau.ds;


import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class DivisorCalculatorTests {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void calculatingNumberOfDivisorsForNegativeNumberThrowsException() {
        // then
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("number is less than 1");

        DivisorCalculator.calculateNumberOfDivisors(-1);
    }

    @Test
    public void calculatingNumberOfDivisorsForZeroNumberThrowsException() {
        // then
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("number is less than 1");

        DivisorCalculator.calculateNumberOfDivisors(-1);
    }

    @Test
    public void calculatingNumberOfDivisorsForOneGivesOne() {

        // when
        DivisorCalculator.DivisorResult obtained = DivisorCalculator.calculateNumberOfDivisors(1);

        // then
        assertThat(obtained.getNumber(), is(equalTo(1)));
        assertThat(obtained.getNumberOfDivisors(), is(equalTo(1)));
    }

    @Test
    public void numberOfDivisorsForPrimeNumbersIsTwo() {

        // given
        int[] primeNumbers = new int[] { 2, 3, 5, 7, 11, 13, 17, 19, 23 };

        // when
        List<DivisorCalculator.DivisorResult> obtained = Arrays.stream(primeNumbers)
                .boxed()
                .map(DivisorCalculator::calculateNumberOfDivisors)
                .collect(Collectors.toList());

        // then
        List<DivisorCalculator.DivisorResult> expected = Arrays.stream(primeNumbers)
                .boxed()
                .map(primeNumber -> new DivisorCalculator.DivisorResult(primeNumber, 2))
                .collect(Collectors.toList());

        assertThat(obtained, is(equalTo(expected)));
    }

    @Test
    public void numberOfDivisorsForNonPrimeNumbers() {

        // given
        int[] primeNumbers = new int[] { 4, 6, 8, 9, 10, 12, 14, 15, 16, 18, 20, 21, 22, 24 };

        // when
        List<DivisorCalculator.DivisorResult> obtained = Arrays.stream(primeNumbers)
                .boxed()
                .map(DivisorCalculator::calculateNumberOfDivisors)
                .collect(Collectors.toList());

        // then
        List<DivisorCalculator.DivisorResult> expected = Arrays.asList(
                new DivisorCalculator.DivisorResult(4,3),
                new DivisorCalculator.DivisorResult(6, 4),
                new DivisorCalculator.DivisorResult(8, 4),
                new DivisorCalculator.DivisorResult(9, 3),
                new DivisorCalculator.DivisorResult(10, 4),
                new DivisorCalculator.DivisorResult(12, 6),
                new DivisorCalculator.DivisorResult(14, 4),
                new DivisorCalculator.DivisorResult(15, 4),
                new DivisorCalculator.DivisorResult(16, 5),
                new DivisorCalculator.DivisorResult(18, 6),
                new DivisorCalculator.DivisorResult(20, 6),
                new DivisorCalculator.DivisorResult(21, 4),
                new DivisorCalculator.DivisorResult(22, 4),
                new DivisorCalculator.DivisorResult(24, 8));
        assertThat(obtained, is(equalTo(expected)));
    }
}
