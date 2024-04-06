package group.representation;

import group.NNegInt;
import group.SymmetricGroup;
import homomorphism.group.GroupHomo;
import homomorphism.module.AbstractRMatrix;
import ring.UnitaryCommRing;
import topo.AbstractElFct;
/**
 * The permutation representation of the symmetric group <tt>S_n</tt>:
 * <p>
 * If the square matrix <tt>e_(i,j)</tt> represents the matrix with all zero entries
 * except of the entry <tt>a_(i,j)</tt> then it is the subset of all matrices <tt>A = sum_{i=1}^n e_(pi(i),i) </tt>
 * over some ring of sub-type of {@link UnitaryCommRing}, for some permutation <tt>pi in S_n</tt>
 *  and some positive integer <tt>n</tt>.</p> 
 * @author adin
 *
 * @param <F> the type of unitary commutative ring
 * @param <X> the type of set the permutation acts on
 */
public class PermutationRep<F extends UnitaryCommRing<F>, X> extends
		AbstractElFct<SymmetricGroup<X>, AbstractRMatrix<F>> implements
		GroupHomo<PermutationRep<F, X>, SymmetricGroup<X>, AbstractRMatrix<F>> {
	/**the one field*/
	private F one;
	/**
	 * Constructs an empty permutation representation
	 */
	private PermutationRep (){super();}//y = new AbstractFMatrix<F>();}
	/**
	 * Constructs an empty permutation representation
	 * with the field specifying parameter <tt>one</tt>
	 * @param one field specifier
	 */
	public PermutationRep (F one) {
		this();
		this.one = one.getOne();
	}
	/**
	 * Constructs a permutation representation
	 * map for a given ring specifier <tt>one</tt>
	 * and of rank <tt>rank</tt> 
	 * @param one the ring specifier
	 * @param rank the rank
	 */
	public PermutationRep (F one, NNegInt rank){
		this(one);
		val = new AbstractRMatrix<F> (rank);
	}
	/**
	 * Computes the permutation matrix of the
	 * current argument if and only if <tt>this.arg!=null</tt>
	 * and <tt>one!=null</tt> return true
	 */
	public void f() {
		if(arg!=null&&one!=null){
			int length = 0;
			if(val!=null){
				length = arg.getMaxArg()>=val.rank()?arg.getMaxArg():val.rank();
				val.clear();
			}
			else val = new AbstractRMatrix<F> (length);
			for (int i = 0; i < length;i++){
				SymmetricGroup.Cycle c;
				if((c = arg.getCycle(i))!=null){
					Integer next = c.getValue(i), prev = i;
					if(val.getValue(next, prev)!=null) continue;
					while (next!=i){
						val.setEntry(next, prev, one);
						prev = next;
						next = c.getValue(next);
					}
					val.setEntry(next, prev, one);
				} else val.setEntry(i,i,one);
			}
		}
		
	}
	
}
