package ring.homo;

import homomorphism.ring.RingHomo;
import ring.Ring;
import topo.AbstractId;
/**
 * The identity map of an algebraic ring
 * @author adin
 *
 * @param <R>
 */
public class AbstractRId<R extends Ring<R>> extends AbstractId<R> implements
		RingHomo<AbstractRId<R>, R, R> {
	/**
	 * Constructs the identity map
	 * with no argument
	 */
	public AbstractRId() {
		super();
	}

	public boolean isKernel(R arg) {
		return arg.isZero();
	}

}
