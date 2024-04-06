package ring.integer;

import module.AbstractFiniteModule;
import ring.AbstractNilDomaine;
import ring.UnitaryCommRing;
import group.NNegInt;
import group.UnitGroup;
/**
 * A free integer module of rank 2, with multiplication:
 * <p><tt>(a + b.y) (a' + b'.y) = a a' + (a b' + a' b).y</tt>
 * where <tt>y * y = 0</tt>. The ring is isomorphic to <tt>Z[X]/(X^2)</tt>
 * <p> 
 * @author adin
 *
 */
public final class IntNilRing extends AbstractNilDomaine<IntRing> {
	/**the zero constant*/
	public static final IntNilRing          ZERO = new IntNilRing(         null,null);
	/**the one constant - generates the whole ring*/
	public static final IntNilRing           ONE = new IntNilRing(  IntRing.ONE,null);
	/**the minus one constant - generates the whole ring*/
	public static final IntNilRing         M_ONE = new IntNilRing(IntRing.M_ONE,null);
	/**the nilpotent generator element <tt>y</tt> - generates the
	 * annihilator ideal <tt>(y) = Z.y</tt>*/
	public static final IntNilRing NIL_GENERATOR = new IntNilRing(  IntRing.ONE,null); 
	
	private static final String EPS = new String (new int[]{0x03B1},0,1);
	//private final IntRing   zElement;
	//private final IntRing nilElement;
	/**
	 * Constructs an element of 
	 * @param freeElement
	 * @param nilElement
	 */
	public IntNilRing (IntRing freeElement, IntRing nilElement){
		super(constructModElement(freeElement,nilElement));
	}
	public IntNilRing (AbstractNilDomaine<IntRing> another){
		super(another);
		if(another.getNilPotencyIndex().compareTo(NNegInt.TWO)>0)
			throw new IllegalArgumentException ("\nOnly nilpotency index <= 2 supported...");
	}
	public IntNilRing add(IntNilRing another) {
		return new IntNilRing (super.add(another));
	}
	
	public IntNilRing addInverse() {
		return new IntNilRing (super.addInverse());
	}
	
	public IntNilRing getOne() {return ONE;}
	/**
	 * Returns the coefficient of the ring
	 * generator <tt>1</tt>
	 * @return the z-element
	 */
	public IntRing getZElement (){
		IntRing freeElement = getElement().getValue(NNegInt.ZERO);
		return freeElement==null?IntRing.ZERO:freeElement;
	}
	/**
	 * Returns the coefficient of the annihilator
	 * generator 0x03B5 
	 * @return
	 */
	public IntRing getNilElement (){
		IntRing nilElement = getElement().getValue(NNegInt.ONE);
		return nilElement==null?IntRing.ZERO:nilElement;
	}

	/**
	 * Returns the inverse or null, if
	 * this is no unit 
	 */
	public IntNilRing inverse() {
		return isUnit()?new IntNilRing(super.inverse()):null;
	}
	
	public IntNilRing multiply(AbstractNilDomaine<IntRing> another) {
		return new IntNilRing (super.multiply(another));
	}		
	
	private static <U extends UnitaryCommRing<U>> AbstractFiniteModule<U> constructModElement(U freeElement, U nilElement){
		AbstractFiniteModule<U> moduleEl = new AbstractFiniteModule<U>();
		if(freeElement!=null) moduleEl.setEntry(NNegInt.ZERO, freeElement);
		if( nilElement!=null) moduleEl.setEntry(NNegInt.ZERO,  nilElement);
		return moduleEl;
	}
	public static void main (String[] args){
		IntNilRing a = new IntNilRing (IntRing.M_ONE,IntRing.TWO);
		IntNilRing b = new IntNilRing (IntRing.M_ONE,IntRing.TWO.addInverse());
		System.out.println(String.format("%1$s * %2$s = \n%3$s",a,b,a.multiply(b)));
	}
}
