package jeranvier.math.dsp;

import jeranvier.math.linearAlgebra.Vector;
import jeranvier.math.util.Complex;

public class Fourier {
		
	public static Vector dft(Vector in) {
		if((in.size() & (in.size() - 1)) == 0){
			return FastFourier.cooleyTukeyFFT(in);
		}
		else{
			return SimpleFourier.simpleDFT(in);
		}
	}

	public static Vector ift(Vector in) {
		if((in.size() & (in.size() - 1)) == 0){
			return FastFourier.cooleyTukeyIFFT(in);
		}
		else{
			return SimpleFourier.simpleIDFT(in);
		}
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
