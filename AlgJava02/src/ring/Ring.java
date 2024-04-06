package ring;

import group.AbelGroup;

/**
 * Interface of algebraic rings
 * @author gmueller
 *
 * @param <R> the sub type
 */
public interface Ring<R extends Ring<R>> extends AbelGroup<R>{
	/**
	 * Returns the additive inverse of <code>this</code>,
	 * such that <code>addInverse().add(this).isZero()</code> returns true
	 * @return the additive inverse
	 */
	public R addInverse ();
	/**
	 * Returns true if this element commutes
	 * (so called central element)
	 * @return true if this is central
	 */
	public boolean isCommutative ();
	/**
	 * Returns true if and only if this
	 * ring (element) object is multiplicatively
	 * invertible
	 * @return true if this is a unit
	 */
	public boolean isUnit();
	/**
	 * Returns true, if <code>this</code> represents the zero element
	 * @return true if this equals zero
	 */
	public boolean isZero();
	/**
	 * Returns the product <code>this * another</code>
	 * @param another some ring element
	 * @return the product
	 */
	public R multiply(R another);
	/**
	 * Auxiliary class - provides display methods to test
	 * the implemented {@link AbelGroup#add(AbelGroup)} and
	 * {@link Ring#multiply(Ring)}-methods for some sub-class
	 * of type <tt>&ltR&gt</tt>
	 * @author adin
	 *
	 * @param <R> type of the implementing class of sub-type {@link Ring}
	 */
	public static final class DisplayResults<R extends Ring<R>> {
		/**format string for formatted output - as
		 * <p><tt>(a)
		 * <p>.
		 * <p>(b)
		 * <p>=
		 * <p>x</tt>,
		 * <p>where <tt>a</tt> and <tt>b</tt> are some instances to test*/
		private static final String FORMAT_STR = "(%1$s)\n%2$s\n(%3$s) =\n%4$s";
		/**the plus string*/
		private static final String   PLUS_STR = "+";
		/**the times string*/
		private static final String  TIMES_STR = "*";
		/**Constructs an display
		 * instance*/
		private DisplayResults (){}
		/**
		 * Computes the product of
		 * <tt>factor1</tt> and <tt>factor2</tt>
		 * and prints it to <code>System.out</code>
		 * @param factor1 left factor
		 * @param factor2 right factor
		 */
		private void printProduct (R factor1, R factor2){
			System.out.println(String.format(FORMAT_STR,factor1,TIMES_STR,factor2,factor1.multiply(factor2)));
		}
		/**
		 * Computes the sum of
		 * <tt>summand1</tt> and <tt>summand2</tt>
		 * and prints it to <code>System.out</code>
		 * @param summand1 first summand
		 * @param summand2 second summand
		 */
		private void printSum (R summand1, R summand2){
			System.out.println(String.format(FORMAT_STR,summand1,PLUS_STR,summand2,summand1.add(summand2)));
		}
		/**
		 * Computes the product of
		 * <tt>first</tt> and <tt>second</tt>
		 * and prints it to <code>System.out</code>
		 * @param first left factor
		 * @param second right factor
		 */
		public static <R extends Ring<R>> void displayProduct(R first, R second){
			DisplayResults<R> res = new DisplayResults<R>();
			res.printProduct(first, second);
		}
		/**
		 * Computes the sum of
		 * <tt>first</tt> and <tt>second</tt>
		 * and prints it to <code>System.out</code>
		 * @param first first summand
		 * @param second second summand
		 */
		public static <R extends Ring<R>> void displaySum(R first, R second){
			DisplayResults<R> res = new DisplayResults<R>();
			res.printSum(first, second);
		}
		
	}
}
