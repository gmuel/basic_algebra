package topo.relation;

import topo.Element;
import topo.Set;

/**
 * The equivalence relation interface
 * @author adin
 *
 * @param <E> the type of the {@link Element}
 * @param <S> the type of the {@link Set}
 */
public interface EquiRel<E extends Element<E>, S extends Set<E,S>> extends Relation<E> {
	/**
	 * Returns the equivalence class
	 * containing the given <tt>element</tt>
	 * @param element the equivalence class defining element
	 * @return the equivalence class
	 */
	public S equiClass(E element);
}
