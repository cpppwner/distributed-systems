package at.aau.ds;

import com.sun.net.httpserver.HttpServer;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

public class SalaryCalculationServer {

    private static final String BASE_URI = "http://localhost:9998/EX5.3";

    public static void main(String[] args) throws Exception {

        ResourceConfig rc = new ResourceConfig(SalaryCalculationService.class);
        URI endpoint = new URI(BASE_URI);

        HttpServer server = JdkHttpServerFactory.createHttpServer(endpoint,rc);

        System.out.println("Server running");
        System.out.println("Visit: http://localhost:9998/EX5.3");
        System.out.println("Hit return to stop...");
        System.in.read();
        System.out.println("Stopping server");
        server.stop(0);
        System.out.println("Server stopped");
    }
}
