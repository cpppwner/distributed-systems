package at.aau.ds;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    private static final String HOST_SHORT_OPT = "-h";
    private static final String HOST_LONG_OPT = "--host";
    private static final String PORT_SHORT_OPT = "-p";
    private static final String PORT_LONG_OPT = "--port";

    private static final String DEFAULT_SERVER_HOST = "localhost";
    private static final int DEFAULT_SERVER_PORT = 1234;

    private static final String PROMPT = "ex1.4> ";

    private static final String COMMAND_QUIT = "quit";
    private static final String COMMAND_HELP = "help";
    private static final String COMMAND_PRIME = "prime";
    private static final String COMMAND_PERIMETER = "perimeter";
    private static final String COMMAND_SQRT = "sqrt";

    private final String serverHost;
    private final int serverPort;

    private Client(String serverHost, int serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    private void run() {

        System.out.println("Distributed Systems Ex1.4.");
        System.out.println("Use help to get a list of available commands.");
        System.out.println();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        boolean quit = false;
        while (!quit) {
            System.out.print(PROMPT);
            String command;
            try {
                command = br.readLine();
            } catch (IOException e) {
                command = null;
            }

            if (command == null) {
                // EOF condition
                quit = true;
                continue;
            }
            if (command.equals(COMMAND_QUIT)) {
                quit = true;
            } else if (command.equals(COMMAND_HELP)) {
                printHelp();
            } else if (command.startsWith(COMMAND_PRIME)) {
                prime(command);
            } else if (command.startsWith(COMMAND_PERIMETER)) {
                perimeter(command);
            } else if (command.startsWith(COMMAND_SQRT)) {
                squareRoot(command);
            } else {
                System.out.println("Unknown command \"" + command + "\"");
                printHelp();
            }
        }

    }

    private void printHelp() {
        System.out.println("\t" + COMMAND_HELP + " \t\tprint this help");
        System.out.println("\t" + COMMAND_QUIT + " \t\tquit application");
        System.out.println("\t" + COMMAND_PRIME + " <int> \t\tcompute prime numbers");
        System.out.println("\t" + COMMAND_PERIMETER + " <double> \t\tcompute perimeter for given radius");
        System.out.println("\t" + COMMAND_SQRT + " <int> \t\tcompute square root");
    }

    private void prime(String command) {
        String argument = command.substring(COMMAND_PRIME.length() + 1).trim();
        try {
            String response = sendToServer(Operation.prime(Integer.parseInt(argument)));
            if (response != null) {
                System.out.println(response);
            }
        } catch (NumberFormatException e) {
            System.out.println("Given argument is not a valid integer.");
        }
    }

    private void perimeter(String command) {
        String argument = command.substring(COMMAND_PERIMETER.length() + 1).trim();
        try {
            String response = sendToServer(Operation.perimeter(Double.parseDouble(argument)));
            if (response != null) {
                System.out.println(response);
            }
        } catch (NumberFormatException e) {
            System.out.println("Given argument is not a valid double.");
        }
    }

    private void squareRoot(String command) {
        String argument = command.substring(COMMAND_SQRT.length() + 1).trim();
        try {
            String response = sendToServer(Operation.squareRoot(Integer.parseInt(argument)));
            if (response != null) {
                System.out.println(response);
            }
        } catch (NumberFormatException e) {
            System.out.println("Given argument is not a valid integer.");
        }
    }

    private String sendToServer(Operation operation) {
        try (Socket socket = new Socket(serverHost, serverPort)) {

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // send a single line to server
            InetAddress address = socket.getLocalAddress();
            String ip = address.getHostAddress();
            int port = socket.getLocalPort();

            String message = ip + ":" + port + "," + operation.getOperation() +  "," + operation.getOperand();
            out.println(message);

            // server sends back data
            return in.readLine();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {

        // parse the arguments
        String host = DEFAULT_SERVER_HOST;
        int port = DEFAULT_SERVER_PORT;

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            switch (arg) {
                case HOST_SHORT_OPT:
                case HOST_LONG_OPT:
                    if (i < args.length - 1) {
                        host = args[++i];
                    } else {
                        System.out.println("Missing <host>.");
                        printUsage();
                        System.exit(1);
                    }
                    break;
                case PORT_SHORT_OPT:
                case PORT_LONG_OPT:
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
                    break;
                default:
                    System.out.println("Unknown argument \"" + arg + "\"");
                    printUsage();
                    System.exit(1);
            }
        }

        // start and run the client
        new Client(host, port).run();
    }

    private static void printUsage() {
        System.out.println("usage: Client [-h <host>] [-p <port>]");
        System.out.println();
        System.out.println(HOST_SHORT_OPT + " | " + HOST_LONG_OPT + " host to connect to (default: " + DEFAULT_SERVER_HOST + ").");
        System.out.println(PORT_SHORT_OPT + " | " + PORT_LONG_OPT + " port number to connect to (default: " + DEFAULT_SERVER_PORT + ").");
    }
}
