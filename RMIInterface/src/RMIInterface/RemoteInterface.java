package RMIInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;



public interface RemoteInterface extends Remote{

	int getHint(Object Game) throws RemoteException,
			CloneNotSupportedException;

}
