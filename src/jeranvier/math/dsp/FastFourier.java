package jeranvier.math.dsp;

import jeranvier.math.linearAlgebra.Vector;
import jeranvier.math.util.Complex;

public class FastFourier {
	
	public static Vector cooleyTukeyFFT(Vector in){
		if((in.size() & (in.size() - 1)) != 0){
			throw new IllegalArgumentException("input vector is not of the size of a power of 2");
		}else{
			Complex[] out = dit2(in.data()[0]);
			return new Vector.Builder(out).build();
		}
	}
	
	public static Vector cooleyTukeyIFFT(Vector in){
		if((in.size() & (in.size() - 1)) != 0){
			throw new IllegalArgumentException("input vector is not of the size of a power of 2");
		}else{
			Complex[] out = idit2(in.data()[0]);
			int size = in.size();
			//rescaling
			for(int i = 0; i< out.length; i++){
				out[i] = out[i].divideBy(size);
			}
			return new Vector.Builder(out).build();
		}
	}

	private static Complex[] dit2(Complex[] in) {
		Complex[] out = new Complex[in.length];
		if(in.length==1){
			out[0] = in[0];
		}
		else{
			Complex[] even = new Complex[in.length/2];
			Complex[] odd = new Complex[in.length/2];
			
			for(int i = 0; i<in.length/2; i++){
				even[i] = in[2*i];
				odd[i] = in[2*i+1];
			}
			even = dit2(even);
			odd = dit2(odd);
			
			for(int k=0; k<in.length/2; k++){
				Complex c = Complex.newPolarComplex(1, -2*Math.PI*k/in.length).multiplyBy(odd[k]);
				out[k] = even[k].add(c);
				out[k+in.length/2] = even[k].substract(c);
			}
		}
		return out;
	}
	
	private static Complex[] idit2(Complex[] in) {
		Complex[] out = new Complex[in.length];
		if(in.length==1){
			out[0] = in[0];
		}
		else{
			Complex[] even = new Complex[in.length/2];
			Complex[] odd = new Complex[in.length/2];
			
			for(int i = 0; i<in.length/2; i++){
				even[i] = in[2*i];
				odd[i] = in[2*i+1];
			}
			even = idit2(even);
			odd = idit2(odd);
			
			for(int k=0; k<in.length/2; k++){
				Complex c = Complex.newPolarComplex(1, 2*Math.PI*k/in.length).multiplyBy(odd[k]);
				out[k] = even[k].add(c);
				out[k+in.length/2] = even[k].substract(c);
			}
		}
		return out;
	}
	
}
