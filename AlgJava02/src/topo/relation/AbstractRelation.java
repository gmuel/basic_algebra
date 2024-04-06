package topo.relation;

import topo.Element;
import topo.TopoSpace;

public abstract class AbstractRelation<E extends Element<E>> implements
		Relation<E>, TopoSpace<AbstractRelation<E>,PairedElement<E>>{
	protected PairedElement<E> basePoint;
	public AbstractRelation() {}
	public PairedElement<E> basePoint(){return basePoint==null?null:basePoint;}
	public AbstractRelation<E> complement(){
		Relation<E> inv = inverse();
		
		return inv!=null?relation(inv):null;
	}
	public boolean contains(PairedElement<E> element){
		return isRelated(element.getFirst(),element.getSecond());
	}
	public boolean equals(Object o){
		if(this==o) return true;
		if(!(o instanceof AbstractRelation)) return false;
		AbstractRelation<?> cp = (AbstractRelation<?>) o;
		if(basePoint==null&&cp.basePoint==null) return true;
		if(basePoint!=null&&cp.basePoint!=null){
			if(basePoint.getElementClass().equals(cp.basePoint.getElementClass())){
				@SuppressWarnings("unchecked")
				AbstractRelation<E> cp1 = (AbstractRelation<E>) cp;
				return isSuperSet(cp1)&&isSubSet(cp1);
			}
		}
		return false;
	}
	public AbstractRelation<E> intersect(AbstractRelation<E> another){
		final AbstractRelation<E> firstRel = this;
		final AbstractRelation<E> secondRel= another;
		AbstractRelation<E>inter = new AbstractRelation<E> (){
			public boolean isClosed() {
				// TODO Auto-generated method stub
				return firstRel.isClosed()&&secondRel.isClosed();
			}

			public boolean isEmpty(){return firstRel.isEmpty()||secondRel.isEmpty();}
			
			public boolean isRelated(E first, E second) {
				if(basePoint==null) basePoint = new PairedElement<E>(first,second);
				return firstRel.isRelated(first, second)&&secondRel.isRelated(first, second);
			}

			
			public boolean isOpen() {return firstRel.isOpen()&&secondRel.isOpen();}
			
		};
		return inter;
	}
	
	public AbstractRelation<E> inverse(){
		final AbstractRelation<E> rel = this;
		return new AbstractRelation<E> (){

			//basePoint = new PairedElement<E>(rel.basePoint());
			public boolean isClosed() {return !rel.isClosed();}

			
			public boolean isRelated(E first, E second) {
				if(basePoint==null){
					if(!rel.isRelated(first, second)) {
						basePoint = new PairedElement<E>(first,second);
						return true;
					}
					return false;
				}	
				return !rel.isRelated(first, second);
			}

			
			public boolean isEmpty() {return rel.isClosed()&&rel.isOpen()&&rel.basePoint!=null?true:false;}

			
			public boolean isOpen() {return rel.isClosed();}
			
		};
	}
	//public boolean isEmpty(){return false;}
	//public boolean isOpen(){return false;}
	public boolean isSubSet (AbstractRelation<E> another){
		return intersect(another).equals(this);
	}
	public boolean isSuperSet (AbstractRelation<E> another){
		return intersect(another).equals(another);
	}
	
	public AbstractRelation<E> union (AbstractRelation<E> another){
		final AbstractRelation<E> rel1 = this, rel2 = another;
		return new AbstractRelation<E> (){
			
			public boolean isClosed() {return rel1.isClosed()&&rel2.isClosed();}

			
			public boolean isRelated(E first, E second) {
				if(basePoint==null){
					if(rel1.isRelated(first, second)||rel2.isRelated(first, second)) {
						basePoint = new PairedElement<E>(first,second);
						return true;
					}
					return false;
				}
				return rel1.isRelated(first, second)||rel2.isRelated(first, second);
			}

			
			public boolean isEmpty() {return rel1.isEmpty()||rel2.isEmpty();}

			
			public boolean isOpen() {return rel1.isOpen()&&rel2.isOpen();}
			
		};
	}
	
	public static <E extends Element<E>> AbstractRelation<E> relation (final Relation<E> rel){
		return new AbstractRelation<E> (){
			
			public boolean isClosed() {return rel.isClosed();}

			
			public boolean isRelated(E first, E second) {
				if(basePoint==null) basePoint = new PairedElement<E> (rel.basePoint());
				return rel.isRelated(first, second);}

			
			public boolean isEmpty() {
				return rel.basePoint()==null?true:false;}

			
			public boolean isOpen() {return !rel.isClosed()&&rel.inverse().isClosed();}
			
			
		};
	}
}
