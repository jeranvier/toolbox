package jeranvier.math.util;

import java.text.DecimalFormat;

public class Complex{
	
	private final double a;
	private final double b;
	private static final DecimalFormat formatter = new DecimalFormat("###0.###");

	
	public Complex(double a, double b){
		this.a = a ;
		this.b = b;
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
	
	public boolean isReal(){
		return Double.compare(b+0d, 0d) == 0;
	}
	
	@Override
	public String toString(){
		return formatter.format(a)+(!isReal()?("+i"+formatter.format(b)):"");
	}

	public Complex substract(Complex that) {
		return new Complex(this.a-that.a, this.b-that.b);
	}

	public Complex add(Complex that) {
		return new Complex(this.a+that.a, this.b+that.b);
	}

	public Complex multiplyBy(Complex that) {
		return new Complex(this.a*that.a - this.b*that.b, this.b*that.a + this.a*that.b);	
	}

	public Complex divideBy(Complex denominator) {
		return new Complex((this.a*denominator.a + this.b*denominator.b)/(denominator.a*denominator.a + denominator.b*denominator.b)
				,(this.b*denominator.a - this.a*denominator.b)/(denominator.a*denominator.a + denominator.b*denominator.b));
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
