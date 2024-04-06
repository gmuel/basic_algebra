package homomorphism.module;

import java.util.Map.Entry;

import ring.poly.AFPoly;
import ring.poly.MonoPoly;
import field.AbstractField;
import group.NNegInt;
import homomorphism.ring.RingHomo;

public class PolyMod<F extends AbstractField<F>> implements
		RingHomo<PolyMod<F>, MonoPoly<NNegInt,F>, AbstractRMatrix<F>> {
	private AbstractFMatrix<F> mat;
	private AbstractFMatrix<F>  id;
	private AbstractFMatrix<F> val;
	private AFPoly<F>          arg;
	private AFPoly<F>          mod;
	
	public PolyMod(AbstractFMatrix<F> matrix) {
		if(matrix!=null) {
			mat = new AbstractFMatrix<F>(matrix);
			mod = mat.charPoly();
			val = new AbstractFMatrix<F>(mod.getDegree());
			id  = AbstractFMatrix.identity(mat.one, mod.getDegree());
		} else throw new IllegalArgumentException ("\nNull argument not permitted...");
	}
	
	public void f() {
		if(mat==null) throw new NullPointerException ("\nMatrix not initialized...");
		if(arg==null) return;
		AFPoly<F> modArg = arg.mod(mod);
		NNegInt lastIndex = NNegInt.ZERO;
		AbstractFMatrix<F> pow = id;
		for (Entry<NNegInt,F> entry:modArg){
			NNegInt index = entry.getKey();
			F       coeff = entry.getValue();
			while(lastIndex.compareTo(index)<0){
				pow = pow.multiply(mat);
				lastIndex = lastIndex.increment();
			}
			if(!pow.isZero()) {
				val = val.add(pow.multiply(coeff));
			} else break;
		}
		
	}

	
	public void f(MonoPoly<NNegInt, F> arg) {
		setArgument(arg);
		f();
	}

	
	public MonoPoly<NNegInt, F> getArgument() {return arg==null?null:arg;}

	
	public AbstractFMatrix<F> getValue() {return val;}

	
	public boolean isKernel(MonoPoly<NNegInt, F> arg) {
		f(arg);
		return val.isZero()?true:false;
	}


	public void setArgument(MonoPoly<NNegInt, F> arg) {
		if(arg!=null) this.arg = new AFPoly<F>(arg);		
	}

	public void setMatrix(AbstractFMatrix<F> mat){
		if(mat!=null) {
			this.mat = new AbstractFMatrix<F>(mat);
			mod = mat.charPoly();
			if(id==null||!id.getDim().equals(mat.getDim())) id  = AbstractFMatrix.identity(mat.one!=null?mat.one:id.one, mod.getDegree());
		}
	}
}
