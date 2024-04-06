package algebras;

import java.util.Map;
import java.util.Map.Entry;

import group.Group;
import group.representation.IndicialGroup;
import ring.AbstractCommRing;
/**
 * The abstract group algebra class:
 * all coefficients are of type {@link AbstractCommRing}
 * and the indices of type {@link IndicialGroup} 
 * @author adin
 *
 * @param <A> the coefficient type
 * @param <G> the index type
 */
public class AGroupAlg<A extends AbstractCommRing<A>, G extends Group<G>>
		extends AbstractAlgebra<A, IndicialGroup<G>> {
	/**
	 * Constructs the zero element
	 */
	public AGroupAlg (){super();}
	/**
	 * Constructs an element
	 * with coefficient entries
	 * specified by the <code>Iterable</code>
	 * object
	 * @param coeffIterable some index to coefficient <tt>Iterable</tt>
	 */
	public AGroupAlg (Iterable<Entry<G,A>> coeffIterable){
		this();
		for (Entry<G,A> coeffEntry:coeffIterable){
			G groupInd = coeffEntry.getKey();
			A    coeff = coeffEntry.getValue();
			if(groupInd!=null&&coeff!=null) setEntry(new IndicialGroup<G>(groupInd),coeff);
		}
	}
	/**
	 * Constructs an element
	 * with coefficient entries
	 * specified by the argument
	 * <tt>coeffMap</tt>
	 * @param coeffMap the coefficient map
	 */
	public AGroupAlg (Map<G, A> coeffMap){
		this(coeffMap.entrySet());
	}
	/**
	 * Constructs a copy instance
	 * of the <tt>original</tt> object
	 * @param original the original element
	 */
	public AGroupAlg (AbstractAlgebra<A, IndicialGroup<G>> original){
		this();
		for (Entry<IndicialGroup<G>,A> coeffEntry:original){
			IndicialGroup<G> groupInd = coeffEntry.getKey();
			A    coeff = coeffEntry.getValue();
			if(groupInd!=null&&coeff!=null) setEntry(new IndicialGroup<G>(groupInd),coeff);
		}
	}
	/**Overrides super class method*/
	public AGroupAlg<A, G> add(AbstractAlgebra<A, IndicialGroup<G>> another){
		return new AGroupAlg<A, G> (super.add(another));
	}
	/**Overrides super class method*/
	public AGroupAlg<A, G> addInverse(){return new AGroupAlg<A, G> (super.addInverse());}
	/**Overrides super class method - always returns
	 * true since any object of this class is associative*/
	public boolean isAssociative (){return true;} 
	/**Overrides super class method*/
	public AGroupAlg<A, G> multiply (A scalar){return new AGroupAlg<A, G>(super.multiply(scalar));}
	/**Overrides super class method*/
	public AGroupAlg<A, G> multiply (AbstractAlgebra<A, IndicialGroup<G>> another){return new AGroupAlg<A, G>(super.multiply(another));}
	/**Overrides super class method*/
	public AGroupAlg<A, G> operate (AbstractAlgebra<A, IndicialGroup<G>> another){return new AGroupAlg<A, G>(super.operate(another));}
	/**Overrides super class method*/
	public AGroupAlg<A, G> ringAct (A scalar){return new AGroupAlg<A, G>(super.ringAct(scalar));}
}
