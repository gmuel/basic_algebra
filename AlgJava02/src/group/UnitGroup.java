package group;

import ring.AbstractCommRing;
import topo.AbstractElFct;

public class UnitGroup<A extends AbstractCommRing<A>> extends
		AbstractAbel<UnitGroup<A>> {
	private static final String ERROR = "\nNo unit element: %1$s";
	private A element;
	protected UnitGroup(){super();}
	public UnitGroup(A element){
		this();
		if(element!=null&&element.isUnit()) this.element = element;
		else throw new IllegalArgumentException (String.format(ERROR, element));
	}
	public UnitGroup(UnitGroup<A> element){this(element.element);}

	
	public UnitGroup<A> add(UnitGroup<A> another) {return new UnitGroup<A> (element.multiply(another.element));}

	
	public boolean equals(UnitGroup<A> another) {return element.equals(another.element);}

	public A getValue (){return element;}
	
	public boolean isDiscrete() {return element.isDiscrete();}
	private class Inv {
		A one;
		AbstractElFct<A,A> unitFinder;
		Inv (){
			unitFinder = new AbstractElFct<A,A> (){
				public void f(){
					if(one!=null&&arg!=null) val = one.add(element.multiply(arg).addInverse());
				}
			};
			one = element;
		}
		void find (){
			int count = 0;
			unitFinder.f(one);
			A val = unitFinder.getValue();
			while (count<200){
				if(!val.isZero()) {
					
					unitFinder.f(val);
					val = unitFinder.getValue();
				} else break;
				count++;
			}
		}
	}
}
