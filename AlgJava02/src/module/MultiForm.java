package module;

import ring.Ring;
import util.Indexing;
/**
 * The multi linear form interface: let <tt>M</tt> be a <tt>R</tt> module,
 * an <tt>n</tt>-form is a mapping <br /><tt>f : M^n -> R</tt>, <tt>(x_1,...,x_n) |-> sum_(k in N^n) a_k x_k_1^t_k_1...x_k_n^t_k_n</tt>
 * <br />Note, that this map is linear in each component
 * @author adin
 *
 * @param <M>
 * @param <N>
 * @param <R>
 */
public interface MultiForm<M extends MultiForm<M, R, N, I>, R extends Ring<R>,N extends Module<N,R,I>, I extends Indexing<I>>
		extends Module<M, R, I> {
	/**
	 * Evaluates this multi linear form and returns the scalar
	 * value
	 * @param arg the argument
	 * @return the scalar value
	 */
	public R eval (N[] arg);
}
