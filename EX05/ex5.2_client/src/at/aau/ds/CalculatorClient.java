package at.aau.ds;

import at.aau.ds.service.CalculationService;
import at.aau.ds.service.CalculationService_Service;

import java.util.*;

public class CalculatorClient {

    private enum Operator {

        ADDITION('+', 2),
        SUBTRACTION('-', 2),
        MULTIPLICATION('*', 1),
        DIVISION('/', 1);

        final char character;
        final int precedence;

        Operator(char charater, int precedence) {
            this.character = charater;
            this.precedence = precedence;
        }

        static boolean isOperator(char character) {
            for (Operator op : Operator.values()) {
                if (op.character == character) {
                    return true;
                }
            }
            return false;
        }

        static Operator get(char character) {
            for (Operator op : Operator.values()) {
                if (op.character == character) {
                    return op;
                }
            }
            throw new IllegalArgumentException("not a valid operator");
        }
    }

    private static final char LEFT_BRACKET = '(';
    private static final char RIGHT_BRACKET = ')';

    private static final String LOGO =
            "|  _________________  |\n" +
            "| | JO           0. | |\n" +
            "| |_________________| |\n" +
            "|  ___ ___ ___   ___  |\n" +
            "| | 7 | 8 | 9 | | + | |\n" +
            "| |___|___|___| |___| |\n" +
            "| | 4 | 5 | 6 | | - | |\n" +
            "| |___|___|___| |___| |\n" +
            "| | 1 | 2 | 3 | | x | |\n" +
            "| |___|___|___| |___| |\n" +
            "| | . | 0 | = | | / | |\n" +
            "| |___|___|___| |___| |\n" +
            "|_____________________|";
    private static final String PROMPT = "eval> ";
    private static final String QUIT = "quit";

    private final CalculationService calculationService;

    private CalculatorClient() {
        calculationService = new CalculationService_Service().getCalculationService();
    }

    private void run() {

        System.out.println(LOGO);
        System.out.println("Remote calculator - just enter your formula.");
        System.out.println("Use " + QUIT + " to exit.");
        System.out.println();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print(PROMPT);
            String line = scanner.nextLine().trim();

            if (line.equals(QUIT)) {
                break;
            }

            try {
                Queue<String> postfixTokens = infixToPostfix(line);
                double result = evaluatePostfix(postfixTokens);
                System.out.println("Result: " + result);
            } catch (Exception e) {
                System.out.println("Failed to evaluate expression. " + e);
            }

        }

    }

    private double evaluatePostfix(Queue<String> postfixTokens) {

        Stack<Double> resultStack = new Stack<>();

        for (String token : postfixTokens) {
            if (isOperator(token)) {
                if (resultStack.size() < 2) {
                    throw new IllegalArgumentException("Number of operands is invalid");
                }
                double rhsOperand = resultStack.pop();
                double lhsOperand = resultStack.pop();

                double result = evaluateOperator(token, lhsOperand, rhsOperand);
                resultStack.push(result);
            } else if (isNumber(token)) {
                resultStack.push(Double.parseDouble(token));
            } else {
                throw new IllegalArgumentException("Unexpected token \"" + token + "\".");
            }
        }

        if (resultStack.size() != 1) {
            throw new IllegalArgumentException("Illegal postfix expression.");
        }

        return resultStack.pop();
    }

    private double evaluateOperator(String token, double lhsOperand, double rhsOperand) {

        Operator operator = Operator.get(token.charAt(0));
        switch (operator) {
            case ADDITION:
                return calculationService.add(lhsOperand, rhsOperand);
            case SUBTRACTION:
                return calculationService.subtract(lhsOperand, rhsOperand);
            case MULTIPLICATION:
                return calculationService.multiply(lhsOperand, rhsOperand);
            case DIVISION:
                return calculationService.divide(lhsOperand, rhsOperand);
            default:
                throw new IllegalArgumentException("Unknown operator \"" + token + "\".");
        }
    }

    private Queue<String> infixToPostfix(String line) {

        // tokenize line
        StringTokenizer tokenizer = new StringTokenizer(line, "+-*/()", true);

        Queue<String> result = new ArrayDeque<>();
        Stack<Character> operators = new Stack<>();

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken().trim();
            if (isNumber(token)) {
                result.add(token);
            } else if (isOperator(token)) {
                boolean checkOperators = true;
                while (!operators.empty() && checkOperators) {
                    if (operators.peek() != LEFT_BRACKET) {
                        Operator topOperator = Operator.get(operators.peek());
                        Operator tokenOperator = Operator.get(token.charAt(0));
                        if (topOperator.precedence <= tokenOperator.precedence) {
                            result.add(String.valueOf(operators.pop()));
                        } else {
                            checkOperators = false;
                        }
                    } else {
                        checkOperators = false;
                    }
                }
                operators.push(token.charAt(0));
            } else  if (token.equals(String.valueOf(LEFT_BRACKET))) {
                operators.push(LEFT_BRACKET);
            } else if (token.equals(String.valueOf(RIGHT_BRACKET))) {
                boolean leftBracketFound = false;
                while (!operators.empty()) {
                    char topOperator = operators.pop();
                    if (topOperator == LEFT_BRACKET) {
                        leftBracketFound = true;
                    } else {
                        result.add(String.valueOf(topOperator));
                    }
                }
                if (!leftBracketFound) {
                    throw new IllegalArgumentException("Mismatched number of brackets.");
                }
            } else {
                throw new IllegalArgumentException("Unexpected token \"" + token + "\".");
            }
        }

        while (!operators.empty()) {
            result.add(String.valueOf(operators.pop()));
        }

        return result;
    }

    private boolean isOperator(String token) {

        return token.length() == 1
                && Operator.isOperator(token.charAt(0));
    }

    private static boolean isNumber(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void main(String[] args) {
        new CalculatorClient().run();
    }
}
