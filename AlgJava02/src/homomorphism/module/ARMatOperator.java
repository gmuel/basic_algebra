package homomorphism.module;

import java.util.HashSet;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.TreeSet;

import module.AbstractFiniteModule;
import group.NNegInt;
import group.SymmetricGroup;
import ring.Ring.DisplayResults;
import ring.UnitaryCommRing;
import ring.integer.IntRing;
import ring.poly.MonoPoly;
/**
 * The abstract square matrix operator class over some arbitrary
 * ring of sub-type {@link UnitaryCommRing}. Provides methods
 * to compute the (pseudo-left) inverse matrix or its kernel
 * for singular matrices
 * <p>The pseudo-left inverse matrix <tt>A'</tt> for a given matrix
 * <tt>A</tt> is defined
 * <p><tt>D := A' A</tt> for some diagonal matrix <tt>D = (d_1,...d_n)</tt>
 * such that <tt>D.det().isUnit()</tt> returns false
 * @author adin
 *
 * @param <U> the type of a sub-class of <code>UnitaryCommRing</tt>
 */
public class ARMatOperator<U extends UnitaryCommRing<U>> {
	/**the matrix object*/
	private final AbstractRMatrix<U> matrix;
	/**the (pseudo-left) inverse matrix*/
	private AbstractRMatrix<U>    inv;
	/**the kernel vector set*/
	private HashSet<AbstractFiniteModule<U>> ker;
	/**auxiliary object - representing the one element
	 * of the underlying ring*/
	private U one;
	/**the rank of the underlying free module*/
	private NNegInt rank;
	/**a permutation  representing the permutation
	 * matrix to convert {@link ARMatOperator#matrix}
	 * to matrix with possibly all non-null entries
	 * on the diagonal*/
	private SymmetricGroup<NNegInt> permMut;
	/**
	 * Constructs an matrix operator object
	 * with the matrix to examine set to <tt>matrix</tt> 
	 * @param matrix the matrix to examine
	 */
	public ARMatOperator(AbstractRMatrix<U> matrix) {
		boolean isZero = matrix==null||matrix.isZero();
		this.matrix = isZero?new AbstractRMatrix<U> ():
			new AbstractRMatrix<U> (matrix);
		if(isZero) rank = NNegInt.ZERO;
		else {
			rank = new NNegInt(matrix.rank());
			setOne();
		}
		
	}
	/**
	 * Computes:
	 * <ol><li>the inverse matrix if <code>matrix.det().isUnit()</code> returns true</li>
	 * <li>the left pseudo inverse, if <code>!matrix.det().isUnit()</code> returns true</li></ol>
	 * 
	 */
	public void computeInverse () throws IllegalArgumentException {
		/*if(matrix.det().isZero()) throw new IllegalArgumentException (
				"\nSingular matrix - no inverse defined");*/
		if(matrix.isDiagonal()) {
			inv = new AbstractRMatrix<U> (rank);
			for (Entry<NNegInt,U> entry:matrix){
				inv.setEntry(entry.getKey(), entry.getValue().inverse());
			}
			return;
		}
		if(!matrix.det().isUnit())
			System.err.println(String.format(
					"Matrix A =\n%1$s\nhas non-unit determinant det(A) = %2$s\ncomputing left inverse...", matrix,matrix.det()));
		inverse();
		//return inv;
	}
	/**
	 * Computes the kernel elements
	 */
	public void computeKernel (){kernel();}
	/**
	 * Returns the (pseudo-left) inverse matrix, if this matrix operator
	 * was initialized with a (pseudo-) regular matrix (pseudo-regular if
	 * the determinant is not a unit, possibly zero)
	 * @return the (pseudo-left) inverse
	 */
	public AbstractRMatrix<U> getInverse(){
		if(inv==null) computeInverse();
		return new AbstractRMatrix<U>(inv);
	}
	/**
	 * Returns a set of kernel elements - 
	 * <p><b>Note</b> each module element in
	 * the returned set is a deep copy of the
	 * original elements, changes to those
	 * will not be reflect in the original set
	 * @return the kernel set
	 */
	public HashSet<AbstractFiniteModule<U>> getKernel (){
		HashSet<AbstractFiniteModule<U>> cp = new HashSet<AbstractFiniteModule<U>>();
		for (AbstractFiniteModule<U> kernelElements:ker) cp.add(kernelElements);
		return cp;
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
	private AbstractRMatrix<U> getRowAddMat (NNegInt row, NNegInt col, U coeff){
		AbstractRMatrix<U> add = getIdentity(one, rank);
		add.setEntry(row,col,coeff);
		return add;
	}
	/**
	 * Returns a matrix version of {@link ARMatOperator#matrix}
	 * that via row permutation has all non-zero entries on the diagonal
	 * @return a permuted matrix
	 */
	private AbstractRMatrix<U> getDiagonizable (){
		
		NNegInt dim = rank;//, dim1 = dim.decrement();
		//the dimension
		
		inv = getIdentity(one, dim);
		//the inverse field initialized
		
		AbstractRMatrix<U> loc = new AbstractRMatrix<U> (matrix);
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
			NNegInt row = col.increment();
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
				}
				row = row.increment();
			}
			if(first!=null){
				AbstractRMatrix<U> perm = transpositionMat(first,col);
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
		AbstractRMatrix<U> locPerm = getDiagonizable();
		//getting a permuted matrix version of this.matrix such that
		//all diagonal entries are non-zero
		
		if(locPerm.isDiagonal()) {
			//test whether this is a diagonal matrix
			
			NNegInt index = NNegInt.ZERO, dim = rank;
			AbstractRMatrix<U> scl = new AbstractRMatrix<U> (dim);
			while(index.compareTo(dim)<0) {
				U coeff = locPerm.getValue(index, index);
				if(coeff!=null&&!coeff.equals(one))
					scl.setEntry(index, index, coeff);
				else scl.setEntry(index, index, one);
				index = index.increment();
			}
			inv = scl.multiply(inv);
			return;
		}
		
		NNegInt col = NNegInt.ZERO, dim = rank;
		//some useful numbers
		
		while (col.compareTo(dim)<0){
			//iterating over all column vectors of the permuted matrix
			
			U coeff = locPerm.getValue(col, col);
			//getting the diagonal entry a_col,col
			
			if(coeff==null) {
				locPerm = rediagonalize(locPerm);
				if((coeff= locPerm.getValue(col,col))==null){
					col = col.increment();
					continue;
				}
			}
			
			boolean coeffEquals1 =coeff.equals(one), coeffIsUnit = coeff.isUnit(); 
			if(!coeffEquals1&&coeffIsUnit){
				//only if entry is different from one
				//multiply scalar to the current row at col
				//to normalize
				
				AbstractRMatrix<U> scl = rowMultiply(col,coeff.inverse());
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
				
				U entry = locPerm.getValue(row,col);
				//getting the matrix entry a_row,col
				
				if(entry!=null){
					//some non-zero entry
					
					U rowEntry = locPerm.getValue(col, col);
					//get the diagonal entry
					
					if(rowEntry==null) {row = row.increment();continue;}
					//if null continue
					
					AbstractRMatrix<U> add = getRowAddMat(row,col,
							entry.addInverse());
					//construct a row addition matrix:
					//adding a scalar multiple of row row, to row col					
					
					if(!rowEntry.equals(one)){
						//if the diagonal entry didnt get normalized:
						
						//AbstractRMatrix<U> scl = rowMultiply(col,entry);
						//first, compute a row multiplier multiplying
						//the diagonal entry to the current row <tt>col</tt> 
						
						//add = add.multiply(scl);
						//locPerm = scl.multiply(locPerm);
						//inv     = scl.multiply(inv);
						
						add.setEntry(row, row, rowEntry);
						
					}
					
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
	/**
	 * Auxiliary method: computes the kernel elements
	 */
	private void kernel (){
		
		//compute the pseudo-left inverse
		inverse();
		
		//and the product inverse*matrix
		AbstractRMatrix<U> diagonal = inv.multiply(matrix);
		
		//if all non-null entries are diagonal
		//entries :
		if(diagonal.isDiagonal()){
			
			//index and rank
			NNegInt index = NNegInt.ZERO, dim = rank;
			
			//if the diagonal is already a regular
			//matrix nothing to do but adding the
			//zero element and return
			if(diagonal.isUnit()) {ker.add(new AbstractFiniteModule<U>());return;}
			
			//otherwise: iterate over all diagonal
			//element
			while (index.compareTo(dim)<0){
				
				//if the diagonal entry is null
				if(diagonal.getValue(index,index)==null) {
					
					//construct the canonical basis vector e_index
					AbstractFiniteModule<U> v = new AbstractFiniteModule<U>();
					
					//setting entry for e_index
					v.setEntry(index, one);
					
					//and add it to the set
					ker.add(v);
					
				}
			}
			//done, nothing more to do
			return;
		}
		
		//get the degree map sorted by the matrix's row indices -
		//row index as key and number of non-null/zero entries as value
		TreeMap<NNegInt,Integer> diagMap = diagonal.getDegMap(true);
		
		//the set of all row indices with degree = 1
		TreeSet<NNegInt> degOne = getDegOneSet(diagMap);
		
		//a reversed map: maps the degree to a set of row indices - 
		//the degree as key and the sets of row indices as values
		//lowest key is greater than or equal to 2
		TreeMap<Integer,TreeSet<NNegInt>> deg2Index = getSameDegMapGreater2(diagMap,degOne);

		
		//initialize the kernel vector set
		if(ker==null) ker = new HashSet<AbstractFiniteModule<U>> ();
		
		//iterate over the reversed map:
		for (Entry<Integer,TreeSet<NNegInt>> degEntry:deg2Index.entrySet())
		
		//BEGIN: loop1 (reversed map iteration)
		{
			
			//get the key (degree) for the current
			//map entry
			Integer degree = degEntry.getKey();
			
			//and its value (set of row indices)
			TreeSet<NNegInt> degSet = degEntry.getValue();
			
			//the the degree wrapped as NNegInt
			NNegInt deg = new NNegInt(degree);//, degM = deg.decrement();
			
			//iterate over the row index set
			for (NNegInt index:degSet) setKernelElements(diagonal,degOne,index,deg);
			
			//BEGIN loop2 (row index iteration)
			{
			
				/*
				//starting at zero with the column index and
				//with one for the counter (counts the number of
				//coefficients added)
				NNegInt colIndex = NNegInt.ZERO, counter = NNegInt.ONE;
				
				//and a new module element
				AbstractFiniteModule<U> kerVec = new AbstractFiniteModule<U> ();
				
				//the last coefficient to insert
				//into the module element
				U lastCoeff = null;
				
				//iterating over the column entries of
				//the current row
				while (colIndex.compareTo(rank)<0)
				
				//BEGIN loop3 (column index iteration)
				{
					

					//get the coefficient
					U coeff = diagonal.getValue(index,colIndex);

					//only if the coefficient is
					//not null
					if(coeff!=null)
					
					//					
					{
						
						
						//some boolean flag to test
						//whether the coeff and some
						//degree one entry share their
						//column index (an exclusion entry)
						boolean inSameCol = false;

						//iterate over the row indices
						//with degree one
						for (NNegInt rowTest:degOne)
						
						//BEGIN loop 4 (row exclusion test)
						{

							//does the current column index have a
							//non-null counterpart in the exclusion row?
							if(diagonal.getValue(rowTest, colIndex)!=null){

								//if so, set the flag and
								inSameCol = true;

								//break the iteration
								break;
							}

						//END loop 4 (row exclusion test)
						}

						//if the coefficient does not share a
						//column with an exclusion entry, start:
						if(!inSameCol) {

							//as long as counter is strictly less
							//the the degree:
							if(!counter.equals(deg)) {
								
								//add a one entry to the current column index
								kerVec.setEntry(colIndex, one);
								
								
								
								//if last coefficient is null
								//set it to -coeff
								if(lastCoeff==null) lastCoeff = coeff.addInverse();
								
								//otherwise compute the difference lastCoeff-coeff
								else lastCoeff = lastCoeff.add(coeff.addInverse());
								
								//increment counter
								counter = counter.increment();
								
								//otherwise, coeff is the last non-null
								//entry in the current row (index)
							} else {
								
								//check whether coefficient is a unit
								if(coeff.isUnit()) 
									
									//if so, two cases:
									//1. coeff==one, nothing to do
									//2. multiply with inverse of coeff
									kerVec.setEntry(colIndex, coeff.equals(one)?lastCoeff:lastCoeff.multiply(coeff.inverse()));
								
								//otherwise:
								else {
									
									//some decrement index
									NNegInt formerIndex = colIndex;
									
									//set the coefficient to one for
									//the current column
									kerVec.setEntry(formerIndex, one);
									
									//add -coeff to lastCoeff
									lastCoeff = lastCoeff.add(coeff.addInverse());
									
									//the previous coefficient added
									U former = null;
									
									boolean test = true;
									
									//iterate to the last unit 
									//coefficient:
									while ((former==null||!former.isUnit()||test)&&formerIndex.compareTo(NNegInt.ZERO)>0){
										
										//decrement
										formerIndex = formerIndex.decrement();
																				
										//get the entry
										former = diagonal.getValue(index,formerIndex);
										
										test = former!=null&&kerVec.getValue(formerIndex)==null;
									}
									
									if(former!=null){

										//add the previous coefficient to
										//last one and
										lastCoeff = lastCoeff.add(former);

										//add it to the kernel element
										kerVec.setEntry(formerIndex, former.equals(one)?lastCoeff:lastCoeff.multiply(former.inverse()));
									}
									else {
										formerIndex = NNegInt.ZERO;
										while (former==null&&formerIndex.compareTo(rank)<0) {
											former = diagonal.getValue(formerIndex, formerIndex);
											formerIndex = formerIndex.increment();
										}
										
									}
									
								}
								//from here on, no more columns to check
								break;
							}
							
						} else {
							if(counter.compareTo(degM)>=0){
								NNegInt former = colIndex.decrement();
								U lastInsert = null;
								while (lastInsert==null) {
									lastInsert = diagonal.getValue(index, former);
									former = former.decrement();
								}
								if(lastInsert.isUnit()) {
									lastCoeff = lastCoeff.add(lastInsert);
									kerVec.setEntry(former==null?NNegInt.ZERO:former.increment(),
											lastInsert.equals(one)?
													lastCoeff:
														lastCoeff.multiply(lastInsert.inverse()));
								} else {
									U formerUnit = null;
									while ((formerUnit==null||!formerUnit.isUnit())&&former!=null){
										formerUnit = diagonal.getValue(index, former);
										former = former.decrement();
									}
									if(formerUnit!=null) {
										lastCoeff = lastCoeff.add(formerUnit);
										kerVec.setEntry(former==null?NNegInt.ZERO:former.increment(),
												formerUnit.equals(one)?lastCoeff:lastCoeff.multiply(formerUnit.inverse()));
									}
									
								}
							}
						}
					}
					
					//increment the column index
					colIndex = colIndex.increment();
					
				//END loop 3 (column index iteration)
				}
				
				//all column indices in the row index
				//where used - no more usage for the next
				//rows to come 
				degOne.add(index);
				
				//add the kernel element
				ker.add(kerVec);*/
				
			//END loop 2 (row index iteration)
			}
			
		//END loop 1 (reversed map iteration)
		}
		
	}
	private TreeSet<NNegInt> getDegOneSet(
			TreeMap<NNegInt,Integer> degMap){
		TreeSet<NNegInt> oneSet = new TreeSet<NNegInt> ();
		for (Entry<NNegInt,Integer> degEntry:degMap.entrySet()){
			Integer degree = degEntry.getValue();
			if(degree==1) oneSet.add(degEntry.getKey());
		}
		return oneSet;
	}
	private void setKernelElements (AbstractRMatrix<U> pseudoDiagonal, TreeSet<NNegInt> degOne, NNegInt row, NNegInt degree){
		
		//constructing some module element for ther kernel set
		AbstractFiniteModule<U> element = new AbstractFiniteModule<U> ();

		//some indices: column index, index of a unit in a given row and
		//the first index 
		NNegInt col = NNegInt.ZERO, unitIndex = null, first = null;
		U coeff = null, rowUnit = null, firstIn = null;
		while (col.compareTo(rank)<0) {
			coeff = pseudoDiagonal.getValue(row, col);
			if(coeff!=null) {
				if(coeff.isUnit()&&!sharesColumnEntry(pseudoDiagonal,degOne,col))  {
					rowUnit = coeff;
					unitIndex = col;
					break;
				}
			}
			col = col.increment();
		}
		col = NNegInt.ZERO;
		if(rowUnit!=null) {
			while (col.compareTo(rank)<0){
				boolean sharedColumn = sharesColumnEntry(pseudoDiagonal,degOne,col);
				coeff = pseudoDiagonal.getValue(row, col);
				if(coeff!=null&&!col.equals(unitIndex)&&!sharedColumn) {
					element.setEntry(col, one);
					element.setEntry(unitIndex, rowUnit.equals(one)?
							coeff.addInverse():
								coeff.addInverse().multiply(rowUnit.inverse()));
					ker.add(element);
					element = new AbstractFiniteModule<U> ();
				} else if(coeff==null&&!col.equals(unitIndex)&&!sharedColumn){
					element.setEntry(col,one);
					ker.add(element);
					element = new AbstractFiniteModule<U> ();
				}
				col = col.increment();

			}
			//element.setEntry(unitIndex, one)
		} else {
			while (col.compareTo(rank)<0){
				coeff = pseudoDiagonal.getValue(row, col);
				boolean sharedColumn = sharesColumnEntry(pseudoDiagonal,degOne,col);
				if(coeff!=null&&!sharedColumn){// {
					if(firstIn==null){
						first   = col;
						firstIn = coeff;
					} else {
						element.setEntry(col, firstIn);
						element.setEntry(first, coeff.addInverse());
						ker.add(element);
						element = new AbstractFiniteModule<U> ();
					}

				} else if (coeff==null&&!sharedColumn) {
					element.setEntry(col, one);
					ker.add(element);
					element = new AbstractFiniteModule<U> ();
				}
				col = col.increment();
			}
		}
		degOne.add(row);
	}
	private TreeMap<Integer,TreeSet<NNegInt>> getSameDegMapGreater2 (TreeMap<NNegInt,Integer> degMap, TreeSet<NNegInt> oneSet){
		TreeMap<Integer,TreeSet<NNegInt>> sameDegMap = new TreeMap<Integer,TreeSet<NNegInt>> ();
		for (Entry<NNegInt,Integer> degEntry:degMap.entrySet()){
			Integer degree = degEntry.getValue();
			NNegInt index = degEntry.getKey();
			if(oneSet.contains(index)) continue;
			TreeSet<NNegInt> sameDegSet;
			
			if((sameDegSet = sameDegMap.get(degree))!=null) sameDegSet.add(index);
			else {
				sameDegSet = new TreeSet<NNegInt> ();
				sameDegSet.add(index);
				sameDegMap.put(degree, sameDegSet);
			}
		}
		
		return sameDegMap;
	}
	/**
	 * Returns a transposition matrix equivalent to the
	 * transposition <tt>(i,j)</tt>
	 * @param i the first index
	 * @param j the second index
	 * @return some transposition matrix
	 */
	private AbstractRMatrix<U> transpositionMat(NNegInt i, NNegInt j){
		SymmetricGroup<NNegInt> trans = new SymmetricGroup<NNegInt> (new int[]{(int)i.eval(),(int)j.eval()});
		if(this.permMut==null)
			permMut = trans;
		else permMut = trans.multiply(permMut);
		return trans.constructPermutationMat(one,rank);
	}
	private boolean sharesColumnEntry (AbstractRMatrix<U> pseudoDiag, TreeSet<NNegInt> degOne, NNegInt col){
		for (NNegInt row:degOne){
			if(pseudoDiag.getValue(row, col)!=null) return true;
		}
		return false;
	}
	private AbstractRMatrix<U> rediagonalize (AbstractRMatrix<U> former){
		ARMatOperator<U> inner = new ARMatOperator<U> (former);
		AbstractRMatrix<U> reDiag = inner.getDiagonizable();
		if(inner.permMut!=null) permMut = inner.permMut.multiply(permMut);
		if(inner.inv!=null) inv = inner.inv.multiply(inv);
		return reDiag;
	}
	/**
	 * Returns a row multiply matrix: applied
	 * by left-multiplication yields the same matrix
	 * except the scalar multiple of the row at <tt>row</tt>
	 * @param row row index
	 * @param scalar the scalar
	 * @return the row multiplier
	 */
	private AbstractRMatrix<U> rowMultiply (NNegInt row, U scalar){
		AbstractRMatrix<U> scl = getIdentity(one, rank);
		scl.setEntry(row, row, scalar);
		return scl;
	}
	private void setOne (){
		for (Entry<NNegInt,U> entry:matrix) {
			U one = entry.getValue().getOne();
			if(one!=null) {this.one = one;return;}
		}
	}
	public static final <U extends UnitaryCommRing<U>> AbstractRMatrix<U> getIdentity (U coeff, int rank){
		return getIdentity(coeff,new NNegInt(rank));
	}
	public static final <U extends UnitaryCommRing<U>> AbstractRMatrix<U> getIdentity (U coeff, NNegInt rank){
		AbstractRMatrix<U> id = new AbstractRMatrix<U> (rank);
		if(coeff==null) return id;
		NNegInt index = NNegInt.ZERO;
		U one = coeff.getOne();
		while (index.compareTo(rank)<0){
			id.setEntry(index, index, one);
			index = index.increment();
		}
		return id;
	}

	public static void main (String[] args){
		IntRing ONE = IntRing.ONE, TWO = IntRing.TWO, THREE = TWO.add(ONE), FOUR = THREE.add(ONE);
		IntRing MONE= IntRing.M_ONE;
		AbstractRMatrix<IntRing> mat = new AbstractRMatrix<IntRing>(new IntRing[][]{
				{null,TWO,TWO, FOUR,THREE.addInverse()},
				{ null,null,null,null,              null},
				{ null, null, null,null,null},
				{ null,null,null,null,null},
				{null,null,null,null,null}
		});
		ARMatOperator<IntRing> op = new ARMatOperator<IntRing> (mat);
		op.kernel();
		DisplayResults.displayProduct(op.matrix, op.inv);
		DisplayResults.displayProduct(op.inv, op.matrix);
		for (AbstractFiniteModule<IntRing> kerEl:op.ker){
			op.matrix.f(kerEl);
			System.out.println(String.format("%1$s\n%2$s = \n%3$s",op.matrix,kerEl,op.matrix.getValue()));
		}
		MonoPoly<NNegInt,IntRing> poly = new MonoPoly<NNegInt,IntRing>();
		
		poly.setCoefficient(NNegInt.ZERO, new IntRing(6));
		poly.setCoefficient( NNegInt.ONE, new IntRing(-5));
		poly.setCoefficient( NNegInt.TWO, IntRing.ONE);
	}
}
