package homomorphism.group;

import group.representation.Gl;
import group.representation.Sl;

import ring.UnitaryCommRing;
/**
 * Class that maps an automorphism of some finite module
 * over a ring to the automorphism with deterimant one
 * @author adin
 *
 * @param <A> the type of a sub-class of {@link UnitaryCommRing}
 */
public class Gl2SlHomo<A extends UnitaryCommRing<A>> extends
		AGHomo<Gl<A>,A>{
		//AbstractElFct<Gl<A>, Sl<A>> {//implements GroupHomo<Gl2SlHomo<A>,Gl<A>,Sl<A>> {
	/**
	 * Constructs an empty map object - 
	 * no arguments set
	 */
	public Gl2SlHomo(){super();}
	/**
	 * Computes the image of
	 * the argument matrix if
	 * not null
	 */
	public void f() {
		if(arg!=null){
			A det = arg.det();
			val = new Sl<A>(det.equals(det.multiply(det))?arg:arg.multiply(det.inverse()));
		}
	}
	/**
	 * Overrides sub-class's method:
	 * <p>returns the argument as <code>Sl</code>
	 */
	public Sl<A> getValue (){
		if(val==null){
			f();
		}
		return val==null?null:new Sl<A>(val);
	}
}
