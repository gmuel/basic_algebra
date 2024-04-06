package group;

import util.Indexing;
/**
 * Combination interface of {@link Indexing} and {@link CompMonoid}
 * @author adin
 *
 * @param <I>
 */
public interface IndicialMonoid<I extends IndicialMonoid<I>> extends Indexing<I>, CompMonoid<I> {}
