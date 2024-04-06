package topo.relation;

import topo.AbstractElFct;
import topo.Element;
import topo.Function;
/**
 * The pre-image equivalence relation class:
 * <p>Any object <tt>f</tt> of type {@link AbstractElFct} defines
 * an equivalence relation <tt>D</tt> on <code>X</code>:
 * <p><tt>D := {(x, y) in X x X : f(x) = f(y)}</tt>.
 * @author adin
 *
 * @param <X> the pre image element's type
 * @param <Y> the image element's type
 */
public class PreImageRel<X extends Element<X>, Y> implements EquiRel<X,PreImageSet<X,Y>> {

	/**deep copy of the original function
	 * object*/
	final AbstractElFct<X,Y> elementaryFct;
	/**
	 * Constructs a pre-image equivalence
	 * relation for the given argument
	 * <tt>fct</tt> of type {@link Function}
	 * @param fct the function
	 */
	public PreImageRel (Function<X,Y> fct){
		final Function<X,Y> cpFct = fct;
		elementaryFct = new AbstractElFct<X,Y>(){
			public void f(){
				cpFct.f(arg);
				val = cpFct.getValue();
			}
		};
	}
	/**
	 * Returns the pair <tt>(arg,arg)</tt>
	 * where <tt>arg</tt> is the current
	 * argument of the <code>Function</code> object,
	 * or null if the argument is null
	 */
	public PairedElement<X> basePoint() {
		X arg = elementaryFct.getArgument();
		return arg==null?null:new PairedElement<X>(arg,arg);
	}

	/**
	 * Returns the inverse relation, that
	 * is all pairs of elements in <tt>X</tt>
	 * that do not have the same image, are related
	 * ({@link Relation#isRelated(Element, Element)} returns
	 * true)
	 */
	public Relation<X> inverse() {
		final PreImageRel<X,Y> cp = this;
		
		//anonymous class
		return new Relation <X>() {
			PairedElement<X> basePoint;
			/**Returns the base point, a pair of elements
			 * of <tt>X</tt>, that are related*/
			public PairedElement<X> basePoint() {return basePoint==null?null:basePoint;}
			/**Returns a shallow copy of the outer relation*/
			public Relation<X> inverse() {return cp;}
			/**Returns false, since the
			 * inverse relation is open in
			 * general*/
			public boolean isClosed() {return false;}
			/**
			 * Returns true if the image values of
			 * the two arguments <tt>first</tt> and
			 * <tt>second</tt> are different
			 * @param first the first argument
			 * @param second the second argument
			 * @return true they are related
			 */
			public boolean isRelated(X first, X second) {
				boolean isRelated = cp.isRelated(first, second);
				if(!isRelated) {
					if(basePoint==null) basePoint = new PairedElement<X> (first,second);
					return true;
				}
				return false;
			}
			
		};
	}

	/**
	 * Returns true by default
	 */
	public boolean isClosed() {return true;}

	/**
	 * Returns true if and only if the image
	 * value of the <tt>Function</tt> object
	 * for the two arguments are equal
	 */
	public boolean isRelated(X first, X second) {
		if(first==null&&second==null) return true;
		if(first==null||second==null) return false;
		elementaryFct.f(first);
		Y val1 = elementaryFct.getValue();
		elementaryFct.f(second);
		Y val2 = elementaryFct.getValue();
		
		
		return val1.equals(val2);
	}

	/**
	 * Returns the equivalence class for
	 * the given <tt>element</tt>, the subset
	 * of <tt>X</tt> whose image are all equal
	 */
	public PreImageSet<X,Y> equiClass(X element) {
		PreImageSet<X,Y> equi = new PreImageSet<X,Y> (this);
		equi.contains(element);
		return equi;
	}
	

}
