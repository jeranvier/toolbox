package jeranvier.math.util;

import java.text.DecimalFormat;

public class Complex{
	private static final double DELTA = 1e-15;
	private final double a;
	private final double b;
	private static final DecimalFormat formatter = new DecimalFormat("###0.###");

	
	public Complex(double a, double b){
		this.a = a ;
		this.b = b;
	}
	
	public Complex(){
		this.a = 0 ;
		this.b = 0;
	}
	
	public double a(){
		return this.a;
	}
	
	public double b(){
		return this.b;
	}
	
	public double re(){
		return this.a;
	}
	
	public double im(){
		return this.b;
	}
	
	public double r(){
		return Math.sqrt(a*a+b*b);
	}
	
	public double phi(){
		return Math.atan2(b, a);
	}
	
	public boolean isReal(){
		return b*b < DELTA;
	}
	
	@Override
	public String toString(){
		return formatter.format(a)+(!isReal()?("+"+formatter.format(b)+"i"):"");
	}

	public Complex substract(Complex that) {
		return new Complex(this.a-that.a, this.b-that.b);
	}

	public Complex add(Complex that) {
		return new Complex(this.a+that.a, this.b+that.b);
	}
	
	public Complex add(double a, double b) {
		return new Complex(this.a+a, this.b+b);
	}

	public Complex multiplyBy(Complex that) {
		return new Complex(this.a*that.a - this.b*that.b, this.b*that.a + this.a*that.b);	
	}

	public Complex divideBy(Complex denominator) {
		return new Complex((this.a*denominator.a + this.b*denominator.b)/(denominator.a*denominator.a + denominator.b*denominator.b)
				,(this.b*denominator.a - this.a*denominator.b)/(denominator.a*denominator.a + denominator.b*denominator.b));
	}
	
	public Complex conjugate() {
		return new Complex(a , -1*b);
	}

	public Complex add(double n) {
		return new Complex(this.a+n, this.b);
	}

	public Complex substract(double n) {
		return new Complex(this.a-n, this.b);
	}

	public Complex multiplyBy(double n) {
		return new Complex(this.a*n, this.b*n);
	}

	public Complex divideBy(double n) {
		return new Complex(this.a/n, this.b/n);
	}

}
