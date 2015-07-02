package jeranvier.math.dsp.filters;

import jeranvier.math.linearAlgebra.Matrix;
import jeranvier.math.util.Complex;

public class AdaptiveFilter extends LinearFilter{
	
	private Complex step;

	public AdaptiveFilter(int order, double step){
		super(order);
		this.step = new Complex(step, 0.0);
	}
	
	public Complex processData(Complex x_n, Complex d_n){
		Complex filtered_n = super.processData(x_n);
		return adapteWeightsNLMS(d_n, filtered_n);
	}
	
	private Complex adapteWeightsNLMS(Complex d_n, Complex filtered_n) {
		Matrix x = dequeToVector(window).transpose();
		Complex error = d_n.substract(filtered_n);
		Matrix updateOnWeights = x.conjugate().divideBy(x.hermitianTranspose().multiplyBy(x).get(1, 1)).multiplyBy(error);
		
		weights = weights.add(updateOnWeights.multiplyBy(step));
		
		return error;
	}
}
