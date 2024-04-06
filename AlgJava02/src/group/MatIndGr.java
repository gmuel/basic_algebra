package group;

import ring.CyclicRing;
import ring.integer.IntRing;
/**
 * The matrix index class - implements the
 * {@link AbstractNAG}-interface, although,
 * technically speaking, it is not a group
 * @author adin
 *
 */
public class MatIndGr extends AbstractNAG<MatIndGr> {
	/**an error string: indicates two different
	 * modulo-values*/
	private static final String ERROR_STR = "Different modulo arguments:\nfirst:  %1$s\nsecond: %2$s";
	/**the row index*/
	private final CyclicRing row;
	/**the column index*/
	private final CyclicRing col;
	/**
	 * 
	 * @param row
	 * @param col
	 * @throws IllegalArgumentException
	 */
	public MatIndGr (CyclicRing row, CyclicRing col) throws IllegalArgumentException {
		super();
		IntRing mod = row.getMod();
		if(mod.equals(col.getMod())){
			this.row = new CyclicRing(row.getValue(),mod);
			this.col = new CyclicRing(row.getValue(),mod);
		} else {
			throw new IllegalArgumentException (String.format(ERROR_STR, mod,col.getMod()));
		}
	}
	public MatIndGr (IntRing index, IntRing mod){
		super();
		col = new CyclicRing(index,mod);
		row = new CyclicRing(index.div(col.getValue()),mod);
	}
	
	public MatIndGr multiply(MatIndGr another) {
		return col.equals(another.row)?new MatIndGr(row,another.col):null;
	}
	
	public boolean equals(MatIndGr another) {
		return row.equals(another.row)&&col.equals(another.col);
	}
	
	public boolean isDiscrete() {return true;}
	
	public MatIndGr inverse() {return new MatIndGr(col,row);}
	
	public boolean isNeutral() {return row.equals(col);}
	



}
