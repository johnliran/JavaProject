package RMIInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;



public interface Game2048RemoteInterface extends Remote{

	int getHint(Object Game) throws RemoteException,
			CloneNotSupportedException;

}
