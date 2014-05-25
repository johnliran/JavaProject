package controller;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import model.RemoteImplementation;
import RMIInterface.RMIConstants;

public class SolverServer {
	public static void main(String[] args) throws RemoteException, AlreadyBoundException {
		RemoteImplementation impl = new RemoteImplementation();
		Registry registry = LocateRegistry.createRegistry(RMIConstants.PORT);
		registry.bind(RMIConstants.RMI_ID, impl);
		System.out.println("Server is Started");
	}
}
