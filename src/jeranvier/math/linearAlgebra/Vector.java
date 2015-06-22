package jeranvier.math.linearAlgebra;

import jeranvier.math.util.Complex;

public class Vector extends Matrix{
	
	protected Vector(Complex[][] data){
		super(data);
	}
	
	public int size(){
		return this.data[0].length;
	}
	
	public Complex get(int column){
		return this.data[0][column-1];
	}

	public static final class Builder extends Matrix.Builder{
		
		public Builder(int size){
			super(1, size);
		}
		
		public Builder(Complex[][] data){
			super(data);
			if(data.length !=1){
				throw new IllegalArgumentException();
			}
		}
		
		public void set(int column, Complex value){
			this.data[0][column-1] = value;
		}
		
		public void set(int column, double value){
			this.data[0][column-1] = new Complex(value, 0d);
		}
		
		public Complex get(int column){
			return this.data[0][column-1];
		}
		
		public Vector build(){
			return new Vector(data);
		}
	}

	public static Vector zeros(int size) {
		Builder b = new Builder(size);
		for(int i=1; i<=size; i++){
			b.set(i, 0);
		}
		return b.build();
	}
}
