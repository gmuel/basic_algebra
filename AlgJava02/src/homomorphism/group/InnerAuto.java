package homomorphism.group;

import topo.AbstractElFct;
import util.Pair;
import group.AbstractNAG;
import group.NonAbelGroup;
/**
 * The inner automorhism class: that is all conjugation
 * maps <tt>l_g r_g :G -> G, h mapsto g^-1 h g</tt> for
 * some arbitrary non-abelian group of type &ltG&gt,
 * where
 * <ol>
 * <li><tt>l_g</tt> is the left translation with the inverse of <tt>g</tt></li>
 * <li><tt>r_g</tt> is the right translation with <tt>g</tt></li>
 * </ol>
 * for some <tt>g in G</tt>
 * @author adin
 *
 * @param <G> type of sub class {@link AbstractNAG}
 */
public class InnerAuto<G extends AbstractNAG<G>> extends AbstractElFct<G,G>//extends AbstractNAG<InnerAuto<G>>
	//implements GroupHomo<InnerAuto<G>, G, G> {
	implements NonAbelGroup<InnerAuto<G>>{
	/**the conjugator pair: <code>conjugator.getFirst()</code>
	 * returns the element <tt>g</tt> for <tt>l_g r_g</tt>*/
	private Pair<G> conjugator;
	/**
	 * Constructs in inner automorphism
	 * @param conj the conjugation element <tt>g in G</tt>
	 */
	public InnerAuto(G conj) {
		super();
		if (conj.isNeutral())  conjugator = new Pair<G> (conj,conj);
		else conjugator = new Pair<G> (conj,conj.inverse());
	}
	/**
	 * Returns true if both automorphims are equal	
	 */
	public boolean equals(InnerAuto<G> another){
		return conjugator.equals(another.conjugator)?true:false;
	}
	/**
	 * Overrides super-class method: to
	 * conform to class own <code>equals(InnerAuto)</code>
	 */
	public boolean equals(Object o){
		if(this==o) return true;
		if(!(o instanceof InnerAuto)) return false;
		@SuppressWarnings("unchecked")
		InnerAuto<G> cp = (InnerAuto<G>)o;
		return equals(cp);
	}
	/**
	 * Computes the conjugate
	 * of the current argument
	 */
	public void f() {
		if(arg!=null)
			val = conjugator.getSecond().multiply(
					arg.multiply(conjugator.getFirst()));
	}
	/**
	 * Returns the inverse of the conjugation element
	 * <tt>g in G</tt>
	 * @return the left translator
	 */
	public G getLeftTranslator(){return conjugator.getSecond();}
	/**
	 * Returns the conjugation element
	 * <tt>g in G</tt>
	 * @return the right translator
	 */
	public G getRightTranslator(){return conjugator.getFirst();}
	public int hashCode (){return conjugator.hashCode();}
	public InnerAuto<G> inverse() {
		return new InnerAuto<G> (conjugator.getSecond());
	}
	
	public boolean isDiscrete(){return arg.isDiscrete();}
	public boolean isNeutral(){return conjugator.getFirst().isNeutral()?true:false;}
	/**
	 * Returns the composed conjugation map
	 * <tt>l_(this*another) r_(this*another)</tt>
	 */
	public InnerAuto<G> multiply(InnerAuto<G> another) {
		return new InnerAuto<G> (conjugator.getFirst().multiply(
				another.conjugator.getFirst()));
	}
	public InnerAuto<G> operate (InnerAuto<G> another){return multiply(another);}
	
	public String toString (){
		return String.format("l_%1$s r_%1$s", conjugator.getFirst().toString());
	}
	
}
