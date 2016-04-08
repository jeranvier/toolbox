package jeranvier.math.dsp;


public class Entropy {
	
	public static double computeApproximateEntropy(int m, double r, double[] t){
		return computePhi(m, r, t) - computePhi(m+1, r ,t);
	}
	
	private static double computePhi(int m, double r, double[] t){
		int N = t.length;
		
		//Step 3
		double[][] x = new double[N-m+1][m];
		for(int i = 0; i<=N-m; i++){
			for(int j=0; j<m; j++){
				x[i][j] = t[i+j];
			}
		}
				
		//Step 4
		double[] C = new double[N-m+1];
		for(int i=0; i<=N-m; i++){
			C[i] = computeCim(i, x, m, r, N);
		}
		
		//step5
		double phi =0.0;
		for(int i =0; i<= N-m; i++){
			phi += Math.log(C[i]);
		}
		phi = phi/(N-m+1);
		return phi;
	}
	
	private static double computeCim(int i, double[][] x, int m, double r, int N) {
		double count = 0;
		for(int j=0; j<x.length; j++){
			if(d(x[i], x[j])<=r){
				count++;
			}
		}
		return count/(N-m+1);
	}

	private static double d(double[] x, double[] y){
		double d = 0.0;		
		for(int i = 0; i< x.length; i++){
			d = Math.max(d, Math.abs(x[i]-y[i]));
		}	
		return d;
	}
	
	public static void main(String arg[]){
		double[] t = new double[]{85, 80, 89, 85, 80, 89, 85, 80, 89, 85,
								  80, 89, 85, 80, 89, 85, 80, 89, 85, 80,
								  89, 85, 80, 89, 85, 80, 89, 85, 80, 89,
								  85, 80, 89, 85, 80, 89, 85, 80, 89, 85,
								  80, 89, 85, 80, 89, 85, 80, 89, 85, 80,
								  89};
		System.out.println(computeApproximateEntropy(2, 3, t));
	}

}
