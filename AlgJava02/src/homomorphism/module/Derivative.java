package homomorphism.module;

import java.util.Iterator;
import java.util.Map.Entry;

import algebras.AbstractLieAlgebra;
import algebras.Algebra;

import group.NNegInt;

import ring.AbstractCommRing;
import ring.grading.GradedIndexing;
import ring.poly.MonoPoly;

import topo.AbstractElFct;
import topo.Function;

public class Derivative<F extends AbstractCommRing<F>> extends
AbstractElFct<MonoPoly<GradedIndexing<NNegInt>,F>,MonoPoly<GradedIndexing<NNegInt>,F>>
		implements 	Algebra<Derivative<F>,F,NNegInt>{
	protected MonoPoly<GradedIndexing<NNegInt>,F> arg;
	protected MonoPoly<GradedIndexing<NNegInt>,F> val;
	
	protected Derivative (){super();}
	
	public void f() {
		if(arg==null) return;
		GradedIndexing<NNegInt> deg = arg.getDegree();
		val = new MonoPoly<GradedIndexing<NNegInt>,F> ();
		if(deg==null) return;
		if(deg.isNeutral()) return;
		
	}

	@Override
	public void f(MonoPoly<GradedIndexing<NNegInt>, F> arg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MonoPoly<GradedIndexing<NNegInt>, F> getArgument() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MonoPoly<GradedIndexing<NNegInt>, F> getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Derivative<F> ringAct(F scalar) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isZero() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Derivative<F> add(Derivative<F> another) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Derivative<F> operate(Derivative<F> another) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean equals(Derivative<F> another) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDiscrete() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public F getValue(NNegInt index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NNegInt getIndex(F val) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public F remove(NNegInt index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setEntry(NNegInt index, F value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Iterator<Entry<NNegInt, F>> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Derivative<F> addInverse() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Derivative<F> multiply(Derivative<F> another) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAssociative() {
		// TODO Auto-generated method stub
		return false;
	}

}
