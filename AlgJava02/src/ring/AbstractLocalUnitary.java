package ring;
/**
 * The abstract localization of some {@link UnitaryCommRing}
 * of type &lt<tt>A</tt>&gt:
 * <p><tt>S_I^(-1) A = {[s,x] : x in A, s in S_I, (s',x') in [s,x] &lt=&gt x s' - x' s in I}</tt>
 * <p>where <tt>I</tt> is some prime ideal
 * in <tt>A</tt> and <tt>S_I := A - I</tt> is the multiplicative
 * system.
 * <p><b>Note</b>, the ideal <tt>I</tt> must always contain the
 * annihilator ideal <tt>Ann(A)</tt> of <tt>A</tt>.
 * <p>The two operations <code>add(A)</code> and <code>multiply(A)</code>
 * should be implemented as
 * <ol><li><code>public A add(A another){
 * <p>A sum = new A();
 * <p>sum.numerator = numerator.multiply(another.denominator).add(denominator.multiply(another.numerator));
 * <p>sum.denominator=denominator.multiply(another.denominator);
 * <p>return sum;}</code></li>
 * <li><code>public A multiply(A another){
 * <p>A sum = new A();
 * <p>sum.numerator = numerator.multiply(another.numerator);
 * <p>sum.denominator=denominator.multiply(another.denominator);
 * <p>return sum;}</code></li>
 * </ol>
 * It is best practice to provide a constant <tt>ZERO</tt> and
 * <tt>ONE</tt> element
 * @author adin
 *
 * @param <U> the type of the underlying <code>UnitaryCommRing</code>
 * @param <A> the type of the implementing class
 */
public abstract class AbstractLocalUnitary<U extends UnitaryCommRing<U>,A extends AbstractLocalUnitary<U,A>> extends
		UnitaryCommRing<A> {
	/**the prime ideal defining the
	 * multiplicative system <tt>S_I</tt>*/
	protected PrimeIdeal<U> primeI;
	/**the numerator element - <tt>x</tt> in
	 * equivalence <tt>[s, x]</tt>*/
	protected U numerator;
	/**the denominator element - <tt>s</tt> in
	 * equivalence <tt>[s, x]</tt>*/
	protected U denominator;
	/**
	 * Constructs an empty element
	 */
	protected AbstractLocalUnitary() {
		super();
		setPrimeIdeal();
	}
	/**
	 * Returns true if the numerator of the difference of
	 * both elements is contained in the prime ideal
	 */
	public boolean equals(A another) {
		if(this==another) return true;
		return primeI!=null?primeI.contains(add(another.addInverse()).numerator):false;
	}

	/**
	 * Returns true if <tt>numerator.isDiscrete()</tt> returns true
	 */
	public boolean isDiscrete() {
		return numerator.isDiscrete();
	}

	
	

	/**
	 * Returns true if the numerator is not
	 * in the prime ideal
	 */
	public boolean isUnit() {
		return !primeI.contains(numerator);
	}

	/**
	 * Returns true if <tt>numerator.isDiscrete()</tt> returns true 
	 */
	public boolean isZero() {
		return numerator.isZero();
	}
	/**
	 * Sets the prime ideal of this element
	 */
	protected abstract void setPrimeIdeal ();
	/**
	 * The prime ideal class
	 * @author adin
	 *
	 * @param <U> the type of the underlying <code>UnitaryCommRing</code>
	 */
	public static abstract class PrimeIdeal<U extends UnitaryCommRing<U>> extends IdealInU<U> {
		/**Constructs an empty object*/
		protected PrimeIdeal (){super();}		
	}


}
