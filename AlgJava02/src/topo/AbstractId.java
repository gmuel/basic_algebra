package topo;
/**
 * The identity map class: note that any set has
 * precisely one identity map with arguments of
 * type {@link Element} 
 * @author adin
 *
 * @param <X> the type of the underlying set, sub
 * class of <code>Element</code>
 */
public class AbstractId<X extends Element<X>> extends AbstractElFct<X, X> {
	/**
	 * Constructs the identity
	 */
	public AbstractId() {
		super();
	}
	/**
	 * Constructs the identity
	 * with an argument provided
	 * @param arg the argument
	 */
	public AbstractId(X arg){
		this();
		setArgument(arg);
	}
	/**
	 * Computes the image, if the current argument is not null
	 */
	public void f() {
		if(arg!=null)	
			val = arg;
	}
	/**
	 * Set the argument to <tt>arg</tt> if
	 * <tt>atg!=null</tt> returns true
	 * @param arg the argument
	 */
	public void setArgument(X arg){
		if(arg!=null) this.arg = arg;
	}

}
