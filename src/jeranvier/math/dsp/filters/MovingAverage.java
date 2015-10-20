package jeranvier.math.dsp.filters;

import jeranvier.math.linearAlgebra.Vector;
import jeranvier.math.util.Complex;

public class MovingAverage {
	
	public static Vector SimpleMovingAverage(Vector data, int windowRadius){
		Vector.Builder vb = new Vector.Builder(data.size());
		
		for(int i = 0; i < windowRadius; i++){
			Complex sum = new Complex();
			for(int j=0; j<i+windowRadius; j++){
				sum = sum.add(data.get(j));
			}
			vb.set(i, sum.divideBy(i+windowRadius));
		}
		
		for(int i = windowRadius+1; i < data.size()-windowRadius; i++){
			Complex sum = new Complex();
			for(int j=i-windowRadius; j<i+windowRadius; j++){
				sum = sum.add(data.get(j));
			}
			vb.set(i, sum.divideBy(2*windowRadius));
		}
		
		for(int i = data.size()-windowRadius; i < data.size(); i++){
			Complex sum = new Complex();
			for(int j =i; j<=data.size(); j++){
				sum = sum.add(data.get(j));
			}
			vb.set(i, sum.divideBy(data.size()-i+1));
		}
		return vb.build();	
	}

}
