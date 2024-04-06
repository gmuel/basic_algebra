package field.finitefield;

import ring.CyclicRing;
import field.AbstractField;
/**
 * The field with five elements: <tt>Z/(5)</tt>, the
 * residual classes in the ring of all integers modulo five.
 * <br /><br />This class provides no constructor - all its elements
 * are instantiated as runtime constants
 * @author adin
 *
 */
public class FiveCycle extends AbstractField<FiveCycle> {
	/**the zero element*/
	public static final FiveCycle  ZERO = new FiveCycle(0);
	/**the one element*/
	public static final FiveCycle   ONE = new FiveCycle(1);
	/**the two element*/
	public static final FiveCycle   TWO = new FiveCycle(2);
	/**the three element*/
	public static final FiveCycle THREE = new FiveCycle(3);
	/**the four element*/
	public static final FiveCycle  FOUR = new FiveCycle(4);
	/**the only field: an element of type <code>CyclicRing</code>*/
	private CyclicRing value;
	/**
	 * Constructs an element with value <tt>val</tt> - kept
	 * private to block further instantiation
	 * @param val the value
	 */
	private FiveCycle (int val){
		super();
		value = new CyclicRing(val,5);
	}
	public FiveCycle inverse() {
		if(equals(ZERO)) throw new IllegalArgumentException ("\nOnly non-zero elements invertible");
		if(equals(ONE)) return ONE;
		if(equals(TWO)) return THREE;
		if(equals(THREE)) return TWO;
		return FOUR;
	}

	
	public FiveCycle addInverse() {
		if(equals(ZERO)) return ZERO;
		if(equals(ONE)) return FOUR;
		if(equals(TWO)) return THREE;
		if(equals(THREE)) return TWO;
		return ONE;
	}

	public boolean isDiscrete(){return true;}
	
	public boolean isZero() {return value.isZero()?true:false;}

	
	public FiveCycle multiply(FiveCycle another) {
		if(equals(ZERO)||another.equals(ZERO))return ZERO;
		CyclicRing prod = value.multiply(another.value);
		if(prod.equals(ONE.value)) return ONE;
		if(prod.equals(TWO.value)) return TWO;
		if(prod.equals(THREE.value)) return THREE;
		return FOUR;
	}

	
	public FiveCycle add(FiveCycle another) {
		if(equals(ZERO)&&another.equals(ZERO)) return ZERO;
		CyclicRing sum = value.add(another.value);
		if(sum.equals(ONE.value)) return ONE;
		if(sum.equals(TWO.value)) return TWO;
		if(sum.equals(THREE.value)) return THREE;
		if(sum.equals(FOUR.value)) return FOUR;
		return ZERO;
	}

	
	public boolean equals(FiveCycle another) {return value.equals(another.value)?true:false;}

	
	public FiveCycle constructOne() {return ONE;}
	
	public String toString (){return value.toString();}

}
