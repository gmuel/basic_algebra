package module;

import ring.Ring;
import util.Indexing;
/**
 * The right module interface
 * @author adin
 *
 * @param <M> the module type, subclass implementing this interface
 * @param <R> the ring type, subclass of {@link Ring} 
 * @param <I> the indexing type, subclass of {@link Indexing}
 */

public interface RightModule<M extends RightModule<M, R, I>, R extends Ring<R>, I extends Indexing<I>>
		extends Module<M, R, I> {
	/**
	 * Returns the scalar right multiple - 
	 * <tt>this * scalar</tt>
	 * @param scalar the scalar
	 * @return the multiple
	 */
	public M multiply (R scalar);
}
