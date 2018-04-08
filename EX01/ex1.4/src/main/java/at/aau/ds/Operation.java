package at.aau.ds;

public class Operation {

    private static final String OPERATION_PRIME = "prime";
    private static final String OPERATION_PERIMETER = "perimeter";
    private static final String OPERATION_SQUARE_ROOT = "sqrt";

    private final String operation;
    private final String operand;

    Operation(String operation, String operand) {
        this.operation = operation;
        this.operand = operand;
    }

    String getOperation() {
        return operation;
    }

    String getOperand() {
        return operand;
    }

    static Operation prime(int number) {
        return new Operation(OPERATION_PRIME, Integer.toString(number));
    }

    static Operation perimeter(double radius) {
        return new Operation(OPERATION_PERIMETER, Double.toString(radius));
    }

    static Operation squareRoot(int number) {
        return new Operation(OPERATION_SQUARE_ROOT, Integer.toString(number));
    }

    static boolean isPrimeOperation(String operation) {
        return operation.equals(OPERATION_PRIME);
    }

    static boolean isPerimeterOperation(String operation) {
        return operation.equals(OPERATION_PERIMETER);
    }

    static boolean isSquareRootOperation(String operation) {
        return operation.equals(OPERATION_SQUARE_ROOT);
    }
}
