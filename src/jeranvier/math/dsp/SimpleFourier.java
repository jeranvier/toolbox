package jeranvier.math.dsp;

import jeranvier.math.linearAlgebra.Vector;
import jeranvier.math.util.Complex;

public class SimpleFourier {
		
	public static Vector simpleDFT(Vector in) {
		Vector.Builder outBuilder = new Vector.Builder(in.size());
		int size = in.size();
	    for (int o = 1; o <= in.size(); o++) {
	    	Complex sum = new Complex();
	        for (int i = 1; i <= in.size(); i++) {
	            double angle = 2 * Math.PI * i * o / size;
	            Complex multiple = new Complex(Math.cos(angle), -1*Math.sin(angle));
	            sum = sum.add(in.get(i).multiplyBy(multiple));

	        }
	        outBuilder.set(o, sum);
        }
	    return outBuilder.build();
	}

	public static Vector simpleIDFT(Vector in) {
		Vector.Builder outBuilder = new Vector.Builder(in.size());
		int size = in.size();
	    for (int o = 1; o <= in.size(); o++) {
	    	Complex sum = new Complex();
	        for (int i = 1; i <= in.size(); i++) {
	            double angle = 2 * Math.PI * i * o / size;
	            Complex multiple = new Complex(Math.cos(angle), Math.sin(angle));
	            sum = sum.add(in.get(i).multiplyBy(multiple));
	        }
	        outBuilder.set(o, sum.divideBy(size));
        }
	    return outBuilder.build();
	}
}
