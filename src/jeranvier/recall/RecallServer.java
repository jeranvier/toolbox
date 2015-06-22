package jeranvier.recall;

import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.*; 
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
public class RecallServer extends UnicastRemoteObject implements RecallServerInterface, Viewed{
	
	private static final long serialVersionUID = 1L;
	
	private final Map<String, Serializable> data;

	private Set<View> views;
 
    public RecallServer() throws RemoteException {
        super(0);
        data = new HashMap<>();
        views = new HashSet<View>();
    }
 
    public static void main(String args[]) throws Exception {
        System.out.println("Starting recall server");
 
        try { //special exception handler for registry creation
            LocateRegistry.createRegistry(1099); 
            System.out.println("java RMI registry created.");
        } catch (RemoteException e) {
            //do nothing, error means registry already exists
            System.out.println("java RMI registry already exists.");
        }
 
        //Instantiate RmiServer
 
        RecallServer server = new RecallServer();
        // Bind this object instance to the name "RmiServer"
        Naming.rebind("//localhost/RecallServer", server);
        System.out.println("Server started");
        
        //add a simple ui
        ServerView view = new ServerView();
        server.addView(view);
        view.pack();
		view.setVisible(true);
    }

	@Override
	public Serializable recall(String objectName) {
		return this.data.get(objectName);
	}

	@Override
	public void remember(String objectName, Serializable object) {
		this.data.put(objectName, object);
		this.updateViews();
	}

	@Override
	public void forget(String objectName) {
		this.data.remove(objectName);
		this.updateViews();
	}

	@Override
	public Set<String> getAllNames() {
		return this.data.keySet();
	}
	
	@Override
	public void addView(View view) {
		this.views.add(view);
	}
	
	public void updateViews(){
		for(View observer : views){
			observer.update(this);
		}
	}
}