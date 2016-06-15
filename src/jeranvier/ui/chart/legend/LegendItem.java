package jeranvier.ui.chart.legend;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class LegendItem extends JPanel{
	
	public final String name;

	public LegendItem(String name){
		this.name=name;
		this.setBackground(Color.WHITE);
	}

}
