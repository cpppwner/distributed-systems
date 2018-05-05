package at.aau.ds;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public class CalculationService {

    @WebMethod
    public double add(double augend, double addend) {
        double sum = augend + addend;
        System.out.println("add called - augend=" + augend + "; addend=" + addend + "; sum=" + sum);
        return sum;
    }

    @WebMethod
    public double subtract(double minuend, double subtrahend) {
        double difference = minuend - subtrahend;
        System.out.println("subtract called - minuend=" + minuend + "; subtrahend=" + subtrahend + "; difference=" + difference);
        return difference;
    }

    @WebMethod
    public double multiply(double multiplicand, double multiplier) {
        double product = multiplicand * multiplier;
        System.out.println("multiply called - multiplicand=" + multiplicand + "; multiplier=" + multiplier + "; product=" + product);
        return product;
    }

    @WebMethod
    public double divide(double dividend, double divisor) {
        double quotient = dividend / divisor;
        System.out.println("divide called - dividend=" + dividend + "; divisor=" + divisor + "; quotient=" + quotient);
        return quotient;
    }
}
