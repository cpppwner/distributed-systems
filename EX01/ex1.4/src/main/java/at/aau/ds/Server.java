package at.aau.ds;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Server {

    private static final int NUM_THREADS = 10;

    private static final String PORT_SHORT_OPT = "-p";
    private static final String PORT_LONG_OPT = "--port";

    private static final int DEFAULT_PORT = 1234;

    private final int port;

    private Server(int port) {
        this.port = port;
    }

    private void run() {

        ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                executorService.submit(new ClientSession(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // intentionally left empty
        }
    }

    public static void main(String[] args) {

        // parse the arguments
        int port = DEFAULT_PORT;

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.equals(PORT_SHORT_OPT) || arg.equals(PORT_LONG_OPT)) {
                if (i < args.length - 1) {
                    try {
                        port = Integer.parseInt(args[++i]);
                    } catch (NumberFormatException e) {
                        System.out.println("<port> is not a valid integer.");
                        printUsage();
                        System.exit(1);
                    }
                    if (port < 0 || port > 65535) {
                        System.out.println("<port> is outside range [0, 65535].");
                        printUsage();
                        System.exit(1);
                    }
                } else {
                    System.out.println("Missing <port>.");
                    printUsage();
                    System.exit(1);
                }
            } else {
                System.out.println("Unknown argument \"" + arg + "\"");
                printUsage();
                System.exit(1);
            }
        }

        // start and run the client
        new Server(port).run();
    }

    private static void printUsage() {
        System.out.println("usage: Server [-p <port>]");
        System.out.println();
        System.out.println(PORT_SHORT_OPT + " | " + PORT_LONG_OPT + " port number to connect to (default: " + DEFAULT_PORT + ").");
    }

    private static final class ClientSession implements Runnable {

        private final Socket clientSocket;

        private ClientSession(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {

            try {
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                // read data from the client
                String inputLine = in.readLine();
                if (inputLine != null) {
                    System.out.println("received from client: " + inputLine);
                } else {
                    return;
                }

                // parse the data
                String[] tokens = inputLine.split(",");
                if (tokens.length != 3) {
                    out.println("Unexpected client message");
                    System.out.println("Unexpected client message");
                    return;
                }

                String operation = tokens[1];
                String operand = tokens[2];

                if (Operation.isPrimeOperation(operation)) {
                    out.println(prime(operand));
                } else if (Operation.isPerimeterOperation(operation)) {
                    out.println(perimeter(operand));
                } else if (Operation.isSquareRootOperation(operation)) {
                    out.println(squareRoot(operand));
                } else {
                    out.println("Unknown operation \"" + operation + "\".");
                    System.out.println("Unknown operation \"" + operation + "\".");
                }


            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    // intentionally left empty
                }
            }
        }

        private String prime(String operand) {
            int number;
            try {
                number = Integer.parseInt(operand);
            } catch (NumberFormatException e) {
                return "\"" + operand + "\" is not a valid integer.";
            }

            List<Integer> primeNumbers = new LinkedList<>();
            for (int i = 2; i <= number; i++) {
                // HACK - this is the brute force approach
                if (DivisorCalculator.calculateNumberOfDivisors(i).getNumberOfDivisors() == 2) {
                    primeNumbers.add(i);
                }
            }

            return "List of prime numbers up to "
                    + number + ": "
                    + primeNumbers.stream().map(Object::toString).collect(Collectors.joining(", "));
        }

        private String perimeter(String operand) {
            double radius;
            try {
                radius = Double.parseDouble(operand);
            } catch (NumberFormatException e) {
                return "\"" + operand + "\" is not a valid double.";
            }

            return "Perimeter of circle with radius "
                    + radius + ": "
                    + (Math.PI * 2 * radius);
        }

        private String squareRoot(String operand) {
            int number;
            try {
                number = Integer.parseInt(operand);
            } catch (NumberFormatException e) {
                return "\"" + operand + "\" is not a valid integer.";
            }

            return "Square root of "
                    + number + ": "
                    + Math.sqrt(number);
        }
    }
}
