package topo;
/**
 * Auxiliary class: implements all but one method of the
 * {@link Function} interface. The user only needs to implement
 * the {@link Function#f()} method
 * @author adin
 *
 * @param <X> the domain's type
 * @param <Y> the codomains's type
 */
public abstract class AbstractFct<X, Y> implements Function<X, Y> {
	/**the argument*/
	protected X arg;
	/**the value*/
	protected Y val;
	/**Constructs an empty function object*/
	public AbstractFct() {}
	/**
	 * Computes the image value
	 * for the given argument <tt>arg</tt> 
	 */
	public void f(X arg){
		this.arg = arg;
		f();
	}
	/**
	 * Returns the current argument or null
	 */
	public X getArgument (){return arg==null?null:arg;}
	/**
	 * Returns the image value, possibly null
	 */
	public Y getValue(){
		if(val==null){
			f();
		}
		return val==null?null:val;
	}
	/**
	 * Sets the argument of this
	 * function to <tt>arg</tt>
	 * @param arg the argument
	 */
	public void setArgument(X arg){
		this.arg = arg;
	}
	 
}
