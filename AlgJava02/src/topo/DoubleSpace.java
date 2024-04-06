package topo;

import java.util.Random;

import group.NNegInt;
import homomorphism.module.GenericDual;
import topo.relation.AbstractRelation;
import topo.relation.PairedElement;
import topo.relation.Relation;
import util.Pair;
import module.AbstractFiniteModule;
import module.GenericVSpace;
import field.DoubleField;

public class DoubleSpace implements TopoSpace<DoubleSpace, AbstractFiniteModule<DoubleField>> {
	private AbstractRelation<AbstractFiniteModule<DoubleField>> rel;
	private PairedElement<AbstractFiniteModule<DoubleField>> basePoint;
	private boolean isEmpty;
	public DoubleSpace() {}
	
	public DoubleSpace(Relation<AbstractFiniteModule<DoubleField>> rel){
		if(rel!=null){
			this.rel = AbstractRelation.relation(rel);
			if((basePoint = rel.basePoint())==null) isEmpty = true;			
		}
	}
	
	public DoubleSpace complement(){return new DoubleSpace(rel.inverse());}
	
	public boolean contains(AbstractFiniteModule<DoubleField> element) {
		return rel.isRelated(basePoint.getFirst(),element)||rel.isRelated(basePoint.getSecond(),element);
	}

	
	public DoubleSpace intersect(DoubleSpace another) {
		return new DoubleSpace(rel.intersect(another.rel));
	}

	
	public boolean isEmpty() {isEmpty = rel.isEmpty(); return isEmpty;}

	
	public boolean isOpen() {
		if(isEmpty) return true;
		return rel.isClosed()?false:true;
	}

	
	public DoubleSpace union(DoubleSpace another) {return new DoubleSpace(rel.union(another.rel));}

	
	public boolean isSuperSet(DoubleSpace another) {
		if(!intersect(another).isEmpty()){
			return union(another).equals(another)?true:false;
		}
		return false;
	}

	
	public boolean isSubSet(DoubleSpace another) {
		return another.isSuperSet(this);
	}
	
	public static DoubleSpace constructOpenBall(GenericVSpace<DoubleField> element, DoubleField radius){
		if(radius.compareTo(DoubleField.ZERO)<=0) throw new IllegalArgumentException ("\nOnly positive radius allowed");
		final DoubleField rad = radius;
		AbstractRelation<AbstractFiniteModule<DoubleField>> rel = new AbstractRelation<AbstractFiniteModule<DoubleField>>(){

			
			public boolean isClosed() {return false;}

			
			public boolean isRelated(AbstractFiniteModule<DoubleField> first,
					AbstractFiniteModule<DoubleField> second) {
				//TODO fix method
				GenericDual<DoubleField> fDual = new GenericDual<DoubleField>(first), sDual = new GenericDual<DoubleField>(second), bDual = new GenericDual<DoubleField>(basePoint.getFirst());
				fDual.f(first);
				bDual.f(basePoint.getFirst());
				sDual.f(second);
				DoubleField fsc = fDual.getValue(), bsc = bDual.getValue(), ssc = sDual.getValue();
				DoubleField scalarF = fsc==null&&bsc==null?new DoubleField():fsc==null&&bsc!=null?bsc:fsc!=null&&bsc==null?fsc:fsc.add(bDual.getValue());
				DoubleField scalarS = ssc==null&&bsc==null?new DoubleField():ssc==null&&bsc!=null?bsc:ssc!=null&&bsc==null?ssc:ssc.add(bsc), sqr = rad.multiply(rad);
				fDual.f(basePoint.getFirst());
				bDual.f(first);
				fsc = fDual.getValue(); bsc = bDual.getValue();
				if(fsc!=null&&bsc!=null) scalarF = scalarF.add(fsc.add(bsc).multiply(DoubleField.M_ONE));
				else {
					if(fsc!=null&&bsc==null) scalarF = scalarF.add(fsc.multiply(DoubleField.M_ONE));
					else if(fsc==null&&bsc!=null) scalarF = scalarF.add(bsc.multiply(DoubleField.M_ONE));
				}
				sDual.f(basePoint.getFirst());
				bDual.f(second);
				if(ssc!=null&&bsc!=null) scalarS = scalarS.add(ssc.add(bsc).multiply(DoubleField.M_ONE));
				else {
					if(ssc!=null&&bsc==null) scalarS = scalarS.add(ssc.multiply(DoubleField.M_ONE));
					else if(ssc==null&&bsc!=null) scalarS = scalarS.add(bsc.multiply(DoubleField.M_ONE));
				}
				return scalarF.compareTo(sqr)<0&&scalarS.compareTo(sqr)<0?true:false;
			}

			
			public boolean isEmpty() {return false;}

			
			public boolean isOpen() {return true;}
			
		};
		DoubleSpace sp = new DoubleSpace(rel);
		sp.basePoint = new PairedElement<AbstractFiniteModule<DoubleField>>(element,element);
		return sp;
	}
	
	public static void main(String[] args){
		DoubleSpace ball = constructOpenBall(new GenericVSpace<DoubleField>(), new DoubleField (.1));
		Random rand = new Random();
		for (int i = 0; i < 100; i++){
			GenericVSpace<DoubleField> vec = new GenericVSpace<DoubleField>();
			vec.setEntry(new NNegInt(rand.nextInt(10)), new DoubleField(rand.nextDouble()));
			if(ball.contains(vec)) System.out.println(String.format("Open ball %1$s contains vector %2$s",ball.toString(),vec.toString()));
			else System.err.println(String.format("Open ball %1$s does not contain vector %2$s",ball.toString(),vec.toString()));
		}
	}
}
