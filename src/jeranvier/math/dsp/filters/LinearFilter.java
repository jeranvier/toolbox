package jeranvier.math.dsp.filters;

import java.util.Deque;
import java.util.LinkedList;

import jeranvier.math.linearAlgebra.Matrix;
import jeranvier.math.linearAlgebra.Vector;
import jeranvier.math.util.Complex;

public class LinearFilter {
	
	protected final int order;
	protected Deque<Complex> window;
	protected Matrix weights;

	public LinearFilter(int order) {
		this.order = order;
		window = new LinkedList<>();
		for(int i = 0; i <order; i++){
			window.addFirst(new Complex());
		}
		this.weights =Vector.zeros(order).transpose();
	}
	
	public Complex processData(Complex x_n){
		window.removeLast();
		window.addFirst(x_n);
		return weights.hermitianTranspose().multiplyBy(dequeToVector(window).transpose()).get(1, 1);
	}
	
	protected void setWindow(Deque<Complex> window){
		this.window = window;
	}

	protected Vector dequeToVector(Deque<Complex> window) {
		Vector.Builder builder = new Vector.Builder(window.size());
		int i = 1;
		for(Complex element : window){
			builder.set(i, element);
			i++;
		}
		return builder.build();
	}

}
