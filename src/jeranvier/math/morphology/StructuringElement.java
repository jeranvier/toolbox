package jeranvier.math.morphology;

import jeranvier.math.linearAlgebra.Vector;
import jeranvier.math.util.Complex;

public class StructuringElement extends Vector {

	private int centerIndex;

	public StructuringElement(Complex[][] data, int centerIndex) {
		super(data);
		this.centerIndex = centerIndex;
	}
	
	public int getCenterIndex(){
		return this.centerIndex;
	}
	
	public static final class Builder extends Vector.Builder{

		private int centerIndex = 0;

		public Builder(Complex[][] data, int centerIndex) {
			super(data);
			this.centerIndex = centerIndex;
		}
		
		public Builder(int columns) {
			super(columns);
		}
		
		public void setCenterIndex(int centerIndex){
			this.centerIndex = centerIndex;
		}
		
		
		
		@Override
		public void set(int column, Complex value) {
			super.set(column, value);
			
		}

		@Override
		public void set(int column, double value) {
			super.set(column, value);
		}

		public StructuringElement build(){
			return new StructuringElement(data, centerIndex);
		}
		
	}

}
