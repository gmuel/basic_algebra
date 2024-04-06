package field;

import ring.integer.IntRing;

/**
 * Basic floating point field - note by definition of precision
 * of floating point arithmetics, the set of all floating points 
 * is NOT a field, but for computation with less restrictive precision
 * demands might be handy...
 * @author adin
 *
 */
public class DoubleField extends AbstractField<DoubleField> implements Comparable<DoubleField>{
	/**the zero constant*/
	public static final DoubleField ZERO = new DoubleField();
	/**the one constant*/
	public static final DoubleField  ONE = new DoubleField(Rational.ONE);
	/**the minus one constant*/
	public static final DoubleField M_ONE= new DoubleField(Rational.M_ONE);
	/**the lowest possible element still strictly greater zero*/
	public static final DoubleField MIN_D= new DoubleField(Double.MIN_VALUE);
	/**the highest possible element - see {@link Double#POSITIVE_INFINITY}*/
	public static final DoubleField SUP_D= new DoubleField(Double.POSITIVE_INFINITY);
	/**the lowest psooible element - see {@link Double#NEGATIVE_INFINITY}*/
	public static final DoubleField INF_D= new DoubleField(Double.NEGATIVE_INFINITY);
	/**some precision bound: any two elements <tt>x, y</tt>,
	 * so that <tt>x.dist(y).multiply(x.abs().inverse()).compareTo(EPS_D)<=0</tt> returns true
	 * represent the same element*/
	public static final DoubleField EPS_D= new DoubleField(5e-16);
	/**the value field*/
	private double value;
	/**
	 * Constructs the zero element
	 */
	public DoubleField() {this(0);}
	/**
	 * Constructs the element <tt>value</tt>
	 * @param value the value of this element
	 */
	public DoubleField(double value){
		super();
		this.value = value;
	}
	/**
	 * Constructs the element <tt>value</tt>
	 * @param value the value of this element
	 */
	public DoubleField (IntRing value) {this(value.getValue());}
	/**
	 * Constructs the element <tt>value</tt>
	 * @param value the value of this element
	 */
	public DoubleField (Rational value){this(value.getValue());}

	public DoubleField dist(DoubleField another){
		if(this==null||another==null) return null;
		return add(another.addInverse()).abs();
	}
	
	public DoubleField inverse() {
		if(isZero()) throw new IllegalArgumentException("\nZero division");
		double one = 1, inv = 1/value, diff = Math.abs(value*inv-one);
		while (diff>5e-16) {
			inv += one-value*inv;
			diff = Math.abs(value*inv-one);}		
		return new DoubleField(inv);
	}

	public boolean isDiscrete(){return false;}
	
	public DoubleField addInverse() {return new DoubleField(-value);}

	public int compareTo(DoubleField another){
		return value<another.value?-1:value>another.value?1:0;
	}
	
	public boolean isZero() {return Math.abs(value)<1e-20?true:false;}

	
	public DoubleField multiply(DoubleField another) {return new DoubleField(value*another.value);}

	
	public DoubleField add(DoubleField another) {return new DoubleField(value+another.value);}

	public DoubleField abs (){return value>=0?this:new DoubleField(-value);}
	
	public boolean equals(DoubleField another) {
		if(this==another) return true;
		return Math.abs(value-another.value)<5e-16?true:false;
	}

	public double getValue(){return value;}
	
	public DoubleField constructOne (){return ONE;}
		
	public String toString(){return ""+value;}
	
	public static void main(String[] args){
		DoubleField val = new DoubleField(.0000000187023405456400037223223113123205);
		System.out.println(val.inverse());
	}

}
