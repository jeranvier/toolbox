package jeranvier.math.dsp.filters;

import jeranvier.math.dsp.Fourier;
import jeranvier.math.linearAlgebra.Vector;

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
		double binWidth = this.sampleRate / n; // Hz
		Vector.Builder filteredBuilder = new Vector.Builder(n);
		filteredBuilder.set(1, fft.get(1));
		filteredBuilder.set(n, fft.get(n));
		for(int i = 2; i <= (n+1)/2; i++){
			double binFreq = binWidth * i;
			double gain = Math.abs(DCGain)/(Math.sqrt( ( 1 + Math.pow( binFreq / frequencyCut, 2.0 * filterOrder))));
			filteredBuilder.set(i, fft.get(i).multiplyBy(gain));
			filteredBuilder.set(n-i+1, fft.get(n-i+1).multiplyBy(gain));
		}
		Vector filtered = filteredBuilder.build();

		return Fourier.ift(filtered);
	}
}

