package controller;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import model.RemoteImpl;
import RMIInterface.constants;

public class SolverServer {

	public static void main(String[] args) throws RemoteException, AlreadyBoundException {
		// TODO Auto-generated method stub
		RemoteImpl impl = new RemoteImpl();
		Registry registry = LocateRegistry.createRegistry(constants.port);
		registry.bind(constants.RMI_ID, impl);
		System.out.println("Server is started");
	}

}
