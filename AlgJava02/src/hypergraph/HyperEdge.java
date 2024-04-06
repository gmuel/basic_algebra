package hypergraph;

import group.NNegInt;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.Iterator;

import ring.UnitaryCommRing;
import ring.grading.GrIndex;
import ring.grading.GradedIndexing;
import ring.poly.MonoPoly;
/**
 * The hyper edge class - that is:
 * <p>a map <tt>e : V -&gt Z</tt> from the set of
 * vertices <tt>V</tt> to the set of integers, such that
 * <ol>
 * <li><tt>e(v) < 0</tt>, if <tt>v</tt> is an out-going vertex</li>
 * <li><tt>e(v) > 0</tt>, if <tt>v</tt> is an in-coming vertex</li>
 * <li><tt>e(v) = 0</tt>, if <tt>v</tt> is neither of the above</li>
 *  </ol>
 *  The image values of some vertex <tt>v</tt> represent its degree. 
 * @author bzfmuell
 *
 * @param <U> type of the underlying {@link UnitaryCommRing}
 */
public class HyperEdge<U extends UnitaryCommRing<U>> implements Iterable<HyperNode>{
	/**the node set*/
	private TreeMap<Integer,HyperNode> nodeSet;
	private U coeff;
	/**
	 * Constructs an empty hyper edge
	 */
	public HyperEdge (){
		nodeSet = new TreeMap<Integer,HyperNode>();
	}
	/**
	 * Constructs an empty hyper edge
	 * with weight coefficient <tt>coeff</tt>
	 * @param coeff the weight coefficient
	 */
	public HyperEdge (U coeff){
		this();
		this.coeff = coeff;
	}
	/**
	 * Adds a node to this edge if not present
	 * or increment its degree if present and returns
	 * false only if the former node had degree -1 - 
	 * all other additions increment the degree
	 * @param node node to add
	 * @return false if node present and its former
	 * degree -1
	 */
	public synchronized boolean addNode (HyperNode node){
		Iterator<Entry<Integer,HyperNode>> it = nodeSet.entrySet().iterator();
		while (it.hasNext()){
			Entry<Integer,HyperNode> entry = it.next();
			if(entry.getKey()==node.hashCode()) {
				HyperNode oldNode = entry.getValue();
				short deg = oldNode.getDegree();
				deg++;
				if(deg==0) {
					it.remove();
					return false;
				}
				oldNode.setDegree(deg);
				return true;
			}
		}
		nodeSet.put(node.hashCode(), node);
		return true;
	}
	/**
	 * Adds a collection of nodes to this
	 * edge if not present or increment
	 * its degree if present
	 * @param nodeColl the collection
	 * @return true on success
	 */
	public boolean addNode(Collection<HyperNode> nodeColl){
		boolean succ = true;
		for (HyperNode node:nodeColl) {
			succ &= addNode(node);
		}
		return succ;
	}
	/**
	 * Returns true if <tt>node</tt> is
	 * contained in this edge
	 * @param node to test
	 * @return true if present
	 */
	public boolean contains (HyperNode node){
		return nodeSet.containsKey(node.hashCode());
	}
	/**
	 * Returns true if all nodes present in
	 * <tt>nodeColl</tt> are contained in
	 * this edge
	 * @param nodeColl the collection
	 * @return true if all present
	 */
	public boolean contains (Collection<HyperNode> nodeColl){
		return nodeSet.values().containsAll(nodeColl);
	}
	/**
	 * Returns true if <tt>o</tt> is
	 * a hyper edge, both its vertices
	 * are all equal <b>AND</b> have same degree
	 */
	public boolean equals (Object o){
		if(this==o) return true;
		if(!(o instanceof HyperEdge)) return false;
		@SuppressWarnings("unchecked")
		HyperEdge<U> e = (HyperEdge<U>) o;
		Iterator<HyperNode> it1 = nodeSet.values().iterator(), it2 = e.nodeSet.values().iterator();
		while (it1.hasNext()&&it2.hasNext()){
			HyperNode node1 = it1.next(), node2 = it2.next();
			if (!node1.equals(node2)&&node1.getDegree()!=node2.getDegree()) return false;
		}
		if (it1.hasNext()||it2.hasNext()) return false;
		return true;
	}
	/**
	 * Returns the length of this edge,
	 * the number of vertices connected by
	 * this edge
	 * @return length
	 */
	public int getLength (){
		return nodeSet.size();
	}
	/**
	 * Returns a polynomial <tt>p = c * (m_1 - m_2)</tt>,
	 * where
	 * <ol>
	 * <li><tt>m_1 = X^in</tt> is the incoming monomial, defined by
	 * the array <tt>in = (e(v))_{v in V} in (N_{0})^|V|</tt>,
	 *  of all non-negatively evaluated vertices (incoming/unconnected
	 *  vertices),</li>
	 * <li><tt>m_2 = X^out</tt>, <tt>out = (-e(v))_{v in V} in (N_[0})^|V|</tt>
	 * is the array of all non-positively evaluated vertices (outgoing/unconnected
	 * vertices)</li>
	 * <Li><tt>c</tt> is the weight coefficient over <tt>U</tt></li>
	 * </ol>
	 * @return the edge polynomial
	 */
	public MonoPoly<GradedIndexing<NNegInt>,U> getEdgePoly(){
		MonoPoly<GradedIndexing<NNegInt>,U> poly1 = new MonoPoly<GradedIndexing<NNegInt>, U> (),
				poly2 = new MonoPoly<GradedIndexing<NNegInt>, U> ();
		if(nodeSet.size()==0) return poly1;
		int dim = nodeSet.lastKey()-nodeSet.firstKey()+1;
		NNegInt[] index1 = new NNegInt[dim], index2 = new NNegInt[dim];
		for (HyperNode node:nodeSet.values()){
			short deg = node.getDegree();
			if(deg<0) index2[node.hashCode()] = new NNegInt(-deg);
			else index1[node.hashCode()] = new NNegInt(deg);
		}
		U pCoeff = coeff==null?coeff.getOne():coeff;
		poly1.setCoefficient(new GrIndex(index1), pCoeff);
		poly2.setCoefficient(new GrIndex(index2), pCoeff.addInverse());
		return poly1.add(poly2);
	}
	/**
	 * Returns a hash code based on its vertices and
	 * their respective degrees
	 */
	public int hashCode (){
		int hash = 0;
		for (HyperNode node:nodeSet.values()){
			hash += 17*node.hashCode();
			hash += 23*node.getDegree();
		}
		return hash;
	}
	/**
	 * Returns an iterator over all nodes/vertices
	 * in this edge
	 * <p><b>Note</b>, the order of node sequence
	 * returned is determined by their identifiers
	 */
	public Iterator<HyperNode> iterator(){return new NodeIterator();}
	/**
	 * Removes the <tt>node</tt> from this edge
	 * and returns true only if its counterpart
	 * had degree one (all other remove actions
	 * reduce the degree of the given node)
	 * @param node to remove
	 * @return true only if fully removed
	 */
	public synchronized boolean removeNode (HyperNode node){
		Iterator<Entry<Integer,HyperNode>> it = nodeSet.entrySet().iterator();
		while (it.hasNext()){
			Entry<Integer,HyperNode> entry = it.next();
			if(entry.getKey()==node.hashCode()) {
				HyperNode oldNode = entry.getValue();
				short deg = oldNode.getDegree();
				deg--;
				if(deg==0){
					it.remove();
					return true;
				}
				oldNode.setDegree(deg);
				return false;
			}
		}
		return false;
	}
	public boolean removeNode (Collection<HyperNode> nodeColl){
		boolean succ = true;
		for (HyperNode node:nodeColl) {
			succ&=removeNode(node);
		}
		return succ;
	}
	private class NodeIterator implements Iterator<HyperNode> {
		private Iterator<HyperNode> it;
		NodeIterator(){it = nodeSet.values().iterator();}
		public boolean hasNext(){return it.hasNext();}
		public HyperNode next(){return it.next();}
		public void remove(){}
	}
}
