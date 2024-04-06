package homomorphism.module;

import java.util.Iterator;
import java.util.Map.Entry;

import ring.homo.AbstractRId;
import topo.AbstractFct;

import group.AbstractAbel;
import group.NNegInt;
import field.AbstractField;
import module.AbstractFiniteModule;
import module.GenericVSpace;
/**
 * Class of the generic dual space 
 * @author bzfmuell
 *
 * @param <F>
 */
public class GenericDual<F extends AbstractField<F>> extends AbstractFct<AbstractFiniteModule<F>,F>//AbstractAbel<GenericDual<F>> implements
		//VectorSHomo<GenericDual<F>,GenericVSpace<F>, F, F, NNegInt> {
		implements ModuleHomo<GenericDual<F>,AbstractFiniteModule<F>,F,F,F,NNegInt>{
	private GenericVSpace<F> element;
	private GenericVSpace<F> arg;
	private F val;
	private F scl;
	public GenericDual (AbstractFiniteModule<F> element){
		super();
		this.element = new GenericVSpace<F>(element);
		setScalar();
	}
	public void clear() {}
	
	
	
	public GenericDual<F> add(GenericDual<F> another) {return new GenericDual<F> (element.add(another.element));}

	public boolean equals(GenericDual<F> another) {return element.equals(element)?true:false;}

	public void f() {
		if(arg!=null) {
			if(!arg.isZero()){
				F sum = null;
				for (Entry<NNegInt,F> entry:element){
					F argVal = arg.getValue(entry.getKey());
					if(argVal!=null&&!argVal.isZero()) {
						if(sum!=null) sum = sum.add(argVal.multiply(entry.getValue()));
						else sum = argVal.multiply(entry.getValue());
					}
				}
				if(scl!=null&&!scl.isZero()) val = sum.multiply(scl.inverse());
				
			} 
			
		}
		
	}
	
	public NNegInt getIndex(F val) {NNegInt index; return (index = element.getIndex(val))==null?null:index;}
	
	public F getValue(NNegInt index) {
		F entry;
		return (entry = element.getValue(index))==null?null:entry;
	}
	public boolean isDiscrete(){return element.isDiscrete();}
	public boolean isZero (){return element.isZero()?true:false;}
	
	public Iterator<Entry<NNegInt, F>> iterator() {return element.iterator();}

	public GenericDual<F> multiply(F scalar) {return new GenericDual<F> (element.multiply(scalar));}
	
	public GenericDual<F> operate(GenericDual<F> another){return add(another);}
	
	public void setEntry(NNegInt index, F value) {element.setEntry(index,value);}

	public F getScalar(){return scl==null?null:scl;}
	
	public GenericDual<F> ringAct(F scalar){return multiply(scalar);}
	public F remove (NNegInt index){
		return null;
	}
	public void setArgument(AbstractFiniteModule<F> arg){
		
	}
	private void setScalar (){
		for (Entry<NNegInt,F> entry:element) {
			F coeff = entry.getValue();
			if(scl==null) scl = coeff.multiply(coeff);
			else scl = scl.add(coeff.multiply(coeff));
		}
		
	}
	@SuppressWarnings("unchecked")
	public AbstractRId<F> canonicalHomo(){return new AbstractRId<F>();}
}
