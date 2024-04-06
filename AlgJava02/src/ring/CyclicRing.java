package ring;

import ring.integer.IntRing;
import topo.relation.EquiRel;
import topo.relation.PairedElement;
import topo.relation.Relation;
import group.CompMonoid;
import group.UnitGroup;
/**
 * The class of quotient ring elements of type <tt>Z/nZ =: Z_n</tt>
 * for some positive interger <tt>n</tt>
 * @author adin
 *
 */
public class CyclicRing extends UnitaryCommRing<CyclicRing> implements CompMonoid<CyclicRing>{
	/*--------------------statics--------------------*/
	/*------------constant class variables-----------*/
	/**
	 * the zero element of <tt>Z/(0) ~= Z</tt>
	 */
	public static final CyclicRing Z_RING = new CyclicRing (0,0);
	/**
	 * the zero ring <tt>Z/Z ~= (0)</tt> 
	 */
	public static final CyclicRing ZERO_RING = new CyclicRing (0,1);
	/**
	 * the diagonal of <tt>Z x Z</tt> - the equivalence relation on <code>IntRing</code>
	 * <tt>x ~ y</tt> if and only <tt>x.equals(y)</tt> returns true 
	 */
	public static final IdealRelation INT_DIAGONAL = 
			new IdealRelation (Z_RING){
				public boolean isRelated (IntRing first, IntRing second){return first.equals(second);}
		};
	/**
	 * the equivalence class <tt>0</tt> of <tt>(0)</tt> - 
	 * all element in <code>IntRing</code> are equal
	 */
	public static final IdealRelation ZERO_CLASS = 
			new IdealRelation (Z_RING){
				public boolean isRelated (IntRing first, IntRing second){return first.equals(second);}
			};
	/**
	 * Returns an array of all <tt>mod</tt> equivalence classes
	 * except for
	 * <ol><li><tt>mod==0</tt> is true, it returns <code>INT_DIAGONAL</code> as only array entry</li>
	 * <li><tt>mod==1</tt> is true, it returns <code>ZERO_CLASS</code> as only array entry</li></ol>
	 * @param mod the modulo operator
	 * @return the array of equivalence classes
	 */
	public static final IdealRelation[] getCoSets (int mod){
			return getCoSets(new IntRing(mod));
	}
	/**
	 * Returns an array of all <tt>mod</tt> equivalence classes
	 * except for
	 * <ol><li><code>mod.isZero()</code> is true, it returns <code>INT_DIAGONAL</code> as only array entry</li>
	 * <li><code>mod.equals(IntRIng.ONE)</code>: returns <code>ZERO_CLASS</code> as only array entry</li></ol>
	 * @param mod the modulo operator
	 * @return the array of equivalence classes
	 */
	public static final IdealRelation[] getCoSets (IntRing mod){
		mod = mod.abs();
		if(mod.compareTo(IntRing.ZERO)<0)
			//throw new IllegalArgumentException ("\nOnly non-negative integers supported");
			mod = mod.abs();
		if(mod.isZero())
			return new IdealRelation[]{INT_DIAGONAL};
		if(mod.equals(IntRing.ONE)) {
			return new IdealRelation[]{ZERO_CLASS};
		}
		IdealRelation[] relations = new IdealRelation[(int) mod.eval()];
		for (int i = 0; i < relations.length; i++)
			relations[i] = new IdealRelation (
					new CyclicRing(new IntRing(i),mod)); 
		return relations;
	}
	/*------------------nested classes--------------*/
	/**
	 * The class of all equivalence classes in <tt>Z/(n)</tt>
	 * @author adin
	 *
	 */
	public static class IdealRelation implements EquiRel<IntRing,IdealInU<IntRing>> {
		/**the coset*/
		private final IntIdealCoSet idealCoSet;
		/**the base point: <tt>(representative,modulo)</tt>*/
		private final PairedElement<IntRing> basePoint;
		/**
		 * COnstructs an equivalence class
		 * @param base the base point
		 */
		protected IdealRelation (CyclicRing base){
			idealCoSet = new IntIdealCoSet(base);
			basePoint = new PairedElement <IntRing> (base.value,base.mod);
		}
		/**
		 * Returns the base point
		 */
		public PairedElement<IntRing> basePoint() {
			return basePoint;
		}
		/**
		 * Returns the inverse relation
		 */
		public Relation<IntRing> inverse() {
			final IdealRelation id = this;
			return new Relation<IntRing> (){
				private final PairedElement<IntRing> base = new
						PairedElement<IntRing> (id.basePoint.getFirst().addInverse(),id.idealCoSet.cycle.mod);
				/**Returns the base point*/
				public PairedElement<IntRing> basePoint() {return base;}
				/**Returns a reference to the outer relation*/
				public Relation<IntRing> inverse() {return id;}
				/**Returns true*/
				public boolean isClosed() {return true;}
				/**
				 * Returns true only if the outer relation returns
				 * false for both elements
				 * @param first first element to test
				 * @param second second element
				 * @return true if both are not related by the outer relation
				 */
				public boolean isRelated(IntRing first, IntRing second) {
					return !id.isRelated(first, second);
				}

			};
		}

		/**
		 * Returns always true
		 */
		public boolean isClosed() {
			return true;
		}

		/**
		 * Returns true only if both elements belong
		 * to this same equivalence class
		 */
		public boolean isRelated(IntRing first, IntRing second) {

			return idealCoSet.contains(first)&&idealCoSet.contains(second);
		}

		/**
		 * Returns the coset
		 */
		public IntIdealCoSet equiClass(IntRing element) {
			return idealCoSet;
		}

	}
	/**
	 * The annihilor ideal of <tt>Z/(n)</tt>
	 * @author adin
	 *
	 */
	public static class AnnihilatorIdeal extends IdealInU<CyclicRing> {
		public static final AnnihilatorIdeal ZERO_IDEAL = 
				new AnnihilatorIdeal(Z_RING);
		
		private final IntIdealCoSet inModulator;
		protected AnnihilatorIdeal (CyclicRing cyclic) {
			inModulator = new IntIdealCoSet (cyclic);
		}
		public boolean contains(CyclicRing element) {
			if (inModulator.cycle.mod.equals(element.mod)) {
				if (inModulator.contains(element.value)) return true;
				if (inModulator.cycle.mod.mod(element.value).equals(IntRing.ZERO))
					return true;
				return false;
			}

			return false;
		}

	}
	/**
	 * The integer ideal set class - any modulo operator
	 * defines an ideal in <tt>Z</tt> 
	 * @author adin
	 *
	 */
	public static class IntIdealCoSet extends IdealInU<IntRing> {
		/**the cycle element defining
		 * the ideal*/
		private final CyclicRing cycle;
		/**
		 * 
		 * @param cycle
		 */
		protected IntIdealCoSet (CyclicRing cycle){super();this.cycle = new CyclicRing(cycle);}
		public boolean contains(IntRing element) {
			return element.mod(cycle.mod).equals(cycle.value);
		}


	}

	/**the value = original value modulo  mod*/
	private IntRing value;
	/**the mod*/
	private IntRing   mod;
	/**
	 * Constructs an empty element - 
	 * only for sub-classes intended
	 */
	protected CyclicRing (){super();}
	/**
	 * Constructs the zero element <tt>0 modulo mod</tt>
	 * @param mod the modulo operator
	 */
	public CyclicRing (int mod){this(new IntRing(mod));}
	/**
	 * Constructs the zero element <tt>0 modulo mod</tt>
	 * @param mod the modulo operator
	 */
    public CyclicRing (IntRing mod) {
    	this();
    	if(mod!=null&&!mod.isZero()) 
    		this.mod = mod.abs();
    	else this.mod = IntRing.ZERO;
    }
    /**
	 * Constructs the element <tt>value modulo mod</tt>
	 * @param value the value
	 * @param mod the modulo operator
	 */
    public CyclicRing (IntRing value, IntRing mod){this(mod);setValue(value);}
    /**
	 * Constructs the element <tt>value modulo mod</tt>
	 * @param value the value
	 * @param mod the modulo operator
	 */
    public CyclicRing (int value, int mod){this(mod);setValue(value);}
    /**
	 * Constructs a copy of <tt>another</tt>
	 * @param another the original
	 */
    public CyclicRing (CyclicRing another){this(another.value,another.mod);}
    /**
     * Returns the additive inverse
     */
	public CyclicRing addInverse() {return new CyclicRing (value.add(mod.addInverse()),mod);}
	/**
	 * Returns true if this element is a unit - to specify
	 * <code>getValue().gcd(getMod()).equals(IntRing.ONE</code> returns true
	 */
	public boolean isUnit(){return value.isZero()?false:value.gcd(mod).equals(IntRing.ONE)?true:false;}
	/**
	 * Returns true if this equals zero
	 */
	public boolean isZero() {return value==null?true:value.isZero;}
	/**
	 * Returns the value of this element
	 * @return the value
	 */
	public IntRing getValue (){return value;}
	/**
	 * Returns the modulo operator
	 * @return modulo operator
	 */
	public IntRing getMod   (){return mod;}
	/**
	 * Returns the one element
	 */
	public CyclicRing getOne (){return new CyclicRing(IntRing.ONE,mod);} 
	/**
	 * Returns the product <tt>this * another</tt>
	 * <p><b>Note</b> this method throws an
	 * exception for different modulo operators
	 * @throws IllegalArgumentException if <code>getMod().equals(another.getMod())</code> returns false
	 */
	public CyclicRing multiply(CyclicRing another) throws IllegalArgumentException{
		if(!mod.equals(another.mod)) throw new IllegalArgumentException ("\nDiffering mod operators... \nmod1 = "+
				mod+"\tmod2 = "+another.mod);
		return new CyclicRing (value.multiply(another.value),mod);
	}
	/**
	 * Returns the sum <tt>this + another</tt>
	 * <p><b>Note</b> this method throws an
	 * exception for different modulo operators
	 * @throws IllegalArgumentException if <code>getMod().equals(another.getMod())</code> returns false
	 */
	public CyclicRing add(CyclicRing another) throws IllegalArgumentException {
		if(!mod.equals(another.mod)) throw new IllegalArgumentException ("\nDiffering mod operators... \nmod1 = "+
				mod+"\tmod2 = "+another.mod);
		return new CyclicRing (value.add(another.value),mod);
	}
	/**
	 * Sets the value of this element to <tt>value modulo mod</tt>
	 * @param value the value
	 */
	private void setValue (int value){setValue(new IntRing(value));}
	/**
	 * Sets the value of this element to <tt>value modulo mod</tt>
	 * @param value the value
	 */
	private void setValue (IntRing value){
		if(value!=null){
			if(mod.isZero()) this.value = value;
			if(mod.equals(IntRing.ONE)) this.value = IntRing.ZERO;
			else this.value = value.mod(mod);
		} 
	}
	/**
	 * Returns true if both element are equal
	 */
	public boolean equals(CyclicRing another) {
		if(this==another) return true;
		if(!mod.equals(another.mod)) return false;
		return value.equals(another.value)?true:false;
	}
	/**
	 * Returns true if both element are equal
	 */
	public boolean equals (Object o){
		
		if(this==o) return true;
		if(!(o instanceof CyclicRing)) return false;
		return equals((CyclicRing) o);
	}
	/**
	 * Returns:
	 * <ol><li><code>getMod().compareTo(arg0.getMod())</code>, if the returned value is not zero</li>
	 * <li><code>getValue().compareTo(arg0.getValue())</code>, otherwise</li></ol> 
	 */
	public int compareTo(CyclicRing arg0) {
		int mdComp = mod.compareTo(arg0.mod);
		if(mdComp!=0) return mod.compareTo(arg0.mod);//throw new IllegalArgumentException ("Compare 2 only applicable to numbers of same modulo operator:\nthis mod = "+mod.toString()+"\targ0 mod = "+arg0.mod);
		return value.compareTo(arg0.value);		
	}
	/**
	 * Returns this element as a unit group
	 * element only if this is a unit, null otherwise
	 */
	public UnitGroup<CyclicRing> getUnit(){return isUnit()?new UnitGroup<CyclicRing> (this):null;}
	/**
	 * Returns the multiplicative inverse
	 * of this element only if this is a unit,
	 * null otherwise
	 */
	public CyclicRing inverse (){
		
		if(isUnit()){
			CyclicRing one = getOne(), cp = this;
			while (!cp.value.equals(IntRing.ONE)) {
				one = cp;
				cp = cp.multiply(this);
			}
			return one;
		}
		return null;
	}
	/**Returns always true*/
	public boolean isDiscrete(){return true;}
	/**
	 * Returns true if this equals the zero element
	 */
	public boolean isNeutral(){return value.equals(IntRing.ZERO);}
	/**Returns a string representation of this element*/
	public String toString (){return value.toString()+" mod "+mod.toString();}
}
