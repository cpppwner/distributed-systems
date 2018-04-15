package at.aau.ds.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Server {

    private final Path directory;
    private final int listeningPort;

    private Server(Path directory, int listeningPort) {
        this.directory = directory;
        this.listeningPort = listeningPort;
    }

    private void run() {

        // open up the server socket
        try (ServerSocket serverSocket = new ServerSocket(listeningPort)) {

            // accept new client connections and handle the requests
            while (true) {
                // wait for a new client connection
                Socket socket = serverSocket.accept();
                // handle client request
                new Thread(new ClientSessionRunnable(socket, directory)).start();
            }

        } catch (IOException e) {
            System.err.println("Caught IOException");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        CommandLineParser.CommandLineArguments arguments = CommandLineParser.parse(args);

        new Server(arguments.getDirectory(), arguments.getListeningPort()).run();
    }

    private static class ClientSessionRunnable implements Runnable {

        private static final String COMMAND_LIST = "list";
        private static final String COMMAND_GET = "get";

        private final Socket clientSocket;
        private final Path directory;

        ClientSessionRunnable(Socket clientSocket, Path directory) {
            this.clientSocket = clientSocket;
            this.directory = directory;
        }

        @Override
        public void run() {

            // first receive the command from the client
            String line = readLine();
            if (line == null) {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    // intentionally left empty
                }
                return;
            }

            // tokenize the command
            String[] commandAndArguments = line.split("\\s+");
            String command = commandAndArguments[0];
            String[] arguments = Arrays.copyOfRange(commandAndArguments, 1, commandAndArguments.length);

            // execute command
            // Improvement suggestion: use command pattern, but
            // should also be fine without :)
            executeCommand(command, arguments);

            try {
                clientSocket.close();
            } catch (IOException e) {
                // intentionally left empty
            }
        }

        private String readLine() {
            String line = null;
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                line = in.readLine();
            } catch (IOException e) {
                System.err.println("Failed to read from client socket");
                e.printStackTrace();
            }

            return line;
        }

        private void executeCommand(String command, String[] arguments) {

            switch (command) {
                case COMMAND_LIST:
                    executeListCommand(arguments);
                    break;
                case COMMAND_GET:
                    executeGetCommand(arguments);
                    break;
                default:
                    System.err.println("Unknown command \"" + command + "\".");
                    break;
            }
        }

        private void executeListCommand(String[] arguments) {

            if (arguments.length > 0) {
                // no arg expected
                System.err.println("Unexpected arguments for command \"" + COMMAND_LIST + "\".");
                return;
            }

            List<String> directoryContents;
            try {
                directoryContents = listDirectoryContents();
            } catch (IOException e) {
                System.err.println("Caught IOException while listing directory contents");
                e.printStackTrace();
                return;
            }

            // send the filename - each filename in a separate line
            // plus a terminating newline
            try {
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                directoryContents.forEach(out::println);

                out.println();
            } catch (IOException e) {
                System.err.println("Caught IOException while sending directory contents");
                e.printStackTrace();
            }
        }

        private void executeGetCommand(String[] arguments) {

            if (arguments.length != 1) {
                // no arg expected
                System.err.println("Expected exactly 1 argument for command \""
                        + COMMAND_GET + "\", but got " + arguments.length + " instead.");
                return;
            }

            Path fileToRetrieve = Paths.get(directory.toString(), arguments[0]);
            try {
                if (!Files.isRegularFile(fileToRetrieve, LinkOption.NOFOLLOW_LINKS)) {
                    // it's not a valid file, the pdf states an error shall be sent back
                    // therefore send a negative length and an error message
                    sendFileNotFound();
                } else {
                    // file found, send the contents
                    sendFileContents(fileToRetrieve);
                }
            } catch (IOException e) {
                System.err.println("Caught IOException");
                e.printStackTrace();
            }
        }

        private void sendFileNotFound() throws IOException {

            OutputStream outStream = clientSocket.getOutputStream();

            byte[] data = new byte[Long.BYTES];
            ByteBuffer buffer = ByteBuffer.wrap(data);
            buffer.putLong(-1L);
            outStream.write(data);

            PrintWriter out = new PrintWriter(outStream, true);
            out.println("File not found.");
        }

        private void sendFileContents(Path fileToRetrieve) throws IOException {

            OutputStream outStream = clientSocket.getOutputStream();

            // send file size
            long fileSize = Files.size(fileToRetrieve);
            byte[] data = new byte[Long.BYTES];
            ByteBuffer buffer = ByteBuffer.wrap(data);
            buffer.putLong(fileSize);
            outStream.write(data);

            // send file contents
            data = new byte[4096];
            try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(fileToRetrieve.toFile()))) {
                int bytesRead;
                while ((bytesRead = inputStream.read(data)) >= 0) {
                    outStream.write(data, 0, bytesRead);
                }
            }
        }

        private List<String> listDirectoryContents() throws IOException {
            List<String> fileNames = new ArrayList<>();
            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directory)) {
                for (Path path : directoryStream) {
                    if (Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS)) {

                        fileNames.add(path.getFileName().toString());
                    }
                }
            }

            return fileNames;
        }
    }
}
