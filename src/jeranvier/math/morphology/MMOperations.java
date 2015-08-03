package jeranvier.math.morphology;

import jeranvier.math.linearAlgebra.Vector;

public class MMOperations {
	
	public static Vector dilation(Vector f , StructuringElement g){
		int n = g.size();
		Vector.Builder resultBuilder = new Vector.Builder(f.size()-n);
		for(int x = g.getCenterIndex()+1; x <= f.size()-(n-g.getCenterIndex()); x++){
			double max = Double.NEGATIVE_INFINITY;
			for(int i=1; i<=n; i++){
				double value = f.get(x-g.getCenterIndex() +i).re() + g.get(i).re();
				if(value > max){
					max = value;
				}
			}
			resultBuilder.set(x-g.getCenterIndex(), max);
		}
		
		return resultBuilder.build();
	}
	
	public static Vector erosion(Vector f , StructuringElement g){
		int n = g.size();
		Vector.Builder resultBuilder = new Vector.Builder(f.size()-n);
		for(int x = g.getCenterIndex()+1; x <= f.size()-(n-g.getCenterIndex()); x++){
			double min = Double.POSITIVE_INFINITY;
			for(int i=1; i<=n; i++){
				double value = f.get(x-g.getCenterIndex() +i).re() - g.get(i).re();
				if(value < min){
					min = value;
				}
			}
			resultBuilder.set(x-g.getCenterIndex(), min);
		}
		
		return resultBuilder.build();
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

}