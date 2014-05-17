package controller;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import model.RemoteImplementation;
import RMIInterface.RMIConst;

public class SolverServer {

	public static void main(String[] args) throws RemoteException, AlreadyBoundException {
		// TODO Auto-generated method stub
		RemoteImplementation impl = new RemoteImplementation();
		Registry registry = LocateRegistry.createRegistry(RMIConst.port);
		registry.bind(RMIConst.RMI_ID, impl);
		System.out.println("Server is started");
	}

}
