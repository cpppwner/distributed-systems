package at.aau.ds;

import java.rmi.Naming;

public class StringOpsServer {

    public static void main(String[] args) {
        try {
            Naming.rebind("rmi://localhost/StringOpsSafe", new StringOpsImpl());

            System.out.println("Thread safe StringOps Server is ready.");
        } catch (Exception e) {
            System.out.println("Thread safe StringOps Server failed: " + e);
            e.printStackTrace();
        }
    }
}
