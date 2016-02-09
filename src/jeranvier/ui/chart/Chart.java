package jeranvier.ui.chart;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
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

}
