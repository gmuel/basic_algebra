package group.representation;

import group.Group;
import group.IndicialMonoid;
/**
 * Indicial group class - wraps any
 * arbitrary object of type {@link Group}
 * and induces 
 * @author adin
 *
 * @param <G>
 */
public class IndicialGroup<G extends Group<G>> implements Group<IndicialGroup<G>>,
		IndicialMonoid<IndicialGroup<G>> {
	int indicator;
	G el;
	G neutral;
	protected IndicialGroup() {
		super();
		indicator = ((Object) this).hashCode();
	}
	//IndicialGroup(){super();}
	public IndicialGroup(G another){
		this();
		if(el!=null) el = another;
	}
	
	public IndicialGroup (IndicialGroup<G> another){
		this(another.el);
	}
	

	public long eval() {
		// TODO Auto-generated method stub
		return indicator;
	}

	public boolean equals(IndicialGroup<G> another) {
		if(el.equals(another.el)){
			if(indicator<another.indicator) another.indicator = indicator;
			else indicator = another.indicator;
			return true;
		}
		return false;
	}
	public boolean equals(Object o){
		if(this==o) return true;
		if(!(o instanceof IndicialGroup)) return false;
		//@SuppressWarnings("unchecked")
		IndicialGroup<?> cp = (IndicialGroup<?>) o;
		if(!getClass().equals(cp.getClass())) return false;
		@SuppressWarnings("unchecked")
		boolean test =equals((IndicialGroup<G>)cp); 
		return test;
	}
	public boolean isComparable() {return true;}
	public boolean isDiscrete(){return el==null?true:el.isDiscrete();}
	public boolean isNeutral(){return el.equals(neutral);}
	/**
	 * Returns the right translate of this:
	 * <p><tt>this o another</tt>, or 
	 * <p>the left translate of <tt>another</tt>:
	 * <p><tt>this o another</tt>
	 * <p><b>Note</b>, the method returns null,
	 * in case the method {@link G#operate(Group)} returns null
	 */
	public IndicialGroup<G> operate(IndicialGroup<G> another) {
		G translate = el.operate(another.el);
		return translate==null?null:new IndicialGroup<G>(translate);
	}
	
	public int compareTo(IndicialGroup<G> arg0) {
		if(equals(arg0)) return 0;
		return indicator<arg0.indicator?-1:1;
	}
	


}
