package field;

import java.util.Map.Entry;

import module.GenericVSpace;
import group.NNegInt;
import group.UnitGroup;
import homomorphism.EmbedHomo;
import homomorphism.module.AbstractFMatrix;
import ring.extension.UnitaryExten;
import ring.poly.AFPoly;
import ring.poly.MonoPoly;
/**
 * Finite field extension class: any object of this class
 * represents a field extension of a field of type &ltF&gt
 * @author adin
 *
 * @param <F> type of sub class of {@link AbstractField}
 */
public class AFExten<F extends AbstractField<F>> extends AbstractField<AFExten<F>> {
	/**error string: indicates some field operation was to
	 * be performed on invalid arguments*/
	private static final String ERROR = 
			"\nDiffering minimal polynomials\nthis: %1$s\tanother: %2$s";
	private static final String OUT_STR = "%1$s mod %2$s";
	/**the minimal polynomial*/
	private final AFPoly<F> mini;
	/**the residual class p mod mini
	 * as a vector element*/
	private GenericVSpace<F> element;
	/**auxiliary element*/
	private F one;
	/**the embedding homomorphism
	 * <br /><tt>f: K(x) -> End_K(K(x)),<br /> x = (x_0,x_1,...,x_n-1) |-> sum_i=0^n-1 x_i A^i</tt>*/
	private EmbedHomo<F> embedding;
	/**
	 * Constructs the zero element of this finite field extension,
	 * where the argument <tt>mini</tt> is the minimal polynomial.
	 * <b>Note</b>, that zero polynomials are not supported
	 * @param mini the minimal polynomial
	 * @throws IllegalArgumentException if <tt>mini.isZero()</tt>
	 * returns true
	 */
	public AFExten (MonoPoly<NNegInt,F> mini) throws IllegalArgumentException{
		super();
		F coeff = mini.getCoefficient(mini.getDegree());
		one = coeff.constructOne();
		if(coeff!=null&&!coeff.isZero()) {
			this.mini = new AFPoly<F>(coeff.equals(one)?mini:mini.multiply(coeff.inverse()));
		}
		else throw new IllegalArgumentException ("\nHead term coefficient is zero - minimal polynomial should never be zero!");
	}
	/**
	 * Constructs a new extension element <tt>element mod mini</tt>
	 * @param mini the modulus operator
	 * @param element the actual element
	 */
	public AFExten (MonoPoly<NNegInt,F> mini, MonoPoly<NNegInt,F> element){
		this(mini);
		this.element = new GenericVSpace<F>();
		AFPoly<F> mod = (new AFPoly<F>(element)).mod(this.mini);
		for (Entry<NNegInt,F> coeffEntry:mod) this.element.setEntry(coeffEntry.getKey(), coeffEntry.getValue());
	}
	public AFExten<F> add(AFExten<F> another) {
		if(mini.equals(another.mini)){
			AFExten<F> sum = new AFExten<F>(mini);
			sum.element    = element.add(another.element);
			return sum;
		}
		throw new IllegalArgumentException (String.format(ERROR,mini,another.mini));
	}
	public AFExten<F> addInverse() {
		// TODO Auto-generated method stub
		return null;
	}
	public boolean equals(AFExten<F> another) {
		// TODO Auto-generated method stub
		return false;
	}
	public AFPoly<F> getPolynomial(){return new AFPoly<F>(mini);}
	/**
	 * Returns a copy of this element as
	 * a vector
	 * @return the residuum as vector
	 */
	public GenericVSpace<F> getResiduumAsVector(){return new GenericVSpace<F>(element);}
	public AFExten<F> inverse() {
		// TODO Auto-generated method stub
		return null;
	}
	public boolean isDiscrete(){return one.isDiscrete();}
	public boolean isZero() {return element.isZero()?true:false;}
	
	public AFExten<F> multiply(AFExten<F> another) {
		if(!mini.equals(another.mini)) throw new IllegalArgumentException (String.format(ERROR,mini,another.mini));
		if(embedding==null&&another.embedding==null) embedding= new EmbedHomo<F>(this);
		else{
			if(embedding!=null&&another.embedding==null) another.embedding = new EmbedHomo<F>(embedding);
			else {
				embedding = new EmbedHomo<F>(another.embedding);
				
			}
		}
		embedding.f(this);
		AbstractFMatrix<F> mat = new AbstractFMatrix<F>(embedding.getValue());
		mat.f(another.element);
		AFExten<F> product = new AFExten<F>(mini);
		product.element = mat.getValue();
		return product;
	}
	public UnitGroup<AFExten<F>> getUnit(){return new UnitGroup<AFExten<F>>(this);}
	public EmbedHomo<F> getEmbedding (){return embedding==null?null:embedding;}
	public AFExten<F> constructOne() {return new AFExten<F>(mini,new MonoPoly<NNegInt,F>(NNegInt.ZERO,one));}
		public static void main(String[] args){
		AFPoly<Rational> poly1 = new AFPoly<Rational>(
				new Rational[]{
						Rational.ONE.add(Rational.ONE).addInverse(),
						null,
						null,
						Rational.ONE
				}
				);
		AFExten<Rational> poly1Ex = new AFExten<Rational>(poly1);
	}
	public String toString (){
		if (isZero()) return String.format(OUT_STR,"0",mini.toString());
		return String.format(OUT_STR, new AFPoly<F>(element).toString(),mini.toString());
	}
}
