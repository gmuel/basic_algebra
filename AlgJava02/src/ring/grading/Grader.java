package ring.grading;

import java.util.Iterator;
import java.util.Map.Entry;

import ring.integer.IntRing;

import module.integer.FIntD;
import module.integer.FIntM;

import group.CompMonoid;
import group.NNegInt;
/**
 * The grader class: induces an ordering relation an the
 * the module of non-negative integer array (here: {@link GradedIndexing}
 * @author adin
 *
 */
public class Grader extends Grading<Grader,GradedIndexing<NNegInt>> implements CompMonoid<Grader>{
	/**the dual element mapping an
	 * instance of <code>GradedIndexing</code>
	 * to some integer*/
	private final FIntD dualElement;
	/**
	 * Constructs a new <code>Grader</code>
	 * instance with the dual element speciified
	 * by the parameter <tt>dual</tt>
	 * @param dual the dual element
	 */
	public Grader (FIntM dual){
		super();
		dualElement = new FIntD(dual);
	}
	/**
	 * Constructs a new <code>Grader</code>
	 * instance with the dual element set
	 * to <tt>dual</tt>
	 * @param dual the dual element
	 */
	public Grader (FIntD dual){
		super();
		dualElement = new FIntD(dual);
	}
	/**
	 * Constructs a new <code>Grader</code>
	 * instance by copying the original
	 * <tt>another</tt> instance
	 * @param another some other instance
	 */
	public Grader  (Grader another){
		this(another.dualElement);
	}
	/**
	 * Returns some negative integer if
	 * this element is lexicographically smaller
	 * than <tt>another</tt>, positive if greater
	 * else zero
	 */
	public int compareTo(Grader another){
		Iterator<Entry<NNegInt,IntRing>> entryIt1 = dualElement.iterator();
		Iterator<Entry<NNegInt,IntRing>> entryIt2 = another.dualElement.iterator(); 
		while(entryIt1.hasNext()&&entryIt2.hasNext()){
			Entry<NNegInt,IntRing> entry1 = entryIt1.next(), entry2 = entryIt2.next();
			int keyComp = entry1.getKey().compareTo(entry2.getKey());
			if(keyComp==0){
				int valComp = entry1.getValue().compareTo(entry2.getValue());
				if(valComp==0) continue;
				return valComp;
			}
			return keyComp<0?entry1.getValue().compareTo(IntRing.ZERO):IntRing.ZERO.compareTo(entry2.getValue());
		}
		if(entryIt1.hasNext()) {
			while(entryIt1.hasNext()){
				Entry<NNegInt,IntRing> entry = entryIt1.next();
				if(!entry.getValue().isZero()) return entry.getValue().compareTo(IntRing.ZERO);
			}
		}
		if(entryIt2.hasNext()) {
			while(entryIt2.hasNext()){
				Entry<NNegInt,IntRing> entry = entryIt2.next();
				if(!entry.getValue().isZero()) return IntRing.ZERO.compareTo(entry.getValue());
			}
		}
		return 0;
	}
	/**
	 * Returns true only if both instance have
	 * the same entries 
	 */
	public boolean equals(Grader another){
		return dualElement.equals(dualElement);
	}
	/**
	 * Implemented for correct behavior
	 */
	public boolean equals(Object o){
		if(this==o) return true;
		if(!(o instanceof Grader)) return false;
		return equals((Grader)o);
	}
	/**
	 * Implemented for correct behavior
	 */
	public int hashCode (){
		return dualElement.hashCode();
	}
	public boolean isDiscrete(){return true;}
	public boolean isNeutral(){return dualElement.equals(FIntD.ZERO);}
	/**
	 * Returns the sum of both grader instances
	 */
	public Grader operate(Grader another) {
		return new Grader(new FIntM(dualElement.add(another.dualElement)));
	}

	/**
	 * Computes the image value
	 * <tt>f(v)</tt>, where <tt>f</tt>
	 * is the dual element and <tt>v</tt> the
	 * current argument
	 */
	public void f() {
		if(arg==null) return;
		val = IntRing.ZERO;
		for (Entry<NNegInt,IntRing> entry:dualElement){
			NNegInt index = entry.getKey();
			IntRing dCoeff = entry.getValue(), argCoeff = null;
			NNegInt ent;
			if((ent = arg.indexTuple.getValue(index))!=null){
				argCoeff = ent.getValue();
				val = val.add(dCoeff.multiply(argCoeff));
			}
		}
	}
	public String toString (){
		return dualElement.toString();
	}
}
