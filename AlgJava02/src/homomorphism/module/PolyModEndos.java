package homomorphism.module;

import java.util.Iterator;
import java.util.Map.Entry;

import group.NNegInt;
import ring.AbstractCommRing;
import ring.grading.GrIndex;
import ring.grading.GradedIndexing;
import ring.poly.MonoPoly;
import topo.AbstractElFct;
import algebras.Algebra;
//TODO implement or remove
public class PolyModEndos<A extends AbstractCommRing<A>>
		extends
		AbstractElFct<MonoPoly<GradedIndexing<NNegInt>, A>, MonoPoly<GradedIndexing<NNegInt>, A>>
		implements Algebra<PolyModEndos<A>, A, NNegInt> {
	private GrIndex         derEndo;
	private GrIndex      xTimesEndo;
	private GrIndex multiplierIndex;
	private A scalar;
	protected PolyModEndos (){
		super();
		
	}
	public PolyModEndos<A> ringAct(A scalar) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public boolean isZero() {
		// TODO Auto-generated method stub
		return false;
	}

	
	public PolyModEndos<A> add(PolyModEndos<A> another) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public PolyModEndos<A> operate(PolyModEndos<A> another) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public boolean equals(PolyModEndos<A> another) {
		// TODO Auto-generated method stub
		return false;
	}

	
	public boolean isDiscrete() {
		// TODO Auto-generated method stub
		return false;
	}

	
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	
	public A getValue(NNegInt index) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public NNegInt getIndex(A val) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public A remove(NNegInt index) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public void setEntry(NNegInt index, A value) {
		// TODO Auto-generated method stub
		
	}

	
	public Iterator<Entry<NNegInt, A>> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public void f() {
		if(arg==null) return;
		val = new MonoPoly<GradedIndexing<NNegInt>,A> ();
		if(arg.isZero()) return;
		if(derEndo==null&&xTimesEndo==null&&multiplierIndex==null) return;
		if(derEndo!=null&&xTimesEndo!=null&&multiplierIndex!=null) {
			if(derEndo.equals(xTimesEndo)){
				derEndo = null;
				xTimesEndo = null;
				
			}
		}
		for (Entry<GradedIndexing<NNegInt>,A> entry:arg){
			if(derEndo!=null) {
				
			}
		}
	}

	
	public PolyModEndos<A> addInverse() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public PolyModEndos<A> multiply(PolyModEndos<A> another) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public boolean isAssociative() {return true;}
	
	private void setScalar (int times){
		if(scalar!=null) {
			A cp = scalar;
			int count = 0;
			while (count<times) {
				cp = scalar.add(cp);
				count++;
			}
		}
	}

}
