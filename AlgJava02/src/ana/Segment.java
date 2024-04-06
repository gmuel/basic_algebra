package ana;

import java.util.Iterator;

import field.DoubleField;
/**
 * A connected set of {@link DoubleField} objects: each object of this
 * class is either opened or closed (not both, since the only 
 * @author adin
 *
 */
public class Segment implements Iterable<DoubleField>{
	/**The mid point: the element that has same 
	 * distance to lower and upper bound of this
	 * segment object*/
	DoubleField midPoint;
	/**The radius of the segment, the distance
	 * between the mid point and its bounds*/
	DoubleField radius;
	/**The set size of the segments own iterator:
	 * the call <code>iterator()</code> returns an iterator
	 * whose own <code>next()</code> method returns the sequence
	 * <tt>{midPoint-radius+step*i}</tt>,
	 * <ol><li>where <tt>i</tt> in <tt>{1,..,Math.floor(radius/step)}</tt>
	 * if this is an open segment</li>
	 * <li>where <tt>i</tt> in <tt>{0,...,Math.ceil(radius/step)}</tt>
	 * if this is an closed segment</li></ol>*/
	DoubleField step;
	/**Flag indicating whether an element must have distance
	 * strictly smaller than <tt>radius</tt> or may be equal*/
	boolean isOpen;
	/**
	 * Constructs a segment object: the object models the
	 * real interval <tt>[midPoint-radius,midPoint+radius]</tt>
	 * if <tt>isOpen</tt> is true or <tt>(midPoint-radius,midPoint+radius)</tt>
	 * otherwise. <b>Note</b>
	 * <ol><li>an open segment with radius zero
	 * will be modeled as a closed segment (would be empty otherwise) and</li>
	 * <li><tt>radius</tt> will always be interpret as not negative</li>
	 * <li>any of the two reference arguments must never be null</li></ol>
	 * 
	 * @param midPoint the center or mid point of the segment (same distance to both boundary points)
	 * @param radius the radius always non negative (if zero <tt>isOpen</tt> is set to false!)
	 * @param isOpen the open flag: only if radius is non zero set
	 */
	public Segment(DoubleField midPoint, DoubleField radius, boolean isOpen){
		if(midPoint==null||radius==null) throw new NullPointerException ("\nNull argument:"
				+midPoint==null&&radius==null?" both arguments are":
					midPoint==null?" mid point argument is":" radius argument is"+" null!");
		boolean isNotNeg = DoubleField.ZERO.compareTo(radius)<=0, isZero = DoubleField.ZERO.equals(radius);
		this.midPoint = midPoint;
		this.radius   = isNotNeg&&!isZero?radius:!isNotNeg&&!isZero?radius.addInverse():DoubleField.ZERO;
		this.isOpen   = isZero?false:isOpen;
	}
	/**
	 * Returns true if <tt>element </tt> is contained
	 * in this segment
	 * @param element the element to test
	 * @return true if element belongs to this segment
	 */
	public boolean contains(DoubleField element){
		return isOpen?midPoint.dist(element).compareTo(radius)<0?true:false:
			midPoint.dist(element).compareTo(radius)<=0?true:false;
	}
	/**
	 * Returns the closed hull of this segment - 
	 * the segment containing this object and its boundaries
	 * @return the hull segment
	 */
	public Segment hull (){
		if(!isOpen) return this;
		Segment inte = new Segment(midPoint,radius,false);
		if(step!=null) inte.step = step;
		return inte;
	}
	/**
	 * Returns the lower bound - the infimum or minimum (is closed)
	 * @return the infimum
	 */
	public DoubleField inf(){return midPoint.add(radius.addInverse());}
	/**
	 * Returns the interior of this segment - 
	 * the segment with its boundaries removed (open!)
	 * @return the interior segment
	 */
	public Segment interior (){
		if(isOpen) return this;
		Segment inte = new Segment(midPoint,radius,true);
		if(step!=null) inte.step = step;
		return inte;
	}
	/**
	 * Returns an iterator returning a
	 * finite sequence of equidistant elements contained
	 * in this segment. The iteration stops if
	 * the upper bound is reached
	 */
	public Iterator<DoubleField> iterator(){return new SegIterator(step);}
	/**
	 * Returns the upper bound - the supremum/maximum (if closed)
	 * @return the supremum
	 */
	public DoubleField sup(){return midPoint.add(radius);}
	/**
	 * Returns a string representation of this segment
	 */
	public String toString(){
		String br1 = isOpen?"(":"[", br2 = isOpen?")":"]";
		return br1+midPoint.add(radius.addInverse()).toString()+","+midPoint.add(radius).toString()+br2;}
	private class SegIterator implements Iterator<DoubleField> {
		private DoubleField step, current, max;
		private SegIterator (DoubleField step){
			this.step = step;
			if(isOpen) current = midPoint.add(radius.addInverse()).add(this.step);
			else current = midPoint.add(radius.addInverse());
			max = midPoint.add(radius);
		}
		public boolean hasNext(){
			return isOpen?current.compareTo(max)<0?true:false:current.compareTo(max)<=0?true:false;
		}
		public DoubleField next(){
			DoubleField cp = current;
			current = current.add(step);
			return cp;
		}
		public void remove(){}
	}
}
