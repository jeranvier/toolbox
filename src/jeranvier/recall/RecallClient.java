package jeranvier.recall;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.logging.Logger;

public class RecallClient implements RecallServerInterface{ 
	
    private RecallServerInterface linkedServer;
    private static final Logger log = Logger.getLogger("recall.client");


	public RecallClient(){
        try {
			linkedServer = (RecallServerInterface)Naming.lookup("//localhost/RecallServer");
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
    }

	@Override
	public Serializable recall(String objectName) throws RemoteException {
		log.finest("Recall: "+objectName);
		return linkedServer.recall(objectName);
	}

	@Override
	public void remember(String objectName, Serializable object) throws RemoteException {
		log.finest("Remember: "+objectName);
		linkedServer.remember(objectName, object);
	}

	@Override
	public void forget(String objectName) throws RemoteException {
		log.finest("Forget: "+objectName);
		linkedServer.forget(objectName);
	}

	@Override
	public HashSet<String> getAllNames() throws RemoteException {
		return linkedServer.getAllNames();
	}

	@Override
	public void open(String filePath) throws RemoteException {
		linkedServer.open(filePath);
	}

	@Override
	public void saveToDisk(String filePath) throws RemoteException {
		linkedServer.saveToDisk(filePath);
	}
    
}