package RMIInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;



public interface RemoteInterface extends Remote{

	int get2048Hint(Object Game, int numOfHints) throws RemoteException,
			CloneNotSupportedException, InterruptedException;
	
	public Object getMazeHint(Object Game) throws RemoteException,
	CloneNotSupportedException, InterruptedException;

}
