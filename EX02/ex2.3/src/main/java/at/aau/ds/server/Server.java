package at.aau.ds.server;

import java.io.IOException;
import java.net.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Server {

    private static final String MULTICAST_ADDRESS = "228.5.6.7";
    private static final int MULTICAST_PORT = 1234;
    private static final Pattern REQUEST_PATTERN = Pattern.compile("^REQ#(6553[0-5]|655[0-2]\\d|65[0-4]\\d{2}|6[0-4]\\d{3}|[1-5]\\d{4}|\\d{1,4})#$");

    private final int id;

    private Server(int id) {
        this.id = id;
    }

    private void run() {

        try (MulticastSocket multicastSocket = new MulticastSocket(MULTICAST_PORT)) {
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            multicastSocket.joinGroup(group);

            byte[] buffer = new byte[4096];
            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                multicastSocket.receive(packet);

                String received = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Server [" + id + "] received \"" + received + "\".");

                Matcher matcher = REQUEST_PATTERN.matcher(received);
                if (matcher.matches()) {
                    // get client port
                    int clientPort = Integer.parseInt(matcher.group(1));

                    sendUnicastResponse(packet.getAddress(), clientPort);

                } else {
                    System.out.println("Server received invalid request pattern.");
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendUnicastResponse(InetAddress address, int clientPort) {

        // prepare response packet
        String response = "TS-" + id + "#" + new Date().getTime() + "#";
        byte[] responseData = response.getBytes();
        DatagramPacket responsePacket = new DatagramPacket(responseData,
                responseData.length,
                address,
                clientPort);

        try (DatagramSocket socket = new DatagramSocket()) {
            socket.send(responsePacket);

        } catch (IOException e) {
            System.out.println("Failed to send response to client.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        CommandLineParser.CommandLineArguments arguments = CommandLineParser.parse(args);
        new Server(arguments.getServerId()).run();

    }
}
