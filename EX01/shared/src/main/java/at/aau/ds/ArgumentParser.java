package at.aau.ds;

public class ArgumentParser {

    private static final String NUM_THREADS_SHORT_OPT = "-n";
    private static final String NUM_THREADS_LONG_OPT = "--num-threads";

    private static final int MIN_NUM_THREADS = 1;

    public static int parseNumThreads(String[] args) {

        if (args.length == 0) {
            return MIN_NUM_THREADS; // single threaded by default
        }
        if (!isExpectedArguments(args)) {
            // invalid number of arguments
            printHelp();
            System.exit(1);
        }

        int numThreads =  Integer.parseInt(args[1]);
        if (numThreads < MIN_NUM_THREADS) {
            // invalid number of threads
            printHelp();
            System.exit(1);
        }

        return numThreads;
    }

    private static boolean isExpectedArguments(String[] args) {
        if (args.length != 2) {
            return false;
        }

        return isNumberOfThreadsOption(args[0]) && isValidInteger(args[1]);
    }

    private static boolean isNumberOfThreadsOption(String arg) {
        return arg.equals(NUM_THREADS_SHORT_OPT) || arg.equals(NUM_THREADS_LONG_OPT);
    }

    private static boolean isValidInteger(String arg) {
        try {
            Integer.parseInt(arg);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static void printHelp() {
        System.out.println("usage: Calculator [-n <number of threads>]");
        System.out.println();
        System.out.println(NUM_THREADS_SHORT_OPT + " | " + NUM_THREADS_LONG_OPT + " must be >= " +  MIN_NUM_THREADS + ".");
    }
}
