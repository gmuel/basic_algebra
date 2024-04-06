package homomorphism.module;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import module.Generator;
import module.GenericVSpace;
import module.Tensor;
import field.AbstractField;
import group.AbstractAbel;
import group.NNegInt;
import ring.Ring;
import ring.homo.AbstractRId;
import util.ComposedObject;

public class GenericVSpaceEndo<F extends AbstractField<F>> extends AbstractAbel<GenericVSpaceEndo<F>> implements Ring<GenericVSpaceEndo<F>>{
		//VectorSHomo<GenericVSpaceEndo<F>,GenericVSpace<F>,GenericVSpace<F>,F,NNegInt>, Tensor<GenericVSpaceEndo<F>,GenericDual<F>,GenericVSpace<F>,F,NNegInt>{
	
	private HashSet<ComposedObject<GenericDual<F>,GenericVSpace<F>>> endoMap;
	private GenericVSpace<F> arg;
	private GenericVSpace<F> val;
	//private Generator<GenericVSpace<F>,F> genSet;
	public GenericVSpaceEndo (){super(); endoMap = new HashSet<ComposedObject<GenericDual<F>,GenericVSpace<F>>> ();}
	
	public GenericVSpaceEndo (GenericDual<F> dual, GenericVSpace<F> vector){
		this();
		if(!dual.isZero()&&!vector.isZero()) endoMap.add(new ComposedObject<GenericDual<F>,GenericVSpace<F>>(dual,vector));
	}
	
	public GenericVSpaceEndo<F> add(GenericVSpaceEndo<F> another) {
		GenericVSpaceEndo<F> sum = new GenericVSpaceEndo <F> ();
		for (ComposedObject<GenericDual<F>,GenericVSpace<F>> entry:endoMap) sum.endoMap.add(entry.clone());
		for (ComposedObject<GenericDual<F>,GenericVSpace<F>> entry:another.endoMap) {
			
		}
		
		return null;
	}

	
	public boolean equals(GenericVSpaceEndo<F> another) {
		// TODO Auto-generated method stub
		return false;
	}

	
	

	
	public void clear() {
		// TODO Auto-generated method stub
		
	}

//	public int dim (){return genSet.rank()==0?endoMap.size():genSet.rank();}
	
	public F getValue(NNegInt index) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public NNegInt getIndex(F val) {
		// TODO Auto-generated method stub
		return null;
	}
	public boolean isDiscrete(){
		if(arg!=null) return arg.isDiscrete();
		Iterator<ComposedObject<GenericDual<F>,GenericVSpace<F>>> it = endoMap.iterator();
		if(it.hasNext()) return it.next().getSecond().isDiscrete();
		return true;
	}

	//public Generator<GenericVSpace<F>,F> getGenerators(){return genSet;}
	
	public void setEntry(NNegInt index, F value) {
		// TODO Auto-generated method stub
		
	}

	
	public Iterator<Entry<NNegInt, F>> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public void setArgument(GenericVSpace<F> arg) {
		// TODO Auto-generated method stub
		
	}

	
	public void f() {
		if(arg!=null) {
			GenericVSpace<F> sum = new GenericVSpace <F> ();
			for (ComposedObject<GenericDual<F>,GenericVSpace<F>> entry:endoMap){
				GenericDual<F> dual = entry.getFirst();
				dual.f(arg);
				sum = sum.add(entry.getSecond().multiply(dual.getValue()));
			}
			val = sum;
		}
		
	}

	
	public void f(GenericVSpace<F> arg) {
		this.arg = arg;
		f();
	}

	
	public GenericVSpace<F> getArgument() {return arg==null?null:arg;}

	
	public GenericVSpace<F> getValue() {
		if(val==null){
			if(arg!=null) f();
		}
		return val==null?null:val;
	}

	
	public GenericVSpaceEndo<F> tensorize(GenericDual<F> left,
			GenericVSpace<F> right) {return new GenericVSpaceEndo<F> (left,right);}

	
	public GenericVSpaceEndo<F> multiply(F scalar) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public GenericVSpaceEndo<F> addInverse() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public boolean isCommutative() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean isUnit(){
		//TODO implement me!!!
		return false;
	}

	
	public boolean isZero() {
		// TODO Auto-generated method stub
		return false;
	}

	
	public GenericVSpaceEndo<F> multiply(GenericVSpaceEndo<F> another) {
		// TODO Auto-generated method stub
		return null;
	}
	private ComposedObject<GenericDual<F>,GenericVSpace<F>> findObject (GenericDual<F> dual){
		for (ComposedObject<GenericDual<F>,GenericVSpace<F>> entry:endoMap) if(dual.equals(entry.getFirst())) return entry;
		return null;
	}
	public GenericVSpaceEndo<F> ringAct(F scalar) {return multiply(scalar);}

	public F remove(NNegInt index) {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected HashSet<ComposedObject<GenericDual<F>,GenericVSpace<F>>> getEndoMap(){return endoMap;}
	@SuppressWarnings("unchecked")
	public AbstractRId<F> canonicalHomo(){return new AbstractRId<F>();}
}
