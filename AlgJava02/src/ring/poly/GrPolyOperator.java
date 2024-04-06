package ring.poly;

import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

import module.integer.FIntM;
import module.poly.GradedPolyMod;

import ring.grading.GrIndex;
import ring.grading.GradedIndexing;
import ring.grading.Grader;

import field.AbstractField;
import field.Rational;
import group.NNegInt;
/**
 * The graded polynomial operator class: provides some
 * useful methods to compute normal forms, head term characteristics etc.
 * @author adin
 *
 * @param <F> the type of the sub-class of {@link AbstractField}
 */
public class GrPolyOperator<F extends AbstractField<F>> {
	/**an ordered map of grading objects - for a proper grading
	 * the maximal number of indeterminates should equal the
	 * number of gradings*/
	private TreeMap<NNegInt,Grader>                gradingMap;
	/**an ordered map of polynomials*/
	private TreeMap<NNegInt,GradedMultiPoly<F>>       polyMap;
	private TreeSet<GradedMultiPoly<F>>          transPolySet;
	private TreeMap<NNegInt,GradedMultiPoly<F>> groebnerBasis;
	private GradedPolyMod<F>                    headTermTuple;
	private GradedPolyMod<F>                     divisorTuple;
	private GrPolyOperator<F>                   innerOperator;
	/**the maximal number of indeterminates in the polynomial
	 * map*/
	private NNegInt                                 maxLength;
	/**
	 * Constructs an empty operator
	 * object
	 */
	GrPolyOperator(){
		polyMap       = new TreeMap<NNegInt,GradedMultiPoly<F>>();
		gradingMap    = new TreeMap<NNegInt,Grader> ();
		groebnerBasis = new TreeMap<NNegInt,GradedMultiPoly<F>> ();
		headTermTuple = new GradedPolyMod<F>();
		maxLength     = NNegInt.ZERO;
	}
	/**
	 * Constructs an operator object
	 * with its polynomial map set
	 * to the given iterable object
	 * <tt>polyIter</tt>
	 * <p><b>Note</b>, the sequence of
	 * the iteration determines the sequence
	 * of the polynomial in the ordered polynomial map
	 * <p><b>Also note</b>, that no gradings are provided
	 * @param polyIter some iterable object whose iteration
	 * returns polynomials
	 */
	GrPolyOperator(Iterable<GradedMultiPoly<F>> polyIter) {
		this();
		addPolynomials(polyIter);
	}
	/**
	 * Constructs an operator object by setting
	 * the polynomial map to the sequence of polynomials
	 * returned by the iterable object <tt>polyIter</tt>
	 * and the grading map to the sequence of gradings returned
	 * by the iterable object <tt>
	 * <p><b>Note</b>, the sequence of
	 * the iteration determines the sequence
	 * of the polynomial as well as of the gradings in the ordered polynomial map,
	 * resp. the ordered map of gradings
	 * @param graderIter some iterable object whose iteration
	 * returns grading objects
	 * @param polyIter some iterable object whose iteration
	 * returns polynomials
	 * 
	 */
	public GrPolyOperator(Iterable<Grader> graderIter, Iterable<GradedMultiPoly<F>> polyIter){
		this();
		addAllGrading(graderIter);
		addPolynomials(polyIter);
		setInnerOperator(graderIter);
	}
	/**
	 * Appends the given polynomial <tt>poly</tt> to this
	 * operator's polynomial map
	 * @param poly the polynomial to add
	 */
	public void addPolynomial (GradedMultiPoly<F> poly){
		NNegInt index = polyMap.size()==0?NNegInt.ZERO:polyMap.lastKey().increment(), newLength = poly.getNumberOfIndeterminates();
		GradedMultiPoly<F> cp = new GradedMultiPoly<F>(poly);
		if(gradingMap.size()>0) {
			cp.removeAllGrading();
			cp.addAllGrading(gradingMap.values());
		}
		if(maxLength.compareTo(newLength)<0) maxLength = newLength;
		if(!poly.isZero()&&!polyMap.containsValue(cp)){
			polyMap.put(index, cp);
			headTermTuple.setEntry(index, cp.getHeadTerm());
		}
	}
	/**
	 * Appends a sequence of polynomials to this
	 * operator's polynomial map
	 * @param polyIter an iterable returning a sequence of polynomials
	 */
	public void addPolynomials(Iterable<GradedMultiPoly<F>> polyIter){
		NNegInt index = polyMap.size()==0?NNegInt.ZERO:polyMap.lastKey().increment();
		boolean hasGradingMap = gradingMap.size()>0;
		for (GradedMultiPoly<F> poly:polyIter){
			GradedMultiPoly<F> newPoly = new GradedMultiPoly<F>(poly);
			if(hasGradingMap){
				newPoly.removeAllGrading();
				newPoly.addAllGrading(gradingMap.values());
			}
			NNegInt numOfInd = newPoly.getNumberOfIndeterminates(); 
			if(!newPoly.isZero()&&!polyMap.containsValue(newPoly)){
				if(maxLength.compareTo(numOfInd)<0) maxLength = numOfInd;
				polyMap.put(index, newPoly);
				//headTermTuple.setEntry(index, newPoly.getHeadTerm());
				index = index.increment();
			}
		}
		
	}
	/**
	 * Appends a sequence of gradings, <code>Grader</code> objects,
	 * to this operator
	 * @param gradingIter an <code>Iterable</code> returning the sequence
	 * of gradings
	 */
	public void addAllGrading (Iterable<Grader> gradingIter){
		NNegInt index = gradingMap.size()==0?NNegInt.ZERO:gradingMap.lastKey().increment();
		for (Grader grading:gradingIter){
			gradingMap.put(index, grading);
			index = index.increment();
		}
		if(polyMap.size()!=0){
			for (Entry<NNegInt,GradedMultiPoly<F>> entry:polyMap.entrySet()) entry.getValue().addAllGrading(gradingIter);
		}
	}
	/**
	 * Computes the Groebner basis of this operator's
	 * polynomial map
	 */
	public void computeGroebner (){
		//GrPolyOperator<F> groebner = new GrPolyOperator<F>();
		//groebner.addAllGrading(gradingMap.values());
		NNegInt index1 = NNegInt.ZERO, max = new NNegInt(polyMap.lastKey()), index = index1;
		while (index1.compareTo(max)<0){
			NNegInt index2 = index1.increment();
			GradedMultiPoly<F> poly1 = polyMap.get(index1);
			while (index2.compareTo(max)<=0){
				GradedMultiPoly<F> poly2  = polyMap.get(index2), syzygy = getSyzygy(poly1,poly2), syzygy2 = new GradedMultiPoly<F>();
				for (GradedMultiPoly<F> polys:polyMap.values()) syzygy2 = syzygy2.add(syzygy.multiply(polys));
				GradedMultiPoly<F> normal = getNormalForm(syzygy2);
				GradedIndexing<NNegInt> normalDeg = normal.getDegree();
				if(!normal.isZero()&&!normalDeg.isNeutral()) {
					groebnerBasis.put(index,normal);
					index = index.increment();
				}
				
				index2 = index2.increment();
			}
			index1 = index1.increment();
		}
		if(groebnerBasis.isEmpty()) groebnerBasis.putAll(polyMap);
	}
	/**
	 * Returns the lowest common multiple (LCM) of the head terms
	 * of the two polynomials <tt>first</tt> and <tt>second</tt>
	 * with respect to this operator's grading map
	 * @param first the first polynomial
	 * @param second the second polynomial
	 * @return the LCM
	 */
	GradedMultiPoly<F> lcm(MonoPoly<GradedIndexing<NNegInt>,F> first, MonoPoly<GradedIndexing<NNegInt>,F> second){
		return gcdLcm(first,second,false);
	}
	/**
	 * Returns the greatest common divisor (GCD) of the head terms of the
	 * two polynomials <tt>first</tt> and <tt>second</tt>
	 * @param first the first polynomial
	 * @param second the second polynomial
	 * @return the GCD
	 */
	GradedMultiPoly<F> gcd(MonoPoly<GradedIndexing<NNegInt>,F> first, MonoPoly<GradedIndexing<NNegInt>,F> second){
		return gcdLcm(first,second,true);
	}
	/**
	 * Returns either the greatest common divisor (GCD) or the lowest
	 * common multiple (LCM) for the two polynomials <tt>first</tt> and
	 * <tt>second</tt>, depending on the boolean flag <tt>computeGCD</tt>
	 * <ol><li>true - GCD</li><li>false - LCM</li></ol>
	 * @param first the first polynomial
	 * @param second the second polynomial
	 * @param computeGCD boolean flag whether or not to compute the GCD
	 * @return GCD or LCM
	 */
	GradedMultiPoly<F> gcdLcm (MonoPoly<GradedIndexing<NNegInt>,F> first, MonoPoly<GradedIndexing<NNegInt>,F> second, boolean computeGCD){
		GradedMultiPoly<F> cp1 = copyAndNewGrading(first), cp2 = copyAndNewGrading(second);
		GradedMultiPoly<F> ht1 = cp1.getHeadTerm(), ht2 = cp2.getHeadTerm();
		GradedIndexing<NNegInt> deg1 = ht1.getDegree(), deg2 = ht2.getDegree();
		NNegInt[] index = null;
		NNegInt max1 = deg1.getMaxIndex(), max2 = deg2.getMaxIndex(), max = null;
		if(max1.compareTo(max2)>0) {max = max1; index = new NNegInt[(int) max.increment().eval()];}
		else {max = max2;index = new NNegInt[(int) max.increment().eval()];}
		for (int i = 0; i <= max.eval(); i++){
			NNegInt entryIndex = new NNegInt(i);
			NNegInt val1 = deg1.getIndex(entryIndex), val2 = deg2.getIndex(entryIndex);
			if(computeGCD){if(val1!=null&&val2!=null) index[i] = val1.compareTo(val2)>0?val2:val1;}
			else{
				if(val1==null&&val2==null) continue;
				if(val1!=null&&val2!=null) index[i] = val1.compareTo(val2)>0?val1:val2;
				else index[i] = val1==null?val2:val1;
			}
		}
		return new GradedMultiPoly<F> (new GrIndex(index), ht1.getCoefficient(deg1).constructOne());
	}
	/**
	 * Returns the syzygy of the two polynomials
	 * <tt>first</tt> and <tt>second</tt>
	 * @param first the first polynomial
	 * @param second the second polynomial
	 * @return the syzygy
	 */
	GradedMultiPoly<F> getSyzygy (MonoPoly<GradedIndexing<NNegInt>,F> first, MonoPoly<GradedIndexing<NNegInt>,F> second){
		GradedMultiPoly<F> cp1 = copyAndNewGrading(first), cp2 = copyAndNewGrading(second);
		GradedMultiPoly<F> htLCM = lcm(first,second), ht1 = cp1.getHeadTerm(), ht2 = cp2.getHeadTerm();
		GradedIndexing<NNegInt> deg = htLCM.getDegree(), deg1 = ht1.getDegree();
		NNegInt max = deg.getMaxIndex(), index = NNegInt.ZERO;
		NNegInt[]  grIndex = new NNegInt[(int) max.increment().eval()];
		while(index.compareTo(max)<=0){
			NNegInt ind1 = deg.getIndex(index), ind2 = deg1.getIndex(index);
			if(ind1==null) {index = index.increment();continue;}
			if(ind2==null) grIndex[(int) index.eval()] = ind1;
			else{if(ind1.compareTo(ind2)>=0) grIndex[(int) index.eval()] = ind1.diff(ind2);}
 			index = index.increment();
		}
		return new GradedMultiPoly<F> (new GrIndex(grIndex), ht2.getCoefficient(ht2.getDegree()));
	}
	/**
	 * Returns the normal form of the polynomial
	 * <tt>poly</tt> with respect to this operator's
	 * gradings
	 * @param poly some polynomial
	 * @return the normal form
	 */
	public GradedMultiPoly<F> getNormalForm (MonoPoly<GradedIndexing<NNegInt>,F> poly){
		if(maxLength.compareTo(new NNegInt(gradingMap.size()))>0)
			throw new IllegalArgumentException ("\nInsufficient gradings in this operator\n" +
					"The number of indeterminates has to equal the number of gradings...."); 
		GradedMultiPoly<F> cp = copyAndNewGrading(poly), dummy1 = new GradedMultiPoly<F> ();
		transPolySet = new TreeSet<GradedMultiPoly<F>>(polyMap.values());
		for (Entry<NNegInt,MonoPoly<GradedIndexing<NNegInt>,F>> headEntry:headTermTuple){
			MonoPoly<GradedIndexing<NNegInt>,F> head = headEntry.getValue();
			do{
				GradedMultiPoly<F> cpHead = cp.getHeadTerm(), dummy2 = new GradedMultiPoly<F>();
				GradedMultiPoly<F> headLCM = lcm(cpHead,head), div = div(headLCM,head);
				F coeff1 = cpHead.getCoefficient(cpHead.getDegree()), coeff2 = head.getCoefficient(head.getDegree());
				F coeff3 = div.getCoefficient(div.getDegree()), coeff4 = coeff3.multiply(coeff2);
				if(div.multiply(coeff1).equals(cpHead.multiply(coeff3))) {
					dummy2 = div.multiply(coeff1.multiply(coeff3.inverse()));
					dummy1 = dummy1.add(dummy2);
					cp = cp.add(dummy2.addInverse());
					//dummy1.removeCoefficient(dummy1.getDegree());
					continue;
				}
				
				cp = cp.add(div.multiply(head.multiply(coeff4.inverse().multiply(coeff1))).addInverse());
			} while(cp.compareTo(head)>=0&&(!cp.isZero()&&!cp.isUnit()));
			cp = cp.add(dummy1);
			dummy1 = new GradedMultiPoly<F>();
		}
		/*for (GradedMultiPoly<F> nPoly:headTermTuple){
			GradedMultiPoly<F> ht = nPoly.getHeadTerm();
			if(cp.compareTo(nPoly)>0){
			GradedMultiPoly<F> gcd = gcd(cp,nPoly), cpHt = cp.getHeadTerm();
				GradedMultiPoly<F> gcdSyz = getSyzygy(gcd,cpHt);
				GradedIndexing<NNegInt> deg = cpHt.getDegree();
				GradedMultiPoly<F> scalarNPoly = nPoly.multiply(ht.getCoefficient(ht.getDegree()).inverse());
				GradedMultiPoly<F> scalarGCD   = gcdSyz.multiply(cpHt.getCoefficient(deg).addInverse()), prod = scalarGCD.multiply(scalarNPoly);
				
				cp = cp.add(prod);
				//entryCp = entryCp.add(cpHt.addInverse());
			}
		}*/
		return cp;
	}
	/*-----------------------------privates-----------------------------*/
	
	/**
	 * Returns a deep copy of the <tt>original</tt>
	 * polynomial, with the gradings set to this
	 * operator's map of gradings
	 * @param original the original polynomial
	 * @return a copy, with new gradings
	 */
	private GradedMultiPoly<F> copyAndNewGrading (MonoPoly<GradedIndexing<NNegInt>,F> original){
		GradedMultiPoly<F> cp1 = new GradedMultiPoly<F> (original);
		cp1.removeAllGrading();
		cp1.addAllGrading(gradingMap.values());
		return cp1;
	}
	private GradedMultiPoly<F> div(MonoPoly<GradedIndexing<NNegInt>,F> arg, MonoPoly<GradedIndexing<NNegInt>,F> mod){
		GradedIndexing<NNegInt> argDeg = arg.getDegree(), modDeg = mod.getDegree();
		return new GradedMultiPoly<F> (diffDeg(argDeg,modDeg),arg.getCoefficient(argDeg).multiply(mod.getCoefficient(modDeg).inverse()));
	}
	private GradedMultiPoly<F> mod(MonoPoly<GradedIndexing<NNegInt>,F> arg, MonoPoly<GradedIndexing<NNegInt>,F> mod){
		return div(arg,mod).multiply(mod).addInverse().add(arg);
	}
	private GradedIndexing<NNegInt> diffDeg(GradedIndexing<NNegInt> deg1, GradedIndexing<NNegInt> deg2){
		NNegInt max = deg1.getMaxIndex(), index = NNegInt.ZERO;
		NNegInt[]  grIndex = new NNegInt[(int) max.increment().eval()];
		while(index.compareTo(max)<=0){
			//NNegInt ind1 = deg1.getValue(index)
			NNegInt ind1 = deg1.getIndex(index), ind2 = deg2.getIndex(index);
			if(ind1==null) {index = index.increment();continue;}
			if(ind2==null) grIndex[(int) index.eval()] = ind1;
			else{if(ind1.compareTo(ind2)>=0) grIndex[(int) index.eval()] = ind1.diff(ind2);}
 			index = index.increment();
		}
		return new GrIndex(grIndex);
	}
	private void removeHeadTermsRed(){
		TreeSet<GradedMultiPoly<F>> headTree = new TreeSet<GradedMultiPoly<F>> (), redSet = new TreeSet<GradedMultiPoly<F>>();
		for (Entry<NNegInt,MonoPoly<GradedIndexing<NNegInt>,F>> headEN1:headTermTuple){
			GradedMultiPoly<F> head1 = new GradedMultiPoly<F>(headEN1.getValue());
			head1.removeAllGrading();
			head1.addAllGrading(gradingMap.values());
			if(redSet.contains(head1)) continue;
			//boolean doContinue = false;
			for (Entry<NNegInt,MonoPoly<GradedIndexing<NNegInt>,F>>headEN2:headTermTuple){
				GradedMultiPoly<F>  head2 = new GradedMultiPoly<F>(headEN2.getValue());
				if(head1.equals(head2)||redSet.contains(head2)) continue;				
				head2.removeAllGrading();
				head2.addAllGrading(gradingMap.values());
				//GradedMultiPoly<F> gcd = gcd(head1,head2);
				GradedMultiPoly<F> mod1 = mod(head1,head2), mod2 = mod(head2,head1);;
				if(mod1.isZero()||mod2.isZero()) {
					if(mod2.isZero()) headTree.add(head1);
					else headTree.add(head2);
					redSet.add(head2);
					redSet.add(head1);
					//doContinue = true;
					//break;
				} else headTree.add(headTree.contains(head1)?head2:head1);
			}
			//if(doContinue) continue;
		}
		headTermTuple.clear();
		NNegInt index = NNegInt.ZERO;
		for (GradedMultiPoly<F> heads:headTree) {
			headTermTuple.setEntry(index, heads);
			index = index.increment();
		}
	}
	void setHeadTermTuple (){
		if(polyMap.size()==0) return;
		NNegInt maxLength = new NNegInt(headTermTuple.rank());		
		for (Entry<NNegInt,GradedMultiPoly<F>>polyEntry:polyMap.entrySet()){
			GradedMultiPoly<F> residue = polyEntry.getValue();
			for (Entry<GradedIndexing<NNegInt>,F> coeffEnt:residue.getCoefficientMap().entrySet()){
				headTermTuple.setEntry(maxLength, new GradedMultiPoly<F>(coeffEnt.getKey(),coeffEnt.getValue()));
				maxLength = maxLength.increment();
			}
		}
		sortHeadTermTuple();
		removeHeadTermsRed();
	}
	private void sortHeadTermTuple (){
		TreeSet<MonoPoly<GradedIndexing<NNegInt>,F>> headTermTree = new TreeSet<MonoPoly<GradedIndexing<NNegInt>,F>>();
		for (Entry<NNegInt,MonoPoly<GradedIndexing<NNegInt>,F>> headTermEntry:headTermTuple) headTermTree.add(headTermEntry.getValue());
		headTermTuple.clear();
		NNegInt index = NNegInt.ZERO;
		for (MonoPoly<GradedIndexing<NNegInt>,F> headTerm:headTermTree) {
			headTermTuple.setEntry(index, headTerm);
			index = index.increment();
		}
	}
	private void setInnerOperator(Iterable<Grader> graderIter){
		innerOperator = new GrPolyOperator<F>();
		innerOperator.addAllGrading(graderIter);
	}
	private void setInnerOperatorPolys (){
		for (Entry<NNegInt,GradedMultiPoly<F>> entry:polyMap.entrySet()){
			NNegInt index = entry.getKey();
			GradedMultiPoly<F> poly = entry.getValue();
			MonoPoly<GradedIndexing<NNegInt>,F> htForPoly = null;
			if((htForPoly = headTermTuple.getValue(index))!=null){
				innerOperator.addPolynomial(poly.add(htForPoly.addInverse()));
			} else {
				htForPoly = poly.getHeadTerm();
				innerOperator.addPolynomial(poly.add(htForPoly.addInverse()));
				headTermTuple.setEntry(index, htForPoly);
			}
		}
	}
	public static void main(String[] args){
		NNegInt three = NNegInt.TWO.increment(), four = three.increment();
		NNegInt[][] indexMat1 = new NNegInt[][]{
				{       four,NNegInt.ONE},//monom X_0^4 X_1
				{NNegInt.TWO,NNegInt.TWO}//monom X_0^2 X_1^2
				//{NNegInt.ONE,       null},//monom X_0
				//{       null,NNegInt.ONE} //monom       X_1^3
		};
		Rational two = new Rational(2,1), rthree = new Rational(3,1);
		Rational[] coeff1 = new Rational[]{
			Rational.ONE,new Rational(5,1),rthree.addInverse(),two	
		};
		GradedMultiPoly<Rational> poly1 = new GradedMultiPoly<Rational>(indexMat1,coeff1);
		NNegInt[][] indexMat2 = new NNegInt[][]{
				{NNegInt.ONE,       four},//monom X_0   X_1^4
				{NNegInt.TWO,      three},//monom X_0^2 X_1^3
				{      three,NNegInt.TWO},//monom X_0^3 X_1^2
				{       four,       null},//monom X_0^4
				{NNegInt.TWO,NNegInt.ONE},//monom X_0^2 X_1
				{NNegInt.ONE,NNegInt.ONE},//monom X_0   X_1
				{NNegInt.ONE,NNegInt.TWO},//monom X_0   X_1^2
				{       null,NNegInt.TWO} //monom       X_1^2
		};
		Rational[] coeff2 = new Rational[]{
			Rational.ONE,two,Rational.M_ONE,new Rational(4,1), new Rational(6,1), rthree,two,new Rational(9,1)
		
		};
		GradedMultiPoly<Rational> poly2 = new GradedMultiPoly<Rational>(indexMat2,coeff2);
		GradedMultiPoly<Rational> poly3 = poly1.multiply(new GradedMultiPoly<Rational>(new GrIndex(new NNegInt[]{null,three}),Rational.ONE)).
					add(poly2.multiply(new GradedMultiPoly<Rational>(new GrIndex(new NNegInt[]{three,null}),Rational.ONE)));
		GrPolyOperator<Rational> polyOp = new GrPolyOperator<Rational>();
		TreeSet<Grader> ar = new TreeSet<Grader> ();
		ar.add(new Grader(new FIntM(new NNegInt[]{NNegInt.ONE,NNegInt.ONE})));
		ar.add(new Grader(new FIntM(new NNegInt[]{NNegInt.ONE,NNegInt.TWO})));		
		polyOp.addAllGrading(ar);
		polyOp.addPolynomial(poly1);
		polyOp.addPolynomial(poly2);
		//polyOp.setInnerOperator(ar);
		//polyOp.addPolynomial(poly3);
		//polyOp.computeGroebner();
		polyOp.setHeadTermTuple();
		GradedMultiPoly<Rational> normal = polyOp.getNormalForm(poly3); 
		System.out.println(String.format("normalForm(%1$s,%2$s) = \n%3$s",poly3,polyOp.gradingMap,normal));
		System.out.println(String.format("headTermTuple(%1$s, %2$s) = \n%3$s",poly1,poly2,polyOp.headTermTuple));
	}
}
