package field.finitefield;

import ring.CyclicRing;
import field.AbstractField;
/**
 * The field with seven elements: <tt>Z/(7)</tt>, the
 * residual classes in the ring of all integers modulo seven.
 * <br /><br />This class provides no constructor - all its elements
 * are instantiated as runtime constants 
 * @author adin
 *
 */
public class SevenCycle extends AbstractField<SevenCycle> {
	/**the zero element*/
	public static final SevenCycle  ZERO = new SevenCycle(0);
	/**the one element*/
	public static final SevenCycle   ONE = new SevenCycle(1);
	/**the two element*/
	public static final SevenCycle   TWO = new SevenCycle(2);
	/**the three element*/
	public static final SevenCycle THREE = new SevenCycle(3);
	/**the four element*/
	public static final SevenCycle  FOUR = new SevenCycle(4);
	/**the five element*/
	public static final SevenCycle  FIVE = new SevenCycle(5);
	/**the six element*/
	public static final SevenCycle   SIX = new SevenCycle(6);
	/**the only field of this class: an instance of
	 * type <code>CyclicRing</code>*/
	private CyclicRing value;
	/**
	 * Constructs an element of this class setting
	 * its value to <tt>value</tt> - kept private
	 * to prevent instantiation
	 * @param value the value
	 */
	private SevenCycle(int value) {
		super();
		this.value = new CyclicRing(value,7);
	}

	
	public SevenCycle inverse() {
		if(isZero()) throw new IllegalArgumentException ("\nOnly non-zero elements invertible");
		if(equals(ONE)) return ONE;
		if(equals(TWO)) return FOUR;
		if(equals(THREE)) return FIVE;
		if(equals(FOUR)) return TWO;
		if(equals(FIVE)) return THREE;
		return SIX;
	}

	public boolean isDiscrete(){return true;}
	
	public SevenCycle addInverse() {
		if(equals(ZERO)) return ZERO;
		if(equals(ONE)) return SIX;
		if(equals(TWO)) return FIVE;
		if(equals(THREE)) return FOUR;
		if(equals(FOUR)) return THREE;
		if(equals(FIVE)) return TWO;
		return ONE;
	}

	
	public boolean isZero() {return value.isZero()?true:false;}

	
	public SevenCycle multiply(SevenCycle another) {
		if(equals(ZERO)||another.equals(ZERO)) return ZERO;
		CyclicRing prod = value.multiply(another.value);
		if(prod.equals(ONE.value)) return ONE;
		if(prod.equals(TWO.value)) return TWO;
		if(prod.equals(THREE.value)) return THREE;
		if(prod.equals(FOUR.value)) return FOUR;
		if(prod.equals(FIVE.value)) return FIVE;
		return SIX;
	}

	
	public SevenCycle add(SevenCycle another) {
		if(equals(ZERO)||another.equals(ZERO)) return ZERO;
		CyclicRing prod = value.multiply(another.value);
		if(prod.equals(ONE.value)) return ONE;
		if(prod.equals(TWO.value)) return TWO;
		if(prod.equals(THREE.value)) return THREE;
		if(prod.equals(FOUR.value)) return FOUR;
		if(prod.equals(FIVE.value)) return FIVE;
		if(prod.equals(SIX.value)) return SIX;
		return ZERO;
	}

	
	public boolean equals(SevenCycle another) {return value.equals(another.value)?true:false;}

	
	public SevenCycle constructOne() {return ONE;}
	public String toString (){return value.toString();}
}
