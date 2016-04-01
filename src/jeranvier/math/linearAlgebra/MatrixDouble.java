package jeranvier.math.linearAlgebra;

import jeranvier.math.util.Complex;

public class MatrixDouble{
	
	protected final double[][] data;
	private final int rows;
	protected final int columns;
	
	protected MatrixDouble(double[][] data){
		this.data = data;
		this.rows = data.length;
		this.columns = data[0].length;
	}
	
	protected MatrixDouble(double[] vectorData){
		this.rows = 1;
		this.columns = vectorData.length;
		this.data = new double[this.rows][this.columns];
		for(int i = 0; i<vectorData.length; i++){
			this.data[0][i] = vectorData[i];
		}
	}
	
	public double get(int row, int column){
		return this.data[row-1][column-1];
	}
	
	public MatrixDouble transpose(){
		double[][]  data = new double[this.columns][this.rows];
		for(int rowIndex=0; rowIndex<rows; rowIndex++){
			for(int columnIndex=0; columnIndex < columns; columnIndex++){
				data[columnIndex][rowIndex] =this.data[rowIndex][columnIndex];
			}
		}		
		return new MatrixDouble(data);
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(double[] row: data){
			for(double element: row){
				sb.append(element+"\t");
			}
			sb.deleteCharAt(sb.length()-1);
			sb.append("\n");
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
	
	protected boolean sameDimension(MatrixDouble matrix, MatrixDouble that) {
		return this.rows==that.rows && that.columns==that.columns;
	}

	public MatrixDouble add(MatrixDouble that) throws IllegalArgumentException {
		if(!sameDimension(this, that)){
			throw new IllegalArgumentException("["+this.rows+","+this.columns+"] + ["+that.rows+","+that.columns+"]");
		}
		MatrixDouble.Builder mb = new MatrixDouble.Builder(rows, columns);
		for(int rowIndex=0; rowIndex<rows; rowIndex++){
			for(int columnIndex=0; columnIndex<columns; columnIndex++){
				mb.set(rowIndex+1, columnIndex+1, this.data[rowIndex][columnIndex] +that.data[rowIndex][columnIndex]);
			}
		}
		return mb.build();
	}

	public MatrixDouble substract(MatrixDouble that) throws IllegalArgumentException {
		if(!sameDimension(this, that)){
			throw new IllegalArgumentException("["+this.rows+","+this.columns+"] - ["+that.rows+","+that.columns+"]");
		}
		MatrixDouble.Builder mb = new MatrixDouble.Builder(rows, columns);
		for(int rowIndex=0; rowIndex<rows; rowIndex++){
			for(int columnIndex=0; columnIndex<columns; columnIndex++){
				mb.set(rowIndex+1, columnIndex+1, this.data[rowIndex][columnIndex]-that.data[rowIndex][columnIndex]);
			}
		}
		return mb.build();
	}
	
	public MatrixDouble entrywiseMultiplyBy(MatrixDouble that) throws IllegalArgumentException {
		if(!sameDimension(this, that)){
			throw new IllegalArgumentException("["+this.rows+","+this.columns+"] .x ["+that.rows+","+that.columns+"]");
		}
		MatrixDouble.Builder mb = new MatrixDouble.Builder(rows, columns);
		for(int rowIndex=0; rowIndex<rows; rowIndex++){
			for(int columnIndex=0; columnIndex<columns; columnIndex++){
				mb.set(rowIndex+1, columnIndex+1, this.data[rowIndex][columnIndex]*that.data[rowIndex][columnIndex]);
			}
		}
		return mb.build();
	}

	public MatrixDouble multiplyBy(MatrixDouble that) throws IllegalArgumentException {
		if(this.columns != that.rows){
			throw new IllegalArgumentException("["+this.rows+","+this.columns+"] x ["+that.rows+","+that.columns+"]");
		}
		MatrixDouble.Builder mb = new MatrixDouble.Builder(this.rows, that.columns);
		for(int i=0; i<this.rows; i++){
			for(int j=0; j<that.columns; j++){
				double c = 0.0;
				for(int k=0; k<this.columns; k++){
					c = c+this.data[i][k]*that.data[k][j];
				}
				mb.set(i+1, j+1, c);
			}
		}
		return mb.build();
	}

	public MatrixDouble divideBy(MatrixDouble that) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	public MatrixDouble add(double n){
		MatrixDouble.Builder mb = new MatrixDouble.Builder(rows, columns);
		for(int rowIndex=0; rowIndex<rows; rowIndex++){
			for(int columnIndex=0; columnIndex<columns; columnIndex++){
				mb.set(rowIndex+1, columnIndex+1, this.data[rowIndex][columnIndex]+n);
			}
		}
		return mb.build();
	}

	public MatrixDouble substract(double n){
		MatrixDouble.Builder mb = new MatrixDouble.Builder(rows, columns);
		for(int rowIndex=0; rowIndex<rows; rowIndex++){
			for(int columnIndex=0; columnIndex<columns; columnIndex++){
				mb.set(rowIndex+1, columnIndex+1, this.data[rowIndex][columnIndex]-n);
			}
		}
		return mb.build();
	}

	public MatrixDouble multiplyBy(double n){
		MatrixDouble.Builder mb = new MatrixDouble.Builder(rows, columns);
		for(int rowIndex=0; rowIndex<rows; rowIndex++){
			for(int columnIndex=0; columnIndex<columns; columnIndex++){
				mb.set(rowIndex+1, columnIndex+1, this.data[rowIndex][columnIndex]*n);
			}
		}
		return mb.build();
	}

	public MatrixDouble divideBy(double n) throws IllegalArgumentException {
		MatrixDouble.Builder mb = new MatrixDouble.Builder(rows, columns);
		for(int rowIndex=0; rowIndex<rows; rowIndex++){
			for(int columnIndex=0; columnIndex<columns; columnIndex++){
				mb.set(rowIndex+1, columnIndex+1, this.data[rowIndex][columnIndex]/n);
			}
		}
		return mb.build();
	}
	
	public static MatrixDouble identity(int columns) {
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

	public static class Builder{
		protected double[][] data;
		protected int rows;
		protected int columns;
		
		public Builder(int rows, int columns){
			this.data = new double[rows][columns];
			this.rows = rows;
			this.columns = columns;
		}
		
		public Builder(double[][] data){
			this.data = data;
			this.rows = data.length;
			this.columns = data[0].length;
		}
		
		public void set(int row, int column, double value){
			this.data[row-1][column-1] = value;
		}
		
		public double get(int row, int column){
			return this.data[row-1][column-1];
		}
		
		public MatrixDouble build(){
			return new MatrixDouble(data);
		}

		public void setAll(double[] values) {
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

	}

	public double[][] data() {
		return this.data;
	}
}
