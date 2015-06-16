package jeranvier.math.linearAlgebra;

import jeranvier.math.util.Complex;

public interface MatrixOperations<T>{
	public T add( T that) throws IllegalArgumentException;
	public T substract( T that) throws IllegalArgumentException;
	public T multiplyBy( T that) throws IllegalArgumentException;
	public T divideBy( T that) throws IllegalArgumentException;
	public T add(double n);
	public T substract(double n);
	public T multiplyBy(double n);
	public T divideBy(double n) throws IllegalArgumentException;
	public T transpose();
	public Complex determinant() throws IllegalArgumentException;
}
