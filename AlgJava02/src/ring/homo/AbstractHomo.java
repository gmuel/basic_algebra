package ring.homo;

import homomorphism.ring.RingHomo;
import ring.Ring;
import topo.AbstractElFct;
/**
 * The abstract ring homomorhism class
 * @author adin
 *
 * @param <R>
 * @param <S>
 */
public abstract class AbstractHomo<R extends Ring<R>, S extends Ring<S>>
		extends AbstractElFct<R, S> implements RingHomo<AbstractHomo<R, S>, R, S> {
	/**
	 * Constructs the empty homomorphism
	 */
	public AbstractHomo() {
		super();
	}
	

}
