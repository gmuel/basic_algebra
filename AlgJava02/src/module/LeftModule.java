package module;

import ring.Ring;
import util.Indexing;
/**
 * The left module interface
 * @author adin
 *
 * @param <M> the module type, subclass implementing this interface
 * @param <R> the ring type, subclass of {@link Ring} 
 * @param <I> the indexing type, subclass of {@link Indexing}
 */
public interface LeftModule<M extends LeftModule<M, R, I>, R extends Ring<R>, I extends Indexing<I>>
		extends Module<M, R, I> {
	/**
	 * Returns the scalar left multiple - 
	 * <tt>scalar * this</tt>
	 * @param scalar the scalar
	 * @return the multiple
	 */
	public M multiply (R scalar);
}
