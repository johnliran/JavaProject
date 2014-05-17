package model;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import model.algorithms.AIsolver;
import model.algorithms.Model;
import RMIInterface.RemoteInterface;

public class RemoteImplementation extends UnicastRemoteObject implements RemoteInterface{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */

	public RemoteImplementation() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getHint(Object game) throws RemoteException, CloneNotSupportedException{
		Game2048Model myGame = new Game2048Model((Game2048Object)game);		
	return AIsolver.findBestMove((Model)myGame, 7);
		
	}


}
