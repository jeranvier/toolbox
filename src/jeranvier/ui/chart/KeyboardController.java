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
		switch(e.getKeyCode()){
			case KeyEvent.VK_RIGHT: chart.drag(-10, 0); break;
			case KeyEvent.VK_LEFT: chart.drag(10, 0); break;
			case KeyEvent.VK_UP: chart.drag(0, 10); break;
			case KeyEvent.VK_DOWN: chart.drag(0, -10); break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {		
	}

}
