package topo;
/**
 * The element interface - provides two methods
 * <ol><li><code>equals(Element)</code> - for equality test</li>
 * <li><code>isDiscrete()</code> - returns true if the topology is discrete</li></ol>
 * @author adin
 *
 * @param <E>
 */
public interface Element<E extends Element<E>> {
	/**
	 * Returns true if both elements are equal
	 * <p><b>Note</b>, the implementation needs
	 * to override {@link Element#equal(Object)}
	 * for proper behavior with respect to the
	 * collection framework
	 * @param another some other element to test
	 * @return true if both are equals
	 */
	public boolean equals(E another);
	/**
	 * Returns true if the underlying topology
	 * is discrete - all single element sets are
	 * "clopen" or closed-open
	 * @return true if discrete
	 */
	public boolean isDiscrete();
}
