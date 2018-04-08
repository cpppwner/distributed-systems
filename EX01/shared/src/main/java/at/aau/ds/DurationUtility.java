package at.aau.ds;

import java.util.concurrent.TimeUnit;

public class DurationUtility {

    public static String getDurationString(double durationInNanoseconds) {

        if (durationInNanoseconds > TimeUnit.SECONDS.toNanos(10)) {
            return Double.toString(durationInNanoseconds / (1000.0 * 1000.0 * 1000.0)) + " s";
        } else if (durationInNanoseconds > TimeUnit.MILLISECONDS.toNanos(10)) {
            return Double.toString(durationInNanoseconds / (1000.0 * 1000.0)) + " ms";
        } else if (durationInNanoseconds > TimeUnit.MICROSECONDS.toNanos(10)) {
            return Double.toString(durationInNanoseconds / 1000.0) + " µs";
        } else {
            return Double.toString(durationInNanoseconds) + " ns";
        }
    }
}
