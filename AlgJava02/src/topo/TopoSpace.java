package topo;

public interface TopoSpace<T extends TopoSpace<T,E>, E extends Element<E>>
		extends Set<E, T> {
	public T complement();
	public boolean isSuperSet(T another);
	public boolean isSubSet (T another);
}
