package module;

import util.Indexing;
import field.AbstractField;

public interface VectorSpace<V extends VectorSpace<V, F, I>, F extends AbstractField<F>,I extends Indexing<I>>
		extends CommModule<V, F, I> {
}
