package jeranvier.recall;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseListener;
import java.rmi.RemoteException;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class ServerView extends JFrame implements View{

	private static final long serialVersionUID = 1L;
	private JScrollPane scroll;
	private JList<String> objects;
	private JButton forgetButton;
	private JButton saveButton;
	private JButton openButton;
	private JPanel statusBar;
	private JLabel status;
	
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
	    
	    JPanel bottomControls = new JPanel(new GridLayout(4, 1));
	    forgetButton = new JButton("forget object");
	    saveButton = new JButton("write down");
	    openButton = new JButton("read from disk");
	    statusBar = new JPanel();
	    status = new JLabel();
	    statusBar.add(status);
	    bottomControls.add(forgetButton);
	    bottomControls.add(saveButton);
	    bottomControls.add(openButton);
	    bottomControls.add(statusBar);
	    
		this.getContentPane().add(scroll, BorderLayout.CENTER);
		this.getContentPane().add(bottomControls, BorderLayout.PAGE_END);
		this.getContentPane().setPreferredSize(new Dimension(200, 300));
		this.setLocation(200, 200);
	}	

	@Override
	public void update(Viewed viewed) {
		objects.removeAll();
		try {
			Set<String> names = ((RecallServerInterface)viewed).getAllNames();
			String[] stringArray = new String[names.size()];
			names.toArray(stringArray);
			objects.setListData(stringArray);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public void test() {
		objects.removeAll();
		this.invalidate();
	}

	@Override
	public void addControler(Controler controler) {
		objects.addMouseListener((MouseListener) controler);
	    forgetButton.addMouseListener((MouseListener) controler);
	    saveButton.addMouseListener((MouseListener) controler);
	    openButton.addMouseListener((MouseListener) controler);
	}

	public JList<String> objects() {
		return objects;
	}

	public JButton forgetButton() {
		return forgetButton;
	}

	public JButton saveButton() {
		return saveButton;
	}

	public JButton openButton() {
		return openButton;
	}
	
	public JLabel status() {
		return status;
	}
	

}
