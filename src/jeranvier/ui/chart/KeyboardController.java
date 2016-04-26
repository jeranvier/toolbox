package jeranvier.ui.chart;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardController implements KeyListener {

	private Chart<?, ?> chart;

	public KeyboardController(Chart<?, ?> chart) {
		this.chart = chart;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		switch(e.getKeyChar()){
		case 'r': chart.resetView(); break;
		}
    }

	@Override
	public void keyPressed(KeyEvent e) {
		int multiplier = 10;
		if(e.isShiftDown()){
			multiplier=1;
		}else if( e.isAltDown()){
			multiplier = 100;
		}else if(e.isMetaDown()){
			
			switch(e.getKeyCode()){
			case KeyEvent.VK_UP: chart.zoom(-5, chart.getWidth()/2, chart.getHeight()/2); break;
			case KeyEvent.VK_DOWN: chart.zoom(5, chart.getWidth()/2, chart.getHeight()/2); break;
		}
		return;	
		}
		
		switch(e.getKeyCode()){
			case KeyEvent.VK_RIGHT: chart.drag(-multiplier, 0);break;
			case KeyEvent.VK_LEFT: chart.drag(multiplier, 0); break;
			case KeyEvent.VK_UP: chart.drag(0, multiplier); break;
			case KeyEvent.VK_DOWN: chart.drag(0, -multiplier); break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {		
	}

}
