package jeranvier.recall;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Set;

public class RecallClient implements RecallServerInterface{ 
	
    private RecallServerInterface linkedServer;

	public RecallClient(){
        try {
			linkedServer = (RecallServerInterface)Naming.lookup("//localhost/RecallServer");
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
    }

	@Override
	public Serializable recall(String objectName) throws RemoteException {
		return linkedServer.recall(objectName);
	}

	@Override
	public void remember(String objectName, Serializable object) throws RemoteException {
		linkedServer.remember(objectName, object);
	}

	@Override
	public void forget(String objectName) throws RemoteException {
		linkedServer.forget(objectName);
	}

	@Override
	public Set<String> getAllNames() throws RemoteException {
		return linkedServer.getAllNames();
	}
    
}