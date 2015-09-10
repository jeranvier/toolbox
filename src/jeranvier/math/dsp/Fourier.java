package jeranvier.math.dsp;

import jeranvier.math.linearAlgebra.Vector;
import jeranvier.math.util.Complex;

public class Fourier {
		
	public static Vector dft(Vector in) {
		if((in.size() & (in.size() - 1)) == 0){
			return cooleyTukeyFFT(in);
		}
		else{
			return simpleDFT(in);
		}
		
	}
	
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

	public static Vector ift(Vector in) {
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
	
	public static Vector cooleyTukeyFFT(Vector in){
		if((in.size() & (in.size() - 1)) != 0){
			throw new IllegalArgumentException("input vector is not of the size of a power of 2");
		}else{
			Complex[] out = dit2(in.data()[0]);
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

	public static final class Util{
		public static Vector potPadding(Vector in){
			int power=1;
			while(in.size()>power){
				power = power << 1;
			}
			
			System.out.println("was: "+in.size()+", and is now: "+power);
			Vector.Builder vb = new Vector.Builder(power);

			int i = 1;
			for(Complex c : in){
				vb.set(i, c);
				i++;
			}
			
			for(int j = i; j<=power;j++){
				vb.set(j, new Complex());
			}
			return vb.build();
		}
	}

}
