package module.poly;

import ring.Ring;
import ring.poly.MonoPoly;
import group.CompMonoid;
import module.AbstractFiniteModule;
/**
 * Helper class: defines specific dependencies
 * between polynomial objects (all of type <tt>&ltP&gt</tt>
 *  a sub class of {@link MonoPoly} and finitely generated
 *  modules with polynomial entries
 * @author adin
 *
 * @param <P> the type of the sub class of <code>MonoPoly</code>
 * @param <M> the type of some comparable monoidal element
 * @param <R> the type of the underlying {@link Ring}
 */
public class AbstractPolyModule<M extends CompMonoid<M>,R extends Ring<R>> extends
		AbstractFiniteModule<MonoPoly<M,R>> {
	/**
	 * Constructs the zero element
	 */
	public AbstractPolyModule (){super();}
	/**
	 * Constructs a polynomial module element
	 * with entries specified by the returned
	 * sequence of the <code>Iterable</code>
	 * object <tt>polyEntries</tt>
	 * @param polyEntries some object returning a
	 * sequence of polynomials
	 */
	public AbstractPolyModule (Iterable<MonoPoly<M,R>> polyEntries){
		super(polyEntries);
	}
	public AbstractPolyModule (AbstractFiniteModule<MonoPoly<M,R>> another){
		super(another);
	}
	public AbstractPolyModule<M,R> add(AbstractFiniteModule<MonoPoly<M,R>> another){
		return new AbstractPolyModule<M,R> (super.add(another));
	}
	public AbstractPolyModule<M,R> ringAct(MonoPoly<M,R> scalar){
		return new AbstractPolyModule<M,R> (super.ringAct(scalar));
	}
}
