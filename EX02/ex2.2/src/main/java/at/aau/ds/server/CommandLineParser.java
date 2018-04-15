package at.aau.ds.server;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Command line parser utility class used by the {@link Server}.
 */
class CommandLineParser {

    private static final String DIRECTORY_SHORT_OPT = "-d";
    private static final String DIRECTORY_LONG_OPT = "--directory";
    private static final String PORT_SHORT_OPT = "-p";
    private static final String PORT_LONG_OPT = "--port";

    private CommandLineParser() {
        // intentionally left empty, since this is a static class
    }

    static CommandLineArguments parse(String[] args) {

        CommandLineArguments result = new CommandLineArguments();
        boolean directoryParsed = false;
        boolean portParsed = false;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case DIRECTORY_SHORT_OPT:
                case DIRECTORY_LONG_OPT:
                    if (i < args.length - 1) {
                        result.setDirectory(parseDirectory(args[++i]));
                        directoryParsed = true;
                    } else {
                        System.err.println("Missing argument for \"" + args[i] + "\" - expected directory.");
                        printUsage();
                        System.exit(1);
                    }
                    break;
                case PORT_SHORT_OPT:
                case PORT_LONG_OPT:
                    if (i < args.length - 1) {
                        result.setListeningPort(parsePort(args[++i]));
                        portParsed = true;
                    } else {
                        System.err.println("Missing argument for \"" + args[i] + "\" - expected port.");
                        printUsage();
                        System.exit(1);
                    }
                    break;
                default:
                    System.err.println("Unknown argument \"" + args[i] + "\"");
                    printUsage();
                    System.exit(1);
                    break;
            }
        }

        if (!portParsed) {
            System.out.println("Mandatory commandline argument port is missing.");
            printUsage();
            System.exit(1);
        }
        if (!directoryParsed) {
            System.out.println("Mandatory commandline argument directory is missing.");
            printUsage();
            System.exit(1);
        }

        return result;
    }

    private static int parsePort(String argument) {
        int port = -1;
        try {
            port = Integer.parseInt(argument);
        } catch (NumberFormatException e) {
            System.err.println("Given port argument \"" + argument + "\" is not a valid integer.");
            printUsage();
            System.exit(1);
        }

        if (port < 0 || port > 65535) {
            System.err.println("Given port argument \"" + argument + "\" is outside range [0, 65535].");
            printUsage();
            System.exit(1);
        }

        return port;
    }

    private static Path parseDirectory(String argument) {

        Path result = Paths.get(argument);
        if (!Files.exists(result) || !Files.isDirectory(result)) {
            System.err.println("Given directory argument \"" + argument + "\" is not an existing directory.");
            printUsage();
            System.exit(1);
        }

        return result;
    }

    private static void printUsage() {

        System.out.println("usage: Server " + DIRECTORY_SHORT_OPT + "|"  + DIRECTORY_LONG_OPT + " <directory> "
                        + PORT_SHORT_OPT + "|"  + PORT_LONG_OPT + " <port>");
        System.out.println();
        System.out.println("\t"+ DIRECTORY_SHORT_OPT + "|"  + DIRECTORY_LONG_OPT + " directory containing files to serve");
        System.out.println("\t"+ PORT_SHORT_OPT + "|"  + PORT_LONG_OPT + " port the server is listening to");
    }

    static class CommandLineArguments {

        private Path directory;
        private int listeningPort;

        Path getDirectory() {
            return directory;
        }

        void setDirectory(Path directory) {
            this.directory = directory;
        }

        int getListeningPort() {
            return listeningPort;
        }

        void setListeningPort(int listeningPort) {
            this.listeningPort = listeningPort;
        }
    }
}
