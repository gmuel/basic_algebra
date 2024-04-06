package topo.relation;

import topo.Element;
import util.Pair;
/**
 * A class wrapping exact two instances of a
 * given type <tt>E</tt>, equivalent to
 * <tt>E x E</tt>
 * @author adin
 *
 * @param <E> type of the pair to wrap
 */
public class PairedElement<E extends Element<E>>
		extends Pair<E> implements Element<PairedElement<E>> {
	/**
	 * Constructs a paired element
	 * with first entry
	 * @param first
	 * @param second
	 */
	public PairedElement(E first, E second) {
		super(first, second);
	}
	/**
	 * Constructs a paired
	 * element by copying <tt>pair</tt>
	 * @param pair the original
	 */
	public PairedElement(Pair<E> pair){
		super(pair.getFirst(),pair.getSecond());
	}
	/**
	 * Returns true as in super class
	 */
	public boolean equals(PairedElement<E> another) {return super.equals(another);}
	/**
	 * Returns the class object of the
	 * two element
	 * @return the class object
	 */
	public Class<?> getElementClass(){
		E first = getFirst(), second = getSecond();
		return first!=null?first.getClass():second!=null?second.getClass():null;
	}
	/**
	 * Returns true if the underlying element
	 * is discrete
	 */
	public boolean isDiscrete(){return getFirst().isDiscrete();}
}
