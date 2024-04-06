package field;

import group.NNegInt;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import ring.integer.IntRing;
import ring.poly.AFPoly;
import ring.poly.MonoPoly;

public class CyclotomicField extends RationalExten {
	
	public CyclotomicField(int exponent) {
		super(constructCyclotomics(new NNegInt(exponent)));
		// TODO Auto-generated constructor stub
	}

	public CyclotomicField(MonoPoly<NNegInt, Rational> element, int exponent) {
		super(element, constructCyclotomics(new NNegInt(exponent)));
		// TODO Auto-generated constructor stub
	}
	private static TreeMap<NNegInt,AFPoly<Rational>> constructCyclotomics(NNegInt exponent){
		TreeMap<NNegInt,AFPoly<Rational>> cyclotomics = new TreeMap<NNegInt,AFPoly<Rational>>();
		TreeMap<IntRing,NNegInt> primeDivs = IntRing.primeDivisors(exponent.getValue());
		if(primeDivs.size()==1){
			Iterator<Entry<IntRing,NNegInt>> it = primeDivs.entrySet().iterator();
			Entry<IntRing,NNegInt> entry = it.next();
			IntRing prime = entry.getKey();
			NNegInt exp   = entry.getValue();
			if(exp.equals(NNegInt.ONE)){
				AFPoly<Rational> cyclo = new AFPoly<Rational>(new NNegInt(prime),Rational.ONE);
				cyclo.setCoefficient(NNegInt.ZERO, Rational.M_ONE);
				cyclo = cyclo.div(new AFPoly<Rational>(new Rational[]{Rational.M_ONE,Rational.ONE}));
				cyclotomics.put(NNegInt.ZERO, cyclo);
				return cyclotomics;
			}
			NNegInt pow = new NNegInt(prime), pr = new NNegInt(prime);
			AFPoly<Rational> cyclo = new AFPoly<Rational>(exponent,Rational.ONE);
			cyclo.setCoefficient(NNegInt.ZERO, Rational.M_ONE);
			while(pow.compareTo(exponent)<0){ 
				cyclo = cyclo.div(constructCyclotomics(pow).get(NNegInt.ZERO));
				pow = pow.multiply(pr);
			}
			cyclo = cyclo.div(new AFPoly<Rational>(new Rational[]{Rational.M_ONE,Rational.ONE}));
			cyclotomics.put(NNegInt.ZERO, cyclo);
			return cyclotomics;
		}
		Iterator<Entry<IntRing,NNegInt>> it = primeDivs.entrySet().iterator();
		AFPoly<Rational> cyclo = new AFPoly<Rational> (exponent,Rational.ONE);
		cyclo.setCoefficient(NNegInt.ZERO, Rational.M_ONE);
		while (it.hasNext()){
			Entry<IntRing,NNegInt> primeDiv = it.next();
			AFPoly<Rational> div = null;
			if(it.hasNext()) div = constructCyclotomics(new NNegInt(primeDiv.getKey().pow(primeDiv.getValue()))).get(NNegInt.ZERO);
			else div = constructCyclotomics(new NNegInt(primeDiv.getKey().pow(primeDiv.getValue().decrement()))).get(NNegInt.ZERO);
			cyclo = cyclo.div(div);
		}
		cyclo = cyclo.div(new AFPoly<Rational>(new Rational[]{Rational.M_ONE,Rational.ONE}));
		cyclotomics.put(NNegInt.ZERO,cyclo);
		return cyclotomics;
	}
	public static void main(String[] args){
		NNegInt val = NNegInt.TWO, bound = new NNegInt(25);
		while(val.compareTo(bound)<=0) {
			System.out.println(String.format("p_%1$s = %2$s",val.toString(),constructCyclotomics(val).get(NNegInt.ZERO).toString()));
			val = val.increment();
		}
	}
}
