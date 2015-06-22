package jeranvier.recall;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.rmi.RemoteException;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class ServerView extends JFrame implements View{

	private static final long serialVersionUID = 1L;
	private JScrollPane scroll;
	private JList<Object> objects;
	
	public ServerView(){
		this.setTitle("Recall server");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		objects = new JList<>();
		objects.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		objects.setLayoutOrientation(JList.VERTICAL);
		objects.setVisibleRowCount(-1);				
		scroll = new JScrollPane (objects);
	    scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	    scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.getContentPane().add(scroll, BorderLayout.CENTER);
		this.getContentPane().setPreferredSize(new Dimension(200, 300));
		this.setLocation(200, 200);
	}	

	@Override
	public void update(Viewed viewed) {
		objects.removeAll();
		try {
			objects.setListData(((RecallServerInterface)viewed).getAllNames().toArray());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public void test() {
		objects.removeAll();
		this.invalidate();
	}

}
