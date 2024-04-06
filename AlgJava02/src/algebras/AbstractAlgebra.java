package algebras;

import group.IndicialMonoid;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import module.AbstractCommModule;
import module.CommModule;

import ring.AbstractCommRing;
/**
 * The abstract algebra class - provides convenient
 * methods as multiplication and addition.
 * <p><b>Note</b>, the required method {@link AbstractAlgebra#isAssociative()}
 * returns false by default - for the associative case simply sub-class
 * and override super class's methods.
 * <p>Any instance of this class has two type parameters
 * <ol><li><tt>A</tt> - the type of the ring of all coefficients,
 * sub class of {@link AbstractCommRing}</li>
 * <li><tt>I</tt> - the type of the indices, sub class of
 * {@link IndicialMonoid}</li>
 * </ol>
 * @author adin
 *
 * @param <A> the type of the ring
 * @param <I> the type of the indices
 */
public class AbstractAlgebra<A extends AbstractCommRing<A>, I extends IndicialMonoid<I>> implements Algebra<AbstractAlgebra<A,I>,A,I>, CommModule<AbstractAlgebra<A,I>,A,I> {
	/**the module element: maps all indices to the
	 * coefficients*/
	protected AbstractCommModule<A,I> modElement;
	/**
	 * Constructs the zero element
	 */
	public AbstractAlgebra (){modElement = new AbstractCommModule<A,I> ();}
	/**
	 * Constructs an element with
	 * coefficients specified by
	 * the <code>Iterable</code> object
	 * <tt>moduleIter</tt>
	 * @param moduleIter the <code>Iterable</code>
	 */
	public AbstractAlgebra (Iterable<Entry<I,A>> moduleIter){
		this();
		for (Entry<I,A> modEntries:moduleIter){
			I index = modEntries.getKey();
			A coeff = modEntries.getValue();
			if(index!=null&&coeff!=null) modElement.setEntry(index, coeff);
		}
	}
	/**
	 * Constructs an element with
	 * coefficients specifed by
	 * the <code>Map</code>object
	 * <tt>modElement</tt>
	 * @param modElement the <code>Map</code>
	 */
	public AbstractAlgebra (Map<I,A> modElement){
		this(modElement.entrySet());
		//for (Entry<I,A> entry:modElement) this.modElement.setEntry(entry.getKey(),entry.getValue());
	}
	/**
	 * Returns the sum <tt>this + another</tt>
	 */
	public AbstractAlgebra<A,I> add(AbstractAlgebra<A,I> another){
		AbstractAlgebra<A,I> sum = new AbstractAlgebra<A,I> (this);
		for (Entry<I, A> entry: another){
			I index = entry.getKey();
			A coeff = entry.getValue(), sumCoeff = null;
			if((sumCoeff = sum.getValue(index))!=null) {
				sumCoeff = sumCoeff.add(coeff);
				if(sumCoeff.isZero()) sum.remove(index);
				else sum.setEntry(index, sumCoeff);
			} else sum.setEntry(index,coeff);
		}
		return sum;
	}
	/**
	 * Returns the additive inverse
	 */
	public AbstractAlgebra<A,I> addInverse(){
		AbstractAlgebra<A,I> addInv = new AbstractAlgebra<A,I> ();
		for (Entry<I,A> algEntry:this)
			addInv.setEntry(algEntry.getKey(), algEntry.getValue().addInverse());
		return addInv;
	}
	/**
	 * Removes all coefficients and indices
	 * from the element
	 */
	public void clear() {modElement.clear();}
	/**
	 * Returns true only if all pairs of
	 * indices and coefficients are equal
	 */
	public boolean equals(AbstractAlgebra<A,I> another) {return modElement.equals(another.modElement);}
	/**
	 * Returns true only if the argument
	 * is of same type and its own <code>equals()</code>
	 * method returns true
	 */
	public boolean equals(Object o){
		if(this==o) return true;
		if(!(o instanceof AbstractAlgebra)) return false;
		@SuppressWarnings("unchecked")
		AbstractAlgebra<A,I> cp = (AbstractAlgebra<A,I>) o;
		return equals(cp);
	}
	/**
	 * Returns the index for the
	 * given coefficient <tt>val</tt>,
	 * or null if no such coefficient
	 * was found
	 */
	public I getIndex(A val) {
		I index = modElement.getIndex(val);
		return index==null?null:index;
	}
	/**
	 * Returns the coefficient
	 * for the given <tt>index</tt>
	 * or null if no such entry exists
	 */
	public A getValue(I index) {return modElement.getValue(index);}
	/**
	 * Overridering sub class's method,
	 * to ensure proper behavior in 
	 * <code>HashMap</code>'s
	 */
	public int hashCode(){
		int hash = 0;
		for (Entry<I, A> entry:this){
			hash += 17*entry.getKey().hashCode();
			hash += 37*entry.getValue().hashCode();
		}
		return hash;
	}
	/**
	 * Returns the scalar multiple of this
	 * element
	 */
	public AbstractAlgebra<A, I> multiply(A scalar) {return new AbstractAlgebra<A,I>(modElement.multiply(scalar));}
	/**
	 * Returns the product <tt>this * another</tt>
	 */
	public AbstractAlgebra<A, I> multiply(AbstractAlgebra<A, I> another) {
		AbstractAlgebra<A, I> product = new AbstractAlgebra<A, I> ();
		for (Entry<I, A> entries1:this){
			I index1 = entries1.getKey();
			A coeff1 = entries1.getValue();
			for (Entry<I, A> entries2:another){
				I index2 = entries2.getKey(), newIndex = index1.operate(index2);
				A coeff2 = entries2.getValue(), prod = coeff1.multiply(coeff2), newCoeff = null;
				if (newIndex==null) continue;
				if((newCoeff = product.getValue(newIndex))!=null) {
					newCoeff = newCoeff.add(prod);
					if(newCoeff.isZero()) product.remove(newIndex);
					else product.setEntry(newIndex, newCoeff);
				} else product.setEntry(newIndex, prod);
			}
		}
		return product;
	}
	/**
	 * Dummy implementation . returns false
	 * by default. Any associative sub class
	 * must override this method
	 */
	public boolean isAssociative() {return false;}

	/**
	 * Returns true only if this element
	 * is null or zero or one or more entries
	 * is discrete (the monoidal index and its
	 * coeffficient)
	 */
	public boolean isDiscrete(){return modElement.isDiscrete();}
	/**
	 * Returns true for the zero element
	 */
	public boolean isZero() {return modElement.isZero();}
	/**
	 * Returns an iterator over
	 * the set view of this element
	 * <p><b>Note</b>, changes to
	 * the returned iterator will be
	 * reflected in this element
	 */
	public Iterator<Entry<I, A>> iterator() {return modElement.iterator();}	
	/**
	 * Returns the sum - the same as calling
	 * {@link AbstractAlgebra#add(AbstractAlgebra)}
	 */
	public AbstractAlgebra<A,I> operate(AbstractAlgebra<A,I> another) {return add(another);}
	/**
	 * Returns the scalar multiple of this
	 * element
	 */
	public AbstractAlgebra<A, I> ringAct(A scalar) {return multiply(scalar);}
	
	/**
	 * Removes the coefficient and its index from
	 * this element and returns it
	 * <p><b>Note</b>, a null return object indicates
	 * that no such entry was found
	 */
	public A remove(I index) {return modElement.remove(index);}
	/**
	 * Sets the coefficient only if both
	 * arguments are not null and the coefficient
	 * is not zero
	 */
	public void setEntry(I index, A value) {modElement.setEntry(index, value);}
		
}