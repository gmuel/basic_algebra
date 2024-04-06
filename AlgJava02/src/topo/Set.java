package topo;
/**
 * The topological set interface -
 * defines basic methods for set
 * operations as containment or
 * intersection
 * @author adin
 *
 * @param <E> the type of the {@link Element}
 * @param <S> the type of the implementing {@link Set}
 */
public interface Set<E extends Element<E>,S extends Set<E,S>> {
	/**
	 * Returns true if <tt>element</tt>
	 * is contained in this set
	 * @param element the element to test
	 * @return true if contained
	 */
	public boolean contains(E element);
	/**
	 * Returns the intersection of
	 * this and <tt>another</tt> set
	 * @param another some other set
	 * @return the intersection
	 */
	public S intersect(S another);
	/**
	 * Returns true if and only if
	 * this set is empty
	 * @return
	 */
	public boolean isEmpty();
	/**
	 * Returns true if this set
	 * is open
	 * @return true if open
	 */
	public boolean isOpen();
	/**
	 * Returns the union set
	 * of this and <tt>another</tt>
	 * set
	 * @param another some other set
	 * @return the union
	 */
	public S union(S another);
}

