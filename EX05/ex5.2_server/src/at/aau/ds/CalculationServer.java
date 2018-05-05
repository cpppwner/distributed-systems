package at.aau.ds;

import javax.xml.ws.Endpoint;

public class CalculationServer {

    private static final String ADDRESS = "http://localhost:9000/EX5.2/CalculationService";

    public static void main(String[] args) {
        Endpoint.publish(ADDRESS, new CalculationService());
    }
}
