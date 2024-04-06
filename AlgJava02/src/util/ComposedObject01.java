/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

/**
 * Auxiliary class: wraps exactly two objects (components)
 * of (not nessecarily) different types. <b>Note</b>,
 * that this class only deals with shallow copies of
 * both components, so any change to one of the two
 * components after creation of an object of this class
 * will change the according field.
 * <p>Preventing this behaviour requires the user to
 * do the following:
 * <p><tt>ComposedObject co = new ComposedObject (new Type1(...),new Type2(...));</tt>
 * @author gmueller
 *
 * @param <K> the type of the first component
 * @param <V> the type of the second component
 */
public class ComposedObject01<K,V> {
	/**the first component*/
	private K  firstComponent;
	/**the type of of the first component*/
	private Class<? extends Object> type1;
	/**the second component*/
	private V secondComponent;
	/**the type of of the second component*/
	private Class<? extends Object> type2;
	/**
	 * This constructor's usage is limited
	 * to all subclasses.
	 */
	protected ComposedObject01 (){}
	/**
	 * Constructs an instance of this class. <b>Note</b>,
	 * that null is not supported by this class.
	 * @param firstComponent the first component
	 * @param secondComponent the second component
	 */
	public ComposedObject01 (K firstComponent, V secondComponent){
		if(firstComponent==null&&secondComponent==null)
			throw new NullPointerException ("Null is not permitted!");
		if(firstComponent!=null){
			this.firstComponent  = firstComponent;
			type1 = firstComponent.getClass();
		}
		if(secondComponent!=null){
			this.secondComponent = secondComponent;
			type2 = secondComponent.getClass();
		}
	}
	public boolean equals (Object o){
		if(o instanceof ComposedObject01){
			ComposedObject01<?,?> c = (ComposedObject01<?,?>) o;
			return firstComponent.equals(c.firstComponent)&&secondComponent.equals(c.secondComponent)?true:false;
		} else throw new ClassCastException ("Found: "+o.getClass().getName()+"\tRequired: "+getClass().getName());
	}
	/**
	 * Returns the first component
	 * @return the first component
	 */
	public K getFirstComponent (){
		return firstComponent;
	}
	/**
	 * Returns the type of the first component
	 * @return the type of the first component
	 */
	public Class<? extends Object> getFirstType(){
		return type1;
	}
	/**
	 * Returns the second component
	 * @return the second component
	 */
	public V getSecondComponent (){
		return secondComponent;
	}
	/**
	 * Returns the type of the second component
	 * @return the type of the second component
	 */
	public Class<? extends Object> getSecondType (){
		return type2;
	}
	public int hashCode (){
		return 37*firstComponent.hashCode()+2*secondComponent.hashCode();
	}
    public void setFirst (K firstComponent){
        this.firstComponent = firstComponent;
        type1 = firstComponent.getClass();
    }
    public void setSecond (V secondComponent){
        this.secondComponent = secondComponent;
        type2 = secondComponent.getClass();
    }
	public String toString (){
		StringBuilder s = new StringBuilder ();
		if(firstComponent!=null)
			s.append("\nfirst component: "+firstComponent.toString()+"\ttype1: "+type1.getName());
		else s.append("\nfirst component: null\ttype1: no type");
		if(secondComponent!=null)
			s.append("\nsecond component: "+secondComponent.toString()+"\ttype1: "+type2.getName());
		else s.append("\nsecond component: null\ttype2: no type");
		return s.toString();
	}
}