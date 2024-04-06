package group.representation;

import module.AbstractFiniteModule;
import ring.UnitaryCommRing;
import ring.grading.GrIndex;
import ring.poly.UnitaryCommPoly;

import field.finitefield.ThreeCycle;

import group.NNegInt;

import homomorphism.group.Gl2SlHomo;
import homomorphism.module.AbstractRMatrix;
/**
 * The special linear group class: all square matrices
 * with determinant one over some ring <tt>&ltA&gt</tt>
 * @author adin
 *
 * @param <A> type of the a sub-class of {@link UnitaryCommRing}
 */
public class Sl<A extends UnitaryCommRing<A>> extends Gl<A>{
	
	Sl (){super();}
	Sl (NNegInt rank){super(rank);}
	/**
	 * Constructs a matrix with determinant
	 * one if and only if the determinant
	 * of the argument <tt>matrix</tt> is a
	 * unit
	 * @param matrix the argument
	 * @throws IllegalArgumentException non-unit determinant
	 */
	public Sl (A[][] matrix)throws IllegalArgumentException {
		this(new AbstractRMatrix<A>(matrix));
	}
	/**
	 * Constructs a matrix with determinant
	 * one if and only if the determinant
	 * of the argument <tt>matrix</tt> is a
	 * unit
	 * @param matrix the argument
	 * @throws IllegalArgumentException non-unit determinant
	 */
	public Sl(AbstractRMatrix<A> matrix) throws IllegalArgumentException {
		this(new Gl<A>(matrix));
		//if(det.i)
	}
	/**
	 * Constructs a matrix with determinant
	 * one - this consructor throws an
	 * <code>IllegalArgumentException</code>
	 * @param another some automorphism
	 * @throws IllegalArgumentException if either no one-deterimant
	 */
	public Sl(Gl<A> another) throws IllegalArgumentException {
		super(another);
		A rDet = det.getValue(), prod = rDet.multiply(rDet);
		if(!rDet.equals(prod)) throw new IllegalArgumentException("\nDeterminant not one");
	}
	/**
	 * Constructs a copy of <tt>another</tt>
	 * special linear element
	 * @param another some other element
	 */
	public Sl(Sl<A> another) {super(another);}
	/**
	 * Returns the product of this and <tt>another</tt>
	 * <p><b>Note</b>, that this method is not to be
	 * mistaken for {@link Gl#multiply(Gl)}, which will
	 * always work for element in either <code>Gl</code> or
	 * <code>Sl</code>
	 * @param another some other element
	 * @return
	 */
	public Sl<A> multiply(Sl<A> another){return new Sl<A> (super.multiply(another));}
	/**
	 * Overrides super class's method
	 */
	public Sl<A> inverse(){return new Sl<A>(super.inverse());}
	/**
	 * Returns the homomorphism mapping the general linear
	 * group {@link Gl} elements to elements of this class, setting
	 * the current argument to <tt>someMatrix</tt>
	 * @param someMatrix some general linear element
	 * @return the homomorphism
	 */
	public static final <A extends UnitaryCommRing<A>> Gl2SlHomo<A> getHomomorphism (Gl<A> someMatrix){
		Gl2SlHomo<A> homo = new Gl2SlHomo<A>();
		homo.f(someMatrix);
		return homo;
	}
	
	
	public static void main(String[] args){
		NNegInt ONE = NNegInt.ONE, TWO = NNegInt.TWO, ZERO = NNegInt.ZERO;
		NNegInt[][] ar = new NNegInt[][]{
				{null,null,null,null},
				{ ONE,null,null,null},
				{null, ONE,null,null},
				{null,null, ONE,null},
				{null,null,null, ONE}
		};
		
		GrIndex[] indices = new GrIndex[ar.length];
		for (int i = 0; i < ar.length; i++) indices[i] = new GrIndex(ar[i]);
		AbstractRMatrix<UnitaryCommPoly<ThreeCycle>> id = new AbstractRMatrix<UnitaryCommPoly<ThreeCycle>> (TWO);
		AbstractRMatrix<UnitaryCommPoly<ThreeCycle>> s0= new AbstractRMatrix<UnitaryCommPoly<ThreeCycle>>(TWO), t0 = new AbstractRMatrix<UnitaryCommPoly<ThreeCycle>>(TWO);
		AbstractRMatrix<UnitaryCommPoly<ThreeCycle>> s1= new AbstractRMatrix<UnitaryCommPoly<ThreeCycle>>(TWO), t1 = new AbstractRMatrix<UnitaryCommPoly<ThreeCycle>>(TWO);
		UnitaryCommPoly<ThreeCycle> one = new UnitaryCommPoly<ThreeCycle> (indices[0],ThreeCycle.ONE);
		id.setEntry( ONE,  ONE, one);
		id.setEntry(ZERO, ZERO, one);
		s0.setEntry(ONE, ZERO, new UnitaryCommPoly<ThreeCycle>(indices[1],ThreeCycle.ONE));
		s0.setEntry( ONE,  ONE, one);
		s0.setEntry(ZERO, ZERO, one);
		t0.setEntry(ZERO,ONE,new UnitaryCommPoly<ThreeCycle>(indices[2],ThreeCycle.ONE));
		t0.setEntry( ONE,  ONE, one);
		t0.setEntry(ZERO, ZERO, one);
		System.out.println(s0.toString());
		Sl<UnitaryCommPoly<ThreeCycle>> prod1 = new Sl<UnitaryCommPoly<ThreeCycle>>(s0.multiply(t0));
		System.out.println(String.format("%1$s *\n%2$s =\n%3$s", s0, t0, prod1.toString()));
		s1.setEntry(ONE, ZERO, new UnitaryCommPoly<ThreeCycle>(indices[3],ThreeCycle.ONE));
		s1.setEntry( ONE,  ONE, one);
		s1.setEntry(ZERO, ZERO, one);
		t1.setEntry(ZERO,ONE,new UnitaryCommPoly<ThreeCycle>(indices[4],ThreeCycle.ONE));
		t1.setEntry( ONE,  ONE, one);
		t1.setEntry(ZERO, ZERO, one);
		Sl<UnitaryCommPoly<ThreeCycle>> prod2 = new Sl<UnitaryCommPoly<ThreeCycle>>(s1.multiply(t1));
		System.out.println(String.format("%1$s *\n%2$s =\n%3$s", s1, t1, prod2.toString()));
		System.out.println(String.format("%1$s *\n%2$s =\n%3$s", prod1, prod2, prod1.multiply(prod2).toString()));
		AbstractRMatrix<UnitaryCommPoly<ThreeCycle>> prMat = prod1.getAutoMorphism(), trafo = new AbstractRMatrix<UnitaryCommPoly<ThreeCycle>>(TWO);
		AbstractFiniteModule<UnitaryCommPoly<ThreeCycle>> v1 = new AbstractFiniteModule<UnitaryCommPoly<ThreeCycle>>(), v2 = null;
		v1.setEntry(ONE, one);
		prMat.f(v1);
		v2 = prMat.getValue();
		trafo.setEntries(v1,ZERO,false);
		trafo.setEntries(v2, ONE, false);
		System.out.println(trafo);
	}
	
}
