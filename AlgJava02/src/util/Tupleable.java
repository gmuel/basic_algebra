package util;
import java.util.Map.Entry;
/**
 * The tuple interface: any subclass of <code>Object</code> may
 * be used as generic type
 * @author adin
 *
 * @param <X>
 */
public interface Tupleable<X,I extends Indexing<I>> extends Iterable<Entry<I,X>> {
	/**
	 * Removes all entries from this <code>Tupleable</code> object
	 */
	public void clear();
	/**
	 * Returns the value associated with the argument <tt>index</tt>
	 * or null if no such index exists
	 * @param index
	 * @return
	 */
	public X getValue (I index);
	/**
	 * Returns the index associated with the argument <tt>val</tt>
	 * or null if no such value exists
	 * @param val
	 * @return
	 */
	public I getIndex (X val);
	public X remove (I index);
	/**
	 * Sets the entry specified by the index and value
	 * @param index
	 * @param value
	 */
	public void setEntry (I index, X value);
}
