package ana;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;

import field.DoubleField;
import topo.TopoSpace;
/**
 * The real number line modeled by the double precision floating point
 * elements represented by {@link DoubleField}. Each <code>RealLine</code>
 * object consists of the disjoint union of {@link Segment} elements
 * @author adin
 *
 */
public class RealLine implements TopoSpace<RealLine, DoubleField>, Iterable<Entry<DoubleField,Segment>> {
	/**useful constant*/
	public static final DoubleField HALF = new DoubleField(.5);
	/**The empty real line: for any element <tt>e</tt> of type <code>DoubleField</code>
	 * <br /><tt>EMPTY_SET.contains(e)</tt> returns false*/
	public static final RealLine EMPTY_SET = new RealLine();
	/**The real number line: the one dimensional sphere of infinite radius, centered at
	 * zero*/
	public static final RealLine REAL_LINE = new RealLine(DoubleField.ZERO,DoubleField.SUP_D);
	/**The unit interval: all */
	public static final RealLine UNIT_INTV = new RealLine(HALF,HALF);
	public static final RealLine UNIT_SPHE = new RealLine(DoubleField.ZERO,DoubleField.ONE);
	private TreeMap<DoubleField,Segment> midPointMap;
	private RealLine() {
		midPointMap = new TreeMap<DoubleField,Segment> ();
	}
	public RealLine (DoubleField midPoint, DoubleField radius){
		this();
		addSegment(midPoint,radius);
	}
	public RealLine (DoubleField midPoint, DoubleField radius, boolean isOpen){
		this();
		addSegment(midPoint,radius,isOpen);
	}
	public RealLine (Iterable<Entry<DoubleField,Segment>> segments){
		this();
		for (Entry<DoubleField,Segment> entry:segments) {
			Segment seg = entry.getValue();
			addSegment(seg.midPoint,seg.radius,seg.isOpen);
		}
	}
	public void addSegment(DoubleField midPoint, DoubleField radius, boolean isOpen){
		if(midPoint==null||radius==null) return;
		Segment seg = new Segment(midPoint,radius,isOpen);
		if(seg.midPoint==null) return;
		//if(!contains(midPoint)) midPointMap.put(midPoint, seg);
		if(!contains(seg.inf())&&!contains(seg.sup())) midPointMap.put(midPoint, seg);
		else union(new RealLine(midPoint,radius));
	}
	public void addSegment(DoubleField midPoint, DoubleField radius){
		if(midPoint==null||radius==null) return;
		Segment seg = new Segment(midPoint,radius,false);
		if(seg.midPoint==null) return;
		//if(!contains(midPoint)) midPointMap.put(midPoint, seg);
		if(!contains(seg.inf())&&!contains(seg.sup())) midPointMap.put(midPoint, seg);
		else union(new RealLine(midPoint,radius));
	}
	
	public boolean contains(DoubleField element) {
		if(midPointMap.size()==0) return false;
		for (Entry<DoubleField,Segment> entry:midPointMap.entrySet()){
			Segment seg = entry.getValue();
			if(seg.isOpen?
					seg.midPoint.dist(element).compareTo(seg.radius)<0:
						seg.midPoint.dist(element).compareTo(seg.radius)<=0) return true;
		}
		return false;
	}

	
	public RealLine intersect(RealLine another) {
		RealLine intersect = new RealLine ();
		intersect.midPointMap.putAll(another.midPointMap);
		for (Entry<DoubleField,Segment> entry1:another.midPointMap.entrySet()){
			DoubleField midPoint1 = entry1.getKey(), radius1 = entry1.getValue().radius;
			boolean found = false;
			for (Entry<DoubleField,Segment> entry2:midPointMap.entrySet()){
				DoubleField midPoint2 = entry2.getKey(), radius2 = entry2.getValue().radius;
				DoubleField inf = null, sup = null;
				if(midPoint1.compareTo(midPoint2)<=0){
					inf = midPoint2.add(radius2.addInverse());
					sup = midPoint1.add(radius1);
					if(entry1.getValue().isOpen&&entry2.getValue().isOpen?
							inf.dist(midPoint1).compareTo(radius1)<0:
							inf.dist(midPoint1).compareTo(radius1)<=0) {
						intersect.midPointMap.remove(midPoint1);
						intersect.addSegment(inf.add(sup).multiply(HALF), sup.add(inf.addInverse()).multiply(HALF),entry1.getValue().isOpen&&entry2.getValue().isOpen);
						found = true;
						break;
					}
				}
				else{
					inf = midPoint1.add(radius1.addInverse());
					sup = midPoint2.add(radius2);
					if(inf.dist(midPoint2).compareTo(radius2)<=0){
						intersect.midPointMap.remove(midPoint1);
						intersect.addSegment(sup.add(inf).multiply(HALF), sup.add(inf.addInverse()).multiply(HALF),entry1.getValue().isOpen&&entry2.getValue().isOpen);
						found = true;
						break;
					}
				}
			}
			if(found) continue;
			else intersect.midPointMap.remove(midPoint1);
		}
		return intersect;
	}

	
	public boolean isEmpty() {
		if(midPointMap.size()==0) return true;
		for (Entry<DoubleField,Segment> entry:midPointMap.entrySet()){
			Segment seg = entry.getValue();
			if(seg.isOpen&&seg.radius.compareTo(DoubleField.ZERO)>0) return false;
			if(!seg.isOpen&&seg.radius.compareTo(DoubleField.ZERO)>=0) return false;
		}
		return true;
	}

	
	public boolean isOpen() {return true;}
	public Iterator<Entry<DoubleField,Segment>> iterator(){return midPointMap.entrySet().iterator();}

	
	public RealLine union(RealLine another) {
		RealLine union = new RealLine ();
		union.midPointMap.putAll(midPointMap);
		for (Entry<DoubleField,Segment> entry1:another.midPointMap.entrySet()){
			DoubleField midPoint1 = entry1.getKey(), radius1 = entry1.getValue().radius;
			boolean found = false;
			for (Entry<DoubleField,Segment> entry2:midPointMap.entrySet()){
				DoubleField midPoint2 = entry2.getKey(), radius2 = entry2.getValue().radius;
				DoubleField dist = midPoint1.dist(midPoint2), nRadius = radius1.add(radius2);
				
				if(entry1.getValue().isOpen&&entry2.getValue().isOpen?
						dist.compareTo(nRadius)<0:dist.compareTo(nRadius)<=0){
					
					if(midPoint1.compareTo(midPoint2)<=0) {
						union.midPointMap.remove(midPoint2);
						union.addSegment(midPoint1.add(radius1.addInverse())
					
							.add(midPoint2.add(radius2)).multiply(HALF), radius1.add(radius2.add(dist)).multiply(HALF),entry1.getValue().isOpen&&entry2.getValue().isOpen);
						found = true;
						break;
					}
					else {
						union.midPointMap.remove(midPoint2);
						union.addSegment(midPoint2.add(radius2.addInverse())
					
							.add(midPoint1.add(radius1)).multiply(HALF), radius1.add(radius2.add(dist)).multiply(HALF),entry1.getValue().isOpen&&entry2.getValue().isOpen);
						found = true;
						break;
					}
						
				}
			}
			if(found) continue;
			else union.addSegment(midPoint1, radius1,entry1.getValue().isOpen);
		}
		return union;
	}

	
	public RealLine complement() {
		return new RealLine(this){
			
			public boolean contains (DoubleField element){
				return super.contains(element)?false:true; 
			}
		};
	}
	
	public RealLine getSegment(DoubleField element){
		for (Entry<DoubleField,Segment>entry:this){
			Segment segment = entry.getValue();
			if(segment.contains(element)) return new RealLine(entry.getKey(),segment.radius);
		}
		return null;
	}
	
	public boolean isSuperSet(RealLine another) {
		for (Entry<DoubleField,Segment> entry:midPointMap.entrySet()){
			DoubleField midPoint = entry.getKey(), radius = entry.getValue().radius;
			DoubleField inf = midPoint.add(radius.addInverse()), sup = midPoint.add(radius);
			if(!another.contains(inf)||!another.contains(sup)) return false;
		}
		return true;
	}

	
	public boolean isSubSet(RealLine another) {return another.isSuperSet(this);}
	public RealLine scale (DoubleField scalar, boolean isOpen){
		RealLine scale = new RealLine();
		for (Entry<DoubleField,Segment> entry:this) scale.addSegment(entry.getKey().multiply(scalar),
				entry.getValue().radius.multiply(scalar),isOpen);
		return scale;
	}
	public String toString (){
		if(midPointMap.size()==0) return "{ }";
		StringBuilder sb = new StringBuilder ("{");
		String comma = ", ";
		Iterator<Entry<DoubleField,Segment>> it = iterator();
		while (it.hasNext()){
			Entry<DoubleField,Segment> entry = it.next();
			sb.append(entry.getValue().toString());
			if(it.hasNext()) sb.append(comma);
		}
		sb.append("}");
		return sb.toString();
	}
	public RealLine translate(DoubleField trans, boolean isOpen){
		RealLine translate = new RealLine();
		for (Entry<DoubleField,Segment> entry:this) translate.addSegment(entry.getKey().add(trans), entry.getValue().radius,isOpen);
		return translate;
	}
}
