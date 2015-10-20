package jeranvier.math.dsp.filters;

import jeranvier.math.dsp.Fourier;
import jeranvier.math.linearAlgebra.Vector;
import jeranvier.math.util.Complex;

public class ButterworthFilter {
	
	private final double frequencyCut;
	private final double sampleRate;
	private final int filterOrder;
	private final double DCGain;

	public ButterworthFilter(double sampleRate, double frequencyCut, int filterOrder, double DCGain){
		this.frequencyCut = frequencyCut;
		this.sampleRate = sampleRate;
		this.filterOrder = filterOrder;
		this.DCGain = DCGain;	
	}
	
	public ButterworthFilter(double sampleRate, double frequencyCut, int filterOrder){
			this(sampleRate, frequencyCut, filterOrder, 1.0);
	}
	
	
	
	public Vector filter(Vector signal){
		Vector fft = Fourier.dft(signal);
		int n = signal.size();
		int numBins = n / 2;  // Half the length of the FFT by symmetry
		double binWidth = this.sampleRate / n; // Hz
		Vector.Builder filteredBuilder = new Vector.Builder(n);
		filteredBuilder.set(0, fft.get(0));
		filteredBuilder.set(n-1, fft.get(n-1));
		for(int i = 1; i < (n+1)/2; i++){
			double binFreq = binWidth * i;
			double gain = Math.abs(DCGain)/(Math.sqrt( ( 1 + Math.pow( binFreq / frequencyCut, 2.0 * filterOrder))));
			filteredBuilder.set(i, fft.get(i).multiplyBy(gain));
			filteredBuilder.set(n-i, fft.get(n-i).multiplyBy(gain));
		}
		Vector filtered = filteredBuilder.build();

		return Fourier.ift(filtered);
	}
}

