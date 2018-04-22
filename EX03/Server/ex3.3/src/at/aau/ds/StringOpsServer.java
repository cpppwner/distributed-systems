package at.aau.ds;

import java.rmi.Naming;

public class StringOpsServer {

    public static void main(String[] args) {
        try {
            Naming.rebind("rmi://localhost/StringOps", new StringOpsImpl());

            System.out.println("StringOps Server is ready.");
        } catch (Exception e) {
            System.out.println("StringOps Server failed: " + e);
            e.printStackTrace();
        }
    }
}
