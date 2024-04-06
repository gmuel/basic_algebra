package ring;
/**
 * The localization of a unitary ring
 * @author adin
 *
 * @param <U> the type of the underlying unitary ring
 * @param <L> the type of the implementing ring
 */
public abstract class LocalUnitary<U extends AbstractCommRing<U>, L extends LocalUnitary<U,L>> extends
		UnitaryCommRing<L> {
	//TODO fix setup
	protected U numerator;
	protected U denominator;
	/**
	 * Constructs an empty instance
	 */
	public LocalUnitary() {
		super();
	}
	/**
	 * Returns the prime ideal <tt>I</tt>
	 *  that defines the multiplicative
	 * system: <tt>S := U \ I</tt>
	 * <p><b>Note</b>, if the annihilator of
	 * <tt>U</tt> is not trivial, <tt>I</tt> has
	 * to be the ideal containing the
	 * annihilator
	 * @return the ideal
	 */
	public abstract IdealInU<L> getIdeal();
}
