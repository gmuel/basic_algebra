package module;

import group.NNegInt;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import ring.Ring;
/**
 * The module generator class - contains a set of generators,
 * such that the underlying module of type <code>R</code> is spanned
 * @author bzfmuell
 *
 * @param <M> the <code>R</code> module type to span
 * @param <R> the ring type
 */
public class Generator<M extends FiniteGenMod<M,R,NNegInt>, R extends Ring<R>> implements Iterable<Entry<NNegInt,M>>{
	/**the generator set*/
	private HashMap<NNegInt,M> generatorSet;
	/**
	 * Constructs the generator set. <b>Note</b>, if <tt>moduleElement[i].equals(moduleElement[j])</tt>
	 * returns true for some indices <tt>0 &#8804 i &lt j &#8804 modulelement.length-1</tt> then the
	 * <tt>j</tt>-th component will not be added
	 * @param moduleElements an array of generators
	 */
	public Generator (M[] moduleElements){
		generatorSet = new HashMap<NNegInt,M> ();
		int count = 0;
		for (M element:moduleElements){
			if(!generatorSet.containsValue(element)) {if(!element.isZero()){generatorSet.put(new NNegInt(count), element);count++;}}
		}
	}
	/**
	 * Constructs a generator set 
	 * @param generatorSet
	 */
	public Generator (Set<M> generatorSet){
		this.generatorSet = new HashMap<NNegInt,M> (generatorSet.size());
		NNegInt count = NNegInt.ZERO;
		for (M element:generatorSet) if(!element.isZero()){this.generatorSet.put(count,element);count.operate(NNegInt.ONE);}
	}
	/**
	 * Returns the generator set as a {@link HashSet}
	 * @return
	 */
	public HashSet<M> getGeneratorSet (){return new HashSet<M>(generatorSet.values());}
	public Iterator<Entry<NNegInt,M>> iterator (){return generatorSet.entrySet().iterator();}
	
	/**
	 * Returns the rank of this generator set, to specify its
	 * cardinality
	 * @return the rank
	 */
	public int rank (){return generatorSet.size();}
}
