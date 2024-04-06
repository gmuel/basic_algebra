package group;


/**
 * The abstract non-Abelian group class
 * @author muellerg
 *
 * @param <N>
 */
public abstract class AbstractNAG<N extends AbstractNAG<N>> implements
		NonAbelGroup<N> {
	/**
	 * Constructs an empty instance
	 */
	public AbstractNAG (){}
	/**
	 * Returns the commutator element
	 * <tt>[this,another] := this^(-1) another^(-1) this another</tt>
	 * @param another some other element
	 * @return the commutator
	 */
	public N commute(N another){
		return inverse().multiply(another.inverse()).multiply(multiply(another));
	}
	public boolean equals(Object o){
		if(this==o) return true;
		if(!(o instanceof AbstractNAG)) return false;
		@SuppressWarnings("unchecked")
		AbstractNAG<N> cp = (AbstractNAG<N>) o;
		return equals(cp);
	}
	/**
	 * The operation is defined via the
	 * {@link NonAbelGroup#multiply(N)} method
	 */
	public N operate(N another) {return multiply(another);}
	/**
	 * Returns the inverse element of this
	 * @return the inverse
	 */
	public abstract N inverse();
	/**
	 * Returns true if this element equals the neutral element
	 * @return if this element is neutral
	 */
	public abstract boolean isNeutral();
}
