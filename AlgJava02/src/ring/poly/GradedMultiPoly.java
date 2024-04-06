package ring.poly;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import module.integer.FIntD;
import module.integer.FIntM;

import ring.grading.GrIndex;
import ring.grading.GradedIndexing;
import ring.grading.Grader;
import ring.integer.IntRing;
import field.AbstractField;
import field.Rational;
import group.NNegInt;
/**
 * The class of polynomials with index grading - 
 * a dual element mapping an tuple of non-negative
 * integers to some integer. All coefficients are
 * instances of a sub-class <tt>&ltF&gt</tt> of {@link AbstractField}
 * @author adin
 *
 * @param <F> the type of the underlying field
 */
public class GradedMultiPoly<F extends AbstractField<F>> extends
		MonoPoly<GradedIndexing<NNegInt>, F> implements Comparable<MonoPoly<GradedIndexing<NNegInt>,F>>{//,
		//Module<GradedMultiPoly>{//,
		//CommRing<GradedMultiPoly<F>>{
		//MonoPoly<GrIndex,F>{
	
	/**the grading map*/
	private TreeMap<NNegInt,Grader> graderMap;
	/**the grading map's iterator*/
	private GraderIter graderIterator; 
	/**the current grading, determined by the
	 * grading set iterator's <tt>next()</tt> method*/
	private Grader currentGrading;
	/**the maximal length of all graded indices:
	 * the highest index in all indices of this
	 * polynomial - the number of indeterminates/variables*/
	private NNegInt maxLength;
	/**
	 * Constructs the zero object
	 */
	public GradedMultiPoly() {
		super();
		graderMap = new TreeMap<NNegInt,Grader>();
		maxLength = NNegInt.ZERO;
	}
	/**
	 * Constructs the monomial <tt>coeff * x_1^t_1*...*x_n^t_n</tt>
	 * where the powers <tt>t_i</tt> are specified by the argument
	 * <tt>index</tt>
	 * @param index the index
	 * @param coeff the coefficient
	 */
	public GradedMultiPoly(GradedIndexing<NNegInt> index, F coeff){
		this();
		setCanonicalGrader(index.getMaxIndex());
		setCoefficient(index,coeff);
	}
	/**
	 * Constructs a polynomial defined by the
	 * entries return by the argument <tt>iter</tt>
	 * @param iter some <code>Iterable</code> object
	 */
	public GradedMultiPoly(Iterable<Entry<GradedIndexing<NNegInt>,F>> iter){
		this();
		for (Entry<GradedIndexing<NNegInt>,F> entry:iter) {
			GradedIndexing<NNegInt> index = entry.getKey();
			if(maxLength.compareTo(index.getMaxIndex())<0) maxLength = index.getMaxIndex();
		}
		setCanonicalGrader(maxLength);
		for (Entry<GradedIndexing<NNegInt>,F> entry:iter){
			setCoefficient(entry.getKey(),entry.getValue());
		}
	}
	/**
	 * Constructs a polynomial with all indices
	 * specified by the argument <tt>matrixOfIndices</tt>,
	 * where each inner array <tt>matrixOfIndices[i]</tt>
	 * serves as index and their respective coefficient
	 * is specified by the argument array entry <tt>coeffArray[i]</tt>.
	 * <p><b>Note</b>, if the outer length of the first argument
	 * does not equal the length of the second, all further entries
	 * are omitted
	 * @param matrixOfIndices the indicial matrix
	 * @param coeffArray the coefficient array
	 */
	public GradedMultiPoly (NNegInt[][] matrixOfIndices, F[] coeffArray){
		this();
		int lengthOfIndices = matrixOfIndices.length, lengthOfCoeff = coeffArray.length;
		boolean moreIndices = lengthOfIndices>lengthOfCoeff;
		int minLength = moreIndices?lengthOfCoeff:lengthOfIndices;
		for (int i = 0; i < minLength;i++){
			if(matrixOfIndices[i]!=null&&coeffArray[i]!=null) {
				GrIndex index = new GrIndex(matrixOfIndices[i]);
				setCoefficient(index,coeffArray[i]);
				if(maxLength.compareTo(index.getMaxIndex())<0) maxLength = index.getMaxIndex();
			}
		}
		setCanonicalGrader(maxLength);
	}
	/**
	 * Constructs a polynomial specified by the argument
	 * <tt>coeffMap</tt>
	 * @param coeffMap some map with indices as keys and coefficients
	 * as values
	 */
	public GradedMultiPoly(Map<GradedIndexing<NNegInt>, F> coeffMap){
		this(coeffMap.entrySet());
	}
	/**
	 * Constructs a polynomial by copying the argument
	 * <tt>poly</tt>
	 * @param poly some polynomial to copy
	 */
	public GradedMultiPoly(MonoPoly<GradedIndexing<NNegInt>,F> poly){
		this(poly.getCoefficientMap());
	}
	/**
	 * Overriding the super class's method - major difference
	 * to super class: adding all gradings of the left argument (this)
	 * to the sum
	 */
	public GradedMultiPoly<F> add(MonoPoly<GradedIndexing<NNegInt>,F> another){
		GradedMultiPoly<F> sum = new GradedMultiPoly<F> (super.add(another));
		sum.addAllGrading(graderMap.values());
		return sum;
	}
	/**
	 * Overriding the super class's method - major difference
	 * to super class: adding all gradings to the sum
	 */
	public GradedMultiPoly<F> addInverse(){
		GradedMultiPoly<F> inv = new GradedMultiPoly<F> (super.addInverse());
		inv.addAllGrading(graderMap.values());
		return inv;
	}
	/**
	 * Adds the grading <tt>grading</tt> to this polynomial
	 * @param grading some grading
	 */
	public void addGrading(Grader grading){
		if(graderIterator!=null) graderIterator = null;
		graderMap.put(
				graderMap.size()==0?NNegInt.ZERO:graderMap.lastKey().increment(),
						grading);
		graderIterator = new GraderIter();
	}
	/**
	 * Adds all grading objects in the 
	 * iterable argument <tt>gradingSet</tt>
	 * @param gradingSet some iterable 
	 */
	public void addAllGrading(Iterable<Grader> gradingSet){
		graderIterator = null;
		NNegInt index = graderMap.size()==0?NNegInt.ZERO:graderMap.lastKey().increment();
		for (Grader grader:gradingSet) {
			graderMap.put(index, grader);
			index = index.increment();
			
		}
		graderIterator = new GraderIter();
	}
	/**
	 * Exchanges the current grading object
	 * <b>Note</b>, if the iterator over the
	 * grading object set is exhausted, no
	 * exchange is performed
	 */
	public void changeGrading (){
		if(currentGrading==null) {
			graderIterator = new GraderIter();		
		}
		if(graderIterator.hasNext()) currentGrading = graderIterator.next();
	}
	/**
	 * Comparison method: returns some negative integer
	 *  if the grading system of <b>this</b> returns a negative
	 *  integer for all remaining head term indices
	 *  <p><tt>ht_i = (rem_(i-1).add(ht_(i-1).addInverse()).getHeadTerm()</tt>, for this and
	 *  <tt>another</tt>
	 *  <p>some positive integer in the opposite case or zero.
	 *  <b>Note</b>, that only the left hand argument determines
	 *  the grading system - hence, <p><tt>this.compareTo(another)==-another.compareTo(this)</tt>
	 *  <p><b>does not</b> necessarily return true. Although, this
	 *  method conforms to the convention <tt>this.equals(another)</tt> returns true,
	 *  then <tt> this.compareTo(another)==0</tt> returns true
	 */
	public int compareTo(MonoPoly<GradedIndexing<NNegInt>,F> another){
		if(this==another||equals(another)) return 0;
		GradedMultiPoly<F> cp = new GradedMultiPoly<F> (another);
		cp.addAllGrading(graderMap.values());
		GradedMultiPoly<F> ht1 = getHeadTerm(), ht2 = cp.getHeadTerm();
		GradedMultiPoly<F> rem1 = this, rem2 = new GradedMultiPoly<F>(another);
		//Iterator<Entry<GradedIndexing<NNegInt>,F>> it1 = iterator(), it2 = cp.iterator();
		while(!rem1.isZero()&&!rem2.isZero()){
			GradedIndexing<NNegInt> leftIndex = ht1.getDegree(), rightIndex = ht2.getDegree();
			graderIterator = new GraderIter();
			while(graderIterator.hasNext()) {
				currentGrading = graderIterator.next();
				currentGrading.f(leftIndex);
				IntRing val1 = currentGrading.getValue();
				currentGrading.f(rightIndex);
				IntRing val2 = currentGrading.getValue();
				int valComp = val1.compareTo(val2);
				if(valComp!=0) return valComp;
			}
			rem1 = rem1.add(ht1.addInverse());
			rem2 = rem2.add(ht2.addInverse());
			ht1  = rem1.getHeadTerm();
			ht2  = rem2.getHeadTerm();
		}
		return 0;
	}
	/**
	 * Returns the index of highest value determined
	 * by the current grading object
	 */
	public GradedIndexing<NNegInt> getDegree(){
		GrIndex index = null;
		if(currentGrading==null) {
			graderIterator = new GraderIter();
			if(graderIterator.hasNext()) currentGrading = graderIterator.next();
			else return GrIndex.ZERO;
		}
		//currentGrading = grIterator.next();
		long eval1 = 0;
		for (Entry<GradedIndexing<NNegInt>,F> entry:this){
			GrIndex oIndex = new GrIndex(entry.getKey());
			oIndex.setGrader(currentGrading);
			if(index==null) {
				index = oIndex;
				eval1 = index.eval();
			}
			else{
				long eval2 = oIndex.eval();
				if(eval1<eval2) {
					index = oIndex;
					eval1 = eval2;
				}
			}
			
		}
		return index;
	}
	/**
	 * Returns the grading degree of the given
	 * argument <tt>index</tt> for the current grading
	 * object in this polynomial
	 * @param index some index
	 * @return the grading degree
	 */
	public IntRing getGradingDegree(GradedIndexing<NNegInt> index){
		if(currentGrading==null) changeGrading();
		currentGrading.f(index);
		return currentGrading.getValue();
	}
	/**
	 * Returns the highest term with respect to
	 * the gradings present in this polynomial
	 * <p><b>Note</b>, that the coefficient is not
	 * removed so technically speaking the returned
	 * object is equivalent to <tt>hc(this) * ht(this)</tt> -
	 * the product of head term and head coefficient.
	 * To get the actual head term, simply call
	 * <tt><p>GradedMultiPoly&ltF&gt ht = getHeadTerm();
	 * <p>ht = ht.multiply(ht.getCoefficient(ht.getDegree()).inverse())</tt>
	 * @return the head term
	 */
	public GradedMultiPoly<F> getHeadTerm (){
		GradedMultiPoly<F> ht = new GradedMultiPoly<F> (this);
		ht.addAllGrading(graderMap.values());
		ht.graderIterator = ht.getIterator();
		while (ht.graderIterator.hasNext()){
			//currentGrading = graderIterator.next();
			ht.currentGrading = ht.graderIterator.next();
			GradedMultiPoly<F> ht1 = ht.getInitialForm();
			if(ht1.getCoefficientMap().size()<=1) {ht = ht1; break;}
			//ht.graderIterator.remove();
			//ht1.removeGrading(ht.currentGrading);
			//if(ht1.equals(ht)) break;
			ht = ht1;
		}
		ht.graderMap.clear();
		ht.addAllGrading(graderMap.values());
		return ht;
	}
	/*
	public HashSet<GradedMultiPoly<F>> getHeadTermDecomposition(){
		HashSet<GradedMultiPoly<F>> headTerms = new HashSet<GradedMultiPoly<F>>();
		GradedMultiPoly<F> cp = new GradedMultiPoly<F> (this);
		cp.addAllGrading(graderMap.values());
		while(!cp.isZero()) {
			
		}
		return headTerms;
	}*/
	/**
	 * Returns the homogeneous part
	 * of this polynomial (the sum of all monomials
	 * with grading degree equal to <tt>degree</tt>, possibly
	 * the zero polynomial)
	 * <p><b>Note</b>, that the returned polynomial has the same
	 * grading map as this, except for the current grading object
	 * (doesn't make sense keeping it since all monomials in the returned
	 * polynomial are of the same grading degree) 
	 * @param degree the grading degree
	 * @return the homogeneous part of the given grading degree
	 */
	public GradedMultiPoly<F> getHomogeneousForm(IntRing degree){
		GradedMultiPoly<F> homo = new GradedMultiPoly<F> ();
		if(currentGrading==null) changeGrading();
		for (Entry<GradedIndexing<NNegInt>,F> entries:this){
			GradedIndexing<NNegInt> index = entries.getKey();
			currentGrading.f(index);
			if(currentGrading.getValue().equals(degree)){
				homo.setCoefficient(index, entries.getValue());
			}
		}
		homo.addAllGrading(graderMap.values());
		homo.removeGrading(currentGrading);
		return homo;
	}
	/**
	 * Returns a hash set of all homogeneous monomials with grading
	 * degree equal to <tt>degree</tt>, possibly empty
	 * <p><b>Note</b> summing over all entries in the set
	 * returns a polynomial equal to {@link GradedMultiPoly#getHomogeneousForm(IntRing)}
	 * 
	 * @param degree the grading degree
	 * @return a set of all initial monomials of the given degree
	 */
	public HashSet<GradedMultiPoly<F>> getHomogeneousMonomials(IntRing degree){
		HashSet<GradedMultiPoly<F>> homoSet = new HashSet<GradedMultiPoly<F>> ();
		if(currentGrading==null) changeGrading();
		for (Entry<GradedIndexing<NNegInt>,F> entries:this){
			GradedIndexing<NNegInt> index = entries.getKey();
			currentGrading.f(index);
			if(currentGrading.getValue().equals(degree)){
				GradedMultiPoly<F> iniMono = new GradedMultiPoly<F>(index, entries.getValue());
				iniMono.addAllGrading(graderMap.values());
				iniMono.removeGrading(currentGrading);
				homoSet.add(iniMono);
				
			}
		}
		return homoSet;
	}
	/**
	 * Returns the homogeneous part of this polynomial
	 * of highest grading degree with respect to the
	 * current grading
	 * @return the homogeneous part of this polynomial of highest degree
	 */
	public GradedMultiPoly<F> getInitialForm (){
		if(currentGrading==null) changeGrading();
		currentGrading.f(getDegree());
		return getHomogeneousForm(currentGrading.getValue());
	}
	/**
	 * Returns the number of indeterminates (or
	 * variables) of this polynomial
	 * @return the number of variables
	 */
	public NNegInt getNumberOfIndeterminates (){
		return maxLength;
	}
	
	/**
	 * Returns true if there is no
	 * iterator attached to the grading set
	 * or the iteration isn't exhausted
	 * @return 
	 */
	public boolean hasMoreGrading (){
		if(graderIterator==null) return true;
		return graderIterator.hasNext();
	}
	/**
	 * Returns true if there's some 
	 * iterator attached to the grading set
	 * @return true if there is some iterator
	 */
	public boolean hasGradingIterator (){
		return graderIterator==null?false:true;
	}
	/*
	 * Returns the residual element: <tt>this modulo another</tt>,
	 * where the returned object is less than the right hand argument
	 * with respect to <b>this</b> object's grading system.
	 * @param another
	 * @return
	
	public GradedMultiPoly<F> mod(GradedMultiPoly<F> another){
		if(compareTo(another)<0) return new GradedMultiPoly<F> (this);
		GradedMultiPoly<F> cp1 = new GradedMultiPoly<F> (this), cp2 = new GradedMultiPoly<F> (another);
		cp1.addAllGrading(graderMap.values());
		cp2.addAllGrading(graderMap.values());
		while(cp1.compareTo(cp2)>=0){
			cp2.graderIterator = cp2.getGraderIterator();
			while(cp2.graderIterator.hasNext()){
				cp2.currentGrading = cp2.graderIterator.next();
				GradedMultiPoly<F> ht2 = cp2.getHeadTerm(), ht1 = cp1.getHeadTerm();
				//cp2.currentGrading = 
				GradedIndexing<NNegInt> deg1 = ht1.getDegree(), deg2 = ht2.getDegree();
				NNegInt maxIndex1 = deg1.getMaxIndex(), maxIndex2 = deg2.getMaxIndex();
				NNegInt maxIndex  = maxIndex1.compareTo(maxIndex2)>=0?maxIndex1:maxIndex2, index = NNegInt.ZERO;
				NNegInt[] degAr = new NNegInt[(int) maxIndex.getValue().eval()+1];
				
				while (index.compareTo(maxIndex)<=0 ){
					NNegInt left = deg1.getIndex(index), right = deg2.getIndex(index);
					if(left!=null&&right!=null){
						if(left.compareTo(right)>=0) degAr[(int) index.eval()] = left.diff(right);
					}
					if(left!=null&&right==null) degAr[(int) index.eval()] = left;
					index = index.increment();
				}
				cp1 = cp1.add(ht2.addInverse().multiply(new GradedMultiPoly<F>(new GrIndex(degAr),null)));
				if(cp1.compareTo(cp2)<0) break;
			}
		}
		return cp1;
	}*/
	/**
	 * Overriding the super class's method - major difference
	 * to the super class: all grading objects are added to the
	 * scalar product
	 */
	public GradedMultiPoly<F> multiply(F scalar){
		GradedMultiPoly<F> scl = new GradedMultiPoly<F>(super.multiply(scalar));
		scl.addAllGrading(graderMap.values());
		return scl;
	}
	/**
	 * Overriding the super class's method - major difference
	 * to the super class: all grading objects of the left argument
	 * (this) are added to the product
	 */
	public GradedMultiPoly<F> multiply(MonoPoly<GradedIndexing<NNegInt>,F> another){
		GradedMultiPoly<F> prod = new GradedMultiPoly<F>(super.multiply(another));
		prod.addAllGrading(graderMap.values());
		return prod;
	}
	/**
	 * Removes all grading objects from this
	 * polynomial
	 */
	public void removeAllGrading(){
		graderMap.clear();
	}
	/**
	 * Removes the specified <tt>grading</tt>
	 * from this polynomial and returns true
	 * only on success
	 * @param grading the grading to remove
	 * @return true on success
	 */
	public boolean removeGrading (Grader grading){
		graderIterator = new GraderIter();
		while(graderIterator.hasNext()) {
			Grader cuGrad = graderIterator.next();
			if(cuGrad.equals(grading)) {
				graderIterator.remove();
				return true;
			}
		}
		return false;
	}
	/**
	 * Sets the coefficient specified by the arguments
	 * <tt>index</tt> and <tt>coeff</tt>
	 */
	public void setCoefficient(GradedIndexing<NNegInt> index, F coeff){
		GrIndex nIndex = new GrIndex(index);
		if(currentGrading!=null) nIndex.setGrader(currentGrading);
		if(maxLength.compareTo(index.getMaxIndex())<0) maxLength = index.getMaxIndex();
		super.setCoefficient(nIndex, coeff);
	}
	public String toString (){
		if(isZero()) return "0";
		String plus = " + ", x = " X", underscore = "_", pow = "^", form = "%1$s%2$s%3$s%4$s%5$s";
		StringBuilder sb = new StringBuilder();
		Iterator<Entry<GradedIndexing<NNegInt>,F>> it = iterator();
		while(it.hasNext()){
			Entry<GradedIndexing<NNegInt>,F> entry = it.next();
			GradedIndexing<NNegInt> index = entry.getKey();
			if(index.length()==0) sb.append(entry.getValue().toString());
			else{
				StringBuilder sb2 = new StringBuilder ();
				for (Entry<NNegInt,NNegInt> indexEntry:index)
					sb2.append(String.format(form, x,underscore,indexEntry.getKey().toString(),pow,indexEntry.getValue().toString()));
				sb.append(entry.getValue().toString());
				sb.append(sb2);
			}
			if(it.hasNext()) sb.append(plus); 
		}
		return sb.toString();
	}
	/*---------------------privates---------------------*/
	private GraderIter getIterator(){return new GraderIter();}
	/**
	 * Sets the canonical grading - 
	 * (to the dual element <tt>sum e^*_i</tt>)
	 * @param maxLength
	 */
	private void setCanonicalGrader (NNegInt maxLength){
		NNegInt counter = NNegInt.ZERO;
		FIntD dual = new FIntD();
		while (counter.compareTo(maxLength)<=0){
			dual.setEntry(counter, IntRing.ONE);
			counter = counter.increment();
		}
		Grader first = new Grader(dual);
		graderMap.put(graderMap.size()==0?NNegInt.ZERO:graderMap.lastKey().increment(),first);
	}
	/*------------------inner class------------------*/
	/**
	 * The grader iterator class - wraps the set view iterator
	 * of the outer class's grading map
	 * @author adin
	 *
	 */
	private class GraderIter implements Iterator <Grader> {
		/**zhe set view iterator*/
		private Iterator<Entry<NNegInt,Grader>> entryIterator;
		/**Constructs an iterator with all
		 * grading objects returned in the 
		 * same fashion they were inserted*/
		GraderIter(){entryIterator = graderMap.entrySet().iterator();}
		
		public boolean hasNext() {return entryIterator.hasNext();}

		
		public Grader next() {return entryIterator.next().getValue();}

		
		public void remove() {entryIterator.remove();}
		
	}
	public static void main (String[] args){
		NNegInt four = NNegInt.TWO.operate(NNegInt.TWO), three = NNegInt.TWO.operate(NNegInt.ONE);
		NNegInt[][] indexMat = new NNegInt[][]{
				{NNegInt.ONE,four},
				{NNegInt.TWO,three},
				{three,NNegInt.TWO},
				{four,null},
				{NNegInt.TWO,NNegInt.ONE},
				{NNegInt.ONE,NNegInt.ONE},
				{NNegInt.ONE,NNegInt.TWO},
				{null,NNegInt.TWO}
		};
		int length = indexMat.length;
		GradedMultiPoly<Rational> poly = new GradedMultiPoly<Rational>();
		for (int i = 0; i < length; i++){
			poly.setCoefficient(new GrIndex(indexMat[i]), Rational.ONE);
		}
		poly.setCanonicalGrader(NNegInt.ONE);
		poly.addGrading(new Grader(new FIntM(indexMat[6])));
		System.out.println(String.format("p = \n%1$s\ninif(p) = \n%2$s",poly,poly.getInitialForm()));
		System.out.println(String.format("p = \n%1$s\nht(p) = \n%2$s",poly,poly.getHeadTerm()));
	}
	
}
