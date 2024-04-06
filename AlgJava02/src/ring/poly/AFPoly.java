package ring.poly;

import java.util.Iterator;
import java.util.TreeMap;
import java.util.Map.Entry;

import module.CommModule;
import module.GenericVSpace;

import homomorphism.module.ModuleHomo;
import homomorphism.ring.RingHomo;
import ring.PID;
import ring.homo.AbstractRId;
import ring.integer.IntRing;
import topo.AbstractFct;
import util.Pair;
import field.AbstractField;
import field.Rational;
import group.NNegInt;
/**
 * The the ring of all polynomials over some {@link AbstractField} of sub type
 * <tt>A</tt>.
 * @author bzfmuell
 *
 * @param <A> the field type
 */
public class AFPoly<A extends AbstractField<A>> extends MonoPoly<NNegInt,A> implements PID<AFPoly<A>>{
	private static final String ERROR1 = "\nNull objects...";
	private boolean isAscending;
	public AFPoly (){super();isAscending = true;}
	public AFPoly (NNegInt index, A coeff){this(index,coeff,true);}
	public AFPoly (NNegInt index, A coeff, boolean isAscending){
		this();
		index.setAscending(isAscending);
		setCoefficient(index,coeff);
	}
	public AFPoly (A[] coeffArray){
		this();
		int length = coeffArray.length, counter = 0;
		while(counter<length){
			if(coeffArray[counter]!=null) setCoefficient(new NNegInt(counter),coeffArray[counter]);
			counter++;
		}
	}
	public AFPoly(GenericVSpace<A> element){
		this();
		for (Entry<NNegInt,A> coeffEntry:element) setCoefficient(coeffEntry.getKey(),coeffEntry.getValue());
		
	}
	public AFPoly (MonoPoly<NNegInt,A> poly){this(poly,true);}
	public AFPoly (MonoPoly<NNegInt,A> poly, boolean isAscending){
		this();
		this.isAscending = isAscending;
		for (Entry<NNegInt,A> entry:poly){
			NNegInt key = entry.getKey();
			if(key.isAscending()==isAscending) setCoefficient(key,entry.getValue());
			else{
				NNegInt newKey = new NNegInt(key);
				newKey.setAscending(this.isAscending);
				setCoefficient(newKey,entry.getValue());
			}
		}
	}
	public AFPoly<A> gcd(AFPoly<A> another) {
		if(this==null||another==null) throw new NullPointerException(ERROR1);
		NNegInt deg1 = getDegree(), deg2 = another.getDegree();
		if(isAscending){
			if(deg1.compareTo(deg2)>=0){
				AFPoly<A> mod = mod(another);
				if(mod.isZero()) return another;
				if(mod.getDegree().equals(NNegInt.ZERO)) return new AFPoly<A>(NNegInt.ZERO,mod.getCoefficient(mod.getDegree()).constructOne(),isAscending);
				return another.gcd(mod);
			}
			else {
				AFPoly<A> mod = another.mod(this);
				if(mod.isZero()) return this;
				if(mod.getDegree().equals(NNegInt.ZERO)) return new AFPoly<A>(NNegInt.ZERO,mod.getCoefficient(mod.getDegree()).constructOne(),isAscending);
				return another.gcd(mod);
			}
		}
		else return (new AFPoly<A>(this)).gcd(another);
	}
	public NNegInt getDegree (){
		TreeMap<NNegInt,A> map = getCoefficientMap();
		if(map.size()==0) return null;
		return isAscending?map.lastKey():map.firstKey();
	}
	public EvalHomo<A> getEvalHomomorphism (A evl){
		EvalHomo<A> eval = new EvalHomo<A> (evl);
		eval.setArgument(this);
		return eval;
	}
	/**
	 * Returns the image polynomial under some field homomorphism
	 * <tt>f : A -> B</tt>, where <tt>A</tt> is the field type of this
	 * polynomial and <tt>B</tt> is the field type of the image polynomial
	 * 
	 * @param fieldHomo
	 * @return
	 */
	public <R extends RingHomo<R,A,B>,B extends AbstractField<B>>AFPoly<B> getImage (R fieldHomo){
		AFPoly<B> image = new AFPoly<B>();
		image.isAscending = isAscending;
		for (Entry<NNegInt,A> entry:this){
			NNegInt index = entry.getKey();
			A       coeff = entry.getValue();
			fieldHomo.f(coeff);
			image.setCoefficient(index, fieldHomo.getValue());
		}
		return image;
	}
	public AFPoly<A> div(AFPoly<A> another){return divmod(another).getFirst();}
	public Pair<AFPoly<A>> divmod(AFPoly<A> another) {
		if(this==null||another==null) throw new NullPointerException(ERROR1);
		AFPoly<A> fac = new AFPoly<A>();
		if(isZero()||another.getDegree().equals(NNegInt.ZERO)){
			//TODO fix one
			fac.setCoefficient(NNegInt.ZERO, null);
			return new Pair<AFPoly<A>>(fac,new AFPoly<A>());
		}
		if(isAscending==another.isAscending){
			if(isAscending){
				if(another.isZero()||getDegree().compareTo(another.getDegree())<0) return new Pair<AFPoly<A>>(fac,new AFPoly<A> (this));
				NNegInt deg1 = getDegree(), deg2 = another.getDegree();
				NNegInt newDeg = new NNegInt(deg1.getValue().add(deg2.getValue().addInverse()));
				AFPoly<A> factor = new AFPoly<A> (newDeg,getCoefficient(deg1).multiply(another.getCoefficient(deg2).inverse()),isAscending);
				MonoPoly<NNegInt,A> mod = add(another.multiply(factor).addInverse());
				fac = new AFPoly<A>(fac.add(factor));
				AFPoly<A> moD = new AFPoly<A> (mod,isAscending);
				NNegInt d = moD.getDegree();
				if(d==null||d.compareTo(deg2)<0) return new Pair<AFPoly<A>>(fac,moD);
				Pair<AFPoly<A>> modPair = moD.divmod(another);
				return new Pair<AFPoly<A>>(new AFPoly<A>(fac.add(modPair.getFirst())),modPair.getSecond());
			}
			else {
				if(another.isZero()||getDegree().compareTo(another.getDegree())>0) return new Pair<AFPoly<A>>(fac,new AFPoly<A> (this));
				NNegInt deg1 = getDegree(), deg2 = another.getDegree();
				NNegInt newDeg = new NNegInt(deg1.getValue().add(deg2.getValue().addInverse()));
				AFPoly<A> factor = new AFPoly<A> (newDeg,getCoefficient(deg1).multiply(another.getCoefficient(deg2).inverse()),isAscending);
				fac = new AFPoly<A>(fac.add(factor));
				AFPoly<A> moD = new AFPoly<A> (add(another.multiply(factor).addInverse()),isAscending);
				NNegInt d = moD.getDegree();
				if(d==null||d.compareTo(deg2)>0) return new Pair<AFPoly<A>>(fac,moD);
				Pair<AFPoly<A>> modPair = moD.divmod(another);
				return new Pair<AFPoly<A>>(new AFPoly<A>(fac.add(modPair.getFirst())),modPair.getSecond());
			}
		}
		return divmod(new AFPoly<A>(another,isAscending));
	}
	public AFPoly<A> mod(AFPoly<A> another){return divmod(another).getSecond();}
	public String toString (){
		if(isZero()) return "0";
		StringBuilder sb = new StringBuilder ();
		String x = "X", pow = "^", plc = " ", plus = " + ";
		Iterator<Entry<NNegInt,A>> it = iterator();
		while(it.hasNext()){
			Entry<NNegInt,A> entry = it.next();
			NNegInt deg = entry.getKey();
			if(deg.equals(NNegInt.ZERO)) sb.append(entry.getValue().toString());
			else{
				if(deg.equals(NNegInt.ONE)){
					sb.append(entry.getValue().toString());
					sb.append(plc);
					sb.append(x);
				}
				else {
					sb.append(entry.getValue().toString());
					sb.append(plc);
					sb.append(x);
					sb.append(pow);
					sb.append(deg.toString());
				}
			}
			if(it.hasNext()) sb.append(plus);
		}
		return sb.toString();
	}
	public void setCoefficient(NNegInt index, A coeff){
		index.setAscending(isAscending);
		super.setCoefficient(index, coeff);
	}
	/**
	 * The evaluation homomorphism class: the set of all mappings of
	 * the ring of polynomials evaluated at a given ring element
	 * @author bzfmuell
	 *
	 * @param <A> the field type
	 */
	public static class EvalHomo<A extends AbstractField<A>> implements RingHomo<EvalHomo<A>,MonoPoly<NNegInt,A>,A> {
		/**the polynomial argument: the polynomial to evaluate*/
		private AFPoly<A> arg;
		/**the image value*/
		private A val;
		/**the ring element the argument
		 * is evaluated at*/
		private A evl;
		
		EvalHomo (A evl){setEvaluator(evl);}

		
		public void f() {
			if(evl==null) return;
			if(arg==null) return;
			Iterator<Entry<NNegInt,A>> it = arg.iterator();
			NNegInt lastKey = NNegInt.ZERO;
			A pow = evl.constructOne();
			while (it.hasNext()){
				Entry<NNegInt,A> entry1 = it.next();
				NNegInt index = entry1.getKey();
				while (!index.equals(lastKey)){
					pow = pow.multiply(evl);
					lastKey = lastKey.operate(NNegInt.ONE);
				}
				if(val==null) val = entry1.getValue().multiply(pow);
				else val = val.add(entry1.getValue().multiply(pow));
			}
		}

		
		public void f(MonoPoly<NNegInt, A> arg) {
			setArgument(arg);
			f();
		}

		
		public MonoPoly<NNegInt, A> getArgument() {return arg==null?null:arg;}

		
		public A getValue() {
			if(val==null){
				if(arg==null){
					f();
					return val;
				}
				return null;
			}
			return null;
		}

		public boolean isKernel(MonoPoly<NNegInt,A> arg){
			f(arg);
			return val.isZero()?true:false;
		}
		
		public void setArgument(MonoPoly<NNegInt, A> arg) {this.arg = new AFPoly<A>(arg,true);}
		public void setEvaluator(A evl){if(evl!=null) this.evl = evl;}
	}
	public static class Derivation<A extends AbstractField<A>> extends AbstractFct<PolyMod<A>,PolyMod<A>> 
	implements ModuleHomo<Derivation<A>,PolyMod<A>,PolyMod<A>,A,A, NNegInt>{
		private PolyMod<A> arg;
		private PolyMod<A> val;
		
		
		public Derivation<A> ringAct(A scalar) {
			final Derivation<A> cp = this;
			final A scl = scalar;
			return new Derivation<A>(){
				public void f(){
					cp.f(arg);
					val = cp.val.multiply(scl);
				}
			};
		}

		
		public boolean isZero() {
			// TODO Auto-generated method stub
			return false;
		}

		
		public Derivation<A> add(Derivation<A> another) {
			final Derivation<A> cp1 = this, cp2 = another;
			return new Derivation<A>(){
					public void f(){
						cp1.f(arg);
						cp2.f(arg);
						val = cp1.val.add(cp2.val);
					}
			};
		}

		public boolean isDiscrete(){return arg==null?true:arg.isDiscrete();}
		
		public Derivation<A> operate(Derivation<A> another) {return add(another);}

		
		public boolean equals(Derivation<A> another) {
			if(this==another) return true;
			PolyMod<A> arg1 = arg, arg2 = another.arg;
			if(arg1.equals(arg2)){
				f();
				another.f();
				return val.equals(another.val)?true:false;
			}
			f(arg1);
			another.f(arg1);
			PolyMod<A> val11 = val, val12 = another.val;
			f(arg2);
			another.f(arg2);
			PolyMod<A> val21 = val, val22 = another.val;
			return val11.equals(val12)&&val21.equals(val22)?true:false;
		}

		
		public void clear() {
			// TODO Auto-generated method stub
			
		}

		
		public A getValue(NNegInt index) {
			// TODO Auto-generated method stub
			return null;
		}

		
		public NNegInt getIndex(A val) {
			// TODO Auto-generated method stub
			return null;
		}

		
		public A remove(NNegInt index) {
			// TODO Auto-generated method stub
			return null;
		}

		
		public void setEntry(NNegInt index, A value) {
			// TODO Auto-generated method stub
			
		}

		
		public Iterator<Entry<NNegInt, A>> iterator() {
			// TODO Auto-generated method stub
			return null;
		}

		
		public void setArgument(PolyMod<A> arg) {this.arg = arg;}

		
		public void f() {
			if(arg==null) return;
			if(val==null) val = new PolyMod<A>(new AFPoly<A>());
			if(arg.poly.isZero()||arg.poly.getDegree().equals(NNegInt.ZERO)) return;
			for (Entry<NNegInt,A> entry:arg.poly){
				NNegInt index = entry.getKey();
				A       coeff = entry.getValue();
				if(!index.equals(NNegInt.ZERO)) {
					A newCoeff = coeff;
					NNegInt count = NNegInt.ONE;
					while (!count.equals(index)){
						newCoeff = newCoeff.add(coeff);
						count = count.operate(NNegInt.ONE);
					}
					val.poly.setCoefficient(new NNegInt(index.getValue().add(IntRing.M_ONE)), newCoeff);
				}
			}
		}
		@SuppressWarnings("unchecked")
		public AbstractRId<A> canonicalHomo() {
			// TODO Auto-generated method stub
			return new AbstractRId<A>();
		}
		
	}
	public static class PolyMod<A extends AbstractField<A>> implements CommModule<PolyMod<A>,A, NNegInt>{
		private AFPoly<A> poly;
		PolyMod (MonoPoly<NNegInt,A> poly){
			if(!poly.isZero()) this.poly = new AFPoly<A> (poly);
			else this.poly = new AFPoly<A> ();
		}
		PolyMod (){}
		public PolyMod<A> multiply(A scalar) {
			PolyMod<A> scl = new PolyMod<A> (new AFPoly<A>());
			for (Entry<NNegInt,A> entry:poly){scl.poly.setCoefficient(entry.getKey(), entry.getValue().multiply(scalar));}
			return scl;
		}

		
		public PolyMod<A> ringAct(A scalar) {return multiply(scalar);}

		
		public PolyMod<A> add(PolyMod<A> another) {return new PolyMod<A>(poly.add(another.poly));}

		public boolean isDiscrete(){return poly.isDiscrete();}
		
		public PolyMod<A> operate(PolyMod<A> another) {return add(another);}

		
		public boolean equals(PolyMod<A> another) {return poly.equals(another.poly)?true:false;}

		
		public void clear() {}

		
		public A getValue(NNegInt index) {
			// TODO Auto-generated method stub
			return null;
		}

		
		public NNegInt getIndex(A val) {return NNegInt.ZERO;}

		
		public A remove(NNegInt index) {return null;}

		
		public void setEntry(NNegInt index, A value) {
			// TODO Auto-generated method stub
			
		}


		
		public boolean isZero() {return poly.isZero()?true:false;}


		
		public Iterator<Entry<NNegInt, A>> iterator() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	public static void main(String[] args){
		AFPoly<Rational> p1 = new AFPoly<Rational> (), p2 = new AFPoly<Rational>();
		p1.setCoefficient(new NNegInt(3), Rational.ONE);
		p1.setCoefficient(new NNegInt(2), Rational.ONE);
		p1.setCoefficient(NNegInt.ZERO, Rational.ONE);
		p2.setCoefficient(NNegInt.ONE, Rational.ONE);
		p2.setCoefficient(NNegInt.ZERO, new Rational(2));
		System.out.println(String.format("%1$s mod %2$s = %3$s",p1.toString(),p2.toString(),p1.mod(p2).toString()));
		System.out.println(String.format("%1$s gcd %2$s = %3$s",p1.toString(),p2.toString(),p1.gcd(p2).toString()));
	}


	
}