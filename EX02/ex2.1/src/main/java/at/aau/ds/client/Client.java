package at.aau.ds.client;

import java.io.IOException;
import java.net.*;
import java.nio.charset.Charset;

public class Client {

    private static final String DEFAULT_SERVER_HOST = "localhost";
    private static final int DEFAULT_SERVER_PORT = 1234;
    private static final Charset QUOTE_CHARSET = Charset.forName("UTF-8");

    private static final int NUM_QUOTES = 1;

    private final String serverHost;
    private final int serverPort;

    private Client(String serverHost, int serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    private void run() {

        for (int i = 0; i < NUM_QUOTES; i++) {
            String quote = getQuoteFromServer();
            if (quote != null) {
                System.out.println(quote);
            }
        }
    }

    private String getQuoteFromServer() {

        try (DatagramSocket datagramSocket = new DatagramSocket()) {

            // send an empty datagram packet to server
            DatagramPacket out = new DatagramPacket(new byte[0], 0, new InetSocketAddress(serverHost, serverPort));
            datagramSocket.send(out);

            // receive the quote from the server
            byte[] data = new byte[4096];
            DatagramPacket in = new DatagramPacket(data, data.length);
            datagramSocket.receive(in);

            // decode data
            return new String(data, QUOTE_CHARSET);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {

        String host = DEFAULT_SERVER_HOST;
        int port = DEFAULT_SERVER_PORT;

        new Client(host, port).run();
    }
}
