package ring;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import ring.integer.IntRing;
import group.UnitGroup;
//TODO implement class!!!
public class Float extends AbstractCommRing<Float>{
	private TreeMap<Integer,Object> valueMap;
	/**the number of trailing (post comma) values*/
	private int trailingFloats;
	private int modCount;
	//TODO implement this class and remove exception
	public Float (){throw new UnsupportedOperationException("\nClass not implemented");}//super();valueMap = new TreeMap<Integer,Object> ();}
	public Float (float value){this((double) value);}
	public Float (double value){
		this(20,5);
		constructVal(value);
	}
	public Float (int trailingFloats, int modCount){
		this();
		this.modCount       = modCount;
		this.trailingFloats = trailingFloats;
		
	}
	public Float addInverse() {
		// TODO Auto-generated method stub
		return null;
	}
	public double getValue (){
		double val = 0, arg = 1d;
		for (Entry<Integer,Object> entry:valueMap.entrySet()){
			int count = 0;
			while(count!=entry.getKey()) {
				arg = arg*modCount;
				count++;
			}
			val += arg;
		}
		return val;
	}
	public UnitGroup<Float> getUnit(){return isUnit()?new UnitGroup<Float> (this):null;}
	public Float inverse(){return isUnit()?null:new Float(1d/getValue());}
	public boolean isUnit(){return isZero()?false:true;}
	public boolean isZero() {return valueMap.size()==0?true:false;}
	public Float multiply(Float another) {
		// TODO Auto-generated method stub
		return null;
	}
	public Float add(Float another) {
		if(modCount!=another.modCount) throw new IllegalArgumentException ("");
		Float sum = new Float (Math.max(trailingFloats,another.trailingFloats),modCount);
		for (Map.Entry<Integer,Object> entry:valueMap.entrySet()) sum.valueMap.put(entry.getKey(), entry.getValue());
		return sum;
	}
	private void constructVal (double value){
		int counter = 0;
		double cp = value;
		while (counter < trailingFloats){
			if(cp>0) {
				IntRing val1 = new IntRing ((int) Math.floor(cp));
			}
			else {
				
			}
			counter++;
		}
	}
	
	public boolean equals(Float another) {return add(another.addInverse()).isZero()?true:false;}
	public boolean isDiscrete(){return false;}
}
