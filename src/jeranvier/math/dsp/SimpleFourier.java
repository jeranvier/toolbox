package jeranvier.math.dsp;

import jeranvier.math.linearAlgebra.Vector;
import jeranvier.math.util.Complex;

public class SimpleFourier {
	
	public static Vector simpleDFT(Vector in) {
		return new Vector.Builder(simpleDFT(in.data()[0])).build();
	}
		
	public static Complex[] simpleDFT(Complex[] in) {
		Complex[] out = new Complex[in.length];
		int size = in.length;
	    for (int o = 0; o < size; o++) {
	    	Complex sum = new Complex();
	        for (int i = 0; i < size; i++) {
	            double angle = 2 * Math.PI * i * o / size;
	            Complex multiple = new Complex(Math.cos(angle), -1*Math.sin(angle));
	            sum = sum.add(in[i].multiplyBy(multiple));

	        }
	        out[o] = sum;
        }
	    return out;
	}
	
	public static Vector simpleIDFT(Vector in) {
		return new Vector.Builder(simpleIDFT(in.data()[0])).build();
	}

	public static Complex[] simpleIDFT(Complex[] in) {
		Complex[] out = new Complex[in.length];
		int size = in.length;
	    for (int o = 0; o < size; o++) {
	    	Complex sum = new Complex();
	        for (int i = 0; i < size; i++) {
	            double angle = 2 * Math.PI * i * o / size;
	            Complex multiple = new Complex(Math.cos(angle), Math.sin(angle));
	            sum = sum.add(in[i].multiplyBy(multiple));
	        }
	        out[o] = sum.divideBy(size);
        }
	    return out;
	}
}
