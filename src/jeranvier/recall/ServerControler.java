package jeranvier.recall;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ServerControler extends MouseAdapter implements Controler{
	
	private Viewed viewed;
	private ServerView view;
	final JFileChooser fc = new JFileChooser();

	public ServerControler( Viewed viewed, ServerView view){
		this.viewed = viewed;
		this.view = view;
		fc.setFileFilter(new FileNameExtensionFilter("memorable object","mem"));
		this.view.setStatus(this.view.getMemoryConsumptionMessage());
	}

	public void mouseClicked(MouseEvent evt) {
		if(evt.getSource() instanceof JList){
	        @SuppressWarnings("unchecked")
			JList<String> list = (JList<String>)evt.getSource();
	        if (evt.getClickCount() == 2) {
	            int index = list.locationToIndex(evt.getPoint());
	            try {
	            	this.view.setStatus("Displaying object...");
					display(list.getModel().getElementAt(index), ((RecallServerInterface)viewed).recall(list.getModel().getElementAt(index)));
	            	this.view.setStatus(this.view.getMemoryConsumptionMessage());
	            } catch (RemoteException e) {
					e.printStackTrace();
				}
	        }
		}
		
		if(evt.getSource() instanceof JButton){
			if(evt.getSource().equals(view.forgetButton()) && evt.getClickCount() >= 1){
				try {
					((RecallServerInterface)viewed).forget(view.objects().getSelectedValue());
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(evt.getSource().equals(view.saveButton()) && evt.getClickCount() >= 1){
		        this.view.setStatus("Saving file");
				int returnVal = fc.showSaveDialog(view);
		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            try {
						((RecallServerInterface)viewed).saveToDisk(file.getCanonicalPath());
					} catch (IOException e) {
						e.printStackTrace();
					}
		        }
		        this.view.setStatus(this.view.getMemoryConsumptionMessage());
			}else if(evt.getSource().equals(view.openButton()) && evt.getClickCount() >= 1){
				this.view.setStatus("Opening file");
				int returnVal = fc.showOpenDialog(view);
		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            try {
						((RecallServerInterface)viewed).open(file.getCanonicalPath());
					} catch (IOException e) {
						e.printStackTrace();
					}
		        }
		        this.view.setStatus(this.view.getMemoryConsumptionMessage());
			}
		}
    }
	
	private void display(String objectName, Serializable object) {
		Thread viewer = new Thread(new ObjectView(objectName, object));
		viewer.start();
	}


	public class ObjectView extends JFrame implements Runnable{

		private static final long serialVersionUID = 1L;
		private JTextPane output;
		private Object object;
		
		public ObjectView(String objectName, Object object){
			this.object = object;
			this.setTitle(objectName);
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			
			output = new JTextPane();
			
			JScrollPane scroll = new JScrollPane (output);
		    scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		    scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			this.getContentPane().add(scroll, BorderLayout.CENTER);
			this.getContentPane().setPreferredSize(new Dimension(400, 600));
			this.setLocation(400, 200);
			this.pack();
			this.setVisible(true);
		}

		@Override
		public void run() {
			output.setText(object.toString());
		}

	}
}
