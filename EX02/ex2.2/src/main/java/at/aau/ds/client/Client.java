package at.aau.ds.client;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Scanner;

public class Client {

    private static final String LOGO =

            "_______   __       _______.___________..______       __  .______    __    __  .___________. _______  _______  \n" +
            "|       \\ |  |     /       |           ||   _  \\     |  | |   _  \\  |  |  |  | |           ||   ____||       \\ \n" +
            "|  .--.  ||  |    |   (----`---|  |----`|  |_)  |    |  | |  |_)  | |  |  |  | `---|  |----`|  |__   |  .--.  |\n" +
            "|  |  |  ||  |     \\   \\       |  |     |      /     |  | |   _  <  |  |  |  |     |  |     |   __|  |  |  |  |\n" +
            "|  '--'  ||  | .----)   |      |  |     |  |\\  \\----.|  | |  |_)  | |  `--'  |     |  |     |  |____ |  '--'  |\n" +
            "|_______/ |__| |_______/       |__|     | _| `._____||__| |______/   \\______/      |__|     |_______||_______/ \n" +
            "                                                                                                               \n" +
            "                    _______.____    ____  _______.___________. _______ .___  ___.      _______.\n" +
            "                   /       |\\   \\  /   / /       |           ||   ____||   \\/   |     /       |\n" +
            "                  |   (----` \\   \\/   / |   (----`---|  |----`|  |__   |  \\  /  |    |   (----`\n" +
            "                   \\   \\      \\_    _/   \\   \\       |  |     |   __|  |  |\\/|  |     \\   \\    \n" +
            "               ----)   |       |  | .----)   |      |  |     |  |____ |  |  |  | .----)   |   \n" +
            "               _______/        |__| |_______/       |__|     |_______||__|  |__| |_______/    \n" +
            "                                                                                               \n\n";

    private static final String PROMPT = "client> ";

    private static final String COMMAND_HELP = "help";
    private static final String COMMAND_QUIT = "quit";
    private static final String COMMAND_CONNECT = "connect";
    private static final String COMMAND_DISCONNECT = "disconnect";
    private static final String COMMAND_LIST = "list";
    private static final String COMMAND_GET = "get";

    private static final String USAGE_TEXT = "Use " + COMMAND_HELP + " to print a list of available commands.";

    private boolean quit = false;

    private String host;
    private int port;

    private void run() {

        System.out.print(LOGO);
        System.out.println(USAGE_TEXT);

        Scanner scanner = new Scanner(System.in);
        do {
            // print prompt and read line
            System.out.print(PROMPT);
            String line = scanner.nextLine();

            // tokenize the command
            String[] commandAndArguments = line.split("\\s+");
            String command = commandAndArguments[0];
            String[] arguments = Arrays.copyOfRange(commandAndArguments, 1, commandAndArguments.length);

            executeCommand(command, arguments);

        } while (!quit);
    }

    private void executeCommand(String command, String[] arguments) {

        switch (command) {
            case COMMAND_HELP:
                executeHelpCommand();
                break;
            case COMMAND_QUIT:
                quit = true; // no matter if arguments were given
                break;
            case COMMAND_CONNECT:
                executeConnectCommand(arguments);
                break;
            case COMMAND_DISCONNECT:
                executeDisconnectCommand(arguments);
                break;
            case COMMAND_LIST:
                executeListCommand(arguments);
                break;
            case COMMAND_GET:
                executeGetCommand(arguments);
                break;
            default:
                System.out.println("Unknown command \"" + command + "\". " + USAGE_TEXT);
        }
    }

    private void executeHelpCommand() {
        // help command
        System.out.println("\t" + COMMAND_HELP);
        System.out.println("\t\t\tPrint this help text.");
        // quit command
        System.out.println("\t" + COMMAND_QUIT);
        System.out.println("\t\t\tQuit this application.");
        // connect command
        System.out.println("\t" + COMMAND_CONNECT + " <server> <port>");
        System.out.println("\t\t\tConnect to given <server> and <port>.");
        // disconnect command
        System.out.println("\t" + COMMAND_DISCONNECT);
        System.out.println("\t\t\tDisconnect from server.");
        // list command
        System.out.println("\t" + COMMAND_LIST);
        System.out.println("\t\t\tList files on server.");
        // get command
        System.out.println("\t" + COMMAND_GET + " <file>");
        System.out.println("\t\t\tRetrieve <file> from server and store it current directory.");

        System.out.println();
    }

    private void executeConnectCommand(String[] arguments) {
        if (arguments.length != 2) {
            System.out.println("Unexpected arguments to command \"" + COMMAND_CONNECT + "\".");
            System.out.println("Expected <host> and <port> argument.");
            System.out.println(USAGE_TEXT);
            return;
        }

        String host = arguments[0];
        int port;

        try {
            port = Integer.parseInt(arguments[1]);
        } catch (NumberFormatException e) {
            System.out.println("Port \"" + arguments[1] + "\" is not a valid integer.");
            return;
        }

        // validate port
        if (port < 0 || port > 65535) {
            System.out.println("Port \"" + arguments[1] + "\" is outside range [0, 65535].");
            return;
        }

        // and for now just store connection information
        this.host = host;
        this.port = port;
    }

    private void executeDisconnectCommand(String[] arguments) {
        if (arguments.length != 0) {
            System.out.println("Unexpected arguments to command \"" + COMMAND_DISCONNECT + "\".");
            System.out.println(USAGE_TEXT);
            return;
        }

        this.host = null;
        this.port = -1;
    }

    private void executeListCommand(String[] arguments) {

        if (arguments.length != 0) {
            System.out.println("Unexpected arguments to command \"" + COMMAND_DISCONNECT + "\".");
            System.out.println(USAGE_TEXT);
            return;
        }

        if (isConnectionParametersMissing()) {
            System.out.println(COMMAND_CONNECT + "must be called first.");
            return;
        }

        try (Socket socket = new Socket(host, port)) {
            // send list command
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("list");

            // read incoming lines until an empty line is encountered.
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            for (String line = reader.readLine(); line != null && !line.isEmpty(); line = reader.readLine()) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Exception occurred during get command.");
            e.printStackTrace();
        }
    }

    private void executeGetCommand(String[] arguments) {

        if (arguments.length != 1) {
            System.out.println("Unexpected arguments to command \"" + COMMAND_DISCONNECT + "\".");
            System.out.println(USAGE_TEXT);
            return;
        }

        if (isConnectionParametersMissing()) {
            System.out.println(COMMAND_CONNECT + "must be called first.");
            return;
        }

        try (Socket socket = new Socket(host, port)) {
            // send get command
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("get " + arguments[0]);

            // response is binary
            BufferedInputStream inputStream = new BufferedInputStream(socket.getInputStream());

            // read the first 8 bytes, which denotes the file length
            byte[] data = new byte[Long.BYTES];
            if (inputStream.read(data) < 0) {
                System.out.println("Error while reading file length - end of stream.");
                return;
            }
            ByteBuffer buffer = ByteBuffer.wrap(data);
            long fileLength = buffer.getLong();

            if (fileLength < 0) {
                // error message from server - a line describing the error will follow
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String line = reader.readLine();
                if (line != null) {
                    System.out.println(line);
                } else {
                    System.out.println("Error was sent from server");
                }

                return;
            }

            // read the file itself
            try (BufferedOutputStream fileOutputStream = new BufferedOutputStream(new FileOutputStream(arguments[0]))) {
                data = new byte[4096];
                while (fileLength > 0) {
                    int read = inputStream.read(data);
                    if (read < 0) {
                        // end of stream encountered
                        System.out.println("Error while transferring file - end of stream.");
                        break;
                    }
                    if (read > 0) {
                        fileOutputStream.write(data, 0, read);
                    }
                    fileLength -= read;
                }
            }

        } catch (IOException e) {
            System.out.println("Exception occurred during get command.");
            e.printStackTrace();
        }
    }

    private boolean isConnectionParametersMissing() {
        return host == null || port < 0 || port > 65535;
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }
}
