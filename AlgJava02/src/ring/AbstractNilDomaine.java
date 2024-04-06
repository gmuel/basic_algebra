package ring;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import ring.integer.IntRing;
import topo.AbstractElFct;

import group.NNegInt;
import group.UnitGroup;
import homomorphism.module.AbstractRMatrix;
import homomorphism.ring.RingHomo;
import module.AbstractFiniteModule;
import module.FiniteGenMod;
/**
 * An abstract nipotent ring over some commutative
 * unitary domaine of type <tt>U</tt>- there exists an element <tt>a</tt>
 * and a positive integer <tt>n</tt> such that <tt>a^n = 0</tt>. The integer
 * <tt>n</tt> is called the nilpotency index of <tt>a</tt>.
 * <p>In this sense this class is isomorphic to
 * <p><tt>U[X]/(X^n) = sum_(0 &lt= i &lt= n-1) U.a^i</tt>,
 * <p>where <tt>U.a^0</tt> is the torsion free <tt>U</tt> sub-module of the ring 
 * (sometimes referred to as free part or free sub-module etc.). The torsion sub-module
 * <p><tt>sum_(1 &lt= i &lt= nilIndex - 1) U.a^i</tt>
 * <p>is called the annihilator ideal. The nilpotency index of its generator
 * <tt>a</tt> is sometimes referred to as 'the nilpotency index'.</tt>  
 * <p>
 * <p><b>Note</b>, that addition and multiplication is only supported for
 * elements of same nilpotency index, although recursive parameterization
 * <p><code>AbstractNilDomaine<...<AbstractNilDomaine<U>>...></code> might help
 * to circumvent this issue (still, ordering of nilpotency indices of different value
 * in the recursive parameterization is problematic)
 * @author adin
 *
 * @param <U> type of the underlying {@link UnitaryCommRing}
 */
public class AbstractNilDomaine<U extends UnitaryCommRing<U>> extends
		UnitaryCommRing<AbstractNilDomaine<U>> implements FiniteGenMod<AbstractNilDomaine<U>,U,NNegInt>{
	
	/**the element as a free finite module element*/
	private AbstractFiniteModule<U> element;
	/**the rank of the module - equivalently
	 * the nilpotency index <tt>n</tt> of <tt>a</tt>*/
	private NNegInt rank;
	/**the matrix mapping each instance <tt>another</tt>
	 * to the product <tt>this * another</tt>*/
	private AbstractRMatrix<U> operator;
	/**the embedding homomorphism*/
	private Embed<U> embedder;
	/**
	 * Constructs an empty instance - only
	 * to be used in sub-classes
	 */
	protected AbstractNilDomaine() {
		super();
		element = new AbstractFiniteModule<U> ();
	}
	/**
	 * Constructs the zero element with nilpotency
	 * index of <tt>a</tt> set to <tt>rank</tt>
	 * @param rank
	 */
	public AbstractNilDomaine(NNegInt rank){
		this();
		this.rank = rank; 
		embedder  = getEmbedder(rank);
	}
	/**
	 * Constructs an element with coefficients
	 * specified by the argument <tt>array</tt>.
	 * <p><b>Note</b> the length of the array determines
	 * the nilpotency index of <tt>a</tt>
	 * @param array the coefficient array
	 */
	public AbstractNilDomaine (U[] array){
		this(new NNegInt(array.length));
		for (int i = 0; i < array.length; i++) {
			if(array[i]!=null) element.setEntry(new NNegInt(i), array[i]);
		}
	}
	/**
	 * Constructs an element with coefficients
	 * specified by the argument <tt>element</tt>
	 * @param element the coefficient module element
	 */
	public AbstractNilDomaine (AbstractFiniteModule<U> element){
		this(NNegInt.ZERO);
		for (Entry<NNegInt,U> entry:element) {
			NNegInt index = entry.getKey();
			this.element.setEntry(index, entry.getValue());
			if(rank.compareTo(index)<=0) rank = index.increment();
		}
	}
	/**
	 * Constructs an element with coefficients specified
	 * by the argument <tt>element</tt> and with nilpotency
	 * index of <tt>a</tt> set to <tt>rank</tt> only if
	 * the argument is greater than the internal rank
	 * @param element the coefficient module element
	 * @param rank the nilpotency index of <tt>a</tt>
	 */
	public AbstractNilDomaine (AbstractFiniteModule<U> element, NNegInt rank){
		this(element);
		if(element.rank()<=rank.eval()) this.rank = rank;
		else {
			System.err.println("Low rank - set to element's rank");
			this.rank = new NNegInt(element.rank());
		}
	}
	/**
	 * Constructs a copy from the argument
	 * <tt>another</tt>
	 * @param another some other element
	 */
	public AbstractNilDomaine (AbstractNilDomaine<U> another){
		this(another.element);
	}
	/**
	 * Returns the sum, only if both nilpotency indices are equal
	 */
	public AbstractNilDomaine<U> add(AbstractNilDomaine<U> another) throws IllegalArgumentException {
		if(!rank.equals(another.rank))
			throw new IllegalArgumentException (String.format("\nDifferent ranks: rank (this) = %1$s\trank(another) = %2$s",rank,another.rank));
		return new AbstractNilDomaine<U> (element.add(another.element));
	}
	/**
	 * Returns the additive inverse
	 */
	public AbstractNilDomaine<U> addInverse() {
		AbstractNilDomaine<U> addInv = new AbstractNilDomaine<U> (rank);
		for (Entry<NNegInt,U> entry:element)
			addInv.element.setEntry(entry.getKey(), entry.getValue().addInverse());
		return addInv;
	}
	/**
	 * Clears nothing - stub method
	 */
	public void clear() {}
	
	/**
	 * Returns true if both elements have the
	 * same nilpotency index and the same module
	 * coefficients
	 * <p><b>Note</b>, however, the zero elements
	 * are always considered equal regardless of the
	 * nilpoteny index
	 */
	public boolean equals(AbstractNilDomaine<U> another) {
		if(isZero()&&another.isZero()) return true;
		return rank.equals(another.rank)?element.equals(another.element):false;
	}
	/**
	 * Returns a deep copy of this elements module
	 * element (to specify: the coefficient element
	 * <tt>(c_0,...c_[rank-1])</tt> for <tt>this=sum_i&gt= rank-1 c_i a^i</tt>)
	 * @return the coefficient element
	 */
	public AbstractFiniteModule<U> getElement (){
		return new AbstractFiniteModule<U> (element);
	}
	/**
	 * Returns an empty set if this equals zero
	 * or a set with exact <tt>n</tt> elements
	 * <tt>a_i</tt> for <tt>0 &lt= i &lt= n - 1</tt>
	 */
	public HashSet<AbstractNilDomaine<U>> getGenerators() {
		HashSet<AbstractNilDomaine<U>> setOfGens = new HashSet<AbstractNilDomaine<U>>();
		U one = null;
		for (Entry<NNegInt,U> entry:this) {one = entry.getValue().getOne();break;}
		if(one==null) return setOfGens;
		NNegInt index = NNegInt.ZERO;
		while(index.compareTo(rank)<0){
			AbstractFiniteModule<U> mod = new AbstractFiniteModule<U>();
			mod.setEntry(index, one);
			setOfGens.add(new AbstractNilDomaine<U>(mod));
			index = index.increment();
		}
		return setOfGens;
	}
	public NNegInt getIndex(U val) {
		for (Entry<NNegInt,U> entry:this){
			if(entry.getValue().equals(val)) return entry.getKey();
		}
		return null;
	}
	/**
	 * Returns the nilpotency index of the annihilator
	 * generator <tt>a</tt>
	 * @return
	 */
	public NNegInt getNilPotencyIndex (){return rank;}
	/**
	 * Returns the one element
	 */
	public AbstractNilDomaine<U> getOne() {return constructOne(null,rank);}
	/**
	 * Returns this element if <code>isUnit()</tt> returns
	 * true otherwise null
	 */
	public UnitGroup<AbstractNilDomaine<U>> getUnit() {
		return isUnit()?new UnitGroup<AbstractNilDomaine<U>> (this):null;
	}
	public U getValue(NNegInt index) {return element.getValue(index);}
	/**
	 * Returns the multiplicative inverse of
	 * this or null, if <code>isUnit()</code> returns
	 * false
	 */
	public AbstractNilDomaine<U> inverse() {
		if(!isUnit()) return null;
		AbstractNilDomaine<U> inv = new AbstractNilDomaine<U>(rank);
		U freeInv = element.getValue(NNegInt.ZERO).inverse();
		inv.element.setEntry(NNegInt.ZERO, freeInv);
		NNegInt index = NNegInt.ONE;
		while (index.compareTo(rank)<0) {
			NNegInt nIndex = index;
			U invCoeff = null;
			while(true){
				if(invCoeff==null) {
					U thisCoeff = element.getValue(nIndex), invCoeff1 = inv.element.getValue(index.diff(nIndex));
					boolean nullT1 = thisCoeff!=null, nullT2 = invCoeff1!=null;
					invCoeff = nullT1&&nullT2?thisCoeff.multiply(invCoeff1):null;
				}
				else {
					U thisCoeff = element.getValue(nIndex), invCoeff1 = inv.element.getValue(index.diff(nIndex));
					boolean nullT1 = thisCoeff!=null, nullT2 = invCoeff1!=null;
					invCoeff = nullT1&&nullT2?thisCoeff.multiply(invCoeff1).add(invCoeff):invCoeff;
				}
				if(nIndex.compareTo(NNegInt.ZERO)>0) nIndex = nIndex.decrement();
				else break;
				
			}
			if(invCoeff!=null) inv.element.setEntry(index, invCoeff.multiply(freeInv).addInverse());
			index = index.increment();
		}
		return inv;
	}
	/**
	 * Returns true if the module element
	 * is discrete
	 */
	public boolean isDiscrete() {
		return element.isDiscrete();
	}
	/**
	 * Returns true only if the
	 * free coefficient is a
	 * unit, equivalently
	 * <p><code>element.getValue(NNegInt.ZERO)!=null&&</code>
	 * <p><code>element.getValue(NNegInt.ZERO).isUnit()</code> returns
	 * true 
	 */
	public boolean isUnit() {
		U zeroEntry = element.getValue(NNegInt.ZERO);
		return zeroEntry==null?false:zeroEntry.isUnit();
	}
	/**
	 * Returns true if this equals the zero element
	 */
	public boolean isZero() {return element.isZero();}
	/**
	 * Returns an iterator over the coefficient entries - 
	 * <p><b>Note</b> remove operations are not supported,
	 * although no exception is thrown
	 */
	public Iterator<Entry<NNegInt, U>> iterator() {return new EntryIterator();}
	/**
	 * Returns the product of this and another
	 * element, only if both elements have same 
	 * nilpotency index
	 */
	public AbstractNilDomaine<U> multiply(AbstractNilDomaine<U> another) {
		if(operator==null) setOperator();
		operator.f(another.element);
		return new AbstractNilDomaine<U> (operator.getValue());
	}
	/**
	 * Returns the rank of the module element less than
	 * or equal to the nilpotency index of the generator
	 */
	public int rank() {return element.rank();}
	public AbstractNilDomaine<U> ringAct(U scalar) {
		embedder.f(scalar);
		return multiply(embedder.getValue());
	}
	
	/**
	 * Removes nothing - stub method
	 */
	public U remove(NNegInt index) {return null;}
	/**
	 * Sets nothing - stub method
	 */
	public void setEntry(NNegInt index, U value) {}
	/**
	 * Returns a string representation of
	 * this element
	 */
	public String toString (){
		if(isZero()) return "0";
		StringBuilder sb = new StringBuilder ();
		String a = " a^%1$s", pls = " + ";
		Iterator<Entry<NNegInt,U>> entryIt = element.iterator();
		while(entryIt.hasNext()) {
			Entry<NNegInt,U> entry = entryIt.next();
			NNegInt index = entry.getKey();
			U coefficient = entry.getValue();
			sb.append(coefficient);
			if(!index.equals(NNegInt.ZERO)) sb.append(String.format(a, index));
			if(entryIt.hasNext()) sb.append(pls);
		}
		return sb.toString();
	}
	/*---------------------privates---------------------*/
	/**
	 * Initializes the multiplication operator:
	 * <tt>A = {
	 * <p>{c_0,   0, 0, ..., 0},
	 * <p>(c_1, c_0, 0, ..., 0},
	 * <p>(c_2, c_1, c_0, ..., 0},
	 * <p>...
	 * <p>{c_rank-1,c_rank-2,c_rank-3,...,c_0}}</tt>
	 */
	private void setOperator (){
		operator = new AbstractRMatrix<U>(rank);
		for (Entry<NNegInt,U> entry:element) {
			NNegInt index = entry.getKey(), nIndex = NNegInt.ZERO;
			U coefficient = entry.getValue();
			while (index.compareTo(rank)<0){
				operator.setEntry(index, nIndex, coefficient);
				index = index.increment();
				nIndex= nIndex.increment();
			}
		}
	}
	/*---------------------inner classes---------------------*/
	/**
	 * Wrapper for the entry set iterator - preents remove operations
	 * @author adin
	 *
	 */
	private class EntryIterator implements Iterator<Entry<NNegInt,U>>{
		/**the module element iterator*/
		Iterator<Entry<NNegInt,U>> it;
		/**Constructs an iterator wrappter*/
		EntryIterator (){it = element.iterator();}
		/**returns true if the wrapped iteratorwon
		 * method returns true*/
		public boolean hasNext() {return it.hasNext();}

		/**returns the next element in this iterator*/
		public Entry<NNegInt, U> next() {return it.next();}

		/**Removes nothing*/
		public void remove() {}
		
	}
	/*---------------------statics---------------------*/
	//private static final HashMap<NNegInt,Embed<U>>
	/**
	 * Returns the one element for a given nilpotency index of
	 * <tt>nilIndex</tt>. <b>Note</b>, the first argument <tt>element</tt>
	 * defines the one in that sense: <tt>element.getOne()</tt> is called
	 * @param element some arbitrary element
	 * @param nilIndex the nilpotency index
	 * @return the one element
	 */
	public static <U extends UnitaryCommRing<U>> AbstractNilDomaine<U> constructOne(U element, NNegInt nilIndex){
		AbstractNilDomaine<U> one = new AbstractNilDomaine<U>(nilIndex);
		one.element.setEntry(NNegInt.ZERO, element.getOne());
		return one;
	}
	/**
	 * Computes the product <tt>factor1 * factor2</tt> and
	 * prints it to console
	 * @param factor1 first factor
	 * @param factor2 second factor
	 */
	static void display (AbstractNilDomaine<IntRing> factor1, AbstractNilDomaine<IntRing> factor2){
		System.out.println(String.format("(%1$s) * (%2$s) = \n%3$s",factor1,factor2,factor1.multiply(factor2)));
	}
	/**
	 * Returns the embedding homomorphism 
	 * <p><tt>jota : U -&gt sum_(0 &lt= j &lt= rank - 1) U.a^j,
	 * <p>x |-&gt x.a^0</tt>
	 * @param rank the nilpotency index
	 * @return the embedder
	 */
	public static <U extends UnitaryCommRing<U>> Embed<U> getEmbedder (NNegInt rank){
		return new Embed<U>(rank);
	}
	public static void main (String[] args){
		AbstractNilDomaine<IntRing> x1 = new AbstractNilDomaine<IntRing>(
				new IntRing[]{
						IntRing.M_ONE,IntRing.ONE,IntRing.TWO
		});
		AbstractNilDomaine<IntRing> x2 = new AbstractNilDomaine<IntRing>(
	 			new IntRing[]{
						IntRing.M_ONE,null,IntRing.TWO.addInverse()
		});
		
		display(x1,x2);
		AbstractNilDomaine<IntRing> invX1 = x1.inverse(), invX2 = x2.inverse();
		
		display(x1,invX1);
		display(x2,invX2);
		
	}
	/*---------------------nesteds---------------------*/
	/**
	 * The embedding homomorphism class - maps each element
	 * <tt>x in U </tt> to <tt>x.a^0</tt>. Each instance
	 * of this class is defined by the nilpotency index f
	 * the annihilator generator element <tt>a</tt> 
	 * @author adin
	 *
	 * @param <U>
	 */
	public static class Embed<U extends UnitaryCommRing<U>> extends AbstractElFct<U,AbstractNilDomaine<U>> implements
	RingHomo<Embed<U>, U, AbstractNilDomaine<U>> {
		final NNegInt rank;
		private Embed (NNegInt rank){
			super();
			this.rank = rank;
		}
		
		public void f() {
			if(arg==null) return;
			if(val==null) val = new AbstractNilDomaine<U>(rank);
			val.element.setEntry(NNegInt.ZERO, arg);
		}

		
		public boolean isKernel(U arg) {return arg==null?true:arg.isZero();}
		
	}
	/**
	 * The nilpotent ideal class - its <code>contains(AbstractNilDomaine)</code> method
	 * returns true for all nilpotent elements
	 * @author adin
	 *
	 * @param <U> the type of the underlying {@link UnitaryCommRing}
	 */
	public static class NilIdeal<U extends UnitaryCommRing<U>> extends IdealInU<AbstractNilDomaine<U>>{
		
		/**
		 * Returns true if the free part is zero, to specify:
		 * <p><tt>
		 */
		public boolean contains(AbstractNilDomaine<U> element) {
			return element.element.getValue(NNegInt.ZERO)==null?true:false;
		}
		
	}
	/**
	 * An enumeration of the first nine embedding
	 * generators:
	 * <p>each instance 
	 * @author adin
	 *
	 */
	public enum EmbedGenerator {
		TWO(NNegInt.TWO),
		THREE(TWO.nilIndex.increment()),
		FOUR(THREE.nilIndex.increment()),
		FIVE(FOUR.nilIndex.increment()),
		SIX(FIVE.nilIndex.increment()),
		SEVEN(SIX.nilIndex.increment()),
		EIGHT(SEVEN.nilIndex.increment()),
		NINE(EIGHT.nilIndex.increment()),
		TEN(NINE.nilIndex.increment());
		private NNegInt nilIndex;
		private EmbedGenerator (NNegInt nilIndex){
			this.nilIndex = nilIndex;
		}
		public <U extends UnitaryCommRing<U>> Embed<U> getDomaine (){
			return getEmbedder(nilIndex);
		}
		
	}
		
}
