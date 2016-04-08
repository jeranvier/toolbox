package jeranvier.ui.chart;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.GroupLayout;
import javax.swing.JPanel;

import jeranvier.ui.chart.axis.Axis;
import jeranvier.ui.chart.axis.HorizontalAxis;
import jeranvier.ui.chart.axis.VerticalAxis;

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
		Axis vertical = new VerticalAxis(chart.verticalAxisFormater());
		Axis horizontal = new HorizontalAxis(chart.horizontalAxisFormater());
		chart.addChartListener(vertical);
		chart.addChartListener(horizontal);
		
		
	    GroupLayout layout = new GroupLayout(this);
	    this.setLayout(layout);
//	    layout.setAutoCreateGaps(true);
	    layout.setAutoCreateContainerGaps(true);
	
	    layout.setHorizontalGroup(
	      layout.createSequentialGroup()
	      .addGroup(layout.createParallelGroup()
	    		  .addComponent(vertical, Axis.AXIS_SIZE, Axis.AXIS_SIZE, Axis.AXIS_SIZE)
		          .addComponent(cross, Axis.AXIS_SIZE, Axis.AXIS_SIZE, Axis.AXIS_SIZE))
	         .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
	              .addComponent(this.chart)
	              .addComponent(horizontal))
		);
	    
	    layout.setVerticalGroup(
	    		layout.createParallelGroup(GroupLayout.Alignment.LEADING)
	    		.addGroup(layout.createSequentialGroup()
		              .addComponent(vertical)
		              .addComponent(cross, Axis.AXIS_SIZE, Axis.AXIS_SIZE, Axis.AXIS_SIZE))
	    		.addGroup(layout.createSequentialGroup()
		              .addComponent(this.chart)
		              .addComponent(horizontal, Axis.AXIS_SIZE, Axis.AXIS_SIZE, Axis.AXIS_SIZE)) 
		   );
	}

}
