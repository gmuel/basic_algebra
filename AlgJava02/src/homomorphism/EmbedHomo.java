package homomorphism;

import java.util.Map.Entry;
import java.util.TreeMap;

import ring.poly.AFPoly;
import topo.AbstractElFct;
import field.AFExten;
import field.AbstractField;
import field.Rational;
import group.NNegInt;
import homomorphism.module.AFMatOperator;
import homomorphism.module.AbstractFMatrix;
import homomorphism.module.AbstractRMatrix;
import homomorphism.ring.RingHomo;
/**
 * Embedding homomorphism class: defines the ring homomorphism:
 * <br /><tt>f : F[X]/(p) -> M(deg p, deg p; F)</tt>,
 * <br /><tt>q mod p = sum_(i=0)^(n-1) q'_i X'^i|->
 * sum_(i=0)^(n-1) q'_i rho(X)^i</tt>, where
 * <ol>
 * <li><tt>F</tt> is the type of the
 * underlying field, (sub class of {@link AbstractField}) and <tt>F[X]</tt> its polynomial ring in one indeterminate</li>
 * <li><tt>p = sum_(i=0)^(n-1) p_i X^i + X^n in F[X]</tt> some (not necessarily) irreducible polynomial over <tt>F</tt> and <tt>deg p = n</tt> its degree</li>
 * <li><tt>M(n, n; F)</tt> the ring of square matrices over <tt>F</tt></li>
 * <li><tt>e_(i,j) in M(n, n; F)</tt> the elementary matrix with all but one entry zero (at <tt>(i,j)</tt></li>
 * <li><tt>rho(X') = sum_(i=1)^(n-1) (e_(i+1,i) -  p_i e_(i,n))</tt> the representation matrix of the monom <tt>X'</tt> acting on the
 * canonical base <tt>B_X' = {1, X', X'^2, ..., X'^(n-1)}</tt> of the <tt>F</tt>-vector space <tt>F[X]/(p)</tt></li>
 * </ol>
 * If the polynomial <tt>p</tt> is reducible, the representation is reducible (contains <tt>F[X]/(p)</tt> stable
 * sub modules).
 * @author adin
 *
 * @param <F>
 */
public class EmbedHomo<F extends AbstractField<F>> extends AbstractElFct<AFExten<F>,AbstractRMatrix<F>> implements
		RingHomo<EmbedHomo<F>, AFExten<F>, AbstractRMatrix<F>> {
	/**a map contain all monomials <tt>rho(X')^i</tt>, where the degree
	 * <tt>i</tt> is the key in this map*/
	private TreeMap<NNegInt,AbstractFMatrix<F>> canonicalBase;
	/**the matrix <tt>rho(X')</tt>*/
	private AbstractFMatrix<F> resiMatrix;
	/**the matrix operator: used to compute */
	private AFMatOperator<F> operator;
	/**the polynomial <tt>p</tt> in <tt>F[X]/(p)</tt>*/
	private AFPoly<F> mini;	
	/**field specifier: determines the type of the field*/
	private F one;
	/**
	 * Constructs an empty embedding
	 */
	protected EmbedHomo() {
		super();
	}
	/**
	 * Constructs an embedding for the given field
	 * extension <tt>poly</tt> 
	 * @param extElement the field extension
	 */
	public EmbedHomo (AFExten<F> extElement) {
		this();
		mini = extElement.getPolynomial();
		NNegInt deg = mini.getDegree();
		one = mini.getCoefficient(deg);
		resiMatrix = new AbstractFMatrix<F> (deg);
		NNegInt index = NNegInt.ZERO, dim = resiMatrix.getDim().decrement();
		while(index.compareTo(dim)<0){
			NNegInt indexP = index.increment();
			resiMatrix.setEntry(indexP, index, one);
			F coeff;
			if((coeff = mini.getCoefficient(index))!=null)
				resiMatrix.setEntry(index, dim, coeff.addInverse());
			index = indexP;
		}
		operator = new AFMatOperator<F>(resiMatrix);
		canonicalBase = new TreeMap<NNegInt,AbstractFMatrix<F>> ();
	}
	/**
	 * Constructs an embedding for the given
	 * polynomial <tt>poly</tt>
	 * @param poly
	 */
	public EmbedHomo (AFPoly<F> poly){
		this(new AFExten<F>(poly));
		
	}
	/**
	 * Constructs an embedding by copying the original
	 * embedding <tt>another</tt>
	 * @param another some other embedding
	 */
	public EmbedHomo (EmbedHomo<F> another){
		this();
		mini = new AFPoly<F> (another.mini);
		resiMatrix = new AbstractFMatrix<F>(another.resiMatrix);
		canonicalBase = new TreeMap<NNegInt,AbstractFMatrix<F>> ();
		if(another.canonicalBase.size()>0) canonicalBase.putAll(another.canonicalBase);  
	}
	void operate(){
		if(resiMatrix.isZero());
		else operator.computeInverse();
	}

	
	public void f() {
		if(arg!=null){
			if(canonicalBase.isEmpty()) setCanBase();
			AbstractFMatrix<F> mat = null;
			for (Entry<NNegInt,F> coeffEnt:arg.getResiduumAsVector()){
				if(mat==null) mat = canonicalBase.get(coeffEnt.getKey()).multiply(coeffEnt.getValue());
				else mat = mat.add(canonicalBase.get(coeffEnt.getKey()).multiply(coeffEnt.getValue()));
			}
			val = mat;
		}
	}
	/**
	 * Returns true, if <tt>arg.isZero()</tt> returns true
	 */
	public boolean isKernel(AFExten<F> arg) {return arg==null?false:arg.isZero()?true:false;}

	public void setArgument(AFExten<F> arg) {
		if(arg!=null) this.arg = arg;
		
	}
	
	private void setCanBase (){
		NNegInt index = NNegInt.ZERO, dim = resiMatrix.getDim();
		AbstractFMatrix<F> mat = AbstractFMatrix.identity(one, dim);
		while (index.compareTo(dim)<0){
			canonicalBase.put(index, mat);
			mat = mat.multiply(resiMatrix);
			index = index.increment();
		}
	}
	public static void main (String[] args){
		AFPoly<Rational> p = new AFPoly<Rational>(
				new Rational[]{Rational.ONE,Rational.ONE,Rational.ONE,Rational.ONE}
				);
		AFPoly<Rational> q = new AFPoly<Rational>(
				new Rational[]{Rational.ONE,null,Rational.ONE}
				);
		AFExten<Rational> extenQWithP = new AFExten<Rational> (p,q);
		EmbedHomo<Rational> embed = new EmbedHomo<Rational>(extenQWithP);//extenQWithP.getEmbedding();
		embed.f(extenQWithP);
	}
}
