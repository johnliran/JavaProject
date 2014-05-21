package model;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import model.algorithms.AISolver;
import model.algorithms.Model;
import RMIInterface.RemoteInterface;

public class RemoteImplementation extends UnicastRemoteObject implements RemoteInterface {
	private final ExecutorService tpes ;
	private boolean isChanged = false;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */

	public RemoteImplementation() throws RemoteException {
		super();
		tpes = Executors.newFixedThreadPool(1);
	}

	@Override
	public int getHint(final Object game) throws RemoteException, CloneNotSupportedException, InterruptedException{
		final HashMap<String, Integer> container = new HashMap<>();
		container.put("Move", 0);
		container.put("Changed", 0);
		isChanged = false;
		tpes.execute(new Runnable() {
			@Override
			public void run() {
				System.out.println("Im in hint");
				Game2048Model myGame = new Game2048Model((Game2048Object)game);	
				try {
					int move = AISolver.findBestMove((Model)myGame, 7);
					container.put("Move", move);
					container.put("Changed", 1);
					System.out.println("move value is " + container.get("Move"));
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}						
		});	
		try {
			while (container.get("Changed") != 1)
			{
				Thread.sleep(10);
				System.out.println("Im calculating");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	return container.get("Move");				
	}


}
