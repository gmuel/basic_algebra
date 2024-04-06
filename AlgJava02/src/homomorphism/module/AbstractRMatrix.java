package homomorphism.module;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;

import module.AbstractFiniteModule;

import ring.Ring;
import ring.homo.AbstractRId;

import group.AbstractAbel;
import group.NNegInt;

/**
 * The abstract square matrix class over some arbitrary ring of type 
 * <tt>R</tt>. To specify, let <tt>M</tt> be a free <tt>R</tt>-module
 * than each object <tt>x</tt> of this class represents an <tt>R</tt>-
 * endomorphism (the set of all <tt>R</tt>-linear maps <tt>f: M -> M</tt>
 *   
 * @author adin
 *
 * @param <R> the ring type
 */
public class AbstractRMatrix<R extends Ring<R>> extends
		AbstractAbel<AbstractRMatrix<R>>implements ModuleHomo<AbstractRMatrix<R>,AbstractFiniteModule<R>,//C,NNegInt>,CommModule<
		AbstractFiniteModule<R>,R,R,NNegInt>, Ring<AbstractRMatrix<R>>{//,C,C,NNegInt> {
	/**the argument*/
	private AbstractFiniteModule<R> arg;
	/**the value*/
	private AbstractFiniteModule<R> val;
	/**the index to coefficient map*/
	private TreeMap<NNegInt,R> coeffMap;
	/**the rank*/
	private NNegInt rank;
	/**the current row index*/
	private NNegInt row;
	/**the current column index*/
	private NNegInt col;
	/**the determinant*/
	private R det = null;
	/**Constructs the zero matrix - 
	 * <p><b>Note</b> the rank is always
	 * null, rather call <code>AbstractRMatrix(int)</code>*/
	protected AbstractRMatrix (){
		super();
		coeffMap = new TreeMap<NNegInt,R>();
	}
	/**
	 * Constructs a <tt>dim x dim</tt>
	 * matrix with no entries (equals zero)
	 * @param dim the dimension
	 */
	public AbstractRMatrix(int dim) {
		this(new NNegInt(dim));		
	}
	/**
	 * Constructs the zero matrix of
	 * @param rank
	 * @throws IllegalArgumentException
	 */
	public AbstractRMatrix(NNegInt rank) throws IllegalArgumentException {
		this();
		if(rank.compareTo(NNegInt.ZERO)<0) throw new IllegalArgumentException (String.format("\nNo negative indexing: %1$s",rank));
		this.rank = rank;
		coeffMap = new TreeMap<NNegInt,R>();
	}
	/**
	 * Constructs a matrix determined by
	 * the argument <tt>matrixArray</tt>
	 * @param matrixArray some array of arrays
	 * @throws IllegalArgumentException inner length does not
	 * match outer length of argument
	 */
	public AbstractRMatrix (R[][] matrixArray) throws IllegalArgumentException {
		this(matrixArray.length);
		int outerLength = matrixArray.length;
		for (int i = 0; i < outerLength; i++){
			int innerLength = matrixArray[i].length;
			if(innerLength!=outerLength)
				
				throw new IllegalArgumentException(String.format(
						"\nArray length mismatch:\ninner length: %1$d\nouter length: %2$d",innerLength, outerLength));
			NNegInt row = new NNegInt(i);
			for (int j = 0; j < outerLength; j++){
				if(matrixArray[i][j]!=null) setEntry(row, new NNegInt(j), matrixArray[i][j]);
			}
		}
	}
	/**
	 * Constructs a copy of <tt>another</tt>
	 * matrix
	 * @param another some other matrix
	 */
	public AbstractRMatrix (AbstractRMatrix<R> another){
		this(another.rank);
		for (Entry<NNegInt,R> entry:another) {
			R coeff = entry.getValue();
			if(coeff!=null&&!coeff.isZero()) this.setEntry(entry.getKey(), coeff);
		}
	}
	/**
	 * Returns the sum of this and another
	 * matrix
	 */
	public AbstractRMatrix<R> add(AbstractRMatrix<R> another) {
		if(!rank.equals(another.rank)) throw new IllegalArgumentException ("\nDimension mismatch:\ndim this: "+rank.toString()+"\ndim another: "+another.rank.toString());
		AbstractRMatrix<R> sum = new AbstractRMatrix<R> (rank);
		sum.coeffMap.putAll(coeffMap);
		for (Entry<NNegInt,R> entry:another){
			NNegInt index = entry.getKey();
			R coeff;
			if((coeff = sum.getValue(index))!=null) {
				coeff = coeff.add(entry.getValue());
				if(!coeff.isZero()) sum.coeffMap.put(index, coeff);
				else sum.coeffMap.remove(index);
			} else sum.coeffMap.put(index, coeff);
		}
		return sum;
	}
	/**
	 * Returns the additive inverse
	 */
	public AbstractRMatrix<R> addInverse() {
		AbstractRMatrix<R> inv = new AbstractRMatrix<R> (rank);
		for (Entry<NNegInt,R> entry:this) inv.coeffMap.put(entry.getKey(), entry.getValue().addInverse());
		return inv;
	}
	/**
	 * Returns the identity map
	 */
	@SuppressWarnings("unchecked")
	public AbstractRId<R> canonicalHomo (){return new AbstractRId<R>();}
	/**
	 * Removes all coefficients from
	 * this matrix
	 * <p><b>Note</b> subsequent calls
	 * of <tt>isZero()</tt> will return true
	 */
	public void clear() {
		coeffMap.clear();
	}
	/**
	 * Returns the determinant
	 * @return the determinant
	 */
	public R det (){
		if(det!=null) return det;
		
		if(rank.equals(NNegInt.TWO)) {
			R a = getValue(NNegInt.ZERO,NNegInt.ZERO), b = getValue(NNegInt.ZERO,NNegInt.ONE);
			R c = getValue(NNegInt.ONE, NNegInt.ZERO), d = getValue(NNegInt.ONE, NNegInt.ONE);
			if(a!=null&&d!=null) return b!=null&&c!=null?a.multiply(d).add(b.multiply(c).addInverse()):a.multiply(d);
			return b!=null&&c!=null?b.multiply(c).addInverse():null;
		}
		TreeMap<NNegInt,Integer> degMapRow = getDegMap(true);
		NNegInt minRow = NNegInt.ZERO;
		Integer rowDeg = Integer.MAX_VALUE;
		for (Entry<NNegInt,Integer> entry:degMapRow.entrySet()){
			NNegInt row = entry.getKey().div(rank);
			if(rowDeg>entry.getValue()) {minRow = row;rowDeg = entry.getValue();} 
		}
		TreeMap<NNegInt,Integer> degMapCol = getDegMap(true);
		NNegInt minCol = NNegInt.ZERO;
		Integer colDeg = Integer.MAX_VALUE;
		for (Entry<NNegInt,Integer> entry:degMapCol.entrySet()){
			NNegInt row = entry.getKey().div(rank);
			if(colDeg>entry.getValue()) {minCol = row;colDeg = entry.getValue();} 
		}
		if(minRow.compareTo(minCol)<=0){
			NNegInt ind = NNegInt.ZERO;
			while (ind.compareTo(rank)<0) {
				R entry;
				if((entry = getValue(minRow,ind))!=null) {
					if(det==null) det = ((minRow.operate(ind)).isEven()?
							entry.multiply(getSubMatrix(minRow,ind).det()):
								entry.addInverse().multiply(getSubMatrix(minRow,ind).det()));
					else det = (det.add((minRow.operate(ind)).isEven()?
							entry.multiply(getSubMatrix(minRow,ind).det()):
								entry.addInverse().multiply(getSubMatrix(minRow,ind).det())));
				}
				ind = ind.increment();
			}
			return det;
		}
		NNegInt ind = NNegInt.ZERO;
		while (ind.compareTo(rank)<0) {
			R entry;
			if((entry = getValue(ind,minCol))!=null) {
				if(det==null) det = ((minCol.operate(ind)).isEven()?
						entry.multiply(getSubMatrix(ind,minRow).det()):
							entry.addInverse().multiply(getSubMatrix(ind,minRow).det()));
				else det = (det.add((minCol.operate(ind)).isEven()?
						entry.multiply(getSubMatrix(ind,minRow).det()):
							entry.addInverse().multiply(getSubMatrix(ind,minRow).det())));
			}
			ind = ind.increment();
		}
		return det;
	}
	/**
	 * Returns true only if both elements are equal
	 */
	public boolean equals(AbstractRMatrix<R> another) {return this==another?true:coeffMap.equals(another.coeffMap)?true:false;}
	/**
	 * Overrides super-class method - same as
	 * <code>equals(ABstractRMatrix)</code>
	 */
	public boolean equals(Object o){
		if(this==o) return true;
		if(!(o instanceof AbstractRMatrix)) return false;
		@SuppressWarnings("unchecked")
		AbstractRMatrix<R> cp = (AbstractRMatrix<R>) o;
		return equals(cp);
	}
	/**
	 * Computes the image vector
	 */
	public void f() {
		if(arg==null) return;
		if(val==null) val = new AbstractFiniteModule<R> ();
		else val.clear();
		for (Entry<NNegInt,R> entry:this) {
			NNegInt index = entry.getKey();
			//col = index.mod(rank), row = index.div(rank);
			getIndex(index);
			R ent = null, currArg = null;
			if((currArg = arg.getValue(col))==null) continue;
			R prod = currArg.multiply(entry.getValue());
			if((ent = val.getValue(row))!=null) {
				ent = ent.add(prod);
				if(!ent.isZero()) val.setEntry(row, ent);
				else val.remove(row);
			}
			else val.setEntry(row, prod);
		}
	}

	/**
	 * Computes the image vector for the given
	 * argument <tt>arg</tt>
	 */
	public void f(AbstractFiniteModule<R> arg) {
		setArgument(arg);
		f();
	}
	/**
	 * Returns the current argument or null
	 */
	public AbstractFiniteModule<R> getArgument() {return arg==null?null:arg;}
	/**
	 * Returns the lowest index associated with the given
	 * coefficient or null if no such coefficient was found
	 */
	public NNegInt getIndex(R val) {
		for (Entry<NNegInt,R> entry:this) {if(entry.getValue().equals(val)) return entry.getKey();}
		return null;
	}
	/**
	 * Returns the sub-matrix (of rank stricly one less than this)
	 * of this matrix with row <tt>rowEx</tt> and column <tt>colEx</tt>
	 * excluded
	 * @param rowEx row to exclude
	 * @param colEx column to exclude
	 * @return the sub-matrix
	 * @throws IllegalArgumentException if either index is equal or
	 * greater than the rank
	 */
	public AbstractRMatrix<R> getSubMatrix (NNegInt rowEx, NNegInt colEx) throws IllegalArgumentException{
		AbstractRMatrix<R> sub = new AbstractRMatrix <R> (rank.decrement());
		boolean testRow = rowEx.compareTo(rank)>=0, testCol = colEx.compareTo(rank)>=0; 
		if(testRow||testCol)
			throw new IllegalArgumentException (String.format("\n%1$s exceed%3$s rank = %2$s",
					testRow&&testCol?"both indices":testRow?"row index":"column index",rank,testRow&&testCol?"":"s"));
		for (Entry<NNegInt,R>entry:this){
			NNegInt col = entry.getKey().mod(rank), row = entry.getKey().div(rank);
			if(col.equals(colEx)||row.equals(rowEx)) continue;
			boolean colComp = col.compareTo(colEx)<0, rowComp = row.compareTo(rowEx)<0;
			if(colComp&&rowComp) sub.setEntry(row, col, entry.getValue());
			else {
				if(colComp&&!rowComp) sub.setEntry(row.decrement(), col, entry.getValue());
				else {
					if(!colComp&&rowComp)sub.setEntry(row, col.decrement(), entry.getValue());
					else sub.setEntry(row.decrement(), col.decrement(), entry.getValue());
				}
			}
		}
		
		return sub;
	}
	/**
	 * Returns the image value of the current
	 * argument
	 */
	public AbstractFiniteModule<R> getValue() {
		if(val==null){
			if(arg==null) return null;
			f();
		}
		return val;
	}
	/**
	 * Returns the coefficient associated with
	 * the given index
	 */
	public R getValue(NNegInt index) {return coeffMap.get(index);}
	/**
	 * Returns the coefficient or null for the given
	 * <tt>row</tt> and <tt>col</tt> indices
	 * @param row the row index
	 * @param col the column index
	 * @return the coefficient
	 */
	public R getValue(NNegInt row, NNegInt col) {return getValue(getIndex(row,col));}
	/**
	 * Returns the coefficient or null for the given
	 * <tt>row</tt> and <tt>col</tt> indices
	 * @param row the row index
	 * @param col the column index
	 * @return the coefficient
	 */
	public R getValue(int row, int col) {return getValue(new NNegInt(row), new NNegInt(col));}
	public int hashCode(){
		int hash = 0;
		for (Entry<NNegInt,R> coeffEntry:this) {
			hash += 59*coeffEntry.getKey().hashCode();
			hash += 47*coeffEntry.getValue().hashCode();
		}
		return hash;
	}
	public boolean isCommutative() {
		if(coeffMap.size()==0) return true;
		NNegInt lastIndex = null;
		R coeff = null;
		for (Entry<NNegInt,R>entry:this) {
			NNegInt index = entry.getKey();
			if(!index.mod(rank).equals(index.div(rank))) return false;
			if(coeff==null) {coeff = entry.getValue();lastIndex = index;continue;}
			if(!coeff.equals(entry.getValue())||!lastIndex.increment().equals(index)) return false;
			lastIndex = index;
		}
		return true;
	}
	/**
	 * Returns true if all non-zero entries are
	 * diagonal entries
	 * @return true if this is a diagonal matrix
	 */
	public boolean isDiagonal (){
		for (Entry<NNegInt,R> entry:this) {
			getIndex(entry.getKey());
			if(!row.equals(col)) return false;
		}
		return true;
	}
	/**
	 * Returns true if this is a discrete element
	 */
	public boolean isDiscrete(){
		if(arg!=null) return arg.isDiscrete();
		Iterator<Entry<NNegInt,R>> it = iterator();
		if(it.hasNext()) return it.next().getValue().isDiscrete();
		return true;
	}
	/**
	 * Returns true if this is an invertible matrix
	 */
	public boolean isUnit(){return det().isUnit();}
	/**
	 * Returns true if this is the zero matrix
	 */
	public boolean isZero() {return coeffMap.size()==0?true:false;}
	/**
	 * Returns an iterator wrapping the index and
	 * coefficient of this matrix
	 */
	public Iterator<Entry<NNegInt, R>> iterator() {return coeffMap.entrySet().iterator();}
	/**
	 * Returns the product <tt>this * another</tt>
	 */
	public AbstractRMatrix<R> multiply(AbstractRMatrix<R> another) {
		if(!rank.equals(another.rank)) throw new IllegalArgumentException ("\nDimension mismatch:\ndim this: "+rank.toString()+"\ndim another: "+another.rank.toString());
		AbstractRMatrix<R>prod = new AbstractRMatrix<R> (rank);
		if(isZero()||another.isZero()) return prod;
		for (Entry<NNegInt,R> entry1:this){
			NNegInt index1 = entry1.getKey();
			getIndex(index1);
			R coeff1       = entry1.getValue();
			for (Entry<NNegInt,R> entry2:another){
				NNegInt index2 = entry2.getKey();
				another.getIndex(index2);
				if(!col.equals(another.row)) continue;
				NNegInt nIndex = getIndex(row,another.col);
				R ent = null, pr = coeff1.multiply(entry2.getValue());
				if((ent = prod.getValue(nIndex))!=null){
					ent = ent.add(pr);
					if(!ent.isZero()) prod.coeffMap.put(nIndex, ent);
					else prod.coeffMap.remove(nIndex);
				}
				else prod.coeffMap.put(nIndex, pr);
			}
		}
		return prod;
	}
	/**
	 * Returns the rank of the underlying free module
	 * @return the rank
	 */
	public int rank (){return (int) rank.eval();}
	/**
	 * Returns the removed coefficient, possibly null
	 */
	public R remove(NNegInt index) {return coeffMap.remove(index);}
	/**
	 * Returns the entry at row index <tt>row</tt>
	 * and column index </tt> if present otherwise null
	 * @param row row index
	 * @param col column index
	 * @return the entry
	 */
	public R remove(NNegInt row, NNegInt col) {
		return remove(getIndex(row,col)); 
		
	}
	/**
	 * Returns the scalar multiple
	 */
	public AbstractRMatrix<R> ringAct(R scalar) {
		AbstractRMatrix<R> mul = new AbstractRMatrix<R> (rank);
		if(scalar.isZero()) return mul;
		for (Entry<NNegInt,R> entry:this) {mul.coeffMap.put(entry.getKey(), entry.getValue().multiply(scalar));}
		return mul;
	}
	/**
	 * Sets the argument for this map/matrix
	 */
	public void setArgument(AbstractFiniteModule<R> arg) {
		if(arg!=null) this.arg = arg;
	}
	/**
	 * Sets the coefficient of this matrix of the row/column
	 * specified by the parameter <tt>insertIndex</tt> as a
	 * <ol><li>row vector, if <tt>rowCol</tt> is true</li>
	 * <li>column vector, otherwise</li></ol>
	 * No assumption on the sequence order is made and entries exceeding
	 * the rank will be omitted. Null or zero coefficients returned by
	 * the <code>Iterable</code> cause removal of coefficients at the given positions
	 * @param coeffIterable an <code>Iterable</code> as returned by <code>java.util.Map.entrySet().iterator()</code>
	 * @param insertIndex insertion index: the row/column index where to insert
	 * @param rowCol flag whether to insert as row (true) or column vector
	 * @throws IllegalArgumentException insertion index
	 */
	public void setEntries (Iterable<Entry<NNegInt,R>> coeffIterable, NNegInt insertIndex, boolean rowCol)
	throws IllegalArgumentException {
		if(coeffIterable==null) return;
		if(insertIndex.compareTo(rank)>=0)
			throw new IllegalArgumentException (String.format("\nInsertion index = %1$s exceeds rank = %2$s",insertIndex, rank));
		for (Entry<NNegInt,R> entry:coeffIterable){
			NNegInt index = entry.getKey(), nIndex = null;
			if(index.compareTo(rank)>=0) {
				System.err.println("Index exceeds rank!");
				continue;
			}
			R coefficient = entry.getValue();
			if(rowCol){
				nIndex = getIndex(insertIndex,index);
				if(coefficient==null||coefficient.isZero()) remove(nIndex);
				else setEntry(nIndex, coefficient);
			} else {
				nIndex = getIndex(index,insertIndex);
				if(coefficient==null||coefficient.isZero()) remove(nIndex);
				else setEntry(nIndex, coefficient);
			}
		}
	}
	/**
	 * Sets the entry at position
	 * <tt>(index.div(rank),index.mod(rank))</tt>
	 * to <tt>value</tt>
	 * @param index the position index
	 * @param value the value
	 */
	public void setEntry(int index, R value){
		setEntry(new NNegInt(index),value);
	}
	/**
	 * Sets the coefficient to <tt>value</tt> at
	 * position <tt>(index.div(rank),index.mod(rank))</tt> 
	 */
	public void setEntry(NNegInt index, R value) {
		if(rank==null||rank.equals(NNegInt.ZERO)){
			System.err.println("\nZero matrix - cannot set entry");
			return;
		}
		if(value==null||value.isZero()) return;
		if(index!=null&&index.compareTo(rank.multiply(rank))<0) coeffMap.put(index, value);
	}
	/**
	 * Sets the entry at position
	 * <tt>(row,col)</tt> to <tt>value</tt>
	 * @param row the row index
	 * @param col the column index
	 * @param value the value
	 */
	public void setEntry(int row, int col, R value){
		setEntry(new NNegInt(row),new NNegInt(col),value);
	}
	/**
	 * Sets the entry at position <tt>(row,col)</tt>
	 * to <tt>value</tt>
	 * @param row the row index
	 * @param col the column index
	 * @param value the value
	 */
	public void setEntry(NNegInt row, NNegInt col, R value){
		if(value==null||value.isZero()) return;
		if(row==null||col==null) return;
		boolean rowInRange = row.compareTo(rank)<0, colInRange = col.compareTo(rank)<0;
		if(rowInRange&&colInRange) coeffMap.put(getIndex(row,col), value);
		else throw new IllegalArgumentException ("\nIndex out of range:\n"+
		(!rowInRange&&!colInRange?String.format("row index: %1$s\ncolumn index: ",row.toString(),col.toString()):
			colInRange?"\nRow index "+row.toString():"\nColumn index: "+col.toString())
					);
	}
	
	public String toString(){
		if(coeffMap.size()==0) return "0";
		String zero = "0", open = "[", close = "]\n", comma = ",";
		StringBuilder sb = new StringBuilder (open);
		row = NNegInt.ZERO;col = NNegInt.ZERO;
		boolean inRowRank = row.compareTo(rank)<0, inColRank = col.compareTo(rank)<0;
		while (inRowRank){
			while(inColRank){
				R coeff;
				if((coeff = coeffMap.get(getIndex(row,col)))!=null) sb.append(coeff.toString());
				else sb.append(zero);
				col = col.increment();
				inColRank = col.compareTo(rank)<0;
				if(inColRank) sb.append(comma);
			}
			col = NNegInt.ZERO;
			inColRank = true;
			sb.append(close);
			row = row.increment();
			inRowRank = row.compareTo(rank)<0;
			if(inRowRank) sb.append(open);
		}
		return sb.toString();
	}
	/*-------------------------privates-------------------------*/
	/**
	 * Returns a tree map with row/column indices as keys and
	 * number of non-zero entries as values
	 * @param rowCol true sorted by row indices othwise be column
	 * @return the degree map
	 */
	TreeMap<NNegInt,Integer> getDegMap (boolean rowCol){
		TreeMap<NNegInt,Integer> degMap = new TreeMap<NNegInt,Integer> ();
		for (Entry<NNegInt,R> entry:this) {
			getIndex(entry.getKey());
			//NNegInt col = index.mod(dim), row = index.diff(col).div(dim);
			if(rowCol) {
				Integer deg = null;
				if((deg = degMap.get(row))!=null)
					degMap.put(row, ++deg);
				else degMap.put(row, 1);
			} else {
				Integer deg = null;
				if((deg = degMap.get(col))!=null)
					degMap.put(col, ++deg);
				else degMap.put(col, 1);
			}
		}
		return degMap;
	}
	
	private synchronized void getIndex(NNegInt index){
		col = index.mod(rank);
		row = index.diff(col).div(rank);		
	}
	private NNegInt getIndex (NNegInt row, NNegInt col){
		return row.multiply(rank).operate(col);
	}
}
