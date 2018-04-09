package at.aau.ds;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.Charset;

public class Server {

    private static final int DEFAULT_DATAGRAM_PORT = 1234;
    private static final Charset QUOTE_CHARSET = Charset.forName("UTF-8");

    private final int port;

    private Server(int port) {
        this.port = port;
    }

    private void run() {

        // open the datagram socket
        try (DatagramSocket datagramSocket = new DatagramSocket(port)) {

            while (true) {
                // receive empty datagram packet from the client
                DatagramPacket inPacket = new DatagramPacket(new byte[0], 0);
                datagramSocket.receive(inPacket);

                // just some debug output
                System.out.println("Received packet from " + inPacket.getAddress() + ":" + inPacket.getPort());

                // reply with a random quote
                String quote = Quotes.getRandomQuote();

                // build outgoing datagram package
                byte[] data = quote.getBytes(QUOTE_CHARSET);
                DatagramPacket outPacket = new DatagramPacket(data, data.length, inPacket.getAddress(), inPacket.getPort());
                datagramSocket.send(outPacket);
            }

        } catch (IOException e) {
            System.out.println("Failed to bind datagram socket.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        int port = DEFAULT_DATAGRAM_PORT;

        Server server = new Server(port);
        server.run();
    }
}
