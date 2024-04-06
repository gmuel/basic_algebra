package field;

import group.NNegInt;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import ring.poly.AFPoly;
import ring.poly.MonoPoly;
/**
 * The rational extension class
 * @author adin
 *
 */
public class RationalExten extends AbstractField<RationalExten> {
	/**the polynomial element*/
	private AFPoly<Rational> element;
	/**the modulus map*/
	private TreeMap<NNegInt,AFPoly<Rational>> modMap;
	public RationalExten(Map<NNegInt,AFPoly<Rational>> modMap){
		super();
		this.modMap = new TreeMap<NNegInt,AFPoly<Rational>> (modMap);
	}
	
	public RationalExten(MonoPoly<NNegInt,Rational> element, Map<NNegInt,AFPoly<Rational>> modMap){
		this(modMap);
		setElement(element);
	}
	public RationalExten inverse() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public RationalExten addInverse() {return new RationalExten(element.addInverse(),modMap);}

	
	public boolean isZero() {return element==null?true:element.isZero();}

	
	public RationalExten multiply(RationalExten another) {return new RationalExten(element.add(another.element),unite(another));}

	
	public RationalExten add(RationalExten another) {return new RationalExten(element.multiply(another.element),unite(another));}

	
	public boolean equals(RationalExten another) {return modMap.equals(another.modMap)?element.equals(another.element)?true:false:false;}
	//private Poly
	public boolean isDiscrete(){return true;}

	
	public RationalExten constructOne() {return new RationalExten(new AFPoly<Rational> (NNegInt.ZERO,Rational.ONE),modMap);}
	
	public void setElement(MonoPoly<NNegInt,Rational> element){
		NNegInt deg = element.getDegree();
		if(deg.equals(NNegInt.ZERO)) this.element = new AFPoly<Rational>(element);
		else {
			AFPoly<Rational> res = new AFPoly<Rational>(element);
			for (Entry<NNegInt,AFPoly<Rational>> entry:modMap.entrySet()){
				AFPoly<Rational> mod = entry.getValue();
				if(deg.compareTo(mod.getDegree())>=0) res = res.mod(mod);
			}
			this.element = res;
		}
	}
	private TreeMap<NNegInt,AFPoly<Rational>> unite(RationalExten another){
		//TODO: maybe only modulus is required...
		TreeMap<NNegInt,AFPoly<Rational>> newModMap  = new TreeMap<NNegInt,AFPoly<Rational>>(modMap);
		NNegInt lastKey = newModMap.lastKey();
		for (Entry<NNegInt,AFPoly<Rational>> entry:another.modMap.entrySet()){
			NNegInt index = entry.getKey();
			AFPoly<Rational> modPoly = entry.getValue();
			if(!newModMap.containsValue(modPoly)) newModMap.put(index.operate(lastKey), modPoly);
		}
		return newModMap;
	}
	public String toString(){
		return element.toString();
	}
	public static void main(String[] args){
		Rational two = new Rational(-2), three = new Rational(3);
		AFPoly<Rational> p1 = new AFPoly<Rational>(new Rational[]{two,Rational.ZERO,Rational.ZERO,Rational.ONE});
		AFPoly<Rational> p2 = new AFPoly<Rational>(new Rational[]{three,Rational.ZERO,Rational.ONE});
		AFPoly<Rational> p3 = new AFPoly<Rational>(new Rational[]{Rational.ONE,Rational.ZERO,Rational.ONE});
		TreeMap<NNegInt,AFPoly<Rational>> modMap = new TreeMap<NNegInt,AFPoly<Rational>>();
		NNegInt count = NNegInt.ZERO;
		modMap.put(count, p1);
		modMap.put(count.increment(),p2);
		RationalExten ex = new RationalExten(p3,modMap);
		System.out.println(String.format("%1$s mod %2$s = %3$s", p3.toString(),ex.modMap.values().toString(),ex.toString()));
	}
}
