package topo.relation;

import topo.Element;
import topo.Function;
import topo.Set;
/**
 * The pre-image set class - used
 * primarily in {@link PreImageRel}.
 * Each instance 
 * @author adin
 *
 * @param <X> the pre-image's element type, sub class of {@link Element}
 * @param <Y> the image type,
 */
public class PreImageSet<X extends Element<X>, Y> implements
		Set<X, PreImageSet<X, Y>> {
	private static final IllegalArgumentException ERROR1 = 
			new IllegalArgumentException ("\nNull Function-object not permitted!");
	private static final IllegalArgumentException ERROR2 = 
			new IllegalArgumentException ("\nNull Element-object not permitted!");
	/**the pre-image equivalence relation*/
	private final PreImageRel<X,Y> preImageRelation;
	/**
	 * Constructs a pre-image set for the
	 * given <code>Function</code> object <tt>fct</tt>
	 * @param fct the function
	 * @throws IllegalArgumentException if <tt>fct==null</tt> returns true
	 */
	public PreImageSet (Function<X,Y> fct) throws IllegalArgumentException {
		if(fct==null) throw ERROR1;
		preImageRelation = new PreImageRel<X,Y>(fct);
	}
	/**
	 * Constructs a pre-image set for the
	 * given <code>Function</code> object <tt>fct</tt>
	 * and sets the representative to <tt>element</tt>
	 * @param fct the function
	 * @param element the representative of the equi-class
	 * @throws IllegalArgumentException if either of the two arguments
	 * is null
	 */
	public PreImageSet (Function <X,Y> fct, X element) throws IllegalArgumentException {
		this(fct);
		if(element!=null) preImageRelation.elementaryFct.f(element);
		else throw ERROR2;
	}
	/**
	 * Constructs a pre-image set for
	 * the given <code>PreImageRel</code>
	 * object <tt>preImageRelation</tt>
	 * <p><b>Note</b> the instance represents
	 * the equivalence class of the current argument, if
	 * not null (then it is empty an will be set by the
	 * first call <code>contains(Element)</tt> to the given
	 * element
	 * @param preImageRelation the equivalence relation
	 * @throws IllegalArgumentException if <tt>preImageRelation==null</tt>
	 * returns true
	 */
	public PreImageSet (PreImageRel<X,Y> preImageRelation) throws IllegalArgumentException {
		if(preImageRelation==null) throw ERROR1;
		this.preImageRelation = new PreImageRel<X,Y>(preImageRelation.elementaryFct);
	}
	/**
	 * Constructs the pre-image set for
	 * the given <code>PreImageRel</code>
	 * object <tt>preImageRelation</tt> 
	 * representing the equiv-class <tt>[element]</tt>
	 * @param preImageRelation the equivalence relation
	 * @param element the representative
	 * @throws IllegalArgumentException if either of the two arguments
	 * is null
	 */
	public PreImageSet (PreImageRel<X,Y> preImageRelation, X element) throws IllegalArgumentException {
		this(preImageRelation);
		if(element!=null) this.preImageRelation.elementaryFct.f(element);
		else throw ERROR2;
	}
	/**
	 * Returns true if the <tt>element</tt> belongs
	 * to the same equivalence class
	 * <p><b>Note</b> an empty pre-image set gets
	 * populated, that is <tt>element</tt> gets its
	 * representative
	 */
	public boolean contains(X element) {
		X arg = preImageRelation.elementaryFct.getArgument(); 
		if(arg==null&&element==null) return true;
		if(arg==null||element==null) {
			if(element!=null) preImageRelation.elementaryFct.f(element);
			return false;
		}
		return preImageRelation.isRelated(arg, element);
	}

	/**
	 * Returns the intersection set of the two pre image
	 * sets or null if they are disjoint
	 */
	public PreImageSet<X,Y> intersect(final PreImageSet<X,Y> another) {
		return new PreImageSet<X,Y> (preImageRelation.elementaryFct){
			public boolean contains(X element){
				return super.contains(element)&&another.contains(element);
			}
			public boolean isEmpty (){
				return super.isEmpty()&&another.isEmpty();
			}
		};
	}
	/**
	 * Returns true only if the function object has
	 * no argument (<tt>getArgument()==null</tt> returns true)
	 */
	public boolean isEmpty() {
		return preImageRelation.elementaryFct.getArgument()==null?true:false;
	}

	/**
	 * Returns true only if the elements
	 * are discrete
	 */
	public boolean isOpen() {
		X arg = preImageRelation.elementaryFct.getArgument();
		preImageRelation.elementaryFct.f();
		Y val = preImageRelation.elementaryFct.getValue();
		return arg==null||val==null?true:
			arg.isDiscrete();
	}

	/**
	 * Returns the union of two pre-image sets
	 * of same element type <tt>X</tt>
	 */
	public PreImageSet<X,Y> union(final PreImageSet<X,Y> another) {
		return new PreImageSet<X,Y>(preImageRelation.elementaryFct){
			public boolean contains(X element){return super.contains(element)||another.contains(element);}
			public boolean isEmpty(){return super.isEmpty()||another.isEmpty();}
		};
	}
}
