package topo;

import topo.relation.PreImageRel;

/**
 * Subclass of {@link AbstractFct}
 * @author adin
 *
 * @param <X>
 * @param <Y>
 */
public abstract class AbstractElFct<X extends Element<X>,Y> extends AbstractFct<X, Y> {
	/**
	 * Constructs an empty function
	 * object
	 */
	public AbstractElFct (){super();}
	/**
	 * Returns the pre image
	 * of <tt>argument</tt> - the 
	 * equivalence class of all elements
	 * mapped to same image as the argument
	 * @param argument the argument
	 * @return the equivalence class of the argument
	 */
	public PreImageRel<X,Y> getEquivRel (X argument){
		f(argument);
		return new PreImageRel<X,Y>(this);
	}
}
