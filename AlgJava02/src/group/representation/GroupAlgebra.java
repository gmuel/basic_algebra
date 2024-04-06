package group.representation;

import java.util.Map;
import java.util.Map.Entry;

import field.ComplexDouble;

import group.Group;

import algebras.AGroupAlg;
import algebras.AbstractAlgebra;
/**
 * The complex group algebra class - its
 * sole type parameter is the underlying
 * {@link Group}
 * @author adin
 *
 * @param <G> the group type
 */
public class GroupAlgebra<G extends Group<G>> extends
		AGroupAlg<ComplexDouble, G>{
	/**
	 * Constructs the zero element
	 */
	public GroupAlgebra(){super();}
	/**
	 * Constructs the element
	 * with coefficient entries
	 * specified by the <code>Iterable</code>
	 * object <tt>modIter</tt>
	 * @param modIter the <code>Iterable</code>
	 */
	public GroupAlgebra(Iterable<Entry<G,ComplexDouble>> modIter){
		super(modIter);
	}
	/**
	 * Constructs the element
	 * with coefficient entries
	 * specified by the <code>Map</code>
	 * object <tt>moduleMap</tt>
	 * @param moduleMap the coefficient map
	 */
	public GroupAlgebra(Map<G,ComplexDouble> moduleMap){
		super(moduleMap.entrySet());
	}
	/**
	 * Constructs a copy by copying
	 * the <tt>original</tt> instance
	 * @param original the original
	 */
	public GroupAlgebra(AbstractAlgebra<ComplexDouble, IndicialGroup<G>> original){
		super(original);
	}
	/**Overrides sub class method*/
	public GroupAlgebra<G> add(AbstractAlgebra<ComplexDouble,IndicialGroup<G>> another){
		return new GroupAlgebra<G>(super.add(another));
	}
	/**Overrides sub class method*/
	public GroupAlgebra<G> addInverse() {
		return new GroupAlgebra<G>(super.addInverse());
	}
	/**Overrides sub class method*/
	public GroupAlgebra<G> multiply(AbstractAlgebra<ComplexDouble,IndicialGroup<G>> another){
		return new GroupAlgebra<G>(super.multiply(another));
	}
	/**Overrides sub class method*/
	public GroupAlgebra<G> multiply(ComplexDouble scalar){return new GroupAlgebra<G>(super.multiply(scalar));}
	/**Overrides sub class method*/
	public GroupAlgebra<G> operate(AbstractAlgebra<ComplexDouble, IndicialGroup<G>> another){
		return new GroupAlgebra<G>(super.operate(another));
	}
	/**Overrides sub class method*/
	public GroupAlgebra<G> ringAct(ComplexDouble scalar){
		return new GroupAlgebra<G> (super.ringAct(scalar));
	}
	
}
