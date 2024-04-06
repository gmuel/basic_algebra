package algebras;

import ring.AbstractCommRing;
import util.Indexing;
import module.Module;
/**
 * The algebra interface. An algebra is 
 * a module <tt>M</tt> over some arbitrary 
 * {@link AbstractCommRing} of type <tt>&ltA&gt</tt>,
 * with an <tt>A</tt>-bilinear mapping 
 * <p><tt>b : M x M -&gt M</tt>, that is
 * <ol><li><tt>b(x_1 m_1 + x_2 m_2, n) = x_1 b(m_1, n) + x_2 b(m_2, n)</tt></tt></li>
 * <li><tt>b(n, x_1 m_1 + x_2 m_2) = x_1 b(n, m_1) + x_2 b(n, m_2)</tt></li>
 * </ol>
 * for all <tt>x_1, x_2 in A</tt> and <tt>m_1, m_2, n in M.
 * <p><b>Note</b> any associative algebra may implement the
 * {@link Ring} interface.
 * 
 * 
 * 
 * @author adin
 *
 * @param <A> the type of the implementing <code>Algebra</code>-class
 * @param <C> the type of the underlying <code>AbstractCommRing</code>-class
 * @param <I> the type of some <code>IndicialMonoid</code>-class
 */
public interface Algebra<A extends Algebra<A, C, I>, C extends AbstractCommRing<C>, I extends Indexing<I>>
		extends Module<A, C, I> {
	/**
	 * Returns the additive inverse
	 * of this element
	 * @return the additive inverse
	 */
	public A addInverse();
	/**
	 * Returns the product
	 * <tt>this * another</tt>
	 * @param another some other algebra element
	 * @return the product
	 */
	public A multiply (A another);
	/**
	 * Returns true if and only if this
	 * algebra is associative
	 * @return true if associative
	 */
	public boolean isAssociative ();
}
