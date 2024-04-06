package module;

import ring.Ring;
import util.Indexing;
import util.Tupleable;
import group.AbelGroup;
/**
 * The module interface over some ring of type {@link R}
 * @author adin
 *
 * @param <M> the type of the implementing submodule
 * @param <R> the type of the ring
 */
public interface Module<M extends Module<M,R,I>, R extends Ring<R>, I extends Indexing<I>> extends AbelGroup<M>, Tupleable<R,I> {
	/**
	 * Returns the scalar multiple of this
	 * module element
	 * @param scalar the scalar
	 * @return the scalar multiple
	 */
	public M ringAct (R scalar);
	/**
	 * Returns true if this element
	 * is the zero element
	 * @return if this is zero
	 */
	public boolean isZero();
}
