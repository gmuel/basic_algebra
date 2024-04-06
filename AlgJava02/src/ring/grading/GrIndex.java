package ring.grading;

import java.util.Iterator;
import java.util.Map.Entry;

import module.integer.FIntD;

import ring.integer.IntRing;

import group.NNegInt;
/**
 * The graded index sub-class: 
 * @author adin
 *
 */
public class GrIndex extends GradedIndexing<NNegInt>{
	/**the zero index constant*/
	public static final GrIndex ZERO = new GrIndex();
	/**the grader: maps each instance of this
	 * class to some integer*/
	private Grader currentGrading;
	/**
	 * Constructs the empty graded index
	 */
	protected GrIndex (){
		super();
	}
	/**
	 * Constructs an graded index object
	 * specified by the given argument array <tt>index</tt>
	 * <p><b>note</b>, that be default the instance has
	 * the canonical grader object (the sum degree)
	 * @param index the argument array
	 */
	public GrIndex (int[] index){
		this();
		for (int i = 0; i < index.length; i++){
			if(index[i]>0) {
				maxIndex = new NNegInt(i);
				indexTuple.setEntry(maxIndex, new NNegInt(index[i]));
			}
		}
		setCanonicalGrader();
	}
	/**
	 * Constructs an graded index object
	 * specified by the given argument array <tt>index</tt>
	 * <p><b>note</b>, that be default the instance has
	 * the canonical grader object (the sum degree)
	 * @param index the argument array
	 */
	public GrIndex (NNegInt[] index){
		this();
		for (int i = 0; i < index.length; i++) {
			if(index[i]==null) continue;
			if(!index[i].equals(NNegInt.ZERO)){
				maxIndex = new NNegInt(i);
				indexTuple.setEntry(maxIndex, index[i]);
				
			}
		}
		setCanonicalGrader();
	}
	/**
	 * Constructs an graded index object
	 * specified by the given argument <tt>array</tt>
	 * <p><b>note</b>, that be default the instance has
	 * the canonical grader object (the sum degree)
	 * @param index the argument array
	 */
	public GrIndex (GradedIndexing<NNegInt> index){
		this();
		for (Entry<NNegInt,NNegInt> entry:index) {
			NNegInt eIndex = entry.getValue();
			if(!eIndex.equals(NNegInt.ZERO)){
				maxIndex = entry.getKey();
				indexTuple.setEntry(maxIndex, eIndex);
			}
		}
		setCanonicalGrader();
	}
	/**
	 * Returns the image of the grader
	 * evaluated at this element
	 */
	public long eval() {
		currentGrading.f(this);
		return currentGrading.getValue().eval();
	}
	/**
	 * Returns the sum of both indices
	 */
	public GradedIndexing<NNegInt> operate(GradedIndexing<NNegInt> another) {
		GrIndex op = new GrIndex(this);
		for (Entry<NNegInt,NNegInt> entry:another){
			NNegInt key = entry.getKey(), val = entry.getValue();
			NNegInt en;
			if((en = op.indexTuple.getValue(key))!=null) {
				en = en.operate(val);
				if(!en.equals(NNegInt.ZERO)) op.indexTuple.setEntry(key, en);
				else op.indexTuple.remove(key);
			}
			else op.indexTuple.setEntry(key, val);
		}
		return op;
	}
	/**
	 * Returns some negative integer
	 * if this index object is lexicographically smaller,
	 * some positive integer if it is greater or zero  
	 */
	public int compareTo(GradedIndexing<NNegInt> o) {
		//if(equals(o)) return 0;
		Iterator<Entry<NNegInt,NNegInt>> it1 = iterator(), it2 = o.iterator();
		Entry<NNegInt,NNegInt> entry1 = null, entry2 = null;
		while (it1.hasNext()&&it2.hasNext()){
			entry1 = it1.next();
			entry2 = it2.next();
			int keyComp = entry1.getKey().compareTo(entry2.getKey());
			if(keyComp!=0){
				return keyComp<0?entry1.getValue().compareTo(NNegInt.ZERO):
					NNegInt.ZERO.compareTo(entry2.getValue());
			}
			int valComp = entry1.getValue().compareTo(entry2.getValue());
			if(valComp!=0) return valComp;
		}
		if(it1.hasNext()) {
			entry1 = it1.next();
			return entry1.getValue().compareTo(NNegInt.ZERO);
		}
		if(it2.hasNext()){
			entry2 = it2.next();
			return NNegInt.ZERO.compareTo(entry2.getValue());
		}
		return 0;
	}
	public boolean isNeutral(){return length()==0?true:false;}
	
	/**
	 * Sets the current grader instance to <tt>another</tt>
	 * if and only if <tt>another!=null</tt> returns true
	 * @param another some non-null grader
	 */
	public synchronized void setGrader (Grader another){
		if(another!=null) currentGrading = new Grader(another);
	}
	/**
	 * Sets the grader instance to sum grader
	 */
	private void setCanonicalGrader(){
		FIntD dual = new FIntD();
		for (Entry<NNegInt,NNegInt> entry:indexTuple){
			dual.setEntry(entry.getKey(), IntRing.ONE);
		}
		currentGrading = new Grader(dual);
	}
	public String toString (){
		if(length()==0) return "(0)";
		StringBuilder sb = new StringBuilder ("(");
		String zero = "0", comma = ",";
		NNegInt index = NNegInt.ZERO;
		while (index.compareTo(maxIndex)<=0) {
			NNegInt entry = indexTuple.getValue(index);
			if(entry!=null) {
				if(entry.isNeutral()) sb.append(zero);
				else sb.append(entry.toString());
			} else sb.append(zero);
			if(index.compareTo(maxIndex)<0) sb.append(comma);
			index = index.increment();
		}
		sb.append(")");
		return sb.toString();
	}
}
