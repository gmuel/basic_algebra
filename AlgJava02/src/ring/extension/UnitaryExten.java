package ring.extension;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import module.AbstractFiniteModule;
import module.FiniteGenMod;

import group.NNegInt;
import group.UnitGroup;

import homomorphism.module.ARMatOperator;
import homomorphism.module.AbstractRMatrix;
import homomorphism.ring.PolyExtenEmbed;
import homomorphism.ring.PolyExtenEmbed.EmbedHomo;

import ring.UnitaryCommRing;
import ring.integer.IntRing;
import ring.poly.MonoPoly;

/**
 * The class of finite  extension over some unitary ring 
 * of type <tt>U</tt> - accepts polynomials
 * type {@link MonoPoly} with index type {@link NNegInt}
 * and head coefficients of sub-type {@link UnitaryCommRing}
 * <b>only</b> as units.
 * <p>Any element is represented as an element of a free module
 * of rank <tt>deg modulator</tt>, with basis
 * <p><tt>B := {X^i : 0 &lt= i &lt= deg modulator - 1}</tt> 
 * @author adin
 *
 * @param <U> the type of the underlying <code>UnitaryCommRing</code>
 */
public class UnitaryExten<U extends UnitaryCommRing<U>> extends UnitaryCommRing<UnitaryExten<U>> 
implements FiniteGenMod<UnitaryExten<U>,U,NNegInt>{
	/**
	 * Returns the embedding homomorphism
	 * <p><tt>f : U -&gt U[X]/(poly), u |-&gt u.1</tt>
	 * @param poly the modulo operator
	 * @return the embedder
	 */
	public static final <U extends UnitaryCommRing<U>> EmbedHomo<U> getEmbedder (MonoPoly<NNegInt,U> poly){
		return PolyExtenEmbed.getEmbedder(poly);
	}
	/**
	 * Returns the polynomial <tt>poly</tt> embedded in
	 * <tt>U[X]/(mod)[X]</tt>
	 * @param poly the polynomial
	 * @param mod the modulator
	 * @return the embedded polynomial
	 */
	public static final <U extends UnitaryCommRing<U>> MonoPoly<NNegInt,UnitaryExten<U>> getImage (MonoPoly<NNegInt,U> poly,
			MonoPoly<NNegInt,U> mod){
		PolyExtenEmbed<U> emb = new PolyExtenEmbed<U>(mod);
		emb.f(poly);
		return emb.getValue();
	}
	/**
	 * Auxiliary method - returns a polynomial <tt>sum_i a_i X^i</tt>
	 * for all module element entries not null/zero
	 * @param element the module element
	 * @return the polynomial
	 */
	public static final <U extends UnitaryCommRing<U>> MonoPoly<NNegInt,U> getPoly (AbstractFiniteModule<U> element){
		MonoPoly<NNegInt,U> poly = new MonoPoly<NNegInt,U> ();
		for (Entry<NNegInt,U> entry:element) poly.setCoefficient(entry.getKey(), entry.getValue());
		return poly;
	}
	/**
	 * Returns a polynomial embedder object - 
	 * a ring monomorphism extending the underlying
	 * ring embedder to the polynomial rings
	 * <p><tt>U[X] C-&gt U'[X]</tt>, where <tt>U' = U[X]/(poly)</tt>
	 * @param poly the modulo polynomial
	 * @return the polynomial embedder
	 */
	public static final <U extends UnitaryCommRing<U>> PolyExtenEmbed<U> getPolyEmbedder(MonoPoly<NNegInt, U> poly){
		return new PolyExtenEmbed<U> (poly);
	}
	/**the element polynomial modulo modulator
	 * as a noetherian module element*/
	private AbstractFiniteModule<U> element;
	/**the modulator polynomial*/
	private MonoPoly<NNegInt,U> modulator;
	/**the matrix representing the x-multiplication
	 * in this class*/
	private AbstractRMatrix<U>   xMonom;
	/**the multiplication operator matrix -
	 * for any given free module element it
	 * computes the product <tt>this * another</tt>
	 * for some other element <tt>another</tt>*/
	private AbstractRMatrix<U> operator;
	/**the inverse marix of <tt>operator</tt>,
	 * computed only if <tt>operator.det().isUnit()</tt>
	 * returns true*/
	private AbstractRMatrix<U>      inv;
	/**the determinant*/
	private U det;
	/**
	 * Constructs an empty element
	 */
	protected UnitaryExten() {
		super();
		this.element = new AbstractFiniteModule<U> ();
	}
	/**
	 * Constructs an element of this class
	 * by defining <tt>this = element mod modulator</tt> 
	 * @param element the element
	 * @param modulator the modul(o-oper)ator
	 * @throws IllegalArgumentException <ol>
	 * <li><tt>modulator==null</tt> returns true</li>
	 * <li><tt>element.getCoefficient(element.getdegree()).isUnit()</tt> returns false</li></ol>
	 */
	public UnitaryExten (MonoPoly<NNegInt,U> element, MonoPoly<NNegInt,U> modulator)
	throws IllegalArgumentException {
		this();
		if(modulator!=null) {
			NNegInt deg = modulator.getDegree();
			U headCoeff = modulator.getCoefficient(deg);
			if(headCoeff.isUnit()) {
				this.modulator = headCoeff.equals(headCoeff.getOne())?new MonoPoly<NNegInt,U> (modulator):modulator.multiply(headCoeff.inverse());
			}else throw new IllegalArgumentException ("\nOnly monic polynomials permitted!");
			if(element!=null){
				if(!element.isZero()){
					NNegInt elDeg = element.getDegree();
					while (elDeg.compareTo(deg)>=0&&!element.isZero()){
						U coeff = element.getCoefficient(elDeg);
						MonoPoly<NNegInt,U> fac = new MonoPoly<NNegInt,U> (elDeg.diff(deg),coeff.addInverse());
						element = element.add(fac.multiply(this.modulator).addInverse());
						elDeg = element.getDegree();
						if(elDeg==null||element==null) break;
					}
					for (Entry<NNegInt,U> coeffEntry:element)
						this.element.setEntry(coeffEntry.getKey(), coeffEntry.getValue());
				}
			}
			
		} else throw new IllegalArgumentException ("\nModulator never null!");
		
	}
	/**
	 * Constructs an element by converting the argument
	 * <tt>element</tt> to a polynomial
	 * @param element some module element
	 * @param modulator the modulator
	 * @throws IllegalArgumentException <ol>
	 * <li><tt>modulator==null</tt> returns true</li>
	 * <li><tt>element.getCoefficient(element.getdegree()).isUnit()</tt> returns false</li></ol>
	 */
	public UnitaryExten (AbstractFiniteModule<U> element, MonoPoly<NNegInt,U> modulator)
	throws IllegalArgumentException {
		this(getPoly(element),modulator);
	}
	/**
	 * Constructs a copy of some
	 * other element
	 * @param another some other element
	 */
	public UnitaryExten (UnitaryExten<U> another){
		this();
		this.modulator = new MonoPoly<NNegInt,U> (another.modulator);
		this.element   = new AbstractFiniteModule<U> (another.element);
	}
	/**
	 * Returns the sum of this and another
	 * element only if the modulator polynomials
	 * are equal - throws an exception otherwise
	 * <p><b>Note</b> to prevent this simply
	 * try addition with the product of both
	 * polynomials as modulator
	 * @throws IllegalArgumentException if the modulator polynomials
	 * are not equal
	 */
	public UnitaryExten<U> add(UnitaryExten<U> another) throws IllegalArgumentException {
		if(!modulator.equals(another.modulator))
			throw new IllegalArgumentException (String.format("\n%1$s only permitted with same modulator," +
					"\nthis mod = %2$s\tanother mod = %3$s","Addition",modulator,another.modulator));
		return new UnitaryExten<U>(element.add(another.element),modulator);
	}
	/**
	 * Returns the additive inverse
	 */
	public UnitaryExten<U> addInverse() {
		if(isZero()) return this;
		AbstractFiniteModule<U> addInv = new AbstractFiniteModule<U> ();
		for (Entry<NNegInt,U> entry:element) addInv.setEntry(entry.getKey(), entry.getValue().addInverse());
		
		return new UnitaryExten<U> (addInv,modulator);
	}
	/**
	 * Removes/Clears nothing
	 */
	@Deprecated
	public void clear() {}
	/**
	 * Returns true if generic
	 * type and element are equal
	 */
	public boolean equals(Object o){
		if(this==o) return true;
		if(!(o instanceof UnitaryExten)) return false;
		
		try{
			@SuppressWarnings("unchecked")
			UnitaryExten<U> cp = (UnitaryExten<U>)o;
			return equals(cp);
		}catch(ClassCastException e){
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * Returns true only if the modulator polynomials
	 * and the elements are equal
	 */
	public boolean equals(UnitaryExten<U> another) {
		return modulator.equals(another.modulator)?element.equals(another.element):false;
	}
	/**
	 * Returns a deep copy of this element's
	 * module representation
	 * @return the module element
	 */
	public AbstractFiniteModule<U> getElement (){return new AbstractFiniteModule<U> (element);}
	/**
	 * Returns the <tt>factor</tt> in <tt>modulator = (X - x) * factor</tt>
	 * in the polynomial ring of this extension object <tt>U' = U[X]/(modulator)</tt>
	 * <p><b>Note</b>, that <tt>x</tt> represents the zero of the modulator polynomial
	 * @return the divisor over this extension
	 */
	public MonoPoly<NNegInt,UnitaryExten<U>> getFactor (){
		MonoPoly<NNegInt,UnitaryExten<U>> factor = new MonoPoly<NNegInt,UnitaryExten<U>>();
		if(modulator==null||modulator.isZero()) {
			factor.setCoefficient(NNegInt.ZERO, getOne());
			return factor;
		}
		MonoPoly<NNegInt,UnitaryExten<U>> mod = getImage(getModulator(),modulator), xMinusX = new MonoPoly<NNegInt,UnitaryExten<U>>();
		NNegInt deg = mod.getDegree(), zero = NNegInt.ZERO, one = NNegInt.ONE;
		xMinusX.setCoefficient(one, getOne());
		U mOne = modulator.getCoefficient(deg).getOne().addInverse();
		xMinusX.setCoefficient(zero, new UnitaryExten<U> (
				new MonoPoly<NNegInt,U>(one,mOne),modulator));
		while (deg!=null&&deg.compareTo(zero)>0) {
			NNegInt diff = deg.diff(one);
			UnitaryExten<U> coeff = mod.getCoefficient(deg).addInverse();
			MonoPoly<NNegInt,UnitaryExten<U>> fac = new MonoPoly<NNegInt,UnitaryExten<U>> (diff,coeff);
			factor = factor.add(fac);
			mod = mod.add(xMinusX.multiply(fac));
			deg = mod.getDegree();
			
		}
		return factor.addInverse();
	}
	/**
	 * Returns a hash set of generators
	 */
	public HashSet<UnitaryExten<U>> getGenerators() {
		HashSet<UnitaryExten<U>> genSet = new HashSet<UnitaryExten<U>>();
		if(isZero()) return genSet;
		U one = element.iterator().next().getValue().getOne();
		NNegInt index = NNegInt.ZERO, rank = modulator.getDegree();
		while (index.compareTo(rank)<0) {
			genSet.add(new UnitaryExten<U>(new MonoPoly<NNegInt,U>(index,one),modulator));
			index = index.increment();
		}
		return genSet;
	}
	/**
	 * Returns the index associated with
	 * value <tt>val</tt> or null
	 */
	public NNegInt getIndex(U val) {
		for (Entry<NNegInt,U> entry:element) {if(entry.getValue().equals(val)) return entry.getKey();}
		return null;
	}
	/**
	 * Returns a deep copy of the modulator polynomial - 
	 * to specify the modulo operator
	 * @return the modulator
	 */
	public MonoPoly<NNegInt,U> getModulator (){
		return modulator==null?new MonoPoly<NNegInt,U> (modulator):
			new MonoPoly<NNegInt,U> (modulator);
	}
	/**
	 * Returns the one element
	 */
	public UnitaryExten<U> getOne() {
		return new UnitaryExten<U> (
				new MonoPoly<NNegInt,U>(NNegInt.ZERO,
						modulator.getCoefficient(modulator.getDegree()).getOne()),
						modulator);
	}
	/**
	 * Returns a deep copy of the multiplication operator matrix:
	 * <p>the <tt>U</tt>-linear map, mapping any ring element
	 * <tt>p</tt> to the product <tt>this * p</tt>
	 * @return the multiplication operator
	 */
	public AbstractRMatrix<U> getOperator(){
		if(operator==null) setOperator();
		return new AbstractRMatrix<U> (operator);
	}
	/**
	 * Returns the unit group element or null if
	 * this is no unit
	 */
	public UnitGroup<UnitaryExten<U>> getUnit() {
		return isUnit()?new UnitGroup<UnitaryExten<U>> (this):null;
	}
	/**
	 * Returns the matrix representing
	 * the adjoined element <tt>X^1</tt>
	 * @return the x monom matrix
	 */
	public AbstractRMatrix<U> getXMonom (){
		if(xMonom==null)setXMonom();
		return new AbstractRMatrix<U> (xMonom);
	}
	/**
	 * Returns a hash code
	 */
	public int hashCode (){
		int hash = 0;
		hash += 59*element.hashCode();
		hash += 101*modulator.hashCode();
		return hash;
	}
	/**
	 * Returns the coefficient 
	 */
	public U getValue(NNegInt index) {return element.getValue(index);}
	/**
	 * Returns the multiplicative inverse
	 * of this if it is a unit, null otherwise
	 */
	public UnitaryExten<U> inverse() {
		if(!isUnit())
			return null;
		if(inv!=null) {
			inv.f(getOne().element);
			UnitaryExten<U> uInv = new UnitaryExten<U> (inv.getValue(),modulator);
			uInv.operator = inv;
			uInv.inv      = operator;
			return uInv;
		}
		ARMatOperator<U> op = new ARMatOperator<U> (operator);
		inv = op.getInverse();
		return inverse();
	}
	
	/**
	 * Returns true if the module element is
	 * discrete
	 */
	public boolean isDiscrete() {return element.isDiscrete();}
	/**
	 * Returns true if this is
	 * a unit, to specify:
	 * <p>if the determinant of its multiplication
	 * operator matrix is a unit in <tt>U</tt>
	 */
	public boolean isUnit() {
		if(element==null||element.isZero()) return false;
		if(det!=null) return det.isUnit();
		if(operator!=null){
			det = operator.det();
			return det.isUnit();
		}
		if(xMonom==null) setXMonom();
		if(operator==null) setOperator();
		return operator.det().isUnit();
	}
	/**
	 * Returns true only if the module elements
	 * equals zero
	 */
	public boolean isZero() {return element.isZero();}
	/**
	 * Returns an iterator over the module entry
	 * <p><b>Note</b>, however, remove operations
	 * are not supported, but throws no exception
	 * either.
	 * Setting the entry objects will <b>not
	 * alter</b> this element and will be
	 * lost the moment the returned iterator is
	 * out of scope
	 */
	public Iterator<Entry<NNegInt, U>> iterator() {
		return new EntIterator();
	}
	/**
	 * Returns the product of this and another
	 * element
	 * @throws IllegalArgumentException if both modulator polynomials are different
	 */
	public UnitaryExten<U> multiply(UnitaryExten<U> another) throws IllegalArgumentException {
		if(!modulator.equals(another.modulator)) 
			throw new IllegalArgumentException (String.format("\n%1$s only permitted with same modulator," +
					"\nthis mod = %2$s\tanother mod = %3$s","Multiplication",modulator,another.modulator));
		if(operator!=null) {
			operator.f(another.element);
			return new UnitaryExten<U> (operator.getValue(),modulator);
		}
		AbstractFiniteModule<U> anothModEl = another.element, prod = new AbstractFiniteModule<U>(); 
		if(xMonom==null) setXMonom();
		NNegInt powerCount = NNegInt.ZERO;
		for (Entry<NNegInt,U> coeffEnt:element){
			NNegInt index = coeffEnt.getKey();
			if(index.equals(NNegInt.ZERO)) prod = anothModEl.ringAct(coeffEnt.getValue());
			else{
				while (powerCount.compareTo(index)<0) {
					xMonom.f(anothModEl);
					anothModEl = xMonom.getValue();
					powerCount = powerCount.increment();
				}
				prod = prod.add(anothModEl.ringAct(coeffEnt.getValue()));
			}
		}
		UnitaryExten<U> product = new UnitaryExten<U> (prod,modulator);
		return product;
	}
	/**
	 * Returns the rank of this element
	 * (less than or equal to the degree
	 * of the extension)
	 */
	public int rank() {return element.rank();}
	/**
	 * Returns null and removes <b>NOTHING</b>
	 * 
	 */
	@Deprecated
	public U remove(NNegInt index) {return null;}
	/**
	 * Returns the scalar multiple of this
	 */
	public UnitaryExten<U> ringAct(U scalar) {return new UnitaryExten<U>(element.ringAct(scalar),modulator);}
	/**
	 * Sets nothing
	 */
	@Deprecated
	public void setEntry(NNegInt index, U value) {}
	public String toString (){
		if(isZero()) return "0";
		StringBuilder sb = new StringBuilder ();
		String x = " X^%1$s", pls = " + ";
		Iterator<Entry<NNegInt,U>> it = element.iterator();
		while (it.hasNext()){
			Entry<NNegInt,U> entry = it.next();
			NNegInt index = entry.getKey();
			sb.append(entry.getValue());
			if(!index.equals(NNegInt.ZERO)) sb.append(String.format(x, entry.getKey()));
			if(it.hasNext()) sb.append(pls);
		}
		sb.append("\nmod\n");
		sb.append(toString(modulator));
		return sb.toString();
	}
	private String toString (MonoPoly<NNegInt,U> poly){
		if(poly.isZero()) return "0";
		StringBuilder sb = new StringBuilder ();
		String x = " X^%1$s", pls = " + ";
		Iterator<Entry<NNegInt,U>> it = poly.iterator();
		while (it.hasNext()){
			Entry<NNegInt,U> entry = it.next();
			NNegInt index = entry.getKey();
			sb.append(entry.getValue());
			if(!index.equals(NNegInt.ZERO)) sb.append(String.format(x, entry.getKey()));
			if(it.hasNext()) sb.append(pls);
		}
		return sb.toString();
	}
	/**
	 * Sets the operator
	 * <p><tt>A := sum_(i &lt= n - 1) c_i X^i</tt>
	 * <p><tt>c_i</tt> is the <tt>i</tt>-th coefficient of this element
	 * and <tt>X^i</tt> is the <tt>i</tt>-th
	 * power of the <tt>X</tt>-monom endomorphism
	 */
	private void setOperator (){
		U constCoeff = element.getValue(NNegInt.ZERO);
		NNegInt rank = modulator.getDegree(), index = NNegInt.ZERO;
		operator = new AbstractRMatrix<U> (rank);
		AbstractRMatrix<U> pow = new AbstractRMatrix<U>(xMonom);
		if (constCoeff!=null){
			while (index.compareTo(rank)<0) {
				operator.setEntry(index, index, constCoeff);
				index = index.increment();
			}
			index = NNegInt.ZERO;
		}
		for (Entry<NNegInt,U> entry:element){
			NNegInt elIndex = entry.getKey();
			if(elIndex.equals(NNegInt.ZERO)) continue;
			while (index.compareTo(index)<0) {
				pow = pow.multiply(xMonom);
				index = index.increment();
			}
			operator = operator.add(pow.ringAct(entry.getValue()));					
		}
	}
	/**
	 * Sets the endomorphism <tt><p>X : U[X]/(f) -&gt U[X]/(f),
	 * <p>X'^i |-&gt X'^(i + 1)</tt> for <tt>i &lt= n - 2</tt>
	 * <p><tt>X'^i |-&gt - p_(n - 1) * sum_(i &lt= n - 1) f_i X'^i</tt> for <tt>i = n - 1</tt>
	 * <p>for <tt>n = deg f</tt>, the equivalence class
	 * representing the <tt>X</tt> monom
	 * in the polynomial ring <tt>U[X]</tt>
	 */
	private void setXMonom(){
		NNegInt index = NNegInt.ZERO, rank = modulator.getDegree(), rank_M = rank.decrement();
		//operator = new AbstractRMatrix<U> (rank);
		U one = modulator.getCoefficient(rank).getOne();
		xMonom = new AbstractRMatrix<U> (rank);
		while (index.compareTo(rank_M)<0){
			NNegInt indexIncr = index.increment();
			if(indexIncr.compareTo(rank)<0)
				xMonom.setEntry(indexIncr, index, one);
			U modCoeff;
			if((modCoeff = modulator.getCoefficient(index))!=null)
				xMonom.setEntry(index, rank_M, modCoeff.addInverse());
			index = indexIncr;
		}		
	}
	/*-------------------inner classes-------------------*/
	/**
	 * The element entry iterator class - iterators
	 * over a local deep copy for the duration of the iteration
	 * <p>Does not support remove operations and changes the
	 * the returned entries will be lost 
	 * @author adin
	 *
	 */
	private class EntIterator implements Iterator<Entry<NNegInt,U>> {
		/**a wrapped iterator object*/
		private Iterator<Entry<NNegInt,U>> it;
		/**
		 * Constructs the iterator
		 */
		private EntIterator (){it = getElement().iterator();}
		/**Returns true as long as the wrapped iterator
		 * returns true*/
		public boolean hasNext() {return it.hasNext();}
		/**Returns the next entry in the
		 * iteration*/
		public Entry<NNegInt, U> next() {return it.next();}
		/**Removes nothing*/
		public void remove() {}
		
	}
	public static void main (String[] args){
		MonoPoly<NNegInt,IntRing> mod = new MonoPoly<NNegInt,IntRing> (), fac1 = new MonoPoly<NNegInt,IntRing>(), fac2 = new MonoPoly<NNegInt,IntRing>();
		mod.setCoefficient(NNegInt.ZERO, IntRing.TWO.addInverse());
		mod.setCoefficient(NNegInt.TWO, IntRing.ONE);
		fac1.setCoefficient(NNegInt.ZERO, new IntRing(3));
		fac1.setCoefficient(NNegInt.ONE, new IntRing(2));
		UnitaryExten<IntRing> ext1 = new UnitaryExten<IntRing> (fac1,mod);
		fac2.setCoefficient(NNegInt.ZERO, new IntRing(7));
		fac2.setCoefficient(NNegInt.ONE, new IntRing(5));
		UnitaryExten<IntRing> ext2 = new UnitaryExten<IntRing> (fac2,mod);
		DisplayResults.displayProduct(ext1, ext2);
		System.out.println(String.format("\n\n%1$s\n/\n%2$s =\n%3$s",ext2.modulator,"X - x", ext2.getFactor()));
	}
	
	
}
