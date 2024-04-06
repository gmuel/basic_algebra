package field.finitefield;

import ring.CyclicRing;
import ring.integer.IntRing;
import topo.Set;
import topo.relation.EquiRel;
import topo.relation.PairedElement;
import topo.relation.Relation;
import field.AbstractField;
/**
 * The field of three elements <tt>Z_3</tt> - has no constructor
 * but the three elements as constants
 * @author adin
 *
 */
public final class ThreeCycle extends AbstractField<ThreeCycle> {
	private static final IntRing THREE = new IntRing(3);
	/*----------------constant fields--------------------*/
	/**the zero element - the equivalence class of all integers
	 * divisible by 3*/
	public static final ThreeCycle ZERO = new ThreeCycle ("0");
	/**the one element - the equivalence class of all integers
	 * divisible by 3 with residue 1*/
	public static final ThreeCycle  ONE = new ThreeCycle ("1");
	/**the one element - the equivalence class of all integers
	 * divisible by 3 with residue 2*/
	public static final ThreeCycle  TWO = new ThreeCycle ("2");
	/**a value representing string*/
	private final String value;
	/**the equivalence relation*/
	private final ModRel isInClass;
	/**the equivalence class*/
	private final ModClass modClass;
	/**
	 * Sole constructor 
	 * @param value
	 */
	private ThreeCycle (String value){
		super();
		this.value = value;
		modClass   = value.equals("0")?ModClass.ZERO:value.equals("1")?ModClass.ONE:ModClass.TWO;
		isInClass  = new ModRel();
		
	}
	/**
	 * Returns the sum
	 */
	public ThreeCycle add(ThreeCycle another) {
		if(equals(ZERO)&&another.equals(ONE)) return ONE;
		if(equals(ZERO)&&another.equals(TWO)) return TWO;
		if(equals(ONE)&&another.equals(ZERO)) return ONE;
		if(equals(ONE)&&another.equals(ONE)) return TWO;
		if(equals(TWO)&&another.equals(ZERO)) return TWO;
		if(equals(TWO)&&another.equals(TWO)) return ONE;
		return ZERO;
	}
	/**
	 * Returns the additive inverse
	 */
	public ThreeCycle addInverse() {return this==ZERO?ZERO:this==ONE?TWO:ONE;}
	/**
	 * Returns the one element
	 */
	public ThreeCycle constructOne() {return ONE;}
	/**
	 * Overrides super class's method
	 */
	public boolean equals(Object o){
		if(this==o) return true;
		return false;
	}
	/**
	 * Returns true if both elements are equal
	 */
	public boolean equals(ThreeCycle another) {return this==another;}
	/**
	 * Returns the equivalence relation of this element
	 * @return the relation 'is in equivalence class'
	 */
	public EquiRel<IntRing,ModClass> getModRel(){return isInClass;}

	/**
	 * Returns the (multiplicative) inverse element
	 * @throws IllegalArgumentException if this is a non-unit element
	 */
	public ThreeCycle inverse() throws IllegalArgumentException {
		if(isZero()) throw new IllegalArgumentException ("\nZero division not permitted");
		return equals(ONE)?ONE:TWO;
	}
	/**
	 * Returns true
	 */
	public boolean isDiscrete(){return true;}
	/**
	 * Returns true if this is the zero element
	 */
	public boolean isZero() {return equals(ZERO);}

	/**
	 * Returns the product
	 */
	public ThreeCycle multiply(ThreeCycle another) {
		if(this==ZERO||another==ZERO) return ZERO;
		return equals(another)?ONE:TWO;
	}
	/**Returns a string representation of this element*/
	public String toString (){
		return value+" mod 3";
	}
	/*------------------------private classes------------------------*/
	/**
	 * The equivalence relation class: each instance of the outer
	 * class contains an equivalence relation object
	 * @author adin
	 *
	 */
	private class ModRel implements EquiRel<IntRing,ModClass>{
		/**the base point: the pair <tt>(element,element)</tt>*/
		private final PairedElement<IntRing> basePoint;
		/**
		 * Constructs the equivalence relation
		 */
		private ModRel(){
			IntRing el = modClass.cycle.getValue();
			basePoint = new PairedElement<IntRing> (el,el);
		}
		/**Returns the base point*/
		public PairedElement<IntRing> basePoint() {
			return basePoint;
		}

		/**
		 * Returns the inverse relation, the relation
		 * whose <code>isRelated(IntRing,IntRing) returns
		 * true if and only if <code>this.isRelated(IntRing,IntRing)</tt>
		 * returns false 
		 */
		public Relation<IntRing> inverse() {
			//final reference
			final ModRel cp = this;
			
			//returning anonymous object
			return new Relation<IntRing>(){
				/**the base point*/
				private PairedElement<IntRing> basePoint;
				/**Returns the base point if initialized*/
				public PairedElement<IntRing> basePoint() {
					return basePoint==null?null:basePoint;
				}
				/**
				 * Returns the original equivalence relation
				 */
				public Relation<IntRing> inverse() {
					return cp;
				}

				/**
				 * Returns true
				 */
				public boolean isClosed() {
					return !cp.isClosed();
				}

				/**
				 * Returns true if and only if
				 * the equivalence relation own method
				 * returns false
				 * @param first the first element
				 * @param second the second element
				 * @return true if the equivalence relation returns false
				 */
				public boolean isRelated(IntRing first, IntRing second) {
					boolean isRelated = !cp.isRelated(first, second);
					if(isRelated&&basePoint==null) basePoint = new PairedElement<IntRing> (first,second);
					return isRelated;
				}
				
			};
		}

		/**Returns always true*/
		public boolean isClosed() {return true;}

		/**
		 * Returns true if the difference of
		 * <tt>first</tt> and <tt>second</tt>
		 * is in the same equivalence class
		 */
		public boolean isRelated(IntRing first, IntRing second) {return modClass.contains(first.add(second.addInverse()));}

		
		public ModClass equiClass(IntRing element) {
			return modClass;
		}
		
	}
	/*------------------------statics------------------------*/
	/**
	 * The 'modulo-equivalence class' class - representing the
	 * same residual class
	 * @author adin
	 *
	 */
	public static class ModClass implements Set<IntRing,ModClass> {
		/**the empty set - has no representative element and overrides
		 * all super class methods to conform the empty set*/
		public static final ModClass    EMPTY = new ModClass (){
			/**Overrides super class method: returns always false*/
			public final boolean contains (IntRing element){return false;}
			/**Overrides super class method: returns always this*/
			public final ModClass intersect(ModClass another){return this;}
			/**Overrides super class method: returns always true*/
			public final boolean isEmpty(){return true;}
			/**Overrides super class method: returns always the argument <tt>another</tt>*/
			public final ModClass union (ModClass another){return another;} 
		};
		/**the equivalence class of the zero element - the subset of all
		 * integers having 3 as divisor*/
		public static final ModClass     ZERO = new ModClass (IntRing.ZERO);
		/**the equivalence class of the one element - the subset of all
		 * integers <tt>x</tt> such that 3 is a divisor of <tt>x - 1</tt>*/
		public static final ModClass      ONE = new ModClass ( IntRing.ONE);
		/**the equivalence class of the two element - the subset of all
		 * integers <tt>x</tt> such that 3 is a divisor of <tt>x - 2</tt>*/
		public static final ModClass      TWO = new ModClass ( IntRing.TWO);
		/**the union set of {@link ModClass#ZERO} and {@link ModClass#ONE}*/
		public static final ModClass ZERO_ONE = new ModClass (){
			/**Overrides super class method: returns true if <tt>element</tt>
			 * is either in {@link ModClass#ZERO} or in {@link ModClass#ONE}*/
			public final boolean contains (IntRing element){
				IntRing mod = element.mod(THREE);
				return mod.equals(IntRing.ZERO)||mod.equals(IntRing.ONE);
			}
			/**Overrides super class method: returns always false*/
			public final boolean isEmpty(){return false;}
		};
		/**the union set of {@link ModClass#ZERO} and {@link ModClass#TWO}*/
		public static final ModClass ZERO_TWO = new ModClass (){

			/**Overrides super class method: returns true if <tt>element</tt>
			 * is either in {@link ModClass#ZERO} or in {@link ModClass#TWO}*/
			public final boolean contains (IntRing element){
				IntRing mod = element.mod(THREE);
				return mod.equals(IntRing.ZERO)||mod.equals(IntRing.TWO);
			}
			/**Overrides super class method: returns always false*/
			public final boolean isEmpty(){return false;}
		};
		/**the union set of {@link ModClass#ONE} and {@link ModClass#TWO}*/
		public static final ModClass  ONE_TWO = new ModClass (){

			/**Overrides super class method: returns true if <tt>element</tt>
			 * is either in {@link ModClass#ONE} or in {@link ModClass#TWO}*/
			public final boolean contains (IntRing element){
				IntRing mod = element.mod(THREE);
				return mod.equals(IntRing.TWO)||mod.equals(IntRing.ONE);
			}
			/**Overrides super class method: returns always false*/
			public final boolean isEmpty(){return false;}
		};
		/**
		 * the full set (set of all integers) - is never empty, contains
		 * all representatives, is union-invariant and any subset is
		 * invariant under intersection
		 */
		public static final ModClass  FULL = new ModClass (){
			public final boolean contains (IntRing element){return true;}
			public final boolean isEmpty(){return false;}
			public final ModClass intersect(ModClass another){return another;}
			public final ModClass union(ModClass another){return this;}
		};
		/**the representative*/
		private final CyclicRing cycle;
		/**
		 * Constructs a class with no element
		 */
		private ModClass (){this.cycle = null;}
		/**
		 * Constructs the class of <tt>cycle</tt>
		 * @param cycle the representative
		 */
		private ModClass (IntRing cycle){this.cycle = new CyclicRing(cycle,THREE);}
		/**
		 * Returns true if the given element
		 * is contain in this class
		 */
		public boolean contains(IntRing element) {
			
			return cycle.getValue().equals(element.mod(THREE));
		}

		/**
		 * Returns the intersection of this and
		 * <tt>another</tt> class - possibly empty
		 */
		public ModClass intersect(ModClass another) {
			//if another is empty return the empty set
			//constant
			if(another.isEmpty()) return EMPTY;
			
			//if another is the full set return this
			if(another==FULL) return this;
			
			//if both are the same return this
			if(this==another) return this;
			
			//if either of the two classes as a representative:
			if(cycle!=null||another.cycle!=null){
				
				//if both have a representative, return
				//the empty set, since both are not equal
				if(cycle!=null&&another.cycle!=null)return EMPTY;
				
				//if this has a representative:
				if(cycle!=null){
					
					//return this if another contains the representative,
					//else the empty set
					return another.contains(cycle.getValue())?this:EMPTY;
				}
				
				//the same procedure with
				//roles exchanged:
				//return accordingly
				return contains(another.cycle.getValue())?another:EMPTY;
			}
			
			//lastly, composed sets: 3x2 cases
			
			//first case: this equals ZERO_ONE
			if(this==ZERO_ONE) 
				
				//two different sub-cases
				return another==ZERO_TWO?ZERO:ONE;
			
			//second case: this equals ZERO_TWO
			if(this==ZERO_TWO)
				
				//two different sub-cases
				return another==ZERO_ONE?ZERO:TWO;
			
			//finally, this equals ONE_TWO:
			return another==ZERO_ONE?ONE:TWO;
		}

		/**
		 * Returns true if no representative is given
		 */
		public boolean isEmpty() {return cycle==null?true:false;}

		/**
		 * Returns always true
		 */
		public boolean isOpen() {return true;}

		/**
		 * Returns the union of this and
		 * <tt>another</tt> class
		 */
		public ModClass union(ModClass another) {
			//if another is the full set simply
			//return it
			if(another==FULL) return FULL;
			
			//if both are equal return this
			if(this==another) return this;
			
			//check whether any of the two classes is
			//empty
			boolean isEmpty1 = isEmpty(), isEmpty2 = another.isEmpty();
			
			
			//in this event:
			if(isEmpty1||isEmpty2)
				
				//check whether both are empty and return its
				//counterpart or the empty constant
				return isEmpty1&&isEmpty2?EMPTY:isEmpty1?another:this;
			
			//check whether any of the two classes is
			//composed, i.e. is not empty but has no
			//representative
			boolean isComposed1 = !isEmpty1&&cycle==null, isComposed2 = !isEmpty2&&another.cycle==null;
			
			//if either is composed:
			if(isComposed1||isComposed2){
				
				//this composed but not another:
				if(isComposed1&&!isComposed2){
					
					//return this if the representative of another
					//is contain in this, else the full set constant
					return contains(another.cycle.getValue())?this:FULL;
				}
				
				//same procedure with roles exchanged
				if(!isComposed1&&isComposed2){
					
					//return accordingly
					return another.contains(cycle.getValue())?another:FULL;
				}
				
				//if both are composed (but not equal)
				//return the full constant
				return FULL;
			}
			
			//last case: non-composed non-empty classes
			//first, if this equals the zero set,
			//return the unions accordingly
			if(this==ZERO)
				return another==ONE?ZERO_ONE:ZERO_TWO;
			
			//same for the case this==ONE
			if(this==ONE)
				return another==ZERO?ZERO_ONE:ONE_TWO;
			
			//last, this==TWO
			return another==ZERO?ZERO_TWO:ONE_TWO;
		}
		
	}

	/**
	 * Returns the element containing the argument
	 * <tt>value</tt>
	 * @param value the value
	 * @return the cycle element
	 */
	public static ThreeCycle mod(int value){
		return mod(new IntRing(value));
	}
	/**
	 * Returns the element containing the argument
	 * <ttvalue</tt>
	 * @param value the argument value
	 * @return the cycle element
	 */
	public static ThreeCycle mod(IntRing value){
		return ZERO.modClass.contains(value)?ZERO:ONE.modClass.contains(value)?ONE:TWO;
	}
}
