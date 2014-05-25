package model;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import model.algorithms.AISolver;
import model.algorithms.AStar;
import model.algorithms.Action;
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
		tpes = Executors.newFixedThreadPool(2);
		System.out.println("Remote is now Up");
	}

	@Override
	public int get2048Hint(final Object game, final int solveDepth) throws RemoteException, CloneNotSupportedException, InterruptedException{
		final HashMap<String, Integer> container = new HashMap<>();
		container.put("Move", 0);
		container.put("Changed", 0);
		isChanged = false;
		final Thread t=Thread.currentThread();
		tpes.execute(new Runnable() {
			@Override
			public void run() {
				Game2048Model myGame = new Game2048Model((Game2048Object)game);	
				try {
					int move = AISolver.findBestMove((Model)myGame, solveDepth);
					container.put("Move", move);
					container.put("Changed", 1);
					t.interrupt();
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}						
		});
		
		try {
			while (container.get("Changed") != 1) {
				 try {
				 Thread.sleep(Integer.MAX_VALUE);
				 } catch (InterruptedException e) { }
				 }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	return container.get("Move");				
	}
	
	@Override
	public ArrayList<Action> getMazeHint(Object game) {
			System.out.println("Im in getMazeHint");
			System.out.println(game);
			GameMazeModel mazeGame = new GameMazeModel((GameMazeObject)game);
			System.out.println(mazeGame.getStartState());
	        AStar as = new AStar(new GameMazeDomain(mazeGame), new GameMazeDistanceG(), new GameMazeDistanceH());
	        System.out.println("I have the as");
	        ArrayList<Action> actions = as.search(mazeGame.getStartState(), mazeGame.getGoalState());
	        System.out.println("Lets test the actions" + actions);
	        return actions;
	    
	}


}
