package module;

import java.util.Set;

import ring.Ring;
import util.Indexing;
/**
 * The finitely generated {@link R}-module interface:
 * @author adin
 *
 * @param <M>
 * @param <R>
 */
public interface FiniteGenMod<M extends FiniteGenMod<M, R, I>, R extends Ring<R>, I extends Indexing<I>>
		extends Module<M, R, I> {
	/**
	 * Returns a set of generators
	 * @return set of generators
	 */
	public Set<M> getGenerators();
	/**
	 * Returns the rank of this element
	 * @return the rank
	 */
	public int rank();
}
