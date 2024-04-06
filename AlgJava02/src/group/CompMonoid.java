package group;
/**
 * Combines the {@link Monoid} interface an the {@link Comparable}
 * interface
 * @author bzfmuell
 *
 * @param <C> the type of the implementing class
 */
public interface CompMonoid<C extends CompMonoid<C>> extends Monoid<C> , Comparable<C>{
	/**
	 * Returns true if and only if this
	 * monoidal object is the neutral element
	 * @return true if this is neutral
	 */
	public boolean isNeutral();
}
