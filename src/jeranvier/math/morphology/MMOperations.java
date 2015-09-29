package jeranvier.math.morphology;

import jeranvier.math.linearAlgebra.Vector;

public class MMOperations {

	private static double[] dilation(double[] f, double[] g, int centerIndex) {
		double[] result = new double[f.length-g.length];

		int gLeftSideLength = centerIndex;
		int gRightSideLength = g.length-centerIndex;
		for(int x = gLeftSideLength; x<f.length-gRightSideLength; x++){
			double max = Double.NEGATIVE_INFINITY;
			for(int i = 0; i<g.length; i++){
				double value = f[x-gLeftSideLength+i] + g[i];
				if(max < value){
					max = value;
				}

			}
			result[x-gLeftSideLength] = max;
		}
		return result;
	}

	private static double[] erosion(double[] f, double[] g, int centerIndex) {
		double[] result = new double[f.length-g.length];
		int gLeftSideLength = centerIndex;
		int gRightSideLength = g.length-centerIndex;
		double[] reversedG = reverse(g);

		for(int x = gLeftSideLength; x<f.length-gRightSideLength; x++){
			double min = Double.POSITIVE_INFINITY;
			for(int i = 0; i<reversedG.length; i++){
				double value = f[x-gLeftSideLength+i] - reversedG[i];
				if(min > value){
					min = value;
				}

			}
			result[x-gLeftSideLength] = min;
		}
		return result;
	}

	public static Vector erosion(Vector f , StructuringElement g){
		return new Vector(erosion(f.re()[0], g.vector().re()[0], g.getCenterIndex()));
	}

	public static Vector dilation(Vector f , StructuringElement g){
		return new Vector(dilation(f.re()[0], g.vector().re()[0], g.getCenterIndex()));
	}


	public static Vector open(Vector f , StructuringElement g){
		return erosion(dilation(f,g), g);
	}

	public static Vector close(Vector f , StructuringElement g){
		return dilation(erosion(f,g), g);
	}

	public static Vector topHat(Vector f , StructuringElement g){
		return new Vector(f.getRange(2*g.getCenterIndex()+1, f.size()-2*(g.size()-g.getCenterIndex())).substract(open(f, g)).data());
	}

	public static Vector bottomHat(Vector f , StructuringElement g){
		return new Vector(f.getRange(2*g.getCenterIndex()+1, f.size()-2*(g.size()-g.getCenterIndex())).substract(close(f, g)).data());
	}

	private static double[] reverse(double[] g){
		double[] reversedG = g;

		for(int i = 0; i < reversedG.length / 2; i++)
		{
			double temp = reversedG[i];
			reversedG[i] = reversedG[reversedG.length - i - 1];
			reversedG[reversedG.length - i - 1] = temp;
		}
		return reversedG;
	}

}