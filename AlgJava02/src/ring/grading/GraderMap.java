package ring.grading;

import group.NNegInt;

import java.util.TreeMap;

import module.integer.FIntM;

import ring.integer.IntRing;
/**
 * The grader map class - derives from {@link TreeMap}.
 * <p>Uses {@link NNegInt} as keys and {@link Grader}
 * instances as values and provides convenient methods
 * <ol><li>to generate the lexicographical term order
 * via <code>getLexGraderMap(int)</code> or <code>getLexGraderMap(NNegInt)</code></li>
 * <li>the reversed lexicographical term order via
 * <code>getRevLexGraderMap(int/NNegInt)</code> </li>
 * <li>the reversed sum lexicographical term order via
 * <code>getRevSumLexGraderMap(int/NNegInt)</code></li>
 * <li>the sum lexicographical term order via
 * <code>getSumLexGraderMap(int/NNegInt)</code></li>
 * </ol>
 * 
 * @author adin
 *
 */
public class GraderMap extends TreeMap<NNegInt, Grader> {
	/**
	 * Returns the lexicographical grader map - 
	 * induces the lexicographical term order:
	 * <p><tt>prod_i X_i^t_i < prod_j X_j^s_j</tt> if
	 * <tt>t_i==s_i</tt> returns true for all <tt>i &lt k</tt>
	 * and <tt>t_k &lt s_k</tt> for some monomials <tt>X_i^t_i,X_j^s_j</tt>
	 * @param numOfVars number of indeterminates (variables)
	 * @return the lex grader map
	 * @throws IllegalArgumentException if <tt>numOfVars.isNeutral()</tt> returns true 
	 */
	public static GraderMap getLexGraderMap (int numOfVars ) throws IllegalArgumentException {
		return getLexGraderMap(new NNegInt(numOfVars));
	}
	/**
	 * Returns the lexicographical grader map - 
	 * induces the lexicographical term order:
	 * <p><tt>prod_i X_i^t_i < prod_j X_j^s_j</tt> if
	 * <tt>t_i==s_i</tt> returns true for all <tt>i &lt k</tt>
	 * and <tt>t_k &lt s_k</tt> for some monomials <tt>X_i^t_i,X_j^s_j</tt>
	 * @param numOfVars number of indeterminates (variables)
	 * @return the lex grader map
	 * @throws IllegalArgumentException if <tt>numOfVars.isNeutral()</tt> returns true 
	 */	
	public static GraderMap getLexGraderMap(NNegInt numOfVars) throws IllegalArgumentException {
		checkNumber(numOfVars);
		NNegInt index = NNegInt.ZERO;
		IntRing one = IntRing.ONE;
		GraderMap lexGrd = new GraderMap();
		while (index.compareTo(numOfVars)<0){
			lexGrd.put(index,new Grader (new FIntM(index,one)));
			index = index.increment();
		}
		return lexGrd;
	}
	/**
	 * Returns the reversed lexicographical grader map - 
	 * induces the reversed lexicographical term order:
	 * <p><tt>prod_i X_i^t_i < prod_j X_j^s_j</tt> if
	 * <tt>t_i==s_i</tt> returns true for all <tt>i &gt k</tt>
	 * and <tt>t_k &lt s_k</tt> for some monomials <tt>X_i^t_i,X_j^s_j</tt>
	 * @param numOfVars number of indeterminates (variables)
	 * @return the lex grader map
	 * @throws IllegalArgumentException if <tt>numOfVars.isNeutral()</tt> returns true 
	 */	
	public static GraderMap getRevLexGraderMap (int numOfVars) throws IllegalArgumentException {
		return getRevLexGraderMap(new NNegInt(numOfVars));
	}
	/**
	 * Returns the reversed lexicographical grader map - 
	 * induces the reversed lexicographical term order:
	 * <p><tt>prod_i X_i^t_i < prod_j X_j^s_j</tt> if
	 * <tt>t_i==s_i</tt> returns true for all <tt>i &gt k</tt>
	 * and <tt>t_k &lt s_k</tt> for some monomials <tt>X_i^t_i,X_j^s_j</tt>
	 * @param numOfVars number of indeterminates (variables)
	 * @return the lex grader map
	 * @throws IllegalArgumentException if <tt>numOfVars.isNeutral()</tt> returns true 
	 */
	public static GraderMap getRevLexGraderMap (NNegInt numOfVars) throws IllegalArgumentException {
		checkNumber(numOfVars);
		NNegInt index = numOfVars.decrement();
		IntRing one = IntRing.ONE;
		GraderMap lexGrd = new GraderMap();
		while (true){
			lexGrd.put(index,new Grader (new FIntM(index,one)));
			if((index = index.decrement())==null) break;
		}
		return lexGrd;
	}
	/**
	 * Returns the reversed sum lexicographical grader map - 
	 * induces the reversed sum lexicographical term order:
	 * <p><tt>prod_i X_i^t_i < prod_j X_j^s_j</tt> if
	 * <tt>sum_i&gt k t_i==sum_i&gtk s_i</tt> returns true and
	 * and <tt>t_k &lt s_k</tt> for some <tt>k</tt> and some monomials <tt>X_i^t_i,X_j^s_j</tt>
	 * @param numOfVars number of indeterminates (variables)
	 * @return the lex grader map
	 * @throws IllegalArgumentException if <tt>numOfVars.isNeutral()</tt> returns true 
	 */
	public static GraderMap getRevSumLexGraderMap (int numOfVars) throws IllegalArgumentException {
		return getRevSumLexGraderMap(new NNegInt(numOfVars));
	}
	/**
	 * Returns the reversed sum lexicographical grader map - 
	 * induces the reversed sum lexicographical term order:
	 * <p><tt>prod_i X_i^t_i < prod_j X_j^s_j</tt> if
	 * <tt>sum_i&gt k t_i==sum_i&gtk s_i</tt> returns true and
	 * and <tt>t_k &lt s_k</tt> for some <tt>k</tt> and some monomials <tt>X_i^t_i,X_j^s_j</tt>
	 * @param numOfVars number of indeterminates (variables)
	 * @return the lex grader map
	 * @throws IllegalArgumentException if <tt>numOfVars.isNeutral()</tt> returns true 
	 */
	public static GraderMap getRevSumLexGraderMap (NNegInt numOfVars) throws IllegalArgumentException {
		checkNumber(numOfVars);
		GraderMap sumLexGrd = new GraderMap();
		NNegInt index = numOfVars.decrement();
		FIntM ar = new FIntM();
		while (true) {
			ar = ar.add(new FIntM(index,IntRing.ONE));
			sumLexGrd.put(index,new Grader(ar));
			if((index = index.decrement())==null) break;
			
		}
		return sumLexGrd;
	}
	public static GraderMap getSumLexGraderMap (int numOfVars) throws IllegalArgumentException {
		return getSumLexGraderMap(new NNegInt(numOfVars));
	}
	public static GraderMap getSumLexGraderMap (NNegInt numOfVars) throws IllegalArgumentException {
		checkNumber(numOfVars);
		GraderMap sumLexGrd = new GraderMap();
		NNegInt index = NNegInt.ZERO;
		FIntM ar = new FIntM();
		while (index.compareTo(numOfVars)<0) {
			ar = ar.add(new FIntM(index,IntRing.ONE));
			sumLexGrd.put(index,new Grader(ar));
			index = index.increment();
		}
		return sumLexGrd;
	}
	private static void checkNumber (NNegInt number) throws IllegalArgumentException {
		if(number.compareTo(NNegInt.ZERO)<=0)  throw new IllegalArgumentException ("");
	}
	/***/
	private static final long serialVersionUID = -6100734318863693552L;
	public GraderMap (){super();}
	
}
