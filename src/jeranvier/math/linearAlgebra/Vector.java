package jeranvier.math.linearAlgebra;

import jeranvier.math.util.Complex;

public class Vector extends Matrix{
	
	public Vector(Complex[][] data){
		super(data);
		if(data.length !=1){
			throw new IllegalArgumentException();
		}
	}
	
	public int size(){
		return this.data[0].length;
	}
	
	public Complex get(int column){
		return this.data[0][column-1];
	}
	
	

	@Override
	public Vector entrywiseMultiplyBy(Matrix that) throws IllegalArgumentException {
		return new Vector(super.entrywiseMultiplyBy(that).data);
	}
	
	public Complex dot(Vector that) throws IllegalArgumentException {
		if(!sameDimension(this, that)){
			throw new IllegalArgumentException();
		}
		Complex sum = new Complex();
		for(int i=1; i<=this.columns; i++){
			sum = sum.add(this.get(i).multiplyBy(that.get(i)));
		}
		return sum;
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
		
		public Builder(Vector vector) {
			super(vector.data);
		}
		
		public Builder(Complex[] line){
			super(1, line.length);
			this.data[0] = line;
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
