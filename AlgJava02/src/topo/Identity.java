package topo;


/**
 * The identity function class - maps each argument object to
 * its own value. <b>Note</b>, that <code>x.equals(y)</code> always returns
 * true if <code>x</code> is not null. In case of a null argument, it
 * strongly depends on the implementation of the {@link Object#equals(Object)} method 
 * of the argument type <code>X</code>.
 * @author adin
 *
 * @param <X> the argument and image type
 */
public class Identity<X> extends AbstractFct<X, X> {
	/**
	 * Constructs the empty identity object - to
	 * specify: no argument provided
	 */
	public Identity() {super();}
	/**
	 * Constructs an identity object with its
	 * argument and value set to <tt>x</tt> 
	 * @param x the argument
	 */
	public Identity(X x){
		this();
		this.arg = x;
		f();
	}
	/**
	 * Computes the identity image
	 * <p><tt>value = argument</tt>
	 */
	public void f() {val = arg;}

}
