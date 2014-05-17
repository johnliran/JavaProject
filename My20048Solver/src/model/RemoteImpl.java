package model;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import model.algorithms.AIsolver;
import model.algorithms.Model;
import RMIInterface.Game2048RemoteInterface;

public class RemoteImpl extends UnicastRemoteObject implements Game2048RemoteInterface{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */

	RemoteImpl() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getHint(Object game) throws RemoteException, CloneNotSupportedException{
		Game2048Model myGame = new Game2048Model((Game2048Object)game);
		
		
	return AIsolver.findBestMove((Model)myGame, 5);
		
	}
	

	
	

}
