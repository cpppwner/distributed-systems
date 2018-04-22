package at.aau.ds;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.atomic.AtomicInteger;

public class StringOpsImpl extends UnicastRemoteObject implements StringOps {

    private static final AtomicInteger COUNTER = new AtomicInteger(1);

    public StringOpsImpl() throws RemoteException {
        super();
    }

    @Override
    public String uniqueReverse(String input)  throws RemoteException {
        String[] words = input.split("\\W");
        String[] reversedWords = new String[words.length];

        for (int i = words.length - 1, j = 0; i >= 0; i--, j++) {
            reversedWords[j] = words[i];
        }

        int counter = COUNTER.getAndIncrement();
        return counter + ":" + String.join(" ", reversedWords);
    }
}
