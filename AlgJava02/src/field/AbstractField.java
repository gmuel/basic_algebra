package field;

import group.NNegInt;
import group.UnitGroup;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;

import module.VectorSpace;
import ring.UnitaryCommRing;
/**
 * The abstract field class: any integral domain, with
 * maximal zero ideal (to specify: any ideal different from
 * zero ideal is already the whole ring) is an algebraic field
 * @author adin
 *
 * @param <A> the type of the implementing class
 */
public abstract class AbstractField<A extends AbstractField<A>> extends UnitaryCommRing<A>
		implements Field<A>, VectorSpace<A,A, NNegInt> {
	/**
	 * Zero constructor
	 */
	public AbstractField (){super();}
	/**
	 * Unsupported operation: nothing changed
	 * or removed
	 */
	public void clear (){}
	/**
	 * Returns true if this is not the zero element
	 */
	public boolean isUnit(){return isZero()?false:true;}
	/**
	 * Returns true if this equals another element
	 * @param another some other element to compare
	 * @return
	 */
	public boolean equals(AbstractField<A> another){return add(another.addInverse()).isZero()?true:false;}
	/**
	 * Returns an iterator whose
	 * own <code>next()</code> method
	 * returns this element
	 */
	public Iterator<Entry<NNegInt,A>> iterator(){return new AFIterator ();}
	/**
	 * Unsupported operation: does not
	 * change this object at all
	 */
	public void setEntry(NNegInt i, A coeff){}
	public NNegInt getIndex (A coeff){return equals(coeff)?NNegInt.ZERO:null;}
	public A ringAct (A scalar){return multiply(scalar);}
	@SuppressWarnings("unchecked")
	public A getValue (NNegInt index){return (A) this;}
	public A remove (NNegInt index){return null;}
	/**
	 * Auxiliary method: returns the one element
	 * @return one
	 */
	public abstract A constructOne ();
	public A getOne(){return constructOne();}
	@SuppressWarnings("unchecked")
	public UnitGroup<A> getUnit(){
		return new UnitGroup<A>((A) this);
	}
	
	protected class AFIterator implements Iterator<Entry<NNegInt,A>> {
		private TreeMap<NNegInt,A> map;
		private Iterator<Entry<NNegInt,A>> it;
		@SuppressWarnings("unchecked")
		protected AFIterator (){
			map = new TreeMap<NNegInt,A> ();
			map.put(NNegInt.ZERO,(A) A.this);
			it  = map.entrySet().iterator();
		}
		public boolean hasNext (){return it.hasNext()?true:false;}
		public Entry<NNegInt,A> next(){return it.next();}
		public void remove (){}
	}
	
}
