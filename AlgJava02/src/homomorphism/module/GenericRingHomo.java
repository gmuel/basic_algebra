package homomorphism.module;

import homomorphism.ring.RingHomo;
import ring.Ring;
/**
 * The generic ring homomorphism class: three methods need to be implemented by any sub class
 * <ol><li>{@link GenericRingHomo#equals(group.Monoid)} - a comparison method similar to <code>Object.equals(Object)</code></li>
 * <li>{@link GenericRingHomo#f() } - the evaluation method: computes the image <tt>f(arg)</tt> of the current argument <tt>arg</tt></li>
 * <li>{@link GenericRingHomo#isZero()} - must return true, if <tt>getValue().isZero()</tt> returns true for all arguments <tt>arg in R</tt>
 * </ol>
 * @author bzfmuell
 *
 * @param <R> the preimage type
 * @param <S> the image type
 */
public abstract class GenericRingHomo<R extends Ring<R>,S extends Ring<S>> implements RingHomo<GenericRingHomo<R,S>,R,S>{
	/**the argument value*/
	protected R arg;
	/**the image value*/
	protected S val;
	/**
	 * Constructs the empty homomorphism
	 */
	public GenericRingHomo (){super();}
		
	public void f(R arg){
		this.arg = arg;
		f();
	}
	public R getArgument() {return arg==null?null:arg;}
	
	public S getValue() {if(val==null&&arg!=null) f();return val==null?null:val;}
	
	
	public boolean isKernel(R arg){
		f(arg);
		return val.isZero()?true:false;
	}
	
	public void setArgument(R arg) {this.arg = arg;}

		

	
}
