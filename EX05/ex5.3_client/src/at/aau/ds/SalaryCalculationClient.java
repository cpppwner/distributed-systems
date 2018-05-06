package at.aau.ds;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class SalaryCalculationClient {

    private static final String GROSS_SALARY_REQUEST_URI = "http://localhost:8080/EX5.3/SalaryCalculationService/grossSalary";
    private static final String NET_SALARY_REQUEST_URI = "http://localhost:8080/EX5.3/SalaryCalculationService/netSalary";

    private static final String TAXING_PERCENTAGE_QUERY_PARAM = "taxingPercentage";
    private static final String GROSS_SALARY_QUERY_PARAM = "grossSalary";
    private static final String NET_SALARY_QUERY_PARAM = "netSalary";

    private static final double TAXING_PERCENTAGE = 0.42;
    private static final double GROSS_SALARY = 4321;
    private static final double NET_SALARY = 2345.6;

    public static void main(String[] args) {

        Client client = ClientBuilder.newClient();

        System.out.println("Calculating net salary for grossSalary=" + GROSS_SALARY + " and taxingPercentage=" + TAXING_PERCENTAGE + " (HTTP GET)");
        Response result = client
                .target(NET_SALARY_REQUEST_URI)
                .queryParam(GROSS_SALARY_QUERY_PARAM, GROSS_SALARY)
                .queryParam(TAXING_PERCENTAGE_QUERY_PARAM, TAXING_PERCENTAGE)
                .request(MediaType.APPLICATION_JSON)
                .get();
        System.out.println("Got: " + result.readEntity(String.class));

        System.out.println("Calculating net salary for grossSalary=" + GROSS_SALARY + " and taxingPercentage=" + TAXING_PERCENTAGE + " (HTTP POST)");
        Form form = new Form();
        form.param(GROSS_SALARY_QUERY_PARAM, Double.toString(GROSS_SALARY));
        form.param(TAXING_PERCENTAGE_QUERY_PARAM, Double.toString(TAXING_PERCENTAGE));
        result = client
                .target(NET_SALARY_REQUEST_URI)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED_TYPE));
        System.out.println("Got: " + result.readEntity(String.class));

        System.out.println("Calculating gross salary for netSalary=" + NET_SALARY + " and taxingPercentage=" + TAXING_PERCENTAGE + " (HTTP GET)");
        result = client
                .target(GROSS_SALARY_REQUEST_URI)
                .queryParam(NET_SALARY_QUERY_PARAM, NET_SALARY)
                .queryParam(TAXING_PERCENTAGE_QUERY_PARAM, TAXING_PERCENTAGE)
                .request(MediaType.APPLICATION_JSON)
                .get();
        System.out.println("Got: " + result.readEntity(String.class));

        System.out.println("Calculating gross salary for netSalary=" + NET_SALARY + " and taxingPercentage=" + TAXING_PERCENTAGE + " (HTTP POST)");
        form = new Form();
        form.param(NET_SALARY_QUERY_PARAM, Double.toString(NET_SALARY));
        form.param(TAXING_PERCENTAGE_QUERY_PARAM, Double.toString(TAXING_PERCENTAGE));
        result = client
                .target(GROSS_SALARY_REQUEST_URI)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED_TYPE));
        System.out.println("Got: " + result.readEntity(String.class));
    }
}
