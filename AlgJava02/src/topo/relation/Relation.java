package topo.relation;

import topo.Element;

public interface Relation<E extends Element<E>> {
	public PairedElement<E> basePoint();
	public Relation<E>  inverse();
	public boolean isClosed();
	public boolean isRelated(E first, E second);
}
