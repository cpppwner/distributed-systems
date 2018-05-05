package at.aau.ds;

import at.aau.ds.service.CalculationService;
import at.aau.ds.service.CalculationService_Service;

public class CalculationServiceClient {

    private static final String RADIUS_SHORT_OPT = "-r";
    private static final String RADIUS_LONG_OPT = "--radius";

    private static final String HEIGHT_SHORT_OPT = "-h";
    private static final String HEIGHT_LONG_OPT = "--height";

    private final String[] args;

    private double radius;
    private double height;

    private CalculationServiceClient(String[] args) {
        this.args = args;
    }

    private void run() {

        if (!parseArguments()) {
            System.out.println("Failed to parse arguments.");
            printUsage();
            System.exit(1);
        }

        double surfaceAreaOfCylinder = calculateSurfaceAreaOfCylinder();
        System.out.println("Surface area of cylinder = " + surfaceAreaOfCylinder + " (radius = " + radius + "; height = " + height + ")");
    }

    private boolean parseArguments() {

        boolean radiusParsed = false;
        boolean heightParsed = false;

        int argumentIndex = 0;
        while (argumentIndex < args.length) {
            String argument = args[argumentIndex];
            switch (argument) {
                case RADIUS_SHORT_OPT:
                case RADIUS_LONG_OPT:
                    if (argumentIndex < args.length - 1) {
                        try {
                            radius = Double.parseDouble(args[++argumentIndex]);
                            radiusParsed = true;
                        } catch (NumberFormatException e)  {
                            System.out.println("Failed to parse radius-double value "  + e);
                            return false;
                        }
                    } else {
                        System.out.println("Missing radius value.");
                        return false;
                    }
                    break;
                case HEIGHT_SHORT_OPT:
                case HEIGHT_LONG_OPT:
                    if (argumentIndex < args.length - 1) {
                        try {
                            height = Double.parseDouble(args[++argumentIndex]);
                            heightParsed = true;
                        } catch (NumberFormatException e)  {
                            System.out.println("Failed to parse height-double value "  + e);
                            return false;
                        }
                    } else {
                        System.out.println("Missing height value.");
                        return false;
                    }
                    break;
                default:
                    System.out.println("Unknown argument \"" + argument + "\".");
                    return false;
            }

            argumentIndex += 1;
        }

        return radiusParsed && heightParsed;
    }

    private void printUsage() {
        System.out.println("usage: CalculationServiceClient -r <radius> -h <height>");
        System.out.println();
        System.out.println("Calculate surface area of cylinder using given radius and height.");
        System.out.println();
        System.out.println("Required arguments:");
        System.out.println("\t" + RADIUS_SHORT_OPT + "|" + RADIUS_LONG_OPT + "\t\t Radius of cylinder as double value");
        System.out.println("\t" + HEIGHT_SHORT_OPT + "|" + HEIGHT_LONG_OPT + "\t\t Height of cylinder as double value");
    }

    private double calculateSurfaceAreaOfCylinder() {

        CalculationService calculationService = new CalculationService_Service().getCalculationService();

        double radiusPlusHeight = calculationService.add(radius, height);
        double twoTimesPiTimesRadius = calculationService.multiply(2, calculationService.multiply(Math.PI, radius));

        return calculationService.multiply(radiusPlusHeight, twoTimesPiTimesRadius);
    }

    public static void main(String[] args) {

        CalculationServiceClient client = new CalculationServiceClient(args);
        client.run();
    }
}
