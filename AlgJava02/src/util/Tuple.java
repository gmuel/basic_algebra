package util;

import group.NNegInt;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;
/**
 * The tuple class: note any instance of this class is a mutable object.
 * To use objects of this class as keys in {@link java.util.Map}s implement
 * a final subclass overriding the {@link Tuple#clear()} and {@link Tuple#setEntry(int, Object)}
 * as follows:<br />
 * <tt>public void setEntry (int index, X value){}</tt><br />
 * <tt>public void clear (){}</tt>
 * <br />Although, upcasting renders the object mutable
 * @author gmueller
 *
 * @param <X>
 */
public class Tuple<X> implements Tupleable<X,NNegInt>{
	private int length;
	/**a sorted map of all tuple entries*/
	private TreeMap<NNegInt,X> tuple;
	/**
	 * Constructs the default (empty) tuple object
	 */
	public Tuple (){tuple = new TreeMap<NNegInt,X> ();}
	/**
	 * Constructs the tuple <tt>(tuple[0],...,tuple[tuple.length-1])</tt>,
	 * where <tt>tuple</tt> is the argument array. <b>Note</b>, that null
	 * entries are ignored.
	 * @param tuple the argument array
	 */
	public Tuple (X[] tuple){
		this();
		for (int i = 0; i < tuple.length;i++) {
			if(tuple[i]!=null) {
				this.tuple.put(new NNegInt(i), tuple[i]);
				length++;
			}
			
		}
	}
	/**
	 * Removes all entries from this tuple
	 */
	public void clear (){tuple.clear();length = 0;}
	/**
	 * Returns true if and only if <tt>o</tt> is of same
	 * type and all its entries (with exactly the same index)
	 * are present in both instances
	 */
	
	public boolean equals (Object o){
		if(o == this) return true;
		if(!(o instanceof Tuple)) return false;
		@SuppressWarnings("unchecked")
		Tuple<Object> cp = (Tuple<Object>) o;
		Iterator<Entry<NNegInt,X>> it1 = iterator();
		Iterator<Entry<NNegInt,Object>> it2 = cp.iterator();
		while(it1.hasNext()&&it2.hasNext()){
			Entry<NNegInt,X> entry1 = it1.next();
			Entry<NNegInt,Object> entry2 = it2.next();
			if(!entry1.getKey().equals(entry2.getKey())||!entry1.getValue().equals(entry2.getValue())) return false;
		}
		if(it1.hasNext()||it2.hasNext()) return false;
		return true;
	}
	public NNegInt getIndex (X val){
		for (Entry<NNegInt,X> entry:this) if(entry.getValue().equals(val)) return entry.getKey();
		return null;
	}
	/**
	 * Returns the value associated with the <tt>index</tt>
	 * or null if no such entry exists
	 * @param index the index
	 * @return the value
	 */
	public X getValue (NNegInt index){X value; return (value = tuple.get(index))==null?null:value;}
	
	public int hashCode (){
		int hash = 0;
		for (Entry<NNegInt, X> entries:this) hash += (2*entries.getKey().hashCode()+1)*entries.getValue().hashCode();
		return hash;
	}
	public int length(){
		if(length==0) length = tuple.size();
		return length;
	}
	public Iterator<Entry<NNegInt, X>> iterator() {return new TupleIterator();}
	public X remove (NNegInt index){
		X entry = tuple.get(index);
		if(entry==null) return null;
		length--;
		return entry;
	}
	/**
	 * Sets the entry at position <tt>index</tt> to <tt>value</tt> only
	 * if <code>(value!=null)</code> returns true
	 * @param index the index
	 * @param value the value (non null!)
	 * @throws IndexOutOfBoundsException negative index
	 */
	public void setEntry (NNegInt index, X value) throws IndexOutOfBoundsException {
		if(index.compareTo(NNegInt.ZERO)<0) throw new IndexOutOfBoundsException ("\nNegative index: "+index);
		if(value!=null) {
			tuple.put(index, value);
			length++;
		}
	}
	
	/**
	 * Returns a string representation of this tuple object
	 */
	
	public String toString (){
		StringBuilder sb    = new StringBuilder ("(");
		String        comma = ","; 
		Iterator<Entry<NNegInt,X>> it = iterator();
		while (it.hasNext()) {
			Entry<NNegInt,X> entry = it.next();
			sb.append(entry.getValue().toString());
			if(it.hasNext()) sb.append(comma);
		}
		sb.append(")");
		return sb.toString();
	}
	
	private class TupleIterator implements Iterator<Entry<NNegInt,X>> {
		private Iterator<Entry<NNegInt,X>> innerIt;
		
		private TupleIterator (){
			innerIt = tuple.entrySet().iterator();
		}
		
		public boolean hasNext() {return innerIt.hasNext();}

		
		public Entry<NNegInt, X> next() {
			return innerIt.next();
		}

		
		public void remove() {
			length--;
			innerIt.remove();
		}
		
	}
}
