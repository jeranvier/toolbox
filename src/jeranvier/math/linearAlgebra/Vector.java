package jeranvier.math.linearAlgebra;

import java.util.Arrays;
import java.util.Iterator;

import jeranvier.math.util.Complex;

public class Vector extends Matrix implements Iterable<Complex>{
	
	public Vector(Complex[][] data){
		super(data);
		if(data.length !=1){
			throw new IllegalArgumentException();
		}
	}
	
	public Vector(double... values) {
		super(doubleToComplex(values));
	}

	public int size(){
		return this.data[0].length;
	}
	
	public Complex get(int column){
		return this.data[0][column];
	}
	
	public Vector getRange(int min, int max){
		Vector.Builder vb = new Vector.Builder(max - min +1);
		for(int i = min; i <= max; i++){
			vb.set(i-min, this.get(i));
		}
		
		return vb.build();
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
		for(int i=0; i< this.columns; i++){
			sum = sum.add(this.get(i).multiplyBy(that.get(i)));
		}
		return sum;
	}

	public static class Builder extends Matrix.Builder{
		
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
			this.data[0][column] = value;
		}
		
		public void set(int column, double value){
			this.data[0][column] = new Complex(value, 0d);
		}
		
		public Complex get(int column){
			return this.data[0][column];
		}
		
		public Vector build(){
			return new Vector(data);
		}
	}

	public static Vector zeros(int size) {
		Builder b = new Builder(size);
		for(int i=0; i<size; i++){
			b.set(i, 0);
		}
		return b.build();
	}

	@Override
	public Iterator<Complex> iterator() {
		return Arrays.asList(data[0]).iterator();
	}
	
	private static Complex[] doubleToComplex(double[] doubles) {
		Complex[] result = new Complex[doubles.length];
		for(int i=0; i<doubles.length; i++){
			result[i] = new Complex(doubles[i], 0.0);
		}
		return result;
	}

	public double euclidianNorm() {
		return Math.sqrt(this.dot(this.conjugate().vector()).r());
	}

	public Vector normalize() {
		return this.divideBy(new Complex(this.euclidianNorm(), 0.0)).vector();
	}

}
