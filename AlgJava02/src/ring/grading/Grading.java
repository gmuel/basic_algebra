package ring.grading;


import group.Monoid;
import ring.integer.IntRing;
import topo.AbstractFct;
import util.Indexing;
/**
 * The abstract grading class for arbitrary indexing
 * systems
 * @author adin
 *
 * @param <G> the type of the implementing sub-class
 * @param <I> the type of the indexing system
 */
public abstract class Grading<G extends Grading<G,I>,I extends Indexing<I>> 
extends AbstractFct<I,IntRing>
implements Monoid<G> {
	/**Constructs an empty grading*/
	public Grading(){super();}

}
