package at.aau.ds.server;

/**
 * Command line parser utility class used by the {@link Server}.
 */
class CommandLineParser {

    private static final String SERVER_ID_SHORT_OPT = "-s";
    private static final String SERVER_ID_LONG_OPT = "--server-id";

    private CommandLineParser() {
        // intentionally left empty, since this is a static class
    }

    static CommandLineArguments parse(String[] args) {

        CommandLineArguments result = new CommandLineArguments();
        boolean serverIdParsed = false;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case SERVER_ID_SHORT_OPT:
                case SERVER_ID_LONG_OPT:

                    if (i < args.length - 1) {
                        result.setServerId(parseServerId(args[++i]));
                        serverIdParsed = true;
                    } else {
                        System.err.println("Missing argument for \"" + args[i] + "\" - expected directory.");
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
        if (!serverIdParsed) {
            System.out.println("Mandatory commandline argument server id is missing.");
            printUsage();
            System.exit(1);
        }
        return result;
    }

    private static int parseServerId(String argument) {
        int serverId = -1;
        try {
            serverId = Integer.parseInt(argument);
        } catch (NumberFormatException e) {
            System.err.println("Given server id argument \"" + argument + "\" is not a valid integer.");
            printUsage();
            System.exit(1);
        }

        return serverId;
    }

    private static void printUsage() {

        System.out.println("usage: Server " + SERVER_ID_SHORT_OPT + "|"  + SERVER_ID_LONG_OPT + " <server id>");
        System.out.println();
        System.out.println("\t"+ SERVER_ID_SHORT_OPT + "|"  + SERVER_ID_LONG_OPT + " server's unique identifier");
    }

    static class CommandLineArguments {

        private int serverId;

        void setServerId(int serverId) {
            this.serverId = serverId;
        }

        int getServerId() {
            return serverId;
        }
    }
}
