package group;

import java.util.Comparator;

import ring.integer.IntRing;
/**
 * The class of all non negative integers: each instance of this
 * class is immutable
 * @author adin
 *
 */
public class NNegInt implements CompMonoid<NNegInt>, IndicialMonoid<NNegInt> {
	/**the zero element*/
	public static final NNegInt ZERO = new NNegInt ();
	/**the one element: the monoid generator*/
	public static final NNegInt  ONE = new NNegInt (1);
	
	public static final NNegInt  TWO = new NNegInt (2);
	/**the maximal value: no element can be greater than this element*/
	public static final NNegInt MAX_VALUE = new NNegInt(Long.MAX_VALUE);
	/**
	 * The canonical comparator: will return any sequence of
	 * objects of outer class in an ascending order:
	 * <br />to specify: <tt>compare(n,n.operate(k))</tt> returns
	 * a non positive integer for all pairs <tt>(n,k)</tt> of the outer
	 * class
	 */
	private static final Comparator<NNegInt>  ASCENDING = new Comparator<NNegInt>(){
		public int compare(NNegInt o1, NNegInt o2) {
			if(o1.equals(o2)) return 0;
			long v1 = o1.val.getValue(), v2 = o2.val.getValue();
			return v1<v2?-1:1;
		}
	};
	/**sorting flag: */
	private boolean isAscending = true;
	/**the value*/
	private IntRing val;
	/**
	 * Constructs the zero element
	 */
	private NNegInt() {this(0);}
	/**
	 * Constructs the element of
	 * value <tt>val</tt>
	 * @param val the value
	 */
	public NNegInt(long val) {this(new IntRing(val));}
	/**
	 * Constructs a
	 * @param val
	 * @throws IllegalArgumentException
	 */
	public NNegInt(IntRing val) throws IllegalArgumentException {
		if(val.compareTo(IntRing.ZERO)<0) throw new IllegalArgumentException("\nNegative integers not supported...");
		this.val = val;
	}
	public NNegInt(NNegInt val){this(val.val);}
	public NNegInt operate(NNegInt another) {
		NNegInt op = new NNegInt(val.add(another.val));
		op.isAscending = isAscending;
		return op;
	}

	public boolean equals(Object o){
		if(this==o) return true;
		if(!(o instanceof NNegInt)) return false;
		return equals((NNegInt) o);
	}
	
	public boolean equals(NNegInt another) {return val.equals(another.val)?true:false;}

	public long eval(){return val.getValue();}
	
	
	public int compareTo(NNegInt arg0) {
		if(isAscending==arg0.isAscending){
			if(isAscending){
				int comp = ASCENDING.compare(this, arg0);
				return comp;
			}
			int comp = ASCENDING.compare(arg0, this);
			return comp;
		} else {
			NNegInt cp = new NNegInt(arg0);
			cp.isAscending = isAscending;
			return compareTo(cp);
		}
		
	}
	/**
	 * Returns the element <tt>this - 1</tt> or null
	 * if  <tt>this.equals(NNegInt.ZERO)</tt> returns true
	 * @return the decremented value or null
	 */
	public NNegInt decrement(){return val.compareTo(IntRing.ZERO)>0?diff(NNegInt.ONE):null;}
	/**
	 * Returns the absolute difference of the two non negative numbers
	 * @param another
	 * @return
	 */
	public NNegInt diff(NNegInt another){
		return new NNegInt(val.add(another.val.addInverse()).abs());
	}
	/**
	 * Returns the divisor <tt>k</tt> such that
	 * <tt>this.equals(another.multiply(k).operate(this.mod(another)))</tt> returns
	 * true
	 * @param another
	 * @return
	 */
	public NNegInt div (NNegInt another){return new NNegInt(val.div(another.val));} 
	public IntRing getValue(){return val;}
	public int hashCode(){return val.hashCode();}
	public boolean isDiscrete(){return true;}
	/**
	 * Returns the incremented value <tt>this + 1</tt>
	 * @return the incremented value
	 */
	public NNegInt increment (){return operate(NNegInt.ONE);}
	/**
	 * Returns the ascending flag: to specify the boolean
	 * flag indicating whether this object will be compared in
	 * an ascending fashion (greater elements always return
	 * negative, smaller elements return positive integers) or 
	 * in the opposite fashion (i.e. descending).
	 * <br />
	 * <b>Note</b>, that in the ascending mode a minimal element exists - but no
	 * maximal. On the other hand, in descending mode only a maximal element exists,
	 * but no minimal (technically speaking...).
	 * @return true if this object will be compared in an ascending fashion
	 */
	public boolean isAscending(){return isAscending?true:false;}
	public boolean isComparable(){return true;}
	public boolean isEven (){return val.mod(IntRing.TWO).equals(IntRing.ZERO);}
	public boolean isNeutral(){return equals(ZERO);}
	public NNegInt mod(NNegInt another){return new NNegInt(val.mod(another.val));}
	public NNegInt multiply (NNegInt another){return new NNegInt(val.multiply(another.val));}
	/**
	 * Sets the comparison flag to <tt>isAscending</tt>, to specify the boolean
	 * flag indicating whether this object will be compared in
	 * an ascending fashion (greater elements always return
	 * negative, smaller elements return positive integers) or 
	 * in the opposite fashion (i.e. descending).
	 * <br />
	 * <b>Note</b>, that in the ascending mode a minimal element exists - but no
	 * maximal. On the other hand, in descending mode only a maximal element exists,
	 * but no minimal (technically speaking...). 
	 * @param isAscending
	 */
	public void setAscending(boolean isAscending){this.isAscending = isAscending;}
	public String toString (){return val.toString();}
}
