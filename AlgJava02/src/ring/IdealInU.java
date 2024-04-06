package ring;

import topo.Set;
/**
 * The ideal set class - only {@link IdealInU#contains(topo.Element)}
 * needs to be implemented
 * @author adin
 *
 * @param <U>
 */
public abstract class IdealInU<U extends UnitaryCommRing<U>> implements Set<U, IdealInU<U>> {
	/**Constructs an ideal*/
	protected IdealInU() {}
	/**
	 * Returns the intersection ideal
	 */
	public IdealInU<U> intersect(final IdealInU<U> another) {
		final IdealInU<U> cp = this;
		return new IdealInU<U>(){
			public boolean contains (U element){
				return cp.contains(element)&&another.contains(element);
			}
		};
	}
	/**Returns true since any ideal contains
	 * the zero element*/
	public boolean isEmpty() {return false;}

	/**
	 * Returns false since the complement
	 * in the topological closure is open
	 */
	public boolean isOpen() {return false;}

	/**
	 * Returns the union of two ideals,
	 * <b>note</b> the returned object is only
	 * an ideal if either of the two ideals
	 * is contained in the other
	 */
	public IdealInU<U> union(final IdealInU<U> another) {
		final IdealInU<U> cp = this;
		return new IdealInU<U> (){
			public boolean contains (U element){
				return cp.contains(element)||another.contains(element);
			}
		};
	}
	

}
