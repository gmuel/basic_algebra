package ring.integer;

import group.IndicialMonoid;
import group.NNegInt;
import group.UnitGroup;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import ring.PID;
import ring.UnitaryCommRing;

import module.CommModule;
import module.FiniteGenMod;


/**
 * The basic ring of all integers - any object of this class
 * represents an immutable object. Therefore any instance of
 * this class may serve as a {@link java.util.Map} key
 * @author adin
 *
 */
public class IntRing extends UnitaryCommRing<IntRing> implements Comparable<IntRing>, IndicialMonoid<IntRing>, PID<IntRing>, FiniteGenMod<IntRing,IntRing,IntRing> {
	/**the zero element*/
	public static final IntRing  ZERO = new IntRing ();
	/**the one element*/
	public static final IntRing   ONE = new IntRing(1);
	/**the minus one element*/
	public static final IntRing M_ONE = new IntRing (-1);
	/***/
	public static final IntRing   TWO = new IntRing (2);
	public static final UnitGroup<IntRing> U_ONE = new UnitGroup<IntRing>(ONE);
	public static final UnitGroup<IntRing> U_M_ONE = new UnitGroup<IntRing>(M_ONE);
	/**ordering reversion flag*/
	private static boolean REVERSE_ORDERING = true;
	/**the value*/
	private long value;
	/**
	 * Constructs the zero element
	 */
	public IntRing (){super();}
	/**
	 * Constructs the <code>IntRing</code> object
	 * of value <tt>value</tt>
	 * @param value
	 */
	public IntRing (int value){
		this((long) value);
	}
	/**
	 * Constructs the <code>IntRing</code> object
	 * of value <tt>value</tt>
	 * @param value
	 */
	public IntRing (long value){
		this();
		if(value!=0){
			this.value = value;
			isZero = false;
		}
	}
	/**
	 * Copies the argument <tt>another</tt>
	 * @param another
	 */
	public IntRing (IntRing another){
		this(another.value);
	}
	public IntRing add(IntRing another) {
		if(this==null||another==null) {
			if(this==null&&another!=null) throw new NullPointerException ("\nFirst argument of addition method not initialized!");
			else if(this!=null&&another==null) throw new NullPointerException ("\nSecond argument of addition method not initialized!");
			else throw new NullPointerException ("\nBoth arguments of addition method not initialized!");
		}
		if(isZero&&another.isZero) return new IntRing ();
		if(isZero&&!another.isZero) return new IntRing (another);
		if(!isZero&&another.isZero) return new IntRing (this);
		
		return new IntRing (value+another.value);
	}

	public IntRing abs(){return value>=0?new IntRing(this):addInverse();}
	
	public IntRing addInverse() {
		return isZero?new IntRing():new IntRing(-value);
	}
	public int compareTo (IntRing another){
		if(value<another.value) return REVERSE_ORDERING?-1:1;
		return value>another.value?REVERSE_ORDERING?1:-1:0;
	}
	/**
	 * Returns the factor <tt>factor</tt> in the modulus equation
	 * <p><tt>this = factor * another + residual</tt>
	 * @param another
	 * @return
	 */
	public IntRing div (IntRing another){
		IntRing mod = mod(another);
		IntRing diff = null;
		if(value>=0) diff = add(mod.addInverse());
		else diff = add(mod);
		return new IntRing ((int) Math.floor(((double) diff.value)/((double) another.value))); 
	}
	@Override
	public boolean equals (Object o){
		if(this==o) return true;
		if(!(o instanceof IntRing)) return false;
		return ((IntRing) o).value-value==0?true:false;
	}
	public long eval(){return value;}
	public boolean equals (IntRing another){return add(another.addInverse()).isZero();}
	/**
	 * Returns the greatest common divisor of <tt>this</tt> and
	 * <tt>another</tt> <code>IntRing</code> element, that is equivalent
	 * to one if both do not share any divisor or some positive integer
	 * different from one.
	 * <p>The conventions is that the <tt>gcd</tt> is strictly positive (if at least one argument is non-zero) and
	 * <br /><code>this.gcd(IntRing.ZERO).equals(this)</code> returns true for any non-negative
	 * integer as well as 
	 * <br /><code>this.gcd(IntRing.ONE).equals(IntRing.ONE)</code> returns true for any non-zero element
	 * @param another another <code>IntRing</code> element
	 * @return the greatest common divisor
	 */
	public IntRing gcd (IntRing another){
		IntRing val1 = null;
		IntRing val2 = null;
		if(value>=0) val1 = this;
		else val1 = addInverse();
		if(another.value>=0) val2 = another;
		else val2 = another.addInverse();
		int comp = val1.compareTo(val2);
		if(comp==-1){
			if(val1.value==0) return new IntRing(val2);
			if(val1.value==1) return new IntRing(1);
			IntRing mod = val2.mod(val1);
			if(mod.value==0) return new IntRing(val1);
			if(mod.value==1) return new IntRing(1);
			return gcd(mod);
		}
		if(comp==1){
			if(val2.value==0) return new IntRing(val1);
			if(val2.value==1) return new IntRing(1);
			IntRing mod = val1.mod(val2);
			if(mod.value==0) return new IntRing(val2);
			if(mod.value==1) return new IntRing(1);
			return another.gcd(mod);
		}
		return new IntRing (val1);
	}
	public IntRing getOne (){return ONE;}
	public long getValue (){return value;} 
	public UnitGroup<IntRing> getUnit(){return equals(ONE)?U_ONE:equals(M_ONE)?U_M_ONE:null;}
	@Override
	public int hashCode (){return new Long(value).hashCode();}
	public IntRing inverse (){return equals(ONE)?ONE:equals(M_ONE)?M_ONE:null;}
	public boolean isComparable(){return true;}
	public boolean isDiscrete(){return true;}
	public boolean isNeutral(){return equals(ZERO);}
	public boolean isUnit(){return equals(ONE)||equals(M_ONE)?true:false;}
	public boolean isZero() {return isZero;}

	public IntRing mod (IntRing another){
		if(another.isZero) return this;
		if(another.value==1||another.value==-1) return ZERO;
		if(equals(another)||equals(another.addInverse())) return ZERO;
		if(another.value>0){
			if(value>0){
				long start = value;
				while (start>=another.value) start = start-another.value;
				return new IntRing(start);
			}
			long start = value;
			while (start<0) start = start+another.value;
			return new IntRing(start);
		}
		return mod(another.addInverse());
	}
	
	public IntRing multiply(IntRing another) {
		if(this==null||another==null) {
			if(this==null&&another!=null) throw new NullPointerException ("\nFirst argument of addition method not initialized!");
			else if(this!=null&&another==null) throw new NullPointerException ("\nSecond argument of addition method not initialized!");
			else throw new NullPointerException ("\nBoth arguments of addition method not initialized!");
		}
		if(isZero||another.isZero) return new IntRing ();
		return new IntRing (value*another.value);
	}
	/**
	 * Returns the power <tt>this^exp</tt>
	 * @param exp the exponent
	 * @return the power
	 * @throws IllegalArgumentException negative exponent
	 */
	public IntRing pow (int exp) throws IllegalArgumentException {
		if(exp<0) throw new IllegalArgumentException ("\nNegative indices NOT permitted: "+exp);
		IntRing pow = new IntRing (1);
		int count = 0;
		while (count<exp) {
			pow = multiply(pow);
			count++;
		}
		return pow;
	}
	public IntRing pow (NNegInt exp) {
		if(exp.equals(NNegInt.ZERO)) return isZero()?ZERO:ONE;
		if(exp.equals(NNegInt.ONE)) return this;
		NNegInt p = NNegInt.ZERO;
		IntRing pow=ONE;
		while(p.compareTo(exp)<0){
			pow = multiply(pow);
			p = p.increment();
		}
		return pow;
	}
	@Override
	public String toString (){return ""+value;}
	/**
	 * Reverses the natural ordering on the long values. To test the
	 * current ordering schema, simply check the return value of
	 * <br /><tt>IntRing.ZERO.compareTo(IntRing.ONE)</tt> - by default returns -1
	 * <br /><br /><b>Note</b>, that applying this method twice, leaves the ordering flag unchanged
	 */
	public static synchronized void reverseOrdering (){
		REVERSE_ORDERING = !REVERSE_ORDERING;
	}
	/**
	 * Returns a <code>java.util.TreeSet</code> of all prime numbers <tt>p</tt>
	 * in range <tt>(start,end)</tt>, to specify:
	 * <br /><tt>(new IntRing(start)).compareTo(p)<=0&&(new IntRing(end)).compareTo(p)==1</tt> returns true
	 * @param start the lower interval bound
	 * @param end the upper (exclusive) interval bound
	 * @return a set of prime numbers in range <tt>
	 * @throws IllegalArgumentException if one of the following holds:
	 * <ol><li>the start argument is less or equal to one or</li>
	 * <li>is equal to or exceeds end argument</li></ol>
	 */
	public static TreeSet<IntRing> primes (long start, long end) throws IllegalArgumentException {
		return primes(new IntRing(start),new IntRing(end));
	}
	/**
	 * Returns a <code>java.util.TreeSet</code> of all prime numbers <tt>p</tt>
	 * in range <tt>(start,end)</tt>, to specify:
	 * <br /><tt>(new IntRing(start)).compareTo(p)<=0&&(new IntRing(end)).compareTo(p)==1</tt> returns true
	 * @param start the lower interval bound
	 * @param end the upper (exclusive) interval bound
	 * @return a set of prime numbers in range <tt>[start,end]</tt>
	 * @throws IllegalArgumentException if one of the following holds:
	 * <ol><li>the start argument is less or equal to one or</li>
	 * <li>is equal to or exceeds end argument</li></ol>
	 */
	public static TreeSet<IntRing> primes (IntRing start, IntRing end) throws IllegalArgumentException {
		if(!REVERSE_ORDERING) REVERSE_ORDERING = true;
		if(end.compareTo(ONE)<=0) throw new IllegalArgumentException ("\nSecond argument must always be greater than one!");
		if(start.compareTo(ONE)<=0) throw new IllegalArgumentException ("\nFirst argument must always be greater than one!");
		if(start.compareTo(end)>0) throw new IllegalArgumentException (String.format("\nFirst argument must be strictly smaller than second argument\nstart = %1$s\t end = %2$s",start,end));
		TreeSet<IntRing> noPrimes = new TreeSet<IntRing> ();
		long sqrtN = (long) Math.floor(Math.sqrt(end.value));
		for (long i = 2; i <= sqrtN;i++) {
			for (long j = 2*i; j < end.value; j+=i){
				noPrimes.add(new IntRing(j));
			}
		}
		TreeSet<IntRing> primes = new TreeSet<IntRing> ();
		IntRing loBound = new IntRing(start);
		for (long i = 0; i < end.value; i++) {IntRing val = new IntRing(i);if(!noPrimes.contains(val)&&loBound.compareTo(val)<=0) primes.add(val);}
		return primes;
	}
	public static TreeMap<IntRing,NNegInt> primeDivisors(long element){return primeDivisors(new IntRing(element));}
	public static TreeMap<IntRing,NNegInt> primeDivisors(IntRing element){
		TreeSet<IntRing> primes = primes(TWO,element.add(IntRing.ONE));
		TreeMap<IntRing,NNegInt> primeDiv = new TreeMap<IntRing,NNegInt>();
		for (IntRing prime:primes){
			NNegInt exp = NNegInt.ZERO;
			IntRing pow = ONE;
			boolean divides = element.mod(prime).equals(ZERO);
			if(!divides) continue;
			while(true){
				pow = prime.multiply(pow);
				divides = element.mod(pow).equals(ZERO);
				if(!divides) break;
				exp = exp.increment();
			}
			primeDiv.put(prime, exp);
		}
		return primeDiv;
	}
	public static TreeSet<IntRing> divisors(IntRing val){
		TreeMap<IntRing,NNegInt> primeDivs = primeDivisors(val);
		TreeSet<IntRing> divisors = new TreeSet<IntRing> ();
		divisors.addAll(primeDivs.keySet());
		IntRing[] primeList = new IntRing[primeDivs.size()];
		int counter = 0;
		for (Entry<IntRing,NNegInt> entry:primeDivs.entrySet()){
			primeList[counter] = entry.getKey();
			counter++;
		}
		//while ()
		return divisors;
	}
	public static void main (String[] args){
		IntRing v1 = new IntRing (-12);
		IntRing v2 = new IntRing (8);
		IntRing v3 = new IntRing (9);
		IntRing v4 = new IntRing (-8);
		System.out.println(v1.mod(v2));
		System.out.println(v1.mod(v3));
		System.out.println(v1.mod(v4));
		System.out.println(v1.gcd(v2));
		System.out.println(v1.gcd(v3));
		System.out.println(v1.gcd(v4));
		System.out.println(v1.div(v2));
		System.out.println(v1.div(v3));
		System.out.println(v1.div(v4));
		System.out.println(v1.pow(4));
		System.out.println(primeDivisors(20736));
	}
	/**
	 * Leaves the element unaltered
	 */
	public void clear() {}
	/**
	 * Returns null
	 * @param index some index
	 * @return null
	 */
	public IntRing getValue(IntRing index) {return this;}
	/**
	 * Returns null
	 * @param val some value
	 * @return null
	 */
	public IntRing getIndex(IntRing val) {return ZERO;}
	/**
	 * Returns always null - no remove operation implemented
	 * @param index some index
	 * @return null
	 */
	public IntRing remove(IntRing index) {return null;}
	/**
	 * Sets nothing
	 */
	public void setEntry(IntRing index, IntRing value) {}
	
	public Iterator<Entry<IntRing, IntRing>> iterator() {
		HashMap<IntRing,IntRing> map = new HashMap<IntRing,IntRing>();
		map.put(this, this);
		return map.entrySet().iterator();
	}
	
	public IntRing ringAct(IntRing scalar) {return multiply(scalar);}
	
	public Set<IntRing> getGenerators() {
		HashSet<IntRing> gen = new HashSet<IntRing> ();
		gen.add(ONE);
		return gen;
	}
	
	public int rank() {
		return 1;
	}
}
