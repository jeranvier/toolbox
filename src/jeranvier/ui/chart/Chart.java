package jeranvier.ui.chart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.text.Format;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class Chart <X, Y> extends JPanel{
	protected Map<String, TreeMap<X, Y>> data = new HashMap<>();
	protected Space currentSpace;
	protected Space totalSpace;
	protected static final double DATA_SPACE_MARGINS = 0.05; //in percentage
	protected static final ClipboardHandler clipboardHandler = new ClipboardHandler();
	public static final Color[] colors = new Color[]{
		Color.decode("#7B1FA2"),
		Color.decode("#FFAB00"),
		Color.decode("#4A148C"),
		Color.decode("#880E4F"),
		Color.decode("#1B5E20"),
		Color.decode("#006064")};
	public static final Stroke stroke = new BasicStroke(2);
	private List<ChartListener> chartListeners;
	
	public Chart(){
		this.chartListeners = new LinkedList<>();
		MouseController controller = new MouseController(this);
		this.addMouseMotionListener(controller);
		this.addMouseWheelListener(controller);
		this.addMouseListener(controller);
	}

	public void zoom(double delta, int x, int y) {
		try {
			currentSpace.zoom(delta, new Point2D.Double(x, y));
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
		}
		this.repaint();
	}
	
	public void horizontalZoom(double delta, int x, int y) {
		try {
			currentSpace.horizontalZoom(delta, new Point2D.Double(x, y));
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
		}
		this.repaint();
	}
	
	public void verticalZoom(double delta, int x, int y) {
		try {
			currentSpace.verticalZoom(delta, new Point2D.Double(x, y));
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
		}
		this.repaint();
	}
	
	public void addChartListener(ChartListener listener){
		this.chartListeners.add(listener);
	}
	
	public void notifyChartListeners(){
		for(ChartListener cl : chartListeners){
			cl.notify(currentSpace);
		}
	}

	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
		tx.translate(0, -this.getHeight());
		g2d.transform(tx);
		currentSpace.updateSize(this.getSize());
		notifyChartListeners();
	}

	public void drag(int dx, int dy){
		try {
			currentSpace.updateTranslation(new Point2D.Double(dx, dy));
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
		}
		this.repaint();
	}

	public abstract Format verticalAxisFormater();

	public abstract Format horizontalAxisFormater();

	public abstract void highlightClosestDataPoint(int x, int y);
	
	public static final class ClipboardHandler implements ClipboardOwner {

		@Override
		public void lostOwnership(Clipboard clipboard, Transferable contents) {
		}
		
		public void setClipboardContent(String string){
		    StringSelection stringSelection = new StringSelection(string);
		    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		    clipboard.setContents(stringSelection, this);
		  }
		
	}


}
