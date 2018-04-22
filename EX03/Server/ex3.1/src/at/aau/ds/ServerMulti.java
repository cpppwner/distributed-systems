package at.aau.ds;

import java.rmi.Naming;

public class ServerMulti {

    public static void main(String[] args) {
        try {
            Multi multi = new Multi();
            Naming.rebind("rmi://localhost/ABC", multi);

            System.out.println("Multiplication Server is ready.");
        } catch (Exception e) {
            System.out.println("Multiplication Server failed: " + e);
        }
    }
}
