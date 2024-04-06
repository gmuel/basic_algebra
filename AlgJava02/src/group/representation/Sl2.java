package group.representation;

import group.NNegInt;

import ring.UnitaryCommRing;

public class Sl2<A extends UnitaryCommRing<A>> extends Sl<A> {
	
	protected Sl2(){super(NNegInt.TWO);}
	private Sl2 (A parameter){
		this();
	}
	public Sl2<A> multiply (Sl2<A> another){return null;}
	/**
	 * 
	 * @param parameter
	 * @return
	 */
	public static <A extends UnitaryCommRing<A>>  Sl2<A> contructUpperSl2SubGroupGenerator(A parameter){
		if(parameter!=null)
			return new Sl2<A> (parameter);
		return new Sl2<A> ();
	}
}
