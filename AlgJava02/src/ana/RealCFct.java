package ana;

import java.util.Map.Entry;

import field.DoubleField;
import group.UnitGroup;
import ring.AbstractCommRing;
import ring.poly.AFPoly;
import ring.poly.AFPoly.EvalHomo;
import topo.ContiFct;
import topo.relation.EquiRel;
import topo.relation.PairedElement;
import topo.relation.Relation;

public abstract class RealCFct extends AbstractCommRing<RealCFct> implements
		ContiFct<RealCFct, DoubleField, DoubleField, RealLine, RealLine> {
	DoubleField arg;
	DoubleField val;
	IsInPreIm isInPreIm;
	RealLine domain;
	public RealCFct() {
		isInPreIm = new IsInPreIm ();
		setDomain();
	}
	public RealCFct add(RealCFct another){
		final RealCFct cp1 = this, cp2 = another;
		return new RealCFct (){
			public void f(){
				if(arg!=null) {
					DoubleField arg1 = cp1.arg, arg2 = cp2.arg;
					cp1.f(arg);
					cp2.f(arg);
					val = cp1.val.add(cp2.val);
					cp1.f(arg1); cp2.f(arg2);
				}
			}
			public void setDomain(){
				if(cp1.domain!=null&&cp2.domain!=null) this.domain = cp1.domain.intersect(cp2.domain);
			}
		};
	}
	public RealCFct addInverse(){
		final RealCFct cp = this;
		return new RealCFct (){
			public void f(){
				if(arg!=null){
					DoubleField argCP = cp.arg;
					cp.f(arg);
					val = cp.val.addInverse();
					cp.f(argCP);
				}
			}
			public void setDomain(){if(cp.domain!=null)domain = cp.domain;}
		};
	}
	public RealCFct antiDer (final DoubleField constant, final DoubleField start, final DoubleField minError, final DoubleField maxError){
		if(minError.compareTo(maxError)>=0||!domain.contains(start)){
			if(minError.compareTo(maxError)>=0) throw new IllegalArgumentException (String.format("\nminimal error exceeds maximal error: min = %1$s\tmax = %2$s",minError.toString(),maxError.toString()));
			else throw new IllegalArgumentException (String.format("\nStart argument %1$s out of range",start.toString()));
		}
		final RealCFct cp = this;
		return new RealCFct (){
			DoubleField step = new DoubleField(.01); 
			public void f(){
				if(arg!=null) approx(minError,maxError);
			}
			public void setDomain(){if(cp.domain.contains(start)) domain = cp.domain;}
			private void approx(DoubleField minError, DoubleField maxError){
				step = step.multiply(start.dist(arg));
				
				DoubleField currarg = start, val1 = constant, val2 = null;
				while(currarg.compareTo(arg)<0){
					cp.f(currarg.add(step));
					val2 = cp.val;
					DoubleField dist = val1.dist(val2);
					if(dist.compareTo(minError)<=0&&dist.compareTo(maxError)>=0) {
						currarg = currarg.add(step);
						val1 = val2;
						continue;
					}
					else{
						int count = 0;
						if(dist.compareTo(minError)>0&&dist.compareTo(maxError)>=0){
							while(dist.compareTo(minError)>0) {
								if(count>=10) throw new RuntimeException ("\nNo convergent sequence for argument: "+currarg.add(step).toString());
								step = step.multiply(RealLine.HALF);
								cp.f(currarg.add(step));
								val2 = cp.val;
								dist = val1.dist(val2);
								count++;
							}
							val1 = val2;
							currarg = currarg.add(step);
						}
						else {
							if(dist.compareTo(minError)<=0&&dist.compareTo(maxError)<0){
								while(dist.compareTo(maxError)<0){
									if(count>=10) throw new RuntimeException ("\nNo convergent sequence for argument: "+currarg.add(step).toString());
									step = step.multiply(RealLine.HALF);
									cp.f(currarg.add(step));
									val2 = cp.val;
									dist = val1.dist(val2);
									count++;
								}
								val1 = val2;
								currarg = currarg.add(step);
							}
							else {
								if(currarg.add(step).dist(arg).compareTo(minError)<=0) val = val2;
								else throw new RuntimeException ("\nNo convergent sequence for the given argument: "+arg.toString());
							}
						}
					}
				}
			}
		};
	}
	public boolean equals(RealCFct another) {
		return false;
	}
	public RealCFct extend (RealLine superSet){
		//TODO remove this method (and super method!) for interval support in RealLine 
		return this;
	}
	public void f(DoubleField arg){
		if(arg!=null) {
			this.arg = arg;
			f();
		}
	}
	public DoubleField getArgument(){return arg==null?null:arg;}
	public DoubleField getValue(){
		if(val==null){
			if(arg==null) return null;
			f();
		}
		return val;
	}
	public EquiRel<DoubleField,RealLine> isInPreImage(){
		return isInPreIm;
	}
	public RealCFct inverse(){
		if(!isUnit()) throw new IllegalArgumentException ("\nZero division...");
		final RealCFct cp = this; 
		return new RealCFct (){

			@Override
			public void f() {
				if(arg!=null) {
					cp.f(arg);
					if(cp.val.isUnit()) val = cp.val.inverse();
					else System.err.println(String.format("\nSingular point in domain: x = %1$s",arg));
				}
			}

			@Override
			public void setDomain() {
				this.domain = new RealLine(cp.domain);
				
			}
			
		};
	}
	public boolean isDiscrete(){return false;}
	public boolean isUnit(){return val.isUnit();}
	public boolean isZero(){return false;}
	public RealCFct multiply(RealCFct another){
		final RealCFct cp1 = this, cp2 = another;
		return new RealCFct(){
			public void f(){
				if(arg!=null){
					DoubleField arg1 = cp1.arg, arg2 = cp2.arg; 
					cp1.f(arg);
					cp2.f(arg);
					val = cp1.val.multiply(cp2.val);
					cp1.f(arg1);cp2.f(arg2);
				}
			}
			public void setDomain(){
				if(cp1.domain!=null&&cp2.domain!=null) domain = cp1.domain.intersect(cp2.domain); 
			}
		};
	}
	public RealCFct restrict (RealLine supset){return this;}
	public void setArgument(DoubleField arg){
		if(arg!=null) this.arg = arg;
	}
	public UnitGroup<RealCFct> getUnit(){return isUnit()?new UnitGroup<RealCFct>(this):null;}
	public abstract void setDomain();
	private class IsInPreIm implements EquiRel<DoubleField,RealLine> {

		PairedElement<DoubleField> basePoint;
		public PairedElement<DoubleField> basePoint() {return basePoint==null?null:basePoint;}

		
		public Relation<DoubleField> inverse() {
			final IsInPreIm cp = this;
			return new Relation<DoubleField>(){

				PairedElement<DoubleField> basePoint;
				public PairedElement<DoubleField> basePoint() {return basePoint==null?null:basePoint;}

				
				public Relation<DoubleField> inverse() {return cp;}

				
				public boolean isClosed() {return false;}

				
				public boolean isRelated(DoubleField first, DoubleField second) {
					if(cp.isRelated(first, second)) return false;
					basePoint = new PairedElement<DoubleField> (first,second);
					return true;
				}
				
			};
		}

		
		public boolean isClosed() {return true;}

		
		public boolean isRelated(DoubleField first, DoubleField second) {
			f(first);
			DoubleField val1 = getValue();
			f(second);
			DoubleField val2 = getValue();
			if(val1==null||val2==null) return false;
			if(val1.equals(val2)){basePoint = new PairedElement<DoubleField>(first,second);return true;}
			return false;
		}

		
		public RealLine equiClass(DoubleField element) {
			//TODO remove this method from interface
			RealLine equi = RealLine.EMPTY_SET;
			if(domain.contains(element)){
				equi.addSegment(element, DoubleField.ZERO, false);				
			}
			return equi;
		}
		
	}

	public static RealCFct constructRealPolyFct (AFPoly<DoubleField> poly){
		final AFPoly<DoubleField> cp = poly;
		return new RealCFct (){
			EvalHomo<DoubleField> homo = cp.getEvalHomomorphism(DoubleField.ZERO);
			public void f(){
				homo.setEvaluator(arg);
				homo.f();
				val = homo.getValue();
			}
			public void setDomain(){domain = RealLine.REAL_LINE;}
		};
	}
	public static void main (String[] args){
		RealCFct tent = new RealCFct (){
			final DoubleField QUART = new DoubleField(.25);
			final DoubleField TWO   = new DoubleField(2);
			final DoubleField M_TWO = new DoubleField(-2);
			public void f(){
				if(arg==null) return;
				RealLine seg = domain.getSegment(arg);
				if(seg!=null){
					if(seg.contains(QUART)) val = arg.multiply(TWO);
					else {
						 val = TWO.add(arg.multiply(M_TWO));
					}
				} else val = DoubleField.ZERO;
			}
			public void setDomain(){
				domain = RealLine.UNIT_INTV.scale(RealLine.HALF,true);
				domain = domain.union(domain.translate(RealLine.HALF,true));
				
			}
		};
		String formatStr = "f(%1$s) = %2$s";
		Segment seg = new Segment(DoubleField.ONE,DoubleField.ONE,false);
		seg.step = new DoubleField(.01);
		for (DoubleField arg:seg) {
			tent.f(arg);
			System.out.println(String.format(formatStr,arg.toString(),tent.val.toString()));
		}
	}
}
