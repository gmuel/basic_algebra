package group.representation;

import ring.UnitaryCommRing;
import group.AbstractNAG;
import group.NNegInt;
import group.UnitGroup;
import homomorphism.module.AbstractRMatrix;
/**
 * The general linear group class - all square matrices
 * with determinant a unit over some ring of sub-type
 * {@link UnitaryCommRing}
 * @author adin
 *
 * @param <A> the type of the ring
 */
public class Gl<U extends UnitaryCommRing<U>> extends AbstractNAG<Gl<U>> {
	/**the determinant*/
	UnitGroup<U> det;
	/**a copy of the matrix*/
	private AbstractRMatrix<U> autoMorph;
	/**
	 * Default constructor:- only intended
	 * for sub-class usage
	 */
	protected Gl() {super();}
	protected Gl(NNegInt rank){
		this();
		autoMorph = new AbstractRMatrix<U> (rank);
	}
	/**
	 * Constructs an automorphism
	 * object if and only if
	 * the determinant of the argument
	 * <tt>matrix</tt> is a unit
	 * @param matrix some squre matrix
	 * @throws IllegalArgumentException if determinant no unit
	 */
	public Gl(U[][] matrix) throws IllegalArgumentException {
		this(new AbstractRMatrix<U>(matrix));
	}
	/**
	 * Constructs an automorphism
	 * object if and only if
	 * the determinant of the argument
	 * <tt>matrix</tt> is a unit
	 * @param matrix some squre matrix
	 * @throws IllegalArgumentException if determinant no unit
	 */
	public Gl(AbstractRMatrix<U> matrix) throws IllegalArgumentException {
		this();
		det = matrix.det().getUnit();
		autoMorph = new AbstractRMatrix<U> (matrix);
	}
	/**
	 * Constructs a copy of the original
	 * automorphism <tt>another</tt>
	 * @param another the original
	 * @throws IllegalArgumentException should never happen
	 */
	public Gl(Gl<U> another) throws IllegalArgumentException {this(another.autoMorph);}
	/**
	 * Returns the determinant
	 * @return the determinant
	 */
	public U det(){return det.getValue();}
	
	public boolean equals(Gl<U> another) {return autoMorph.equals(another.autoMorph);}
	/**
	 * Returns a deep copy of this
	 * automorphism, changes to the
	 * returned object will not be
	 * reflected in this object
	 * @return the automorphism as a square matrix
	 */
	public AbstractRMatrix <U> getAutoMorphism(){return new AbstractRMatrix<U>(autoMorph);}
	
	public Gl<U> inverse() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean isDiscrete() {return autoMorph.isDiscrete();}
	
	public boolean isNeutral() {
		// TODO Auto-generated method stub
		return false;
	}
	public Gl<U> multiply(U scalar){return new Gl<U> (autoMorph.ringAct(scalar));}
	public Gl<U> multiply(Gl<U> another) {return new Gl<U>(autoMorph.multiply(another.autoMorph));}
	public String toString (){return autoMorph.toString();}
}
