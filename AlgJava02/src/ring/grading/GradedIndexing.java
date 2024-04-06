package ring.grading;

import java.util.Iterator;
import java.util.Map.Entry;

import util.Tuple;

import group.IndicialMonoid;
import group.NNegInt;
/**
 * The graded index class: any object of this class represents
 * a monoidal element as specified by ...
 * @author adin
 *
 * @param <I> the type of some {@link IndicialMonoid} sub-class
 */
public abstract class GradedIndexing<I extends IndicialMonoid<I>> implements
		IndicialMonoid<GradedIndexing<I>>, Iterable<Entry<NNegInt,I>> {
	/**the index tuple*/
	Tuple<I> indexTuple;
	/**the index length: the number of non-null objects in the
	 * index tuple*/
	private int length;
	/**the highest index*/
	NNegInt maxIndex;
	/**
	 * Constructor: creates an empty graded index tuple
	 * with no index generator set
	 */
	protected GradedIndexing(){super();indexTuple = new Tuple<I>();maxIndex = NNegInt.ZERO;}
	/**
	 * Constructor: creates an graded index tuple
	 * with the exact order of the argument array
	 * <tt>indexTuple</tt>
	 * @param indexTuple the argument array
	 */
	public GradedIndexing(I[] indexTuple){
		this();
		for (int i = 0; i < length;i++){
			if(indexTuple[i]!=null){
				NNegInt index = new NNegInt(i);
				this.indexTuple.setEntry(index,indexTuple[i]);
				if(maxIndex.compareTo(index)<0) maxIndex = index;
				length++;
			}
		}
	}
	/**
	 * Constructs a graded index object, with entries specified by
	 * the argument tuple <tt>indexTuple</tt>
	 * @param indexTuple a tuple of indices
	 */
	public GradedIndexing(Tuple<I> indexTuple){
		this();
		length = indexTuple.length();
		for (Entry<NNegInt,I> entry:indexTuple) {
			NNegInt posIndex = entry.getKey();
			I index = entry.getValue();
			if(index!=null){
				this.indexTuple.setEntry(posIndex, entry.getValue());
				if(maxIndex.compareTo(posIndex)<0) maxIndex = posIndex;
			}
		}
		//if(indexTuple.generator.size()!=0) generator.putAll(indexTuple.generator);
	}
	/**
	 * Constructor: copies the argument
	 * index tuple and its generator set,
	 * if present
	 * @param another
	 */
	public GradedIndexing(GradedIndexing<I> another){
		this(another.indexTuple);
		//if(another.generator.size()!=0) generator.putAll(another.generator);
		length = another.length;
	}

	/**
	 * Returns true if and only if both index instance
	 * have the exact same entries
	 */
	public boolean equals(GradedIndexing<I> another) {
		if(this==another) return true;
		return indexTuple.equals(another.indexTuple)?true:false;
	}
	/**
	 * Implementation of <code>Object.equals()</code>
	 * method to have correct behavior  
	 */
	@SuppressWarnings("unchecked")
	public boolean equals(Object o){
		if(!(o instanceof GradedIndexing)) return false;
		return equals((GradedIndexing<I>)o);
	}
	

	
	/*public long eval() {
		if(generator.size()==0) return -1;
		long eval = 0;
		for (Entry<NNegInt,I> entry:indexTuple){
			I index = entry.getValue();
			f(index);
			eval += getValue().eval();
		}
		return eval;
	}*/
	/**
	 * Returns a deep copy of the original
	 * index tuple - that is, changes at
	 * the returned objects will not be reflected
	 * in the original tuple
	 * @return the index tuple
	 */
	public Tuple<I> getIndexTuple(){
		Tuple<I> cp = new Tuple<I> ();
		for (Entry<NNegInt,I> entry:this) cp.setEntry(entry.getKey(), entry.getValue());
		return cp;
	}
	/**
	 * Returns the index associated with the
	 * given <tt>key</tt> or null if no such
	 * key is present 
	 * @param key the key
	 * @return the index
	 */
	public I getIndex (NNegInt key){
		I index = indexTuple.getValue(key);
		return index==null?null:index;
	}
	
	/**
	 * Returns the highest positional
	 * index in this object (highest key)
	 * @return maximal index
	 */
	public NNegInt getMaxIndex(){
		return maxIndex;
	}
	/**
	 * Re-implementation of <code>Object</code>'s
	 * method to have correct behavior
	 */
	public int hashCode (){
		return indexTuple.hashCode();
	}

	/**
	 * Any instance of this class is comparable
	 */
	public boolean isComparable() {return true;}
	public boolean isDiscrete(){
		Iterator<Entry<NNegInt,I>> it = iterator();
		if(it.hasNext()) return it.next().getValue().isDiscrete();
		return true;
	}
	public boolean isNeutral(){return length()==0?true:false;}
	/**
	 * Returns an iterator over the index
	 * entries - <b>note</b> remove operations
	 * are not supported by this iterator
	 */
	public Iterator<Entry<NNegInt,I>> iterator(){return new EntryIterator();}
	/**
	 * Returns the length of this graded index
	 * object
	 * @return the length
	 */
	public int length(){return indexTuple.length();}
	
	public String toString(){
		if(indexTuple.length()==0) return "(0)";
		StringBuilder sb = new StringBuilder("(");
		String zero = "0", comma = ",";
		NNegInt index = NNegInt.ZERO;
		while (index.compareTo(maxIndex)<=0) {
			I entry = indexTuple.getValue(index);
			if(entry!=null) sb.append(entry.toString());
			else sb.append(zero);
			if(index.compareTo(maxIndex)<0) sb.append(comma);
			index = index.increment();
		}
		sb.append(")");
		return sb.toString();
	}
	/*------------------privates------------------*/
	/**
	 * Inner class - wraps the iterator
	 * returned  by  the set {@link java.util.Map#entrySet()}
	 * to prevent removals
	 * @author adin
	 *
	 */
	private class EntryIterator implements Iterator<Entry<NNegInt,I>> {
		/**the encapsulated (wrapped) set iterator*/
		private final Iterator<Entry<NNegInt,I>> it;
		/**
		 * Private constructor - prevents
		 * instantiation outside the outer class
		 */
		private EntryIterator (){
			it = indexTuple.iterator();
		}
		/**
		 * Just as the wrapped iterator
		 */
		public boolean hasNext(){return it.hasNext();}
		/**
		 * Just as the wrapped iterator
		 */
		public Entry<NNegInt,I> next(){return it.next();}
		/**
		 * Not supported - nothing changed
		 */
		public void remove(){}
		public String toString (){
			return indexTuple.toString();
		}
	}
}
