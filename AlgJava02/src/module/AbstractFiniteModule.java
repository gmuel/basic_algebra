package module;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import ring.Ring;
import ring.integer.IntRing;
import topo.Element;
import group.AbstractAbel;
import group.NNegInt;
/**
 * The class of finitely generated modules over some ring of
 * type <tt>&ltR&gt</tt>
 * @author adin
 *
 * @param <R> the ring type
 */
public class AbstractFiniteModule<R extends Ring<R>> extends
		AbstractAbel<AbstractFiniteModule<R>> implements
		FiniteGenMod<AbstractFiniteModule<R>, R, NNegInt>, Element<AbstractFiniteModule<R>> {
	/**the coefficient map*/
	private TreeMap<NNegInt,R> coeffMap;
	/**the number of generating elements*/
	private NNegInt rank;
	/**
	 * Constructs an empty module element
	 */
	public AbstractFiniteModule() {
		super();
		rank = NNegInt.ZERO;
		coeffMap  = new TreeMap<NNegInt,R>();
	}
	/**
	 * Constructs a module element with coefficients
	 * specified by the argument <tt>array</tt>
	 * @param array the argument array
	 */
	public AbstractFiniteModule (R[] array){
		this();
		setEntries(array);
	}
	/**
	 * Constructs a module element with entries
	 * specified by the returned sequence of
	 * elements of type <code>R</code> provided
	 * by the parameter <tt>modEntries</tt>
	 * @param modEntries an <code>Iterable</code>
	 * defining the module element entries 
	 */
	public AbstractFiniteModule (Iterable<R> modEntries){
		this();
		setEntries(modEntries);
	}
	/**
	 * Constructs a copy module element of
	 * the original <tt>another</tt> module element
	 * @param another some other element
	 */
	public AbstractFiniteModule(AbstractFiniteModule<R> another){
		this();
		for (Entry<NNegInt,R> entry:another) {
			setEntry(entry.getKey(),entry.getValue());
		}
	}
	/**
	 * Returns the sum of this and another element
	 */
	public AbstractFiniteModule<R> add(AbstractFiniteModule<R> another) {
		if(isZero()&&another.isZero()) return new AbstractFiniteModule<R>();
		AbstractFiniteModule<R> sum = new AbstractFiniteModule<R>(this);
		for (Entry<NNegInt,R> entry:another){
			NNegInt index = entry.getKey();
			R coef;
			if((coef = sum.getValue(index))!=null){
				coef = coef.add(entry.getValue());
				if(coef.isZero()) sum.remove(index);
				else sum.setEntry(index, coef);
			} else sum.setEntry(index, entry.getValue());
		}
		return sum;
	}
	/**
	 * Clears all entries from the element
	 */
	public void clear() {
		coeffMap.clear();
		rank = NNegInt.ZERO;
	}
	/**
	 * Returns true if and only if both elements
	 * have the same entries
	 */
	public boolean equals(AbstractFiniteModule<R> another) {return coeffMap.equals(another.coeffMap);}
	/**
	 * Implemented for correct behavior
	 */
	public boolean equals(Object o){
		if(this==o) return true;
		if(o instanceof AbstractFiniteModule){
			@SuppressWarnings("unchecked")
			AbstractFiniteModule<R> cp = (AbstractFiniteModule<R>) o;
			return equals(cp);
		}
		return false;
	}
	/**
	 * Implemented for correct behavior
	 */
	public int hashCode (){
		int hash = 0;
		for (Entry<NNegInt,R> entry:coeffMap.entrySet()){
			hash += 17*entry.getKey().hashCode();
			hash += 37*entry.getValue().hashCode();
		}
		return hash;
	}
	/**
	 * Returns the generator set of this module element, that
	 * is the set of module generators, for those
	 * <tt>this in &ltx: x in generator set&gt</tt> still holds
	 */
	public Set<AbstractFiniteModule<R>> getGenerators() {
		HashSet<AbstractFiniteModule<R>> gens = new HashSet<AbstractFiniteModule<R>>();
		for (Entry<NNegInt,R>entry:this) {
			AbstractFiniteModule<R> mod = new AbstractFiniteModule<R> ();
			mod.setEntry(entry.getKey(), entry.getValue());
			gens.add(mod);
		}
		return gens;
	}
	/**
	 * Returns the coefficient value, if present or null
	 */
	public R getValue(NNegInt index) {return coeffMap.get(index);}
	/**
	 * Returns the index associated with the given value <tt>val</tt> or null
	 */
	public NNegInt getIndex(R val) {
		for (Entry<NNegInt,R> entry:this){if(entry.getValue().equals(val)) return entry.getKey();}
		return null;
	}

	public boolean isDiscrete(){
		Iterator<Entry<NNegInt,R>> it = iterator();
		if(it.hasNext()) return it.next().getValue().isDiscrete();
		return true;
	}

	/**
	 * Returns an iterator of the entries
	 */
	public Iterator<Entry<NNegInt, R>> iterator() {return new EntryIt();}
	/**
	 * Returns true if no entries present
	 */
	public boolean isZero() {return coeffMap.size()==0;}
	
	/**
	 * Returns the rank
	 */
	public int rank() {return (int) rank.eval();}
	
	/**
	 * Removes the entry if present
	 */
	public R remove(NNegInt index) {
		R entry = coeffMap.remove(index);
		if(entry!=null) {
			rank = rank.decrement();
			return entry;
		}
		return null;
	}
	/**
	 * Returns the scalar multiple
	 */
	public AbstractFiniteModule<R> ringAct(R scalar) {
		AbstractFiniteModule<R> scal = new AbstractFiniteModule<R> ();
		for (Entry<NNegInt,R> entry:this) scal.setEntry(entry.getKey(), entry.getValue().multiply(scalar));
		return scal;
	}

	/**
	 * Sets the coefficient entry, if and only if
	 * <tt>value!=null&&index!=null</tt> and
	 * <tt>!value.isZero()</tt> return true
	 */
	public void setEntry(NNegInt index, R value) {
		if(value!=null&&index!=null){
			if(!value.isZero()) {
				R formerCoeff = coeffMap.put(index, value);
				if(formerCoeff==null) rank = rank.increment();
			}
			
		}
	}

	public String toString(){
		if(isZero()) return "(0)";
		String zero = "0", comma = ",";
		StringBuilder sb = new StringBuilder ("(");
		NNegInt index = NNegInt.ZERO, highInd = coeffMap.lastKey();
		while (true){
			R coeff = coeffMap.get(index);
			if(coeff!=null) sb.append(coeff.toString());
			else sb.append(zero);
			if(index.compareTo(highInd)<0) sb.append(comma);
			else break;
			index = index.increment();
		}
		sb.append(")");
		return sb.toString();
	}
	/*------------------privates------------------*/
	private void setEntries(Iterable<R> iter){
		NNegInt counter = NNegInt.ZERO;
		for (R modEntry : iter){
			if(modEntry!=null&&!modEntry.isZero()) {
				setEntry(counter,modEntry);
				rank = rank.increment();
			}
			counter = counter.increment();
		}
	}
	private void setEntries(R[] iter){
		NNegInt counter = NNegInt.ZERO;
		for (R modEntry : iter){
			if(modEntry!=null&&!modEntry.isZero()) {
				setEntry(counter,modEntry);
				rank = rank.increment();
			}
			counter = counter.increment();
		}
	}
	/**
	 * Auxiliary class: wraps the iterator interface to
	 * give control over rank and coefficient entries
	 * @author adin
	 *
	 */
	private class EntryIt implements Iterator<Entry<NNegInt,R>> {
		/**the actual iterator*/
		private Iterator<Entry<NNegInt,R>> it;
		/**
		 * Constructs the iterator
		 */
		private EntryIt(){it = coeffMap.entrySet().iterator();}
		/**API-conforming*/
		public boolean hasNext() {return it.hasNext();}

		/**API-conforming*/
		public Entry<NNegInt, R> next() {return it.next();}
		/**
		 * Removes the entry return last by this iterator
		 * or throws an exception if <tt>next()</tt> hasn't been called
		 * */
		public void remove() throws IllegalStateException{
			it.remove();
			rank = rank.decrement();
			
		}
		
	}
	public static void main(String[] args){
		AbstractFiniteModule<IntRing> m1 = new AbstractFiniteModule<IntRing>(
			new IntRing[]{IntRing.ONE, null, IntRing.M_ONE}
		);
		System.out.println(String.format("v = \n%1$s",m1.toString()));
		m1.remove(NNegInt.ZERO);
		System.out.println(String.format("v = \n%1$s",m1.toString()));
	}
}
