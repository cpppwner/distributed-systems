package at.aau.ds;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/SalaryCalculationService")
public class SalaryCalculationService {

    @GET
    @Path("/netSalary")
    @Produces(MediaType.APPLICATION_JSON)
    public String netSalaryGet(@QueryParam("grossSalary") double grossSalary,
                               @QueryParam("taxingPercentage") double taxingPercentage) {

        return netSalary(grossSalary, taxingPercentage);
    }

    @POST
    @Path("/netSalary")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String netSalaryPost(@FormParam("grossSalary") double grossSalary,
                                @FormParam("taxingPercentage") double taxingPercentage) {

        return netSalary(grossSalary, taxingPercentage);
    }

    private String netSalary(double grossSalary, double taxingPercentage) {
        double netSalary = grossSalary / (1 + taxingPercentage);

        return "{\"result\" : " + netSalary + "}";
    }

    @GET
    @Path("/grossSalary")
    @Produces(MediaType.APPLICATION_JSON)
    public String grossSalaryGet(@QueryParam("netSalary") double netSalary,
                                 @QueryParam("taxingPercentage") double taxingPercentage) {

        return grossSalary(netSalary, taxingPercentage);
    }

    @POST
    @Path("/grossSalary")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String grossSalaryPost(@FormParam("netSalary") double netSalary,
                                  @FormParam("taxingPercentage") double taxingPercentage) {

        return grossSalary(netSalary, taxingPercentage);
    }

    private String grossSalary(double netSalary, double taxingPercentage) {

        double grossSalary =  netSalary * (1 + taxingPercentage);

        return "{\"result\" : " + grossSalary + "}";
    }
}
