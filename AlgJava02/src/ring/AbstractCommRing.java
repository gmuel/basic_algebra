package ring;

import group.UnitGroup;

public abstract class AbstractCommRing<A extends AbstractCommRing<A>> extends
		RingElement<A> implements CommRing<A>{
	public AbstractCommRing (){super();}
	public boolean isCommutative (){return true;}
	public abstract UnitGroup<A> getUnit();
	public abstract A inverse();
}
