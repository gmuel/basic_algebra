package module;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import ring.CommRing;
import util.Indexing;
/**
 * The abstract commutative module class - 
 * some module of some ring of type <tt>&ltC&gt</tt>,
 * sub-class of {@link CommRing}
 * @author adin
 *
 * @param <C> the type of the underlying <code>CommRing</code>
 * @param <I> the type of the {@link Indexing} 
 */
public class AbstractCommModule<C extends CommRing<C>, I extends Indexing<I>>
		implements CommModule<AbstractCommModule<C,I>, C, I> {
	/**the coefficient map: mapping each index
	 * to the coefficient*/
	private HashMap<I,C> coeffMap;
	/**
	 * Constructs an empty module
	 * element
	 */
	public AbstractCommModule (){coeffMap = new HashMap<I,C>();}
	/**
	 * Returns the sum <tt>this + another</tt>
	 */
	public AbstractCommModule<C, I> add(AbstractCommModule<C, I> another) {
		AbstractCommModule<C,I> sum = new AbstractCommModule<C,I> ();
		for (Entry<I,C> entry:this) sum.coeffMap.put(entry.getKey(), entry.getValue());
		for (Entry<I,C> entry:another){
			I index = entry.getKey();
			C coeff;
			if((coeff = sum.getValue(index))!=null){
				coeff = coeff.add(entry.getValue());
				if(!coeff.isZero()) sum.coeffMap.put(index, coeff);
				else sum.coeffMap.remove(index);
			} else sum.coeffMap.put(index, coeff);
		}
		return sum;
	}
	/**
	 * Returns true only if both elements
	 * have the same entries
	 */
	public boolean equals(AbstractCommModule<C, I> another) {
		return coeffMap.equals(another.coeffMap);
	}
	/**
	 * Removes all coefficients and their
	 * indices from this element
	 */
	public void clear() {coeffMap.clear();}

	/**
	 * Returns the coefficient
	 * associated with the <tt>index</tt>
	 * or null
	 */
	public C getValue(I index) {
		C val = coeffMap.get(index);
		return val==null?null:val;
	}

	/**
	 * Returns the index associated
	 * with the coefficient <tt>val</tt>
	 * or null
	 */
	public I getIndex(C val) {
		for (Entry<I,C> entry:this){if(entry.getValue().equals(val)) return entry.getKey();}
		return null;
	}
	/**
	 * Returns true if the underlying
	 * ring is discrete or null
	 */
	public boolean isDiscrete(){
		if(this==null) return true;
		Iterator<Entry<I, C>> it = iterator();
		if(it.hasNext())
			return it.next().getValue().isDiscrete();
		return true;
	}
	/**
	 * Returns true if this element is zero
	 */
	public boolean isZero() {return coeffMap.size()==0?true:false;}
	/**
	 * Returns a set view iterator or the
	 * index to coefficient map
	 */
	public Iterator<Entry<I, C>> iterator() {return coeffMap.entrySet().iterator();}
	/**
	 * Returns the scalar multiple
	 */
	public AbstractCommModule<C, I> multiply(C scalar) {
		AbstractCommModule<C,I> scMu = new AbstractCommModule<C,I> ();
		for (Entry<I,C> entry:this){
			C coeff = entry.getValue();
			if(!coeff.isZero())scMu.coeffMap.put(entry.getKey(), entry.getValue());
		}
		return scMu;
	}
	/**
	 * Returns the sum <tt>this + another</tt>
	 */
	public AbstractCommModule<C, I> operate(AbstractCommModule<C, I> another) {return add(another);}
	
	/**
	 * Returns the scalar multiple
	 */
	public AbstractCommModule<C, I> ringAct(C scalar) {return multiply(scalar);}
	/**
	 * Removes the index and its coefficient
	 * from this element and returns the coefficient
	 * or null if no such entry exists
	 */
	public C remove(I index) {return coeffMap.remove(index);}

	/**
	 * Sets the coefficient only if both
	 * arguments are not null and the coefficient
	 * <tt>value</tt> is not zero
	 */
	public void setEntry(I index, C value) {
		if(index==null||value==null) return;
		if(!value.isZero()) coeffMap.put(index, value);
		
	}
}
