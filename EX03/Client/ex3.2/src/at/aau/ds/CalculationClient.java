package at.aau.ds;

import at.aau.ds.operations.Operation;

import java.rmi.Naming;
import java.util.*;

public class CalculationClient {

    private final Map<String, String> operations = new HashMap<>();

    private static final String PROMPT = "calc> ";
    private static final String QUIT_SHORT = "q";
    private static final String QUIT_LONG = "quit";

    private void run() {

        retrieveAvailableOperations();
        printAvailableOperations();
        printUsage();

        Scanner in = new Scanner(System.in);
        System.out.print(PROMPT);

        String line;

        while ((line = in.nextLine()) != null) {
            line = line.trim();
            if (isQuitCommand(line)) {
                // user wants to quit
                break;
            }

            // tokenize into operation (string) & operands (ints)
            String[] tokens = line.split("\\s+");

            if (!operations.containsKey(tokens[0])) {
                System.out.println("Oops ... operation is not supported.");
                printAvailableOperations();
                continue;
            }

            int[] operands;
            try {
                operands = Arrays.stream(Arrays.copyOfRange(tokens, 1, tokens.length))
                        .mapToInt(Integer::parseInt)
                        .toArray();
            } catch (NumberFormatException e) {
                System.out.println("Oops ... one of the operands is not a valid integer...");
                continue;
            }

            // execute the operation
            try {
                Operation operation = (Operation)Naming.lookup(operations.get(tokens[0]));
                int result = operation.execute(operands);
                System.out.println("Result = " + result);
            } catch (Exception e) {
                System.out.println("Oops something went wrong ..." + e);
            }

            System.out.print(PROMPT);
        }
    }

    private static boolean isQuitCommand(String line) {
        line = line.toLowerCase();
        return line.equals(QUIT_SHORT) || line.equals(QUIT_LONG);
    }

    private void retrieveAvailableOperations() {

        // list names and perform a lookup on each
        try {
            String[] names = Naming.list("rmi://localhost/");
            for (String name : names) {
                if (Naming.lookup(name) instanceof Operation) {
                    String operation = name.substring(name.lastIndexOf('/') + 1);
                    operations.put(operation, name);
                }
            }
        } catch (Exception e) {
            System.out.println("Oops ... something is wrong with RMI ..." + e);
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void printAvailableOperations() {
        System.out.println("The following operations are available.");
        List<String> operationNames = new ArrayList<>(operations.keySet());
        Collections.sort(operationNames);
        for(String operation : operationNames) {
            System.out.println("\t" + operation);
        }
    }


    private void printUsage() {
        System.out.println("Usage:");
        System.out.println("\t<operation> <operand1> <operand2> {<operands>]");
        System.out.println("\t" + QUIT_SHORT + "|" + QUIT_LONG + " to quit application.");
    }

    public static void main(String[] args) {
        new CalculationClient().run();
    }
}
