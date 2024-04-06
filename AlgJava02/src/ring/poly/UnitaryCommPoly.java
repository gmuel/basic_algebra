package ring.poly;

import java.util.Iterator;
import java.util.Map.Entry;

import field.AbstractField;
import group.NNegInt;
import group.UnitGroup;
import ring.UnitaryCommRing;
import ring.grading.GradedIndexing;

public class UnitaryCommPoly<F extends AbstractField<F>> extends UnitaryCommRing<UnitaryCommPoly<F>> {
	private CommPoly<F> poly;
	private static final String FORMAT = "abcdefghijklmnopqrstuvwxyz";
	public UnitaryCommPoly() {
		super();
		
	}
	public UnitaryCommPoly(NNegInt[] degree, F coefficient){
		this();
		poly = new CommPoly<F>(degree,coefficient);
	}
	public UnitaryCommPoly(GradedIndexing<NNegInt> degree, F coefficient){
		this();
		poly = new CommPoly<F>(degree,coefficient);
	}
	public UnitaryCommPoly (MonoPoly<GradedIndexing<NNegInt>,F> poly){
		this();
		this.poly = new CommPoly<F>(poly);
	}
	public UnitaryCommPoly (CommPoly<F> poly){
		this();
		this.poly = new CommPoly<F>(poly);
	}
	public UnitaryCommPoly(UnitaryCommPoly<F> poly){
		this(poly.poly);
	}
	public UnitaryCommPoly<F> addInverse() {
		return new UnitaryCommPoly<F>(poly.addInverse());
	}

	
	public boolean isUnit() {return poly.isUnit();}

	
	public boolean isZero() {return poly.isZero();}

	
	public UnitaryCommPoly<F> multiply(UnitaryCommPoly<F> another) {
		return new UnitaryCommPoly<F>(poly.multiply(another.poly));
	}

	
	public UnitaryCommPoly<F> add(UnitaryCommPoly<F> another) {
		return new UnitaryCommPoly<F>(poly.add(another.poly));
	}

	
	public boolean equals(UnitaryCommPoly<F> another) {
		return poly.equals(another.poly);
	}

	
	public boolean isDiscrete() {
		return poly.isDiscrete();
	}

	
	public UnitaryCommPoly<F> getOne() {
		return null;
	}
	/**
	 * Return this polynomial as instance of
	 * <code>GradedMultiPoly</code> - changes to
	 * the returned object <b>will not</b> be reflected in
	 * this instance
	 * @return this polynomial
	 */
	public GradedMultiPoly<F> getPolynomial (){
		return poly.getPolynomial();
	}
	
	public UnitGroup<UnitaryCommPoly<F>> getUnit() {
		return isUnit()?new UnitGroup<UnitaryCommPoly<F>> (this):null;
	}

	
	public UnitaryCommPoly<F> inverse() {
		return new UnitaryCommPoly<F>(poly.inverse());
	}
	public String toString (){
		if(isZero()) return "0";
		String plus = " + ", nl = "\n", format = "%1$s^%2$s", sp = " ";
		StringBuilder sb = new StringBuilder ();
		Iterator<Entry<GradedIndexing<NNegInt>,F>> it = poly.getPolynomial().iterator();
		while (it.hasNext()){
			Entry<GradedIndexing<NNegInt>,F> entry = it.next();
			GradedIndexing<NNegInt> index = entry.getKey();
			StringBuilder indetStr = new StringBuilder();
			Iterator<Entry<NNegInt,NNegInt>> indexIt = index.iterator();
			while (indexIt.hasNext()){
				Entry<NNegInt,NNegInt> IEntry = indexIt.next();
				int sIndex = (int) IEntry.getKey().eval();
				indetStr.append(String.format(format,FORMAT.substring(sIndex,sIndex+1),
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
