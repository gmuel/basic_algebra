package module;

import ring.CommRing;
import util.Indexing;
/**
 * Interface for all modules over some commutative ring of type <tt>R</tt>
 * @author bzfmuell
 *
 * @param <C> the (implementing) module type
 * @param <R> the ring type
 */
public interface CommModule<C extends CommModule<C, R, I>, R extends CommRing<R>, I extends Indexing<I>>
		extends RightModule<C, R, I>, LeftModule<C,R, I> {
}
