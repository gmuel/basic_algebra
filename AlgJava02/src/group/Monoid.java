package group;

import topo.Element;
/**
 * The monoid interface - the only method
 * to be implemented is the {@link Monoid#operate(Monoid)}
 * method - so, technically speaking this interface is
 * closer to a semi-group 
 * @author adin
 *
 * @param <M>
 */
public interface Monoid<M extends Monoid<M>> extends Element<M>{
	/**
	 * Returns the left translate
	 * <tt>this o another</tt>
	 * of <tt>another</tt> by this
	 * @param another some other element
	 * @return the left translate
	 */
	public M operate (M another);
}
