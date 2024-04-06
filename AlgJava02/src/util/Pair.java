package util;
/**
 * The pair class - accepts two arguments
 * of type <tt>X</tt>
 * @author adin
 *
 * @param <X> the type
 */
public class Pair<X> {
	/**the first element*/
	private final X first;
	/**the second element*/
	private final X second;
	/**
	 * Constructs an instance
	 * with first entry set to
	 * <tt>first</tt> and second
	 * entry set to <tt>second</tt>
	 * <p><b>Note</b> null entries
	 * are permitted
	 * @param first the first entry
	 * @param second the second entry
	 */
	public Pair(X first, X second) {
		this.first  =  first;
		this.second = second;
	}
	/**
	 * Returns true only <tt>o</tt>
	 * is of type <code>Pair</code>
	 * and both entries are equal
	 */
	public boolean equals(Object o){
		if(this==o) return true;
		if(this==null||o==null) return false;
		if(!(o instanceof Pair)) return false;
		Pair<?> cp = (Pair<?>) o;
		boolean f1 = first==null, f2 = cp.first==null,s1 = second==null, s2 = cp.second==null;
		if(f1&&f2) {
			if((s1&&!s2)||(!s1&&s2)) return false;
			return true;
		}
		if((f1&&!f2)||(!f1&&f2)) return false;
		return first.equals(cp.first)&&cp.second.equals(second)?true:false;
	}
	/**
	 * Returns a hash code
	 */
	public int hashCode(){return first==null?0:17*first.hashCode()+(second==null?0:37*second.hashCode());} 
	/**
	 * Returns the first entry as shallow copy
	 * @return first
	 */
	public X getFirst(){return first==null?null:first;}
	/**
	 * Returns the second entry
	 * @return second
	 */
	public X getSecond(){return second==null?null:second;}
}
