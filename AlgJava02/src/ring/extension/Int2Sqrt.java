package ring.extension;

import java.util.HashSet;

import module.AbstractFiniteModule;
import group.NNegInt;
import ring.integer.IntRing;
import ring.poly.MonoPoly;

public final class Int2Sqrt extends UnitaryExten<IntRing> {
	//TODO override some methods
	/*-----------------------statics-----------------------*/
	/**the zero element*/
	public static final Int2Sqrt ZERO = new Int2Sqrt (new MonoPoly<NNegInt,IntRing> ());
	/**the one element*/
	public static final Int2Sqrt  ONE = new Int2Sqrt (new MonoPoly<NNegInt,IntRing> (NNegInt.ZERO,IntRing.ONE));
	public static final Int2Sqrt SQUARE_ROOT_OF_TWO = new Int2Sqrt (new MonoPoly<NNegInt,IntRing> (NNegInt.ONE,IntRing.ONE));
	
	/*-------------------private statics-------------------*/
	/**the wrapper for the polynomial <tt>X^2 - 2 in Z[X]</tt>*/
	private static final XMinusTwoPolyWrapper X_POW_2_MINUS_TWO = new XMinusTwoPolyWrapper();
	private static final HashSet<Int2Sqrt>       CAN_GENERATORS = getCanonicalGenerators();
	private static HashSet<Int2Sqrt> getCanonicalGenerators (){
		HashSet<Int2Sqrt> gens = new HashSet<Int2Sqrt> ();
		gens.add(ONE);
		gens.add(SQUARE_ROOT_OF_TWO);
		return gens;
	}
	private static final class XMinusTwoPolyWrapper {
		private final MonoPoly<NNegInt,IntRing> X_SQUARED_MINUS_TWO = UnitaryExten.getPoly(new AbstractFiniteModule<IntRing>(
				new IntRing[]{
						IntRing.TWO.addInverse(),null,IntRing.ONE
				}
				));
	}
	private IntRing det;
	protected Int2Sqrt (AbstractFiniteModule<IntRing> element){
		this(UnitaryExten.getPoly(element));
	}
	public Int2Sqrt(MonoPoly<NNegInt,IntRing> element) {
		super(element,X_POW_2_MINUS_TWO.X_SQUARED_MINUS_TWO);
	}
	public Int2Sqrt (Int2Sqrt another){this(another.getElement());}
	public Int2Sqrt add(UnitaryExten<IntRing> another){
		return new Int2Sqrt (UnitaryExten.getPoly(getElement().add(another.getElement())));
	}
	public Int2Sqrt addInverse(){return new Int2Sqrt (super.addInverse().getElement());}
	public HashSet<UnitaryExten<IntRing>> getGenerators(){
		return new HashSet<UnitaryExten<IntRing>> (CAN_GENERATORS);
	}
	public Int2Sqrt getOne (){return ONE;}
	public Int2Sqrt inverse (){
		if(!isUnit()) return null;
		AbstractFiniteModule<IntRing> invEl = new AbstractFiniteModule<IntRing> (), element = getElement();
		IntRing oneCoeff, sqrt2Coeff;
		if((oneCoeff = element.getValue(NNegInt.ZERO))!=null) invEl.setEntry(NNegInt.ZERO, oneCoeff);
		if((sqrt2Coeff = element.getValue(NNegInt.ONE))!=null) invEl.setEntry(NNegInt.ONE, sqrt2Coeff.addInverse());
		if (det.equals(IntRing.ONE)){
			return new Int2Sqrt (invEl);
		}
		
		return new Int2Sqrt (invEl.ringAct(det));
	}
	public boolean isUnit (){
		if(det!=null) return det.isUnit(); 
		AbstractFiniteModule<IntRing> element = getElement();
		IntRing oneCoeff = element.getValue(NNegInt.ZERO), sqrt2Coeff = element.getValue(NNegInt.ONE);
		if(oneCoeff==null&&sqrt2Coeff==null) {det = IntRing.ZERO;return true;}
		if(sqrt2Coeff==null) {det = oneCoeff.pow(2);return det.equals(IntRing.ONE);}
		if(oneCoeff==null) {det = sqrt2Coeff.pow(2).multiply(IntRing.TWO).addInverse();return false;}
		return (det=oneCoeff.pow(2).add(sqrt2Coeff.pow(2).multiply(IntRing.TWO).addInverse())).isUnit();
		
	}
}
