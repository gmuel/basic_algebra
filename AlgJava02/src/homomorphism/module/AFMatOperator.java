package homomorphism.module;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.TreeMap;

import module.GenericVSpace;

import field.AbstractField;
import field.Rational;
import group.NNegInt;
/**
 * The square matrix operator class - runs inversion
 * @author adin
 *
 * @param <F> sub-type of type {@link AbstractField}  
 */
public class AFMatOperator<F extends AbstractField<F>> {
	/*------------------------fields------------------------*/
	/**the matrix field: a square matrix with entries of
	 * type &ltF&gt*/
	private final AbstractFMatrix<F> matrix;
	/**the inverse of the matrix, computed only if {@link AFMatOperator#matrix} is
	 * regular*/
	private AbstractFMatrix<F>    inv;
	private HashSet<GenericVSpace<F>>      ker;
	
	/**
	 * Constructs a matrix operator with <tt>matrix</tt>
	 * to operate on
	 * @param matrix the matrix
	 */
	public AFMatOperator(AbstractFMatrix<F> matrix) {
		this.matrix = new AbstractFMatrix<F>(matrix);
		ker = new HashSet<GenericVSpace<F>>();
		ker.add(new GenericVSpace<F>());
	}
	/**
	 * Computes the inverse matrix if and only if {@link AbstractFMatrix#det()}
	 * returns a non-zero element (the matrix is then called regular)
	 * @throws IllegalArgumentException matrix has zero determinant (singular matrix)
	 */
	public void computeInverse () throws IllegalArgumentException {
		if(matrix.det().isZero()) throw new IllegalArgumentException (
				"\nSingular matrix - no inverse defined");
		if(matrix.isDiagonal()) {
			inv = new AbstractFMatrix<F> (matrix.getDim());
			for (Entry<NNegInt,F> entry:matrix){
				inv.setEntry(entry.getKey(), entry.getValue().inverse());
			}
			return;
		}
		inverse();
		//return inv;
	}
	/**
	 * Returns the inverse matrix, if this matrix operator
	 * was initialized with a regular matrix
	 * @return the inverse
	 * @throws IllegalArgumentException singular matrix
	 */
	public AbstractFMatrix<F> getInverse() throws IllegalArgumentException {
		if(inv==null) computeInverse();
		return inv;
	}
	/*------------------------privates------------------------*/
	/**
	 * Returns a matrix <p><tt>sum_i e_(i,i) + coeff * e_(row,col)</tt>,</p> a
	 * matrix, that apply by left-multiplication adds the row at
	 * <tt>row</tt> to row <tt>col</tt>
	 * @param row the first row index
	 * @param col the second row index
	 * @param coeff the scalar
	 * @return a row transforming matrix
	 */
	private AbstractFMatrix<F> getRowAddMat (NNegInt row, NNegInt col, F coeff){
		AbstractFMatrix<F> add = AbstractFMatrix.identity(matrix.one, matrix.getDim());
		add.setEntry(row,col,coeff);
		return add;
	}
	/**
	 * Returns a matrix version of {@link AFMatOperator#matrix}
	 * that via row permutation has all non-zero entries on the diagonal
	 * @return a permuted matrix
	 */
	private AbstractFMatrix<F> getDiagonizable (){
		
		NNegInt dim = matrix.getDim();//, dim1 = dim.decrement();
		//the dimension
		
		inv = AbstractFMatrix.identity(matrix.one, dim);
		//the inverse field initialized
		
		AbstractFMatrix<F> loc = new AbstractFMatrix<F> (matrix);
		//constructing a local copy of the original matrix
		
		NNegInt col = NNegInt.ZERO, first = null;
		//the column index plus two additional indices
		
		while (col.compareTo(dim)<0){
			//iterating over all column vectors
			
			if(loc.getValue(col, col)!=null) {
				//does the matrix have a non-zero element
				//as diagonal entry?
				
				col = col.increment();
				//increment column index
				
				continue;
				//and skip
				
			}
			NNegInt row = NNegInt.ZERO;
			//otherwise, start to check for
			//row index such that an exchange with
			//another row yields a non-zero entry
			//in the new matrix
			
			while(row.compareTo(dim)<0){
				//iterating over all row vectors of the
				//local matrix
				
				if(loc.getValue(row, col)!=null){
					//does the current row vector have a non-zero
					//entry at index col?
					
					first = row;
					break;
					
					/*if(first==null) {
						//otherwise, use the first index if
						//not initialized
						
						first = row;
						//assign the current row index
						
						rowSet.add(row);
						//from now on, this index will be excluded
						
						break;
					}
					else{
						//if the first index is already initialized
						//use this one..
						
						second = row;
						//as second index..
						
						rowSet.add(row);
						//and add to the exclusion set
						
						break;
						//done, no more iteration needed
						
					}*/
					
				}
				row = row.increment();
			}
			if(first!=null){
				AbstractFMatrix<F> perm = permutationMat(first,col);
				//construct a permutation matrix equivalent to the
				//transposition (first,second)

				first = null;
				//set the first index to null

				loc = perm.multiply(loc);
				//apply permutation matrix to the local 

				inv = perm.multiply(inv);
				//and the inverse
			}
			
			col = col.increment();
			//increment column index
			
		}
		
		return loc;
	}
	/**
	 * Auxiliary method: runs the actual inversion algorithm
	 */
	private void inverse(){
		AbstractFMatrix<F> locPerm = getDiagonizable();
		//getting a permuted matrix version of this.matrix such that
		//all diagonal entries are non-zero
		
		if(locPerm.isDiagonal()) {
			//test whether this is a diagonal matrix
			
			NNegInt index = NNegInt.ZERO, dim = matrix.getDim();
			AbstractFMatrix<F> scl = new AbstractFMatrix<F> (dim);
			while(index.compareTo(dim)<0) {
				F coeff = locPerm.getValue(index, index);
				if(coeff!=null&&!coeff.equals(matrix.one))
					scl.setEntry(index, index, coeff);
				else scl.setEntry(index, index, matrix.one);
				index = index.increment();
			}
			inv = scl.multiply(inv);
			return;
		}
		
		NNegInt col = NNegInt.ZERO, dim = matrix.getDim();
		//some useful numbers
		
		while (col.compareTo(dim)<0){
			//iterating over all column vectors of the permuted matrix
			
			F coeff = locPerm.getValue(col, col);
			//getting the diagonal entry a_col,col
			
			if(!coeff.equals(matrix.one)){
				//only if entry is different from one
				//multiply scalar to the current row at col
				//to normalize
				
				AbstractFMatrix<F> scl = rowMultiply(col,coeff.inverse());
				//the row multiplier matrix sum_i!=col e_ii + coeff^-1 * e_col,col
				
				locPerm = scl.multiply(locPerm);
				//new permuted matrix 
				
				inv     = scl.multiply(inv);
				//apply the same row multiplier to the inverse
			
			}
			NNegInt row = NNegInt.ZERO;
			//row index
			
			while (row.compareTo(dim)<0){
				//running through all row vectors of the permuted matrix
				
				if(row.equals(col)){row = row.increment();continue;}
				//if we reached a diagonal entry skip
				
				F entry = locPerm.getValue(row,col);
				//getting the matrix entry a_row,col
				
				if(entry!=null){
					//some non-zero entry
					
					AbstractFMatrix<F> add = getRowAddMat(row,col,
							entry.addInverse());
					//construct a row addition matrix:
					//adding a scalar multiple of row row, to row col
					
					locPerm = add.multiply(locPerm);
					//apply row addition to permuted matrix
					
					inv     = add.multiply(inv);
					//as well as to inverse
					
				}
				row = row.increment();
				//increment row index
			}
			col = col.increment();
			//increment column index
			
		}
		
	}
	void kernel (){
		AbstractFMatrix<F> locPerm = getDiagonizable();
		if(locPerm.isDiagonal()){
			NNegInt index = NNegInt.ZERO, dim = matrix.getDim();
			while (index.compareTo(dim)<0){
				if(locPerm.getValue(index,index)==null) {
					GenericVSpace<F> kerVector = new GenericVSpace<F>();
					kerVector.setEntry(index, matrix.one);
					ker.add(kerVector);
				}
			}
			return;
		}
		NNegInt col = NNegInt.ZERO, dim = matrix.getDim();
		while (col.compareTo(dim)<0){
			F diagEnt = locPerm.getValue(col, col);
			if(diagEnt==null){
				col = col.increment();
				continue;
			}
			AbstractFMatrix<F> rowMul = rowMultiply(col, diagEnt.inverse());
			locPerm = rowMul.multiply(locPerm);
			inv     = rowMul.multiply(inv);
			NNegInt row = NNegInt.ZERO;
			while(row.compareTo(dim)<0){
				if(row.equals(col)){row = row.increment();continue;}
				F entry = locPerm.getValue(row,col);
				if(entry!=null){
					AbstractFMatrix<F> rowAdd = this.getRowAddMat(row, col, entry.addInverse());
					locPerm = rowAdd.multiply(locPerm);
					inv     = rowAdd.multiply(inv);
				}
				row = row.increment();
			}
			col = col.increment();
		}
		TreeMap<NNegInt,Integer> rowDeg = locPerm.getDegMap(true);
		HashSet<NNegInt> excluder = new HashSet<NNegInt> ();
		for (Entry<NNegInt,Integer> entry:rowDeg.entrySet()){
			NNegInt row = entry.getKey(), first = null;
			Integer num = entry.getValue();
			GenericVSpace<F> v = new GenericVSpace<F>();
			if(num.equals(1));
			col = NNegInt.ZERO;
			while (col.compareTo(dim)<0){
				F coeff = locPerm.getValue(row, col);
				if(coeff==null) {col = col.increment();continue;}
				if(first==null){
					first = col;
					v.setEntry(col, coeff);
				} else v.setEntry(col, coeff.addInverse());
				excluder.add(col);
				col = col.increment();
			}
			first = null;
			ker.add(v);
		}
		col = NNegInt.ZERO;
		while (col.compareTo(dim)<0){
			if(excluder.contains(col)){col = col.increment();continue;}
			GenericVSpace<F> v = new GenericVSpace<F> ();
			v.setEntry(col, matrix.one);
			ker.add(v);
			col = col.increment();
		}
	}
	/**
	 * Returns a permutation matrix equivalent to the
	 * transposition <tt>(i,j)</tt>
	 * @param i the first index
	 * @param j the second index
	 * @return the permutation matrix
	 */
	private AbstractFMatrix<F> permutationMat(NNegInt i, NNegInt j){
		AbstractFMatrix<F> perm = AbstractFMatrix.identity(matrix.one, matrix.getDim());
		perm.remove(i, i);
		perm.remove(j,j);
		perm.setEntry(i, j, matrix.one);
		perm.setEntry(j,  i, matrix.one);
		return perm;
	}
	/**
	 * Returns a row multiply matrix: applied
	 * by left-multiplication yields the same matrix
	 * except the scalar multiple of the row at <tt>row</tt>
	 * @param row row index
	 * @param scalar the scalar
	 * @return the row multiplier
	 */
	private AbstractFMatrix<F> rowMultiply (NNegInt row, F scalar){
		AbstractFMatrix<F> scl = AbstractFMatrix.identity(matrix.one, matrix.getDim());
		scl.setEntry(row, row, scalar);
		return scl;
	}
	public static void main(String[] args){
		AbstractFMatrix<Rational> mat = new AbstractFMatrix<Rational>(
				new Rational[][]{
						{Rational.ONE  ,Rational.M_ONE,null          ,Rational.ONE},
						{null          ,Rational.ONE  ,Rational.M_ONE,Rational.ONE},
						{null          ,null          ,Rational.ONE  ,Rational.ONE},
						{Rational.M_ONE,null          ,null          ,Rational.ONE}
				}
				);
		AbstractFMatrix<Rational> cycle = new AbstractFMatrix<Rational> (
				new Rational[][]{
						{null          ,null          ,null          ,Rational.ONE},
						{Rational.ONE  ,null          ,null          ,null        },
						{null          ,Rational.ONE  ,null          ,null        },
						{null          ,null          ,Rational.ONE  ,null        },
						
				}
				);
		AFMatOperator<Rational> op = new AFMatOperator<Rational>(mat);
		op.computeInverse();
		String out = "%1$s * \n%2$s = \n\n%3$s";
		System.out.println(
				String.format(out,
						mat.toString(),
						op.inv.toString(),
						mat.multiply(op.inv).toString()
						));
		System.out.println(
				String.format(out,
						op.inv.toString(),
						String.format("%1$s * \n%2$s",
								cycle.toString(),
								mat.toString()),
						op.inv.multiply(cycle.multiply(mat)).toString()));
		AbstractFMatrix<Rational> zeroDiv = cycle.multiply(cycle).add(AbstractFMatrix.identity(cycle.one, cycle.getDim()));
		AFMatOperator<Rational> op1= new AFMatOperator<Rational> (zeroDiv);
		op1.kernel();
		System.out.println(String.format("kernel of:\n%1$s = \n%2$s",zeroDiv,op1.ker));
		//op1.computeInverse();
	}
}
