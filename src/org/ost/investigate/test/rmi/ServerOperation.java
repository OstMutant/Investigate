package org.ost.investigate.test.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServerOperation extends UnicastRemoteObject implements RMIInterface {

    public static final int PORT = 1099;

    public static final String NAME = "//localhost/MyServer";

    public static final String OUTCOME = "Server says hello to %s";

    protected ServerOperation() throws RemoteException {
        super();
    }

    @Override
    public String helloTo(String name) throws RemoteException {
        System.out.println(name + " is trying to contact!");
        return String.format(OUTCOME, name);

    }

    public static void apply() {
        try {
            Registry rmiRegistry = LocateRegistry.createRegistry(PORT);
            rmiRegistry.bind(NAME, new ServerOperation());
            System.out.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        apply();
    }

}