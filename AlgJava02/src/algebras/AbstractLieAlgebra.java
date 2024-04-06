package algebras;

import group.IndicialMonoid;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import module.AbstractCommModule;

import ring.AbstractCommRing;
import util.Pair;
/**
 * The abstract Lie-Algebra class - by default
 * the {@link AbstractLieAlgebra#isAssociative()} returns
 * false, although sub classes can change this behavior
 * @author adin
 *
 * @param <A> the ring type, sub class of {@link AbstractCommRing}
 * @param <I> the indexing type, sub class of {@link IndicialMonoid}
 */
public class AbstractLieAlgebra<A extends AbstractCommRing<A>, I extends IndicialMonoid<I>> extends AbstractAlgebra<A,I>{
	/**a set of generators of this Lie-algebra*/
	private HashSet<AbstractCommModule<A,I>> generatorSet;
	/**the relation map of all generators*/
	private HashMap<Pair<AbstractCommModule<A,I>>,AbstractCommModule<A,I>> generatorMap;
	/**
	 * Constructs the zero element
	 */
	public AbstractLieAlgebra (){
		super();
		initGenerator();
	}
	/**
	 * Constructs the algebra element specified
	 * by the given <code>Iterable</code> object
	 * <tt>algElement</tt> 
	 * @param algElement the <code>Iterable</code>
	 */
	public AbstractLieAlgebra (Iterable<Entry<I,A>> algElement){
		super(algElement);
		initGenerator();
	}
	/**
	 * Constructs an element by copying
	 * the <tt>algElement</tt>
	 * @param algElement the original to copy
	 */
	public AbstractLieAlgebra (AbstractAlgebra<A,I> algElement){
		this(algElement.modElement);
	}
	/**Overrides super class method*/
	public AbstractLieAlgebra<A, I> add(AbstractAlgebra<A, I> another){
		return new AbstractLieAlgebra<A, I> (super.add(another));
	}
	/**Overrides super class method*/
	public AbstractLieAlgebra<A, I> addInverse(){
		return new AbstractLieAlgebra<A, I>(super.addInverse());
	}
	/**Overrides super class method:
	 * <p>by default always false unless
	 * implemented differently*/
	public boolean isAssociative() {return false;}
	/**Overrides super class method*/
	public AbstractLieAlgebra<A, I> multiply(A scalar) {
		return new AbstractLieAlgebra<A,I> (super.multiply(scalar));
	}
	/**
	 * Returns the Lie-bracket:
	 * <p><tt>[this,another] := this * another - another * this</tt>, where
	 * <tt>*</tt> is the multiplication of the underlying <code>AbstractAlgebra</tt>
	 */
	public AbstractLieAlgebra<A, I> multiply(AbstractAlgebra<A, I> another) {
		AbstractAlgebra<A,I> left = super.multiply(another), right = another.multiply(this).addInverse();
		return new AbstractLieAlgebra<A,I> (left.add(right));
	}
	/**Overrides super class method*/
	public AbstractLieAlgebra<A, I> operate(AbstractAlgebra<A, I> another){
		return new AbstractLieAlgebra<A, I> (super.add(another));
	}
	/**Overrides super class method*/
	public AbstractLieAlgebra<A, I> ringAct(A scalar) {
		return new AbstractLieAlgebra<A,I> (super.multiply(scalar));
	}
	/**
	 * Adds all generator elements in <tt>generators</tt>
	 * such that for each such element <tt>e</tt> the call
	 * <code>e.isZero()</code> returns false
	 * @param generators
	 */
	public void setGenerators(Set<AbstractCommModule<A,I>> generators){
		for (AbstractCommModule<A,I> generator:generators){if(!generator.isZero()) generatorSet.add(generator);}
	}
	/**
	 * 
	 * @param genMap
	 */
	public void setGenerators(Map<Pair<AbstractCommModule<A,I>>,AbstractCommModule<A,I>> genMap){
		for (Entry<Pair<AbstractCommModule<A,I>>,AbstractCommModule<A,I>> genEntries:genMap.entrySet()){
			Pair<AbstractCommModule<A,I>> genPairs = genEntries.getKey();
			AbstractCommModule<A,I> first = genPairs.getFirst(), second = genPairs.getSecond();
			if(!first.isZero()&&!second.isZero()){
				generatorMap.put(genPairs, genEntries.getValue());
				if(!generatorSet.contains(first)) generatorSet.add(first);
				if(!generatorSet.contains(second))generatorSet.add(second);
			}
		}
	}
	private void initGenerator (){
		generatorSet = new HashSet<AbstractCommModule<A,I>>();
		generatorMap = new HashMap<Pair<AbstractCommModule<A,I>>,AbstractCommModule<A,I>> ();
	}
}
