package homomorphism.ring;

import java.util.Map.Entry;

import group.NNegInt;
import homomorphism.module.AbstractRMatrix;
import ring.UnitaryCommRing;
import ring.extension.UnitaryExten;
import ring.poly.MonoPoly;
import topo.AbstractElFct;
/**
 * The finite ring extension embedder - maps
 * each extension element to its multiplication
 * operator
 * @author adin
 *
 * @param <U>
 */
public class ExtensionEmbedder<U extends UnitaryCommRing<U>> extends
		AbstractElFct<UnitaryExten<U>, AbstractRMatrix<U>> implements
		RingHomo<ExtensionEmbedder<U>, UnitaryExten<U>, AbstractRMatrix<U>> {
	private final MonoPoly<NNegInt,U> modPoly;
	private AbstractRMatrix<U> xMonom;
	public ExtensionEmbedder (MonoPoly<NNegInt,U> modPoly){
		super();
		this.modPoly = new MonoPoly<NNegInt,U> (modPoly);
	}
	public void f() {
		if(arg==null) return;
		if(xMonom==null) setXMonom();
		if(val==null) val = new AbstractRMatrix<U> (modPoly.getDegree());
		else clearValAndSetIdentity();
		NNegInt counter = NNegInt.ZERO;
		AbstractRMatrix<U> pow = xMonom;
		for (Entry<NNegInt,U> argEntry:arg) {
			NNegInt index = argEntry.getKey();
			U coeff = argEntry.getValue();
			if(!index.equals(NNegInt.ZERO)) {
				while (!counter.equals(index)) {
					pow = pow.multiply(xMonom);
					counter = counter.increment();
				}
				val = val.add(pow.ringAct(coeff));
			}
		}
	}

	
	public boolean isKernel(UnitaryExten<U> arg) {
		return arg==null?true:arg.isZero();
	}
	private void setXMonom (){
		NNegInt index = NNegInt.ZERO, rank = modPoly.getDegree(), lastCol = rank.decrement();
		U one = modPoly.getCoefficient(rank).getOne();
		xMonom = new AbstractRMatrix<U> (rank);
		while (index.compareTo(rank)<0){
			NNegInt indexInc = index.increment();
			xMonom.setEntry(indexInc, index, one);
			U coeff;
			if((coeff = modPoly.getCoefficient(index))!=null) {
				xMonom.setEntry(index, lastCol, coeff.addInverse());
			}
			index = indexInc;
		}
	}
	private void clearValAndSetIdentity(){
		val.clear();
		NNegInt index = NNegInt.ZERO, rank = modPoly.getDegree();
		U one = arg.getValue(index);
		if(one==null) return;
		while (index.compareTo(rank)<0) {
			val.setEntry(index, index, one);
			index = index.increment();
		}
	}
}
