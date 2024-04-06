package ring.integer;

import module.AbstractFiniteModule;
import group.NNegInt;
import group.UnitGroup;
import ring.AbstractNilDomaine;
import ring.IdealInU;
import ring.LocalUnitary;
/**
 * Localization of {@link IntNilRing} with the multiplicative
 * system <tt>IntNilRing - IDEAL</tt>, where <tt>IDEAL</tt> is
 * the annihilator ideal of the underlying ring
 * @author adin
 *
 */
public class IntNilLocal extends LocalUnitary<AbstractNilDomaine<IntRing>, IntNilLocal> {
	/**the annihilator ideal: the set of all zero divisors
	 * of the ring <code>IntNilRing</code>*/
	public static final AnnihilatorIdeal ANN_IDEAL = new AnnihilatorIdeal();
	/**the zero element constant*/
	public static final IntNilLocal        ONE = new IntNilLocal(IntNilRing.ONE,IntNilRing.ONE);
	/**the one element constant*/
	public static final IntNilLocal       ZERO = new IntNilLocal(IntNilRing.ZERO,IntNilRing.ZERO);
	/**the numerator*/
	private IntNilRing   numerator;
	/**the denominator*/
	private IntNilRing denominator;
	/**
	 * Constructs an element of this ring
	 * with numerator and denominator set
	 * accordingly. 
	 * <p><b>Note</b>, that instantiation fails if the
	 * denominator is null or an annihilator element
	 * @param numerator the numerator, null is treated as zero
	 * @param denominator the denominator, null never accepted
	 * @throws IllegalArgumentException <ol>
	 * <li>if <tt>denominator==null</tt> returns true</li>
	 * <li>or <tt>IDEAL.contains(denominator)</tt> returns true</li></ol>
	 */
	public IntNilLocal (IntNilRing numerator, IntNilRing denominator)
			 throws IllegalArgumentException {
		super();
		if(denominator!=null){
			if(ANN_IDEAL.contains(denominator))
				throw new IllegalArgumentException ("\nDenominator in annihilitor ideal NOT permitted!");
			setElements(numerator==null?IntNilRing.ZERO:numerator, denominator);
			
		} else throw new IllegalArgumentException ("\nNull arguments not permitted");
		
	}
	/**
	 * Constructs an element of this ring setting the
	 * numerator to <code>new IntNilRing(numZElement,numNilElement)</tt> and
	 * the denominator to <code>new IntNilRing(denZElement,denNilElement)</tt>
	 * @param numZElement the (torsion-) free element of the numerator
	 * @param numNilElement the nilpotent element of the numerator
	 * @param denZElement the (torsion-) free element of the denominator
	 * @param denNilElement the nilpotent element of the denominator
	 * @throws IllegalArgumentException the denominator is contain in the annihilator
	 */
	public IntNilLocal (IntRing numZElement, IntRing numNilElement, IntRing denZElement,
			IntRing denNilElement) throws IllegalArgumentException {
		this(new IntNilRing(numZElement,numNilElement), new IntNilRing(denZElement, denNilElement));
	}
	/**
	 * Constructs a copy of <tt>another</tt> element
	 * @param another some other element
	 */
	public IntNilLocal (IntNilLocal another){
		this(another.numerator,another.denominator);
	}
	/**
	 * Returns the additive inverse
	 */
	public IntNilLocal addInverse() {return new IntNilLocal(numerator.addInverse(),denominator);}

	/**
	 * Returns true if the numerator is not
	 * contains in the annihilator
	 */
	public boolean isUnit() {return !ANN_IDEAL.contains(this);}

	/**
	 * Returns true if this element is zero
	 */
	public boolean isZero() {return numerator.isZero();}

	/**
	 * Returns the product
	 */
	public IntNilLocal multiply(IntNilLocal another) {
		return new IntNilLocal (numerator.multiply(another.numerator),
				denominator.multiply(another.denominator));
	}

	/**
	 * Returns the sum
	 */
	public IntNilLocal add(IntNilLocal another) {
		return new IntNilLocal(
				numerator.multiply(another.denominator).add(denominator.multiply(another.numerator)),
				denominator.multiply(another.denominator));
	}

	/**
	 * Returns true if both elements are equal
	 */
	public boolean equals(IntNilLocal another) {
		if(this==another) return true;
		return numerator.equals(another.numerator)&&denominator.equals(another.denominator);
	}

	/**
	 * Returns always true, because of the
	 * discrete topology
	 */
	public boolean isDiscrete() {return true;}
	/**
	 * Returns the annihilator
	 */
	public AnnihilatorIdeal getIdeal(){return ANN_IDEAL;}
	/**
	 * Returns the one constant
	 */
	public IntNilLocal getOne() {return ONE;}

	/**
	 * Returns the unit element, if this
	 * is a unit (numerator not in annihilator)
	 * or null
	 */
	public UnitGroup<IntNilLocal> getUnit() {return isUnit()?new UnitGroup<IntNilLocal> (this):null;}

	/**
	 * Returns the inverse if this is a unit
	 * or null
	 */
	public IntNilLocal inverse() {return isUnit()?new IntNilLocal(denominator,numerator):null;}
	/**
	 * The annihilator ideal class
	 * @author adin
	 *
	 */
	protected static class AnnihilatorIdeal extends IdealInU<IntNilLocal> {
		/**Constructs the annihilator ideal - kept
		 * private only one public instance available */
		private AnnihilatorIdeal (){super();}
		/**
		 * Returns true if <tt>element</tt> is
		 * contained in this ideal
		 */
		public boolean contains(IntNilLocal element) {
			AbstractFiniteModule<IntRing> modElement = element.numerator.getElement();
			return modElement==null?true:modElement.isZero()?true:false;
		}
		public boolean contains (IntNilRing element){
			IntRing zeroEntry = element.getValue(NNegInt.ZERO);
			return zeroEntry==null?true:zeroEntry.isZero();
		}
		
	}
	/**
	 * Auxiliary method: sets the numerator and
	 * denominator
	 * <p>First, the GCD of all entries (free and nilpotent elements)
	 * is determined and if not ONE all entries get divided. Next,
	 * the sign of the free part of the denominator is determined,
	 * if negative all entries get the opposite sign 
	 * @param numerator the numerator
	 * @param denominator the denominator
	 */
	private void setElements (IntNilRing numerator, IntNilRing denominator){
		if(!numerator.isZero()) {
			IntRing numZ = numerator.getZElement(), numNil = numerator.getNilElement();
			IntRing denZ = denominator.getZElement(), denNil = denominator.getNilElement();
			boolean numZN = numZ!=null, numNN = numNil!=null, denZN = denZ!=null, denNN = denNil!=null;
			
			IntRing gcdNum = numZN&&numNN?numZ.gcd(numNil):numZN?numZ:numNN?numNil:IntRing.ONE;
			IntRing gcdDen = denZN&&denNN?denZ.gcd(denNil):denZN?denZ:denNN?denNil:IntRing.ONE;
			if(!gcdNum.equals(IntRing.ONE)&&!gcdDen.equals(IntRing.ONE)){
				IntRing gcd = gcdNum.gcd(gcdDen);
				if(!gcd.equals(IntRing.ONE)){
					numZ   = numZN?numZ.div(gcd):numZ;
					numNil = numNN?numNil.div(gcd):numNil;
					denZ   = denZN?denZ.div(gcd):denZ;
					denNil = denNN?denNil.div(gcd):denNil;
				}
			}
			boolean isLess = denZN?denZ.compareTo(IntRing.ZERO)<0:true;
			if(isLess){
				numZ   = numZN?numZ.addInverse():numZ;
				numNil = numNN?numNil.addInverse():numNil;
				denZ   = denZN?denZ.addInverse():denZ;
				denNil = denNN?denNil.addInverse():denNil;
			}
			this.numerator   = new IntNilRing (numZ,numNil);
			this.denominator = new IntNilRing (denZ,denNil); 
			
		} else {
			this.numerator   = IntNilRing.ZERO;
			this.denominator = IntNilRing.ONE; 
		}
	}
}
