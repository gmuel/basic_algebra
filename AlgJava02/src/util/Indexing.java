package util;

public interface Indexing<I extends Indexing<I>> {
	public long eval();
	public boolean equals(I another);
	public boolean isComparable();
}
