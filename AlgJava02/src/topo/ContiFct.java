package topo;

import topo.relation.EquiRel;

/**
 * Basic continuous function interface:
 * <br />besides implementing the {@link Function}
 * interface, three additional methods need to be implemented:
 * <ol>
 * <li>{@link ContiFct#extend(X superSet)} - returns the extension of <tt>this</tt></li>
 * <li>{@link ContiFct#isInPreImage()} - returns the equivalence relation object: "both are preimages of..."</li>
 * <li>{@link ContiFct#restrict(X subset)} - returns the restriction of <tt>this</tt></li>
 * </ol>
 * @author adin
 *
 * @param <C>
 * @param <E>
 * @param <F>
 * @param <X>
 * @param <Y>
 */
public interface ContiFct<C extends ContiFct<C,E, F, X, Y>, E extends Element<E>, F extends Element<F>, X extends TopoSpace<X,E>, Y extends TopoSpace<Y,F>>
		extends Function<E, F> {
	/**
	 * Returns the extension function of this: a trivial
	 * extension would be a constant extension on the difference
	 * set: <tt>
	 * @param superSet
	 * @return
	 */
	public C extend (X superSet);
	public EquiRel<E,X> isInPreImage();
	public C restrict(X subset);
	
}
