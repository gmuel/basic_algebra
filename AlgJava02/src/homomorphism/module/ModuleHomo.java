package homomorphism.module;

import homomorphism.group.GroupHomo;
import homomorphism.ring.RingHomo;
import ring.Ring;
import util.Indexing;
import module.Module;
/**
 * The module homomorphism interface: combines {@link Module} and
 * {@link GroupHomo} interfaces
 * @author adin
 *
 * @param <H> the type of the implementing module homomorphism class
 * @param <M> the type of the pre-image module
 * @param <N> the type of the 
 * @param <R>
 * @param <S>
 * @param <I>
 */
public interface ModuleHomo<H extends ModuleHomo<H, M, N, R, S, I>, M extends Module<M, R,I>, N extends Module<N, S,I>, R extends Ring<R>, S extends Ring<S>, I extends Indexing<I>>
		extends Module<H, R,I>, GroupHomo<H,M,N> {
	public <X extends RingHomo<X,R,S>> RingHomo<X,R,S> canonicalHomo();
}
