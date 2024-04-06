package homomorphism.module;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;

import module.Generator;
import module.GenericVSpace;
import field.AbstractField;
import field.Rational;
import group.AbstractAbel;
import group.NNegInt;
import ring.Ring;
import ring.poly.AFPoly;
import ring.poly.MonoPoly;
import topo.AbstractFct;
import util.ComposedObject01;
/**
 * The abstract square matrices class over some field of type <tt>F</tt> and some
 * finite dimensional vector space
 * @author bzfmuell
 *
 * @param <F>
 */
public class AbstractFMatrix<F extends AbstractField<F>> extends
		//AbstractAbel<AbstractFMatrix<F>> implements Ring<AbstractFMatrix<F>>, VectorSHomo<AbstractFMatrix<F>, GenericVSpace<F>, GenericVSpace<F>, F, NNegInt> {
		AbstractRMatrix<F> {
	public static final NNegInt TWO = new NNegInt(2);
	/**the coefficient map: each matrix
	 * entry is placed in a linear sequence
	 * such that the matrix index <tt>(i,j)</tt>
	 * is mapped to the entry at <tt>i * dim + j</tt>
	 * for all <tt>i, j &lt dim</tt>, and <tt>dim</tt>
	 * the dimension of the underlying vector space*/
	private TreeMap<NNegInt,F> coeffMap;
	/**the dimension of the underlying vector space*/
	private NNegInt dim;
	/**the argument vector*/
	private GenericVSpace<F> arg;
	/**the image value of <tt>this * arg<</tt>*/
	private GenericVSpace<F> val;
	/**the canonical basis*/
	//private Generator<GenericVSpace<F>,F> generators;
	/**the one element of the underlying field*/
	F one;
	private NNegInt row;
	private NNegInt col;
	
	/**
	 * Constructs the zero matrix with <tt>dim</tt> rows and
	 * <tt>dim</tt> columns
	 * @param dim the dimension
	 */
	public AbstractFMatrix (int dim){this(new NNegInt(dim));}
	/**
	 * Constructs the zero matrix with <tt>dim</tt> rows and
	 * <tt>dim</tt> columns
	 * @param dim the dimension
	 */
	public AbstractFMatrix (NNegInt dim){super(dim);coeffMap = new TreeMap<NNegInt,F> (); this.dim = new NNegInt(dim);}
	
	/**
	 * Constructs a square matrix with entries specified
	 * by the argument array <tt>matrix</tt>
	 * @param matrix an array by array of type &ltF&gt
	 * @throws IllegalArgumentException if the inner length of the
	 * array does not match the outer length (<tt>matrix.length!=matrix[i].length</tt>
	 * returns true for some <tt>0 <= i <= matrix.length-1</tt>) 
	 */
	public AbstractFMatrix (F[][] matrix) throws IllegalArgumentException {
		super(matrix);
		dim = new NNegInt(rank());
	}
	public AbstractFMatrix (AbstractRMatrix<F> another){
		super(another);
		dim = new NNegInt(rank());
	}
	/**
	 * Returns the sum <tt>this + another</tt>
	 * @param another
	 * @return
	 */
	public AbstractFMatrix<F> add(AbstractFMatrix<F> another) {
		if(!dim.equals(another.dim)) throw new IllegalArgumentException ("\nDimension mismatch: dim_this = "+dim+"\tdim_another = "+another.dim);
		AbstractFMatrix<F> sum = new AbstractFMatrix<F>(dim);
		for (Entry<NNegInt,F> entry:this) sum.coeffMap.put(entry.getKey(), entry.getValue());
		for (Entry<NNegInt,F> entry:another) {
			NNegInt index = entry.getKey();
			F coeff = entry.getValue(), sEntry = null;
			if((sEntry = sum.coeffMap.get(index))!=null) {
				sEntry = sEntry.add(coeff);
				if(sEntry.isZero()) sum.coeffMap.remove(index);
				else sum.coeffMap.put(index,sEntry);
			} else sum.coeffMap.put(index, coeff);
		}
		if(one!=null) sum.one = one;
		else if(another.one!=null) sum.one = another.one;
		return sum;

	}
	public AbstractFMatrix<F> addInverse() {
		AbstractFMatrix<F> inv = new AbstractFMatrix<F> (dim);
		for (Entry<NNegInt,F> entry:this) inv.coeffMap.put(entry.getKey(),entry.getValue().addInverse());
		if(one!=null) inv.one = one;
		return inv;
	}
	/**
	 * Returns the characteristic polynomial of this
	 * matrix object, to specify: a the unique monic polynomial <tt>p</tt>
	 * over the same field  of degree <tt>dim</tt>, such that
	 * <tt>p(this)</tt> returns the 'near' zero matrix.
	 * <br />'Near' in that sense that for any field of characteristic zero,
	 * especially floating point approximations, the returned polynomial
	 * is the best approximation to the characteristic polynomial
	 * under the current implementation.  
	 * @return the characteristic polynomial
	 */
	public AFPoly<F> charPoly(){
		//first, construct a matrix over the module F[X], the
		//the ring of all polynomials with coefficients in F,
		//of the same rank		
		AbstractRMatrix<MonoPoly<NNegInt,F>> detMat = new AbstractRMatrix<MonoPoly<NNegInt,F>> (dim);
		
		//get this matrix's coefficients and insert them as
		//constant polynomials (polynomials of degree 0)
		for (Entry<NNegInt,F> entry:this){
			
			//each polynomial entry is the constant polynomial
			//entry.getValue().addInverse(), the additive inverse
			NNegInt col = entry.getKey().mod(dim), row = entry.getKey().div(dim);
			F coeff = entry.getValue();
			detMat.setEntry(row, col,new MonoPoly<NNegInt,F>(NNegInt.ZERO,coeff.addInverse()));
			
		}
		//the monomial X over F
		MonoPoly<NNegInt,F> monoX = new MonoPoly<NNegInt,F>(NNegInt.ONE,one);
		
		
		NNegInt index = NNegInt.ZERO;
		
		//now insert monomial X on the diagonal
		while (index.compareTo(dim)<0){
			
			//construct the diagonal index (index,index)
			MonoPoly<NNegInt,F> ent = detMat.getValue(index, index);
			
			//check if their is already a constant monomial, if so
			//X to it
			if(ent!=null) detMat.setEntry(index, index, monoX.add(ent));
			
			//otherwise, simply add X
			else detMat.setEntry(index.multiply(dim).operate(index), monoX);
			index = index.increment();
		}
		return new AFPoly<F> (detMat.det());
	}
	public void clear() {coeffMap.clear();}
	/**
	 * Returns the determinant of this matrix. <b>Note</b>
	 * the implemented algorithm is recursive (Laplacian method),
	 * so in some cases the method does <b>NOT RETURN</b>(!)
	 * <br />Additionally, the method may return null, in case the
	 * one element was not initialized
	 * @return the determinant
	 */
	public F det (){
		if(dim.equals(TWO)) {
			//simplest case a two-by-two matrix, then apply the
			//general determinant equation: a_11 a_22 - a_12 a_21
			
			F a = getValue(0,0), b = getValue(0,1), c = getValue(1,0), d = getValue(1,1);
			//getting all matrix entries
			
			if(a!=null&&b!=null&&c!=null&&d!=null) return a.multiply(d).add((b.multiply(c)).addInverse());
			//case: all entries are non zero: return the aforementioned value
			
			if(a!=null&&d!=null) return a.multiply(d);
			//a_11 and a_22 not zero, return a_11 a_22
			
			if(b!=null&&c!=null) return b.multiply(c).addInverse();
			//a_12 and a_21 not zero, return -a_12 a_21
			
			return one==null?null:one.add(one.addInverse());
			//non of the above cases applied, then return zero
		}
		TreeMap<NNegInt,Integer> rowDegMap = getDegMap(true);
		//a sorted map, where each individual matrix row index is mapped
		//onto the number of non-zero entries (e.g. the row degree)
		
		NNegInt lowRow = NNegInt.ZERO;
		//the row index with lowest degree
		
		Integer minRowDeg = Integer.MAX_VALUE;
		//some maximal value for testing minimality
		
		for (Entry<NNegInt,Integer> degEnt:rowDegMap.entrySet()) {
			//loop to find the row index of lowest degree
			
			NNegInt rowIndex = degEnt.getKey();
			//getting row index
			
			Integer rowDeg = degEnt.getValue();
			//getting the row degree
			
			if(rowDeg.compareTo(minRowDeg)<0) {minRowDeg = rowDeg; lowRow = rowIndex;}
			//only if the current row degree is strictly smaller than the
			//degree before, the index and degree are set
		}
		TreeMap<NNegInt,Integer> colDegMap = getDegMap(false);
		//repeating the above procedure only for the column degree
		
		NNegInt lowCol = NNegInt.ZERO;
		//column index of lowest degree
		Integer minColDeg = Integer.MAX_VALUE;
		//column degree
		for (Entry<NNegInt,Integer> degEnt:colDegMap.entrySet()) {
			//same loop as above to find the lowest column degree
			//(column with the least non-zero entries)
			
			NNegInt colIndex = degEnt.getKey();
			//current index
			Integer colDeg = degEnt.getValue();
			//associated degree
			
			if(colDeg.compareTo(minColDeg)<0) {minColDeg = colDeg; lowCol = colIndex;}
			//adjusting index and degree only if degree strictly smaller
			
		}
		if(minRowDeg.compareTo(minColDeg)<0) {
			//in case there is a row with strictly less non-zeros than in
			//the column case
			
			if(minRowDeg==0) return one==null?null:one.add(one.addInverse());
			//in case one row with no entries (except for zeros) return zero
			
			F det = null;
			//the determinant
			
			NNegInt ind = NNegInt.ZERO;
			//the column index, starting at zero
			
			while (ind.compareTo(dim)<0) {
				//increment column index until it equals the dimension
				
				F entry;
				if((entry = getValue(lowRow,ind))!=null) {
					//getting the matrix entry at row lowRow in position ind
					
					if(det==null) det = ((lowRow.operate(ind)).isEven()?entry:entry.addInverse()).multiply(getSubMatrix(lowRow,ind).det());
					//if the determinant is null, set to (-1)^(lowRow+ind)*a_(lowRow,ind)*det A_(lowRow,ind), where
					//det A_(lowRow,ind) is the determinant of the sub-matrix with row lowRow and
					//column ind omitted
					
					else det = (det.add((lowRow.operate(ind)).isEven()?entry:entry.addInverse())).multiply(getSubMatrix(lowRow,ind).det());
					//otherwise, form the same product and add it to the current det-value
					
				}
				ind = ind.increment();
				//increment the column index
				
			}
			return det;
			//return
		}
		if(minColDeg==0) return one==null?null:one.add(one.addInverse());
		//repeat the same in the column case: one column all zeros
		
		NNegInt ind = NNegInt.ZERO;
		//the row index
		
		F det = null;
		//the determinant value
		
		while (ind.compareTo(dim)<0) {
			//the row index gets incremented until it equals the dimension
			
			F entry;			
			if((entry = getValue(ind,lowCol))!=null) {
				//getting the matrix entry at column lowCol, in position ind
				
				if(det==null) det = ((lowCol.operate(ind)).isEven()?entry:entry.addInverse()).multiply(getSubMatrix(ind,lowCol).det());
				//determinant not initialized then compute the product of the entry (-1)^(lowCol+ind)*a_(ind,lowCol)*det A_(ind,loCol)
				//where, again, det A_(ind,lowCol) the determinant of the sub-matrix with row ind, column lowCol omitted
				
				else det = (det.add((lowCol.operate(ind)).isEven()?entry:entry.addInverse())).multiply(getSubMatrix(ind,lowCol).det());
				//otherwise add the former value of determinant with this product
				
			}
			ind = ind.increment();
			//increment the row index
		}
		return det;
		//return
	}
	public boolean equals(AbstractFMatrix<F> another) {
		if(this==another) return true;
		if(!dim.equals(another.dim)) return false;
		return add(another.addInverse()).isZero()?true:false;
	}
	public boolean equals(Object o){
		if(this==o) return true;
		if(!(o instanceof AbstractFMatrix)) return false;
		if(getField().equals(((AbstractFMatrix<?>) o).getField())) {
			@SuppressWarnings("unchecked")
			AbstractFMatrix<F> cp = (AbstractFMatrix<F>) o;
			return equals(cp);
		}
		return false;
	}

	public void f() {
		if(val==null) val = new GenericVSpace<F> ();
		for (Entry<NNegInt,F> coeff:this) {
			NNegInt index = coeff.getKey();
			NNegInt col = index.mod(dim), row = index.diff(col).div(dim);
			F vectorEntry = arg.getValue(col), valEntry = null, prod = coeff.getValue().multiply(vectorEntry);
			if((valEntry = val.getValue(row))!=null) {
				valEntry = valEntry.add(prod);
				if(!valEntry.isZero()) val.setEntry(row,valEntry);
				else val.remove(row);
			} else val.setEntry(row, prod);
		}
		
	}
	
	public void f(GenericVSpace<F> arg) {
		this.arg = arg;
		f();
	}
	public GenericVSpace<F> getArgument() {return arg==null?null:arg;}
	public NNegInt getIndex(F val) {
		for (Entry<NNegInt,F> entry:this) if(entry.getValue().equals(val)) return entry.getKey();
		return null;
	}
	/**
	 * Returns the type of the underlying field as a class object
	 * @return the field type
	 */
	public Class<?> getField(){return one.getClass();}
	/**
	 * Returns the submatrix of this matrix with rows from <tt>rowStart</tt> to <tt>rowStart + dim - 1</tt> and columns
	 * from <tt>colStart</tt> to <tt>colStart + dim - 1</tt>
	 * @param rowStart the row starting index
	 * @param colStart the column starting index
	 * @param dim the matrix dimension
	 * @return the sub matrix
	 * @throws IllegalArgumentException if <ol><li>the <tt>dim</tt> argument is non-positive</li><li><tt>dim - x</tt> exceeds this matrix's dimension, where <tt>x = rowStart, colStart</tt></li></ol>
	 */
	public AbstractFMatrix<F> getSubMatrix (int rowStart, int colStart, int dim) throws IllegalArgumentException {
		return getSubMatrix(new NNegInt(rowStart), new NNegInt(colStart), new NNegInt(dim));
	}
	/**
	 * Returns the sub matrix starting at <tt>rowStart</tt> as first row and
	 * <tt>colStart</tt> as first column ending at <tt>rowStart+dim-1</tt> as last
	 * row and <tt>colStart+dim-1</tt> as last column. Raises an exception if either
	 * the starting indices or the ending indices exceed this matrix's dimension. 
	 * @param rowStart the first row index
	 * @param colStart the first column index
	 * @param dim the dimension of the sub matrix
	 * @return the sub matrix
	 * @throws IllegalArgumentException either
	 * the starting indices or the ending indices exceed this matrix's dimension
	 */
	public AbstractFMatrix<F> getSubMatrix (NNegInt rowStart, NNegInt colStart, NNegInt dim) throws IllegalArgumentException {
		if(dim.compareTo(NNegInt.ZERO)<=0) throw new IllegalArgumentException ("\nNon-positive dimension: "+dim.toString());
		int rowComp = dim.diff(rowStart).compareTo(this.dim), colComp = dim.diff(colStart).compareTo(this.dim);
		if(rowComp>0||colComp>0)
			 throw new IllegalArgumentException ("\nSubmatrix's dimension exceeds this matrix's dimension!\n"+(rowComp>0?"row: "+rowStart:"col: "+colStart));
		AbstractFMatrix<F> subMatrix = new AbstractFMatrix<F>(dim);
		for (Entry<NNegInt, F> entry:this){
			NNegInt index = entry.getKey();
			NNegInt col = index.mod(this.dim).diff(rowStart), row = index.diff(col).div(this.dim).diff(colStart);
			if(subMatrix.isIndexInRange(col)&&subMatrix.isIndexInRange(row)) subMatrix.setEntry(row, col, entry.getValue());
		}
		if(one!=null) subMatrix.one = one;
		return subMatrix;
	}
	/**
	 * Returns the sub matrix of this matrix with the row <tt>rowExclude</tt> and column <tt>colExclude</tt>
	 * omitted. The submatrix is of dimension <tt>this.dim - 1</tt>
	 * @param rowExclude the row index to exclude
	 * @param colExclude the column index to exclude
	 * @return
	 * @throws IllegalArgumentException
	 */
	public AbstractFMatrix<F> getSubMatrix (int rowExclude, int colExclude) throws IllegalArgumentException {
		return getSubMatrix(new NNegInt(rowExclude),new NNegInt(colExclude));
	}
	/**
	 * Returns the sub matrix with row <tt>rowExclude</tt> and column <tt>colExclude</tt>
	 * omitted
	 * @param rowExclude the row to exclude
	 * @param colExclude the column to exclude
	 * @return the sub matrix
	 * @throws IllegalArgumentException the indices exceed the dimension
	 */
	public AbstractFMatrix<F> getSubMatrix(NNegInt rowExclude, NNegInt colExclude) throws IllegalArgumentException {
		boolean rowEx =rowExclude.compareTo(dim)>=0, colEx = colExclude.compareTo(dim)>=0;
		if(rowEx||colEx) throw new IllegalArgumentException ("\nExclusion value exceeds matrix dimension: "+(rowEx&&colEx?"row: "+rowExclude.toString()+" col: "+colExclude.toString()
				+" dim = "+dim.toString():rowEx?"row: "+rowExclude.toString()+" dim = "+dim.toString():"col: "+colExclude.toString()+" dim = "+dim.toString()));
		AbstractFMatrix<F> subMatrix = new AbstractFMatrix <F> (dim.diff(NNegInt.ONE));
		for (Entry<NNegInt,F> entry:this){
			NNegInt index = entry.getKey();
			NNegInt colL = index.mod(dim), rowL = index.diff(colL).div(dim);
			int rowEq = rowL.compareTo(rowExclude), colEq = colL.compareTo(colExclude);
			if(rowEq!=0&&colEq!=0) subMatrix.setEntry(rowEq<0?rowL:rowL.diff(NNegInt.ONE), colEq<0?colL:colL.diff(NNegInt.ONE), entry.getValue());
		}
		if(one!=null) subMatrix.one = one;
		return subMatrix;
	}
	/**
	 * Returns the dimension of the underlying vector space
	 * @return the dimension
	 */
	public NNegInt getDim(){return new NNegInt(rank());}
	/**
	 * Returns the image value or null if no argument
	 */
	public GenericVSpace<F> getValue() {
		if(val==null&&arg!=null) f();
		return val==null?null:val;
	}
	/**
	 * Returns the matrix entry <p><tt>a_(index div dim, index mod dim)</tt></p>
	 * or null if n such entry exists
	 */
	public F getValue(NNegInt index) {
		F coeff = coeffMap.get(index);
		return coeff==null?null:coeff;
	}
	/**
	 * Returns the matrix entry <tt>a_(row,col)</tt> or null
	 * if no such entry exists
	 * @param row the row index
	 * @param col the column index
	 * @return the entry
	 */
	public F getValue (int row, int col){
		return getValue(new NNegInt(row),new NNegInt(col));
	}
	/**
	 * Returns the matrix entry <tt>a_(row,col)</tt> or null
	 * if no such entry exists
	 * @param row the row index
	 * @param col the column index
	 * @return the entry
	 */
	public F getValue(NNegInt row, NNegInt col){
		return getValue(row.multiply(new NNegInt(dim)).operate(col));
	}
	
	/**
	 * Returns true if and only if this square matrix is a
	 * scalar multiple of the one-matrix <tt>I_dim</tt>
	 */
	public boolean isCommutative() {
		if(isZero()) return true;
		F diag = null;
		for (Entry<NNegInt,F> entry:this){
			getIndex(entry.getKey());
			if(!col.equals(row)&&!entry.getValue().isZero()) return false;
			if(diag!=null&&!diag.equals(entry.getValue())) return false;
			if(diag==null) diag = entry.getValue();
		}
		NNegInt dimM = dim.decrement();
		if(row.compareTo(dimM)<0||col.compareTo(dimM)<0) return false;
		return true;
	}
	
	
	/**
	 * Returns the product <tt>this * another</tt>
	 * @throws IllegalArgumentException matrix dimension mismatch
	 */
	public AbstractFMatrix<F> multiply(AbstractFMatrix<F> another) throws IllegalArgumentException {
		return new AbstractFMatrix<F>(super.multiply(another));	
	}
	/**
	 * Returns the scalar multiple
	 */
	public AbstractFMatrix<F> multiply(F scalar) {return ringAct(scalar);}
	/**
	 * Returns the scalar multiple
	 * @see AbstractFMatrix#multiply(AbstractField)
	 */
	public AbstractFMatrix<F> ringAct(F scalar) {return new AbstractFMatrix<F>(super.ringAct(scalar));}
	/**
	 * Removes the entry at position <tt>index div dim, index mod dim)</tt> and
	 * returns the entry, possibly null 
	 * @param index the index
	 * @return the entry removed
	 */
	public F remove (int index){return remove(new NNegInt(index));}
	/**
	 * Removes the entry at position <tt>index div dim, index mod dim)</tt> and
	 * returns the entry, possibly null 
	 * @param index the index
	 * @return the entry removed
	 */
	public F remove (NNegInt index){return coeffMap.remove(index);}
	/**
	 * Removes the entry at position <tt>row,col)</tt> and
	 * returns the entry, possibly null
	 * @param row the row index
	 * @param col the column index
	 * @return the entry removed
	 */
	public F remove(NNegInt row, NNegInt col){return remove(row.multiply(dim).operate(col));}
	public void setArgument(GenericVSpace<F> arg) {
		this.arg = arg;
		
	}
	/**
	 * Sets the dimension of the underlying vector space on which
	 * this matrix object acts. <b>Note</b>, the dimension will
	 * only be changed if <tt>dim>=11</tt> returns true
	 * @param dim the dimension
	 */
	public void setDim(int dim){
		if(dim>=1) this.dim = new NNegInt(dim);
	}
	
	
	public void setEntry(int index, F value) {
		setEntry(new NNegInt(index),value);
	}
	
	
	
	
	/**
	 * Sets this matrix's entry to <tt>coeff</tt> at position
	 * <tt>(row,col)</tt>
	 * @param row the row index
	 * @param col the column index
	 * @param coeff the coefficient
	 */
	public void setEntry (int row, int col, F coeff){
		if(coeff==null) return;
		setEntry(new NNegInt(row),new NNegInt(col),coeff);
	}
	
	/**
	 * Adds the sub matrix <tt>subMat</tt> to this matrix object starting
	 * at row index <tt>rowStart</tt> and column index <tt>colStart</tt>
	 * @param subMat the sub matrix
	 * @param rowStar the row starting index
	 * @param colStart the column starting index
	 * @throws IllegalAgumentException the dimension of the sub matrix either
	 * exceeds this matrix's dimension or the starting index <tt>(rowStart,colStart)</tt>
	 * violates this matrix's dimension
	 */
	public void setSubMatrix (AbstractFMatrix<F> subMat, int rowStart, int colStart) throws IllegalArgumentException {
		setSubMatrix(subMat, new NNegInt(rowStart), new NNegInt(colStart));
	}
	/**
	 * Auxiliary method: sets the sub matrix to <tt>subMat</tt> by insertion
	 * starting at index <tt>(rowStart,colStart)</tt>. <b>Note</b>, all former
	 * entries, within the range <tt>{rowStart,..,rowStart.operate(dim).decrement()} x 
	 * <br />{colStart,...,colStart.operate(dim).decrement()}</tt>, get removed. 
	 * @param subMat the sub matrix
	 * @param rowStart the starting row index
	 * @param colStart the starting column index
	 * @throws IllegalArgumentException indices or dimension exceeded this matrix's
	 * dimension
	 */
	public void setSubMatrix(AbstractFMatrix<F> subMat, NNegInt rowStart, NNegInt colStart) throws IllegalArgumentException {
		if(subMat.dim.compareTo(dim)>=0) throw new IllegalArgumentException (String.format("\nDimension out of range: this_dim = %1$d\tsubMat_dim = %2$d",dim,subMat.dim));
		if(subMat.dim.operate(rowStart).compareTo(dim)>=0||subMat.dim.operate(colStart).compareTo(dim)>=0)
			throw new IllegalArgumentException ();
		NNegInt rowI = NNegInt.ZERO;
		while (rowI.compareTo(subMat.dim)<0){
			NNegInt colI = NNegInt.ZERO;
			while (colI.compareTo(subMat.dim)<0){
				F newEntry = subMat.getValue(rowI, colI), oldEntry = getValue(rowI,colI);
				if(newEntry!=null) setEntry(rowI,colI,newEntry);
				else if(oldEntry!=null) remove(rowI,colI);
				colI = colI.increment();
			}
			rowI = rowI.increment();
		}
	}
	
	/**
	 * Returns the transpose matrix (rows and columns swapped)
	 * @return the transpose matrix
	 */
	public AbstractFMatrix<F> transpose (){
		AbstractFMatrix<F> trans = new AbstractFMatrix <F> (dim);
		for (Entry<NNegInt,F> entry:this) {
			getIndex(entry.getKey());
			//NNegInt col = index.mod(dim), row = index.diff(col).div(dim);
			trans.setEntry(col, row, entry.getValue());
		}
		return trans;
	}
	/**
	 * Returns a <code>TreeMap</code> mapping the row or column indices
	 * onto the number of non zero entries in the respective row or column
	 * (the so called row or column degree).<br />Setting the parameter <tt>rowCol</tt>
	 * true, returns the row degrees, otherwise the column degrees
	 * @param rowCol boolean flag (true: row degree, false: column degree)
	 * @return the degree map
	 */
	TreeMap<NNegInt,Integer> getDegMap (boolean rowCol){
		TreeMap<NNegInt,Integer> degMap = new TreeMap<NNegInt,Integer> ();
		for (Entry<NNegInt,F> entry:this) {
			getIndex(entry.getKey());
			//NNegInt col = index.mod(dim), row = index.diff(col).div(dim);
			if(rowCol) {
				Integer deg = null;
				if((deg = degMap.get(row))!=null) degMap.put(row, ++deg);
				else degMap.put(row, 1);
			} else {
				Integer deg = null;
				if((deg = degMap.get(col))!=null) degMap.put(col, ++deg);
				else degMap.put(col, 1);
			}
		}
		return degMap;
	}
	
	/**
	 * Creates the canonical basis of the underlying vector space
	 
	private void createCanonGenerators (){
		if(dim.compareTo(NNegInt.ZERO)>0){
			HashSet<GenericVSpace<F>> genSet = new HashSet<GenericVSpace<F>> ();
			NNegInt index = NNegInt.ZERO; 
			while(index.compareTo(dim)<0) {GenericVSpace<F> v = new GenericVSpace<F> ();v.setEntry(index, one);genSet.add(v);index = index.increment();}
			//generators = new Generator<GenericVSpace<F>,F> (genSet);
		}
	}*/
	/**
	 * Computes the row and column indices
	 * defined by <tt>col = index mod dim</tt> and
	 * <tt>row = index div dim</tt>
	 * @param index the index
	 */
	private synchronized void getIndex(NNegInt index){
		col = index.mod(dim);
		row = index.diff(col).div(dim);		
	}
	private boolean isIndexInRange(NNegInt index){
		boolean test1 =NNegInt.ZERO.compareTo(index)<=0, test2 =index.compareTo(dim)<0;  
		return test1&&test2?true:false;
	}
	/**
	 * Constructs the identity matrix <tt>I_dim</tt>
	 * @param one some non zero field element
	 * @param dim the dimension
	 * @return the identity matrix
	 * @throws IllegalArgumentException if <tt>one.isZero()</tt> returns true
	 */
	public static <F extends AbstractField<F>> AbstractFMatrix<F> identity(F one, int dim) throws IllegalArgumentException
	{
		if(one.isZero()) throw new IllegalArgumentException ("\nOnly non zero arguments excepted!");
		F one1 = one.constructOne();
		AbstractFMatrix<F> id = new AbstractFMatrix<F> (dim);
		for (int i = 0; i < dim; i++) id.setEntry(i, i, one1);
		return id;
	}
	/**
	 * Constructs the identity matrix <tt>I_dim</tt> with
	 * entries of type &ltF&gt
	 * @param one some non-zero element
	 * @param dim the dimension
	 * @return the identity matrix
	 * @throws IllegalArgumentException if <tt>one.isZero()</tt> returns true
	 */
	public static <F extends AbstractField<F>> AbstractFMatrix<F> identity(F one, NNegInt dim) throws IllegalArgumentException {
		if(one==null) throw new IllegalArgumentException("\nOne argument cannot be null...");
		one = one.constructOne();
		AbstractFMatrix<F> id = new AbstractFMatrix<F> (dim);
		id.row = NNegInt.ZERO;
		while(id.row.compareTo(dim)<0){
			id.coeffMap.put(id.row.multiply(dim).operate(id.row), one);
			id.row = id.row.increment();
		}
		return id;
	}
	public static void main (String[] args){
		Rational one = Rational.ONE; 
		AbstractFMatrix<Rational> two = new AbstractFMatrix<Rational> (
				new Rational[][]{
						{one,one,one,one},
						{one,one,one,one},
						{one,one,one,one},
						{one,one,one,one}
				});
		AbstractFMatrix<Rational> add = identity(one,4);
		add.setEntry(1, 0, Rational.M_ONE);
		add.setEntry(2, 0, Rational.M_ONE);
		add.setEntry(3, 0, Rational.M_ONE);
		String out = "%1$s * \n%2$s = \n%3$s";
		System.out.println(String.format(out, add.toString(),two.toString(),add.multiply(two).toString()));
		AbstractFMatrix<Rational> tra = add.transpose();
		System.out.println(String.format(out, two.toString(),tra.toString(),two.multiply(tra).toString()));
	}

}
