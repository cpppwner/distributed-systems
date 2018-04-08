package at.aau.ds;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class IntegerRangeTests {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void lowerBoundGreaterThanUpperBoundThrowsException1() {

        // then
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("upperBound > lowerBound");

        new IntegerRange(100, 99);
    }

    @Test
    public void upperBoundIsSameAsLowerBound() {

        // given
        IntegerRange target = new IntegerRange(100, 100);

        // then
        assertThat(target.getLowerBound(), is(equalTo(100)));
        assertThat(target.getUpperBound(), is(equalTo(100)));
    }

    @Test
    public void upperBoundIsGreaterThanLowerBound() {

        // given
        IntegerRange target = new IntegerRange(50, 100);

        // then
        assertThat(target.getLowerBound(), is(equalTo(50)));
        assertThat(target.getUpperBound(), is(equalTo(100)));
    }

    @Test
    public void iteratingOverGivesAllNumbersBetweenBoundsWithBoundsIncluded() {

        // given
        IntegerRange target = new IntegerRange(-2, 2);

        // when
        Iterator<Integer> obtained = target.iterator();

        // then
        assertThat(obtained.hasNext(), is(true));
        assertThat(obtained.next(), is(equalTo(-2)));
        assertThat(obtained.hasNext(), is(true));
        assertThat(obtained.next(), is(equalTo(-1)));
        assertThat(obtained.hasNext(), is(true));
        assertThat(obtained.next(), is(equalTo(0)));
        assertThat(obtained.hasNext(), is(true));
        assertThat(obtained.next(), is(equalTo(1)));
        assertThat(obtained.hasNext(), is(true));
        assertThat(obtained.next(), is(equalTo(2)));
        assertThat(obtained.hasNext(), is(false));
    }

    @Test
    public void dividingIntegerRangeIntoEqualParts() {

        // given
        IntegerRange target = new IntegerRange(1, 10);

        // when
        List<IntegerRange> obtained = target.divide(2);

        // then
        assertThat(obtained, is(equalTo(Arrays.asList(new IntegerRange(1, 5),
                new IntegerRange(6, 10)))));

        // and when
        obtained = target.divide(5);

        // then
        assertThat(obtained, is(equalTo(Arrays.asList(new IntegerRange(1, 2),
                new IntegerRange(3, 4),
                new IntegerRange(5, 6),
                new IntegerRange(7, 8),
                new IntegerRange(9, 10)))));
    }

    @Test
    public void dividingIntegerRangeIntoNonEqualParts() {

        // given
        IntegerRange target = new IntegerRange(1, 10);

        // when
        List<IntegerRange> obtained = target.divide(3);

        // then
        assertThat(obtained, is(equalTo(Arrays.asList(new IntegerRange(1, 3),
                new IntegerRange(4, 6),
                new IntegerRange(7, 10)))));

        // and when
        obtained = target.divide(4);

        // then
        assertThat(obtained, is(equalTo(Arrays.asList(new IntegerRange(1, 2),
                new IntegerRange(3, 4),
                new IntegerRange(5, 7),
                new IntegerRange(8, 10)))));
    }
}
