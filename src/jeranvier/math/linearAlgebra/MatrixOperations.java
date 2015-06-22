package jeranvier.math.linearAlgebra;

import jeranvier.math.util.Complex;

public interface MatrixOperations<T>{
	public T add( T that) throws IllegalArgumentException;
	public T substract( T that) throws IllegalArgumentException;
	public T multiplyBy( T that) throws IllegalArgumentException;
	public T divideBy( T that) throws IllegalArgumentException;
	public T add(Complex n);
	public T substract(Complex n);
	public T multiplyBy(Complex n);
	public T divideBy(Complex n) throws IllegalArgumentException;
	public T transpose();
	public T conjugate();
	public T hermitianTranspose();
	public Complex determinant() throws IllegalArgumentException;
	public T entrywiseMultiplyBy(T that) throws IllegalArgumentException;
}
