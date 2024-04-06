package ring.poly;

import field.AbstractField;
import group.NNegInt;
import group.UnitGroup;

import java.util.Iterator;
import java.util.Map.Entry;

import ring.AbstractCommRing;
import ring.grading.GrIndex;
import ring.grading.GradedIndexing;
/**
 * Wrapper class - an instance of {@link MonoPoly}, with
 * indexing system {@link GradedIndexing} with {@link NNegInt}
 * <p><b>Note</b>, any instance of this class is immutable in contrast
 * to <code>MonoPoly</code>
 * @author adin
 *
 * @param <F>
 */
public class CommPoly<F extends AbstractField<F>> extends AbstractCommRing<CommPoly<F>> {
	/**the polynomial wrapped*/
	private GradedMultiPoly<F> poly;
	/**some output formatting string*/
	private static String FORMAT_STR = "abcdefghijklmnopqrstuvwxyz";
	/**
	 * Constructs the zero polynomial
	 */
	public CommPoly (){super();poly = new GradedMultiPoly<F>();}
	/**
	 * Constructs the monomial <tt>coefficient * X_1^t_1 * ... * X_n^t_n</tt>
	 * where <tt>n</tt> is less or equal to the length of the array <tt>index</tt>
	 * and <tt>t_i</tt> the entry at <tt>index[i]</tt>
	 * @param index the index
	 * @param coefficient the coefficient
	 */
	public CommPoly(NNegInt[] index, F coefficient){
		this(new GrIndex(index),coefficient);
	}
	/**
	 * Constructs the monomial 
	 * <p><tt>coefficient * X_1^t_1 * ... * X_n^t_n</tt>
	 * <p>where <tt>n</tt> is less or equal to the length of the <tt>index</tt>
	 * and <tt>t_i</tt> the entry at <tt>index.getValue(i)</tt>
	 * @param index the index
	 * @param coefficient the coefficient
	 */
	public CommPoly (GradedIndexing<NNegInt> index, F coefficient){
		this();
		if(index!=null&&coefficient!=null) poly.setCoefficient(new GrIndex(index),coefficient);
	}
	/**
	 * Constructs a polynomial specified by the
	 * argument polynomial of type <code>MonoPoly</code>
	 * @param poly some polynomial
	 */
	public CommPoly(MonoPoly<GradedIndexing<NNegInt>,F> poly){
		this();
		for (Entry<GradedIndexing<NNegInt>,F> entry:poly) this.poly.setCoefficient(entry.getKey(), entry.getValue());
	}
	/**
	 * Constructs a copy of <tt>another</tt>
	 * polynomial
	 * @param another some other polynomial
	 */
	public CommPoly(CommPoly<F> another){this(another.poly);}
	/**
	 * Implementing interface
	 */
	public CommPoly<F> add(CommPoly<F> another) {return new CommPoly<F>(poly.add(another.poly));}
	/**
	 * Implementing interface
	 */
	public CommPoly<F> addInverse() {return new CommPoly<F>(poly.addInverse());}
	/**
	 * Implementing interface
	 */
	public boolean isUnit() {return poly.isUnit();}
	/**
	 * Implementing interface
	 */
	public boolean isZero() {return poly.isZero();}
	/**
	 * Implementing interface
	 */
	public CommPoly<F> multiply(CommPoly<F> another) {return new CommPoly<F>(poly.multiply(another.poly));}
	/**
	 * Implementing interface
	 */
	public boolean equals(CommPoly<F> another) {return poly.equals(another.poly);}
	/**
	 * Implementing interface
	 */
	public boolean isDiscrete() {return poly.isDiscrete();}
	/**
	 * Returns a deep copy of this polynomial
	 * @return this polynomial
	 */
	public GradedMultiPoly<F> getPolynomial (){return new GradedMultiPoly<F>(poly);}
	/**
	 * Implementing interface
	 */
	public UnitGroup<CommPoly<F>> getUnit() {
		if(!isUnit()) return null;
		return new UnitGroup<CommPoly<F>>(this);
	}
	/**
	 * Implementing interface
	 */
	public CommPoly<F> inverse() {
		if(!isUnit()) return null;
		GradedIndexing<NNegInt> deg = poly.getDegree();
		return new CommPoly<F> (new GradedMultiPoly<F>(deg,poly.getCoefficient(deg).inverse()));
	}
	public String toString (){
		if(isZero()) return "0";
		String plus = " + ", nl = "\n", format = "%1$s^%2$s", sp = " ";
		StringBuilder sb = new StringBuilder ();
		Iterator<Entry<GradedIndexing<NNegInt>,F>> it = poly.iterator();
		while (it.hasNext()){
			Entry<GradedIndexing<NNegInt>,F> entry = it.next();
			GradedIndexing<NNegInt> index = entry.getKey();
			StringBuilder indetStr = new StringBuilder();
			Iterator<Entry<NNegInt,NNegInt>> indexIt = index.iterator();
			while (indexIt.hasNext()){
				Entry<NNegInt,NNegInt> IEntry = indexIt.next();
				int sIndex = (int) IEntry.getKey().eval();
				indetStr.append(String.format(format,FORMAT_STR.substring(sIndex,sIndex+1),
						IEntry.getValue().toString()));
				if(indexIt.hasNext()) indetStr.append(sp);
			}
			F coeff = entry.getValue();
			if (!coeff.multiply(coeff).equals(coeff)||index.isNeutral()) {
				sb.append(entry.getValue());
				sb.append(sp);
			}
			sb.append(indetStr);
			if(it.hasNext()) {
				sb.append(plus);
				int modLength60 = sb.length()%60;
				if(modLength60>55||modLength60<=5) sb.append(nl);
			}
		}
		return sb.toString();
	}

}
