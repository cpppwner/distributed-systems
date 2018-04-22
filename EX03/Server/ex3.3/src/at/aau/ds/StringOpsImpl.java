package at.aau.ds;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class StringOpsImpl extends UnicastRemoteObject implements StringOps {

    private static int COUNTER = 1;

    public StringOpsImpl() throws RemoteException {
        super();
    }

    @Override
    public String uniqueReverse(String input) throws RemoteException {
        String[] words = input.split("\\W");
        String[] reversedWords = new String[words.length];

        for (int i = words.length - 1, j = 0; i >= 0; i--, j++) {
            reversedWords[j] = words[i];
        }

        int counter = COUNTER++;
        return counter + ":" + String.join(" ", reversedWords);
    }
}
