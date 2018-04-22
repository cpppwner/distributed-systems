package at.aau.ds;

import at.aau.ds.operations.Addition;
import at.aau.ds.operations.Division;
import at.aau.ds.operations.Multiplication;
import at.aau.ds.operations.Subtraction;

import java.rmi.Naming;

public class MathServer {

    public static void main(String[] args) {
        try {
            Naming.rebind("rmi://localhost/add", new Addition());
            Naming.rebind("rmi://localhost/sub", new Subtraction());
            Naming.rebind("rmi://localhost/mul", new Multiplication());
            Naming.rebind("rmi://localhost/div", new Division());

            System.out.println("Calculation Server is ready.");
        } catch (Exception e) {
            System.out.println("Calculation Server failed: " + e);
            e.printStackTrace();
        }
    }
}
