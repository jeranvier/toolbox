package jeranvier.recall;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;

public interface RecallServerInterface extends Remote{
	
	public Serializable recall(String objectName) throws RemoteException;
	
	public void remember(String objectName, Serializable object) throws RemoteException;
	
	public void forget(String objectName) throws RemoteException;

	public HashSet<String> getAllNames() throws RemoteException;

	public void open(String filePath) throws RemoteException;

	public void saveToDisk(String filePath) throws RemoteException;

}
