package at.aau.ds;

import java.rmi.Naming;

public class ClientMulti {

    public static void main(String[] args) {
        try {
            MultiplicationInt multi = (MultiplicationInt) Naming.lookup("rmi://localhost/ABC");
            int result = multi.mull(5, 3);
            System.out.println("Result is: " + result);
        } catch (Exception e) {
            System.out.println("ClientMulti exception: " + e);
        }
    }
}
