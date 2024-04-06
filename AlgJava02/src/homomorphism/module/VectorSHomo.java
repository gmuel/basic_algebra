package homomorphism.module;

import util.Indexing;
import field.AbstractField;
import group.NNegInt;
import module.VectorSpace;
import module.CommModule;
/**
 * The vector space (VS) homomorphism interface over some algebraic field of type <code>F</code>
 * @author bzfmuell
 *
 * @param <H> the VS homomorphism type
 * @param <V> the preimage VS type
 * @param <W> the image VS type
 * @param <F> the field type
 */
public interface VectorSHomo<H extends VectorSHomo<H,V,W,F,I>, V extends VectorSpace<V, F, I>, W extends VectorSpace<W, F, I>, F extends AbstractField<F>, I extends Indexing<I>>
		extends ModuleHomo<H, V, W, F, F,I>, CommModule<H,F,I> {
	
}
