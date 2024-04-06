package module;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;

import field.AbstractField;
import group.AbstractAbel;
import group.NNegInt;
/**
 * Generalized finite dimensional vector space class over some arbitrary field of
 * type &ltF&gt
 * @author adin
 *
 * @param <F> the type of some sub class of {@link AbstractField}
 */
public class GenericVSpace<F extends AbstractField<F>> extends AbstractFiniteModule<F>{//AbstractAbel<GenericVSpace<F>>implements VectorSpace<GenericVSpace<F>,F,NNegInt>{
	/**the coefficient map*/
	private TreeMap<NNegInt,F> coeffMap;
	/**Constructs the zero vector - <b>note</b> all zero vector in
	 * vector spaces of different dimension are considered equal*/
	public GenericVSpace (){super();coeffMap = new TreeMap<NNegInt,F>();}
	/**
	 * Constructs a vector element from
	 * the argument <tt>array</tt>
	 * <br />Calling this constructor with all null array entries
	 * is equivalent to calling the zero-constructor. Null arguments
	 * are not supported
	 * @param array the argument array of type &ltF&gt
	 */
	public GenericVSpace (F[] array){this(); for (int i = 0; i < array.length; i++) {if(array[i]!=null) this.setEntry(new NNegInt(i), array[i]);}}
	/**
	 * Constructs a copy of the original <tt>vector</tt>
	 * @param vector original
	 */
	public GenericVSpace(AbstractFiniteModule<F> vector){
		this();
		for (Entry<NNegInt,F> entry:vector){
			F coeff;
			if(!(coeff = entry.getValue()).isZero()) coeffMap.put(entry.getKey(), coeff); 
		}
	}
	public GenericVSpace<F> multiply(F scalar) {
		GenericVSpace<F> slrProd = new GenericVSpace <F> ();
		for (Entry<NNegInt,F> entry:coeffMap.entrySet()) slrProd.coeffMap.put(entry.getKey(), entry.getValue().multiply(scalar));
		return slrProd;
	}

		
	public GenericVSpace<F>ringAct(F scalar) {return multiply(scalar);}

	
	public GenericVSpace<F> add(GenericVSpace<F> another) {
		GenericVSpace<F> sum = new GenericVSpace <F> ();
		for (Entry<NNegInt,F> entry:coeffMap.entrySet()) sum.coeffMap.put(entry.getKey(), entry.getValue());
		for (Entry<NNegInt,F> entry:another.coeffMap.entrySet()) {
			NNegInt key = entry.getKey();
			F sumEntry;
			if((sumEntry = sum.coeffMap.get(key))!=null) {
				sumEntry = sumEntry.add(entry.getValue());
				if(!sumEntry.isZero()) sum.coeffMap.put(key, sumEntry);
				else sum.coeffMap.remove(key);
			} else sum.coeffMap.put(key, entry.getValue()); 
		}
		return sum;
	}

	
	public boolean equals(GenericVSpace<F> another) {if(this==another) return true; return coeffMap.equals(another.coeffMap)?true:false;}

	
	public void clear() {coeffMap.clear();}

	
	public F getValue(NNegInt index) {
		F coeff;
		return (coeff = coeffMap.get(index))==null?null:coeff;
	}

	
	public NNegInt getIndex(F val) {
		for (Entry<NNegInt,F> entry:this) {if(entry.getValue().equals(val)) return entry.getKey();}
		return null;
	}

	
	public void setEntry(NNegInt index, F value) {
		if(index.compareTo(NNegInt.ZERO)>=0&&value!=null){
			if(!value.isZero()) coeffMap.put(index, value);
		}
	}

	public boolean isZero (){
		for (Entry<NNegInt,F> entry:this) {if(!entry.getValue().isZero()) return false;}
		return true;
	}
	
	public Iterator<Entry<NNegInt, F>> iterator() {return coeffMap.entrySet().iterator();}
	
	public F remove(NNegInt index) {return coeffMap.get(index);}
	
	public String toString(){
		if(coeffMap.size()==0) return "0";
		StringBuilder sb = new StringBuilder ("(");
		String comma = ",", zero = "0";
		NNegInt lastKey = coeffMap.lastKey(), counter = NNegInt.ZERO;
		while(counter.compareTo(lastKey)<=0) {
			F entry = getValue(counter);
			if(entry==null) sb.append(zero);
			else sb.append(entry.toString());
			if(counter.compareTo(lastKey)<0) sb.append(comma);
			counter = counter.increment();
		}
		sb.append(")");
		return sb.toString();
	}
	
}