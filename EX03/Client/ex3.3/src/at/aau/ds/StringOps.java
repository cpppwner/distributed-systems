package at.aau.ds;

import java.rmi.Remote;

public interface StringOps extends Remote {
    String uniqueReverse(String input);
}
