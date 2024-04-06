package homomorphism.group;

import group.Group;

import group.representation.Gl;

import ring.UnitaryCommRing;

import topo.AbstractElFct;
/**
 * The abstract group representation class - each instance
 * is a group homomorphism <tt>f : G -&gt Gl&ltA&gt</tt>,
 * where
 * <ol><li><tt>G</tt> is the group and</li>
 * <li><tt>Gl&ltA&gt</tt> is the general linear group of a Noetherian module
 * over some arbitrary unitary ring of type &ltA&gt</li></ol>
 * @author adin
 *
 * @param <G> the type of the {@link Group}
 * @param <A> the type of the {@link UnitaryCommRing}
 */
public abstract class AGHomo<G extends Group<G>, A extends UnitaryCommRing<A>> extends AbstractElFct<G, Gl<A>>
	implements GroupHomo<AGHomo<G, A>, G, Gl<A>> {
	/**
	 * Default constructor
	 */
	public AGHomo (){super();}
}
