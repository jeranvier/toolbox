package jeranvier.ui.chart;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import jeranvier.ui.chart.axis.Axis;
import jeranvier.ui.chart.axis.HorizontalAxis;
import jeranvier.ui.chart.axis.VerticalAxis;
import jeranvier.ui.chart.legend.Legend;

public class PrettyChart<X extends Number, Y extends Number> extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private Chart<X,Y> chart;
	
	public PrettyChart(Chart<X,Y> chart){
		this.chart = chart;
		
		this.setBackground(Color.WHITE);

		Cross cross = new Cross();
		cross.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {				
			}
			@Override
			public void mousePressed(MouseEvent e) {				
			}
			@Override
			public void mouseExited(MouseEvent e) {				
			}
			@Override
			public void mouseEntered(MouseEvent e) {				
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				chart.resetView();
				invalidate();
			}
		});
		Legend<X,Y> legend = new Legend<>();
		
		JScrollPane scrollableLegend = new JScrollPane(legend);
		scrollableLegend.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrollableLegend.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollableLegend.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		Axis vertical = new VerticalAxis(chart.verticalAxisFormater());
		Axis horizontal = new HorizontalAxis(chart.horizontalAxisFormater());
		chart.addChartListener(vertical);
		chart.addChartListener(horizontal);
		chart.addChartDataListener(legend);
		chart.notifyChartDataListeners();
		
	    GroupLayout layout = new GroupLayout(this);
	    this.setLayout(layout);
	    layout.setAutoCreateContainerGaps(true);
	
	    layout.setHorizontalGroup(
	    	layout.createParallelGroup().
	    	addGroup(
	    			layout.createSequentialGroup()
	    			.addGroup(layout.createParallelGroup()
	    					.addComponent(vertical, Axis.AXIS_SIZE, Axis.AXIS_SIZE, Axis.AXIS_SIZE)
	    					.addComponent(cross, Axis.AXIS_SIZE, Axis.AXIS_SIZE, Axis.AXIS_SIZE))
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(this.chart)
							.addComponent(horizontal)
					)
			)
			.addGroup(
					layout.createSequentialGroup()
					.addGap(2*Axis.AXIS_SIZE)
					.addComponent(scrollableLegend, 100, 500, Integer.MAX_VALUE)
					.addGap(2*Axis.AXIS_SIZE)
			)
		);
	    
	    layout.setVerticalGroup(
	    	layout.createSequentialGroup()
	    	.addGroup(
	    		layout.createParallelGroup(GroupLayout.Alignment.LEADING)
	    		.addGroup(layout.createSequentialGroup()
		              .addComponent(vertical)
		              .addComponent(cross, Axis.AXIS_SIZE, Axis.AXIS_SIZE, Axis.AXIS_SIZE))
	    		.addGroup(layout.createSequentialGroup()
		              .addComponent(this.chart)
		          
		              .addComponent(horizontal, Axis.AXIS_SIZE, Axis.AXIS_SIZE, Axis.AXIS_SIZE)
				)
			)
			.addComponent(scrollableLegend, 50, 50, 50)
    		
	   );
	}
	
	public Chart<X, Y> getChart(){
		return this.chart;
	}

}
