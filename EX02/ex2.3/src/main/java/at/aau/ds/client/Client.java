package at.aau.ds.client;

import java.io.IOException;
import java.math.BigInteger;
import java.net.*;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client {

    private static final String MULTICAST_ADDRESS = "228.5.6.7";
    private static final int MULTICAST_PORT = 1234;

    private static final long TIMEOUT_IN_MILLISECONDS = TimeUnit.SECONDS.toMillis(1);

    private static final Pattern RESPONSE_PATTERN = Pattern.compile("^TS-(\\d+)#(\\d+)#$");


    private void run() {


        List<DatagramPacket> receivedPackets = new LinkedList<>();

        try (DatagramSocket socket = new DatagramSocket()) {

            // set socket timeout
            socket.setSoTimeout((int)TIMEOUT_IN_MILLISECONDS);

            // send the request
            sendMulticastRequest(socket.getLocalPort());

            // receive all packets within timeout
            long start = System.currentTimeMillis();
            long now;
            do {
                byte[] data = new byte[4096];
                DatagramPacket receivePacket = new DatagramPacket(data, data.length);
                try {
                    socket.receive(receivePacket);
                    now = System.currentTimeMillis();
                    if (isWithinTimeout(now, start)) {
                        receivedPackets.add(receivePacket);
                    }
                } catch (SocketTimeoutException e) {
                    System.out.println("SocketTimeout occurred.");
                }
            } while (isWithinTimeout(System.currentTimeMillis(), start));
        } catch(IOException e) {
            e.printStackTrace();
        }

        // parse out data
        BigInteger summedTimestamp = BigInteger.ZERO;
        for(DatagramPacket receivedPacket : receivedPackets) {
            String receivedString = new String(receivedPacket.getData(), 0, receivedPacket.getLength());
            System.out.println("Client received \"" + receivedString + "\".");

            Matcher matcher = RESPONSE_PATTERN.matcher(receivedString);
            if (matcher.matches()) {
                long timestamp = Long.parseLong(matcher.group(2));
                summedTimestamp = summedTimestamp.add(BigInteger.valueOf(timestamp));
            }
        }

        BigInteger averageTimestamp = summedTimestamp.divide(BigInteger.valueOf(receivedPackets.size()));

        System.out.println("Average timestamp: " + averageTimestamp);
    }

    private void sendMulticastRequest(int localPort) throws IOException {

        String requestString = "REQ#" + localPort + "#";
        byte[] requestData = requestString.getBytes();

        DatagramPacket packet = new DatagramPacket(requestData, requestData.length,
                InetAddress.getByName(MULTICAST_ADDRESS), MULTICAST_PORT);

        try (MulticastSocket socket = new MulticastSocket()) {

            socket.send(packet);
        }
    }

    private static boolean isWithinTimeout(long now, long start) {

        return now - start < TIMEOUT_IN_MILLISECONDS;
    }

    public static void main(String[] args) {
        new Client().run();
    }
}
