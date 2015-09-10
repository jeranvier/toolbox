package jeranvier.math.linearAlgebra;

import jeranvier.math.util.Complex;

public class Matrix implements MatrixOperations<Matrix>{
	
	protected final Complex[][] data;
	private final int rows;
	protected final int columns;
	
	protected Matrix(Complex[][] data){
		this.data = data;
		this.rows = data.length;
		this.columns = data[0].length;
	}
	
	protected Matrix(Complex[] vectorData){
		this.rows = 1;
		this.columns = vectorData.length;
		this.data = new Complex[this.rows][this.columns];
		for(int i = 0; i<vectorData.length; i++){
			this.data[0][i] = vectorData[i];
		}
	}
	
	public Complex get(int row, int column){
		return this.data[row-1][column-1];
	}
	
	public double getRe(int row, int column){
		return this.data[row-1][column-1].a();
	}
	
	public Matrix transpose(){
		Builder mb = new Builder(columns, rows);
		
		for(int rowIndex=0; rowIndex<rows; rowIndex++){
			for(int columnIndex=0; columnIndex < columns; columnIndex++){
				mb.set(columnIndex+1, rowIndex+1, this.data[rowIndex][columnIndex]);
			}
		}
		
		return mb.build();
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(Complex[] row: data){
			for(Complex element: row){
				sb.append(element+"\t");
			}
			sb.deleteCharAt(sb.length()-1);
			sb.append("\n");
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
	
	protected boolean sameDimension(Matrix matrix, Matrix that) {
		return this.rows==that.rows && that.columns==that.columns;
	}

	@Override
	public Matrix add(Matrix that) throws IllegalArgumentException {
		if(!sameDimension(this, that)){
			throw new IllegalArgumentException("["+this.rows+","+this.columns+"] + ["+that.rows+","+that.columns+"]");
		}
		Matrix.Builder mb = new Matrix.Builder(rows, columns);
		for(int rowIndex=0; rowIndex<rows; rowIndex++){
			for(int columnIndex=0; columnIndex<columns; columnIndex++){
				mb.set(rowIndex+1, columnIndex+1, this.data[rowIndex][columnIndex].add(that.data[rowIndex][columnIndex]));
			}
		}
		return mb.build();
	}

	@Override
	public Matrix substract(Matrix that) throws IllegalArgumentException {
		if(!sameDimension(this, that)){
			throw new IllegalArgumentException("["+this.rows+","+this.columns+"] - ["+that.rows+","+that.columns+"]");
		}
		Matrix.Builder mb = new Matrix.Builder(rows, columns);
		for(int rowIndex=0; rowIndex<rows; rowIndex++){
			for(int columnIndex=0; columnIndex<columns; columnIndex++){
				mb.set(rowIndex+1, columnIndex+1, this.data[rowIndex][columnIndex].substract(that.data[rowIndex][columnIndex]));
			}
		}
		return mb.build();
	}
	
	@Override
	public Matrix entrywiseMultiplyBy(Matrix that) throws IllegalArgumentException {
		if(!sameDimension(this, that)){
			throw new IllegalArgumentException("["+this.rows+","+this.columns+"] .x ["+that.rows+","+that.columns+"]");
		}
		Matrix.Builder mb = new Matrix.Builder(rows, columns);
		for(int rowIndex=0; rowIndex<rows; rowIndex++){
			for(int columnIndex=0; columnIndex<columns; columnIndex++){
				mb.set(rowIndex+1, columnIndex+1, this.data[rowIndex][columnIndex].multiplyBy(that.data[rowIndex][columnIndex]));
			}
		}
		return mb.build();
	}

	@Override
	public Matrix multiplyBy(Matrix that) throws IllegalArgumentException {
		if(this.columns != that.rows){
			throw new IllegalArgumentException("["+this.rows+","+this.columns+"] x ["+that.rows+","+that.columns+"]");
		}
		Matrix.Builder mb = new Matrix.Builder(this.rows, that.columns);
		for(int i=0; i<this.rows; i++){
			for(int j=0; j<that.columns; j++){
				Complex c = new Complex(0, 0);
				for(int k=0; k<this.columns; k++){
					c = c.add(this.data[i][k].multiplyBy(that.data[k][j]));
				}
				mb.set(i+1, j+1, c);
			}
		}
		return mb.build();
	}

	@Override
	public Matrix divideBy(Matrix that) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Matrix conjugate() {
		Matrix.Builder mb = new Matrix.Builder(rows, columns);
		for(int rowIndex=0; rowIndex<rows; rowIndex++){
			for(int columnIndex=0; columnIndex<columns; columnIndex++){
				mb.set(rowIndex+1, columnIndex+1, this.data[rowIndex][columnIndex].conjugate());
			}
		}
		return mb.build();
	}
	
	@Override
	public Matrix hermitianTranspose() {
		Matrix.Builder mb = new Matrix.Builder(columns, rows);
		for(int rowIndex=0; rowIndex<rows; rowIndex++){
			for(int columnIndex=0; columnIndex<columns; columnIndex++){
				mb.set(columnIndex+1, rowIndex+1, this.data[rowIndex][columnIndex].conjugate());
			}
		}
		return mb.build();
	}

	@Override
	public Matrix add(Complex n){
		Matrix.Builder mb = new Matrix.Builder(rows, columns);
		for(int rowIndex=0; rowIndex<rows; rowIndex++){
			for(int columnIndex=0; columnIndex<columns; columnIndex++){
				mb.set(rowIndex+1, columnIndex+1, this.data[rowIndex][columnIndex].add(n));
			}
		}
		return mb.build();
	}

	@Override
	public Matrix substract(Complex n){
		Matrix.Builder mb = new Matrix.Builder(rows, columns);
		for(int rowIndex=0; rowIndex<rows; rowIndex++){
			for(int columnIndex=0; columnIndex<columns; columnIndex++){
				mb.set(rowIndex+1, columnIndex+1, this.data[rowIndex][columnIndex].substract(n));
			}
		}
		return mb.build();
	}

	@Override
	public Matrix multiplyBy(Complex n){
		Matrix.Builder mb = new Matrix.Builder(rows, columns);
		for(int rowIndex=0; rowIndex<rows; rowIndex++){
			for(int columnIndex=0; columnIndex<columns; columnIndex++){
				mb.set(rowIndex+1, columnIndex+1, this.data[rowIndex][columnIndex].multiplyBy(n));
			}
		}
		return mb.build();
	}

	@Override
	public Matrix divideBy(Complex n) throws IllegalArgumentException {
		Matrix.Builder mb = new Matrix.Builder(rows, columns);
		for(int rowIndex=0; rowIndex<rows; rowIndex++){
			for(int columnIndex=0; columnIndex<columns; columnIndex++){
				mb.set(rowIndex+1, columnIndex+1, this.data[rowIndex][columnIndex].divideBy(n));
			}
		}
		return mb.build();
	}
	
	@Override
	public Complex determinant() throws IllegalArgumentException{
		throw new IllegalArgumentException("error in the method. Matematically incorrect");
//		if(this.rows != this.columns){
//			throw new IllegalArgumentException("Matrix not squared");
//		}
//		Matrix lowerTrangular = computerLowerTriangular();
//		Complex product = new Complex(1, 0);
//		for(int i=0; i<this.rows; i++){
//			product = product.multiplyBy(lowerTrangular.data[i][i]);
//		}
//		return product;
	}
	
	public Matrix computerLowerTriangular() {
		Builder mb = new Builder(this.data);
		for(int i=2; i<=mb.columns; i++){
			for(int k=1; k<i; k++){
				Complex coeff = mb.get(k,i).divideBy(mb.get(k, k));
				for(int j=1; j<=mb.rows; j++){
					mb.set(j, i, mb.get(j, i).substract(coeff.multiplyBy(mb.get(j, k))));
				}
			}
		}		
		return mb.build();
	}
	
	public static Matrix identity(int columns) {
		Builder builder = new Builder(columns, columns);
		for(int i = 1; i <= columns; i++){
			for(int j = 1; j <= columns; j++){
				if(i==j){
					builder.set(i, j, 1);					
				}else{
					builder.set(i, j, 0);
				}
			}
		}
		return builder.build();
	}
	
	public Double[][] re() {
		Double[][] re = new Double[data.length][data[0].length];
		for(int i = 0; i <data.length; i++){
			for(int j = 0; j< data[0].length; j++){
				re[i][j] = data[i][j].re();
			}
		}
		return re;
	}

	public static class Builder{
		protected Complex[][] data;
		protected int rows;
		protected int columns;
		
		public Builder(int rows, int columns){
			this.data = new Complex[rows][columns];
			this.rows = rows;
			this.columns = columns;
		}
		
		public Builder(Complex[][] data){
			this.data = data;
			this.rows = data.length;
			this.columns = data[0].length;
		}
		
		public void set(int row, int column, Complex value){
			this.data[row-1][column-1] = value;
		}
		
		public void set(int row, int column, double value){
			this.data[row-1][column-1] = new Complex(value, 0d);
		}
		
		public Complex get(int row, int column){
			return this.data[row-1][column-1];
		}
		
		public Matrix build(){
			return new Matrix(data);
		}

		public void setAll(Complex[] values) {
			if(values.length != rows*columns){
				throw new IllegalArgumentException("Wrong number of elements. got "+values.length+", expected "+rows*columns);
			}
			
			int i = 0;
			for(int rowIndex=0; rowIndex<rows; rowIndex++){
				for(int columnIndex=0; columnIndex<columns; columnIndex++){
					data[rowIndex][columnIndex] = values[i];
					i++;
				}
			}
		}

		public void setAll(double[] values) {
			if(values.length != rows*columns){
				throw new IllegalArgumentException("Wrong number of elements. got "+values.length+", expected "+rows*columns);
			}
			
			int i = 0;
			for(int rowIndex=0; rowIndex<rows; rowIndex++){
				for(int columnIndex=0; columnIndex<columns; columnIndex++){
					data[rowIndex][columnIndex] = new Complex(values[i], 0);
					i++;
				}
			}
		}
	}

	public Complex[][] data() {
		return this.data;
	}

	public Vector vector() {
		if(this.rows != 1){
			throw new IllegalArgumentException("The matrix cannot be cast as a vector ("+this.rows+"x"+this.columns+")");
		}
		return new Vector(data);
	}
}
