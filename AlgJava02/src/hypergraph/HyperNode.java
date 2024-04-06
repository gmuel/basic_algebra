package hypergraph;

import java.util.HashSet;
import java.util.TreeMap;
/**
 * The hyper node class - a hyper node (or vertex) is simply
 * some unique object with an integer value assigned
 * to it - the so called <b>degree</b>.
 * <p>The degree is defined by some {@link HyperEdge}
 * object containing the vertex
 * @author bzfmuell
 *
 */
public class HyperNode {
	/**the set of all nodes instantiated*/
	private final static TreeMap<Integer,HyperNode> NODE_MAP = new TreeMap<Integer,HyperNode>();
	/**
	 * Convenience method - constructs a new node
	 * object with degree one
	 * @return a new node
	 */
	public static HyperNode constructNewNode (){
		return constructNewNode((short) 1);
	}
	/**
	 * Convenience method - constructs a new node
	 * object with degree <tt>deg</tt>
	 * @param deg the degree
	 * @return the new node
	 */
	public static HyperNode constructNewNode (short deg){
		if (NODE_MAP.size()==0) {
			HyperNode node = new HyperNode(0,deg);
			NODE_MAP.put(node.index, node);
			return node;
		}
		int index = NODE_MAP.lastKey();
		index++;
		HyperNode node = new HyperNode(index,deg);
		NODE_MAP.put(node.index, node);
		return node;
	}
	/**
	 * Constructs a hash set of <tt>size</tt> unique
	 * hyper nodes all with degree one
	 * @param size the number of nodes
	 * @return a set of nodes
	 */
	public static HashSet<HyperNode> constructNewNode(int size){
		HashSet<HyperNode> nodeSet = new HashSet<HyperNode> ();
		for (int i = 0; i < size; i++){
			nodeSet.add(constructNewNode());
		}
		return nodeSet;
	}
	/**the node index - a unique identifier*/
	private final int index;
	/**the degree of the node*/
	private short deg;
	/**
	 * Constructs a new node with given identifier
	 * @param index the identifier
	 */
	private HyperNode (int index){
		this(index,(short)1);
	}
	/**
	 * Constructs a new node with given identifier
	 * and degree
	 * @param index the identifier
	 * @param deg the degree
	 */
	private HyperNode (int index, short deg){
		this.index = index;
		this.deg   = deg;
	}
	/**
	 * Constructs a new node, which is
	 * a copy of <tt>node</tt>, that is
	 * <p><tt>...//some node ori</tt>
	 * <p><tt>HyperNode n = new HyperNode(ori);</tt>
	 * <p><tt>n.equals(ori)</tt> returns <b>true</b>
	 * regardless of their respective degrees.
	 * @param node the original node
	 */
	public HyperNode (HyperNode node){
		this(node.index);
	}
	/**
	 * Returns true if <tt>o</tt> is a
	 * hyper node and both have the same
	 * identifier
	 */
	public boolean equals (Object o){
		if(this==o) return true;
		if(!(o instanceof HyperNode)) return false;
		HyperNode cp = (HyperNode) o;
		return index==cp.index;
	}
	/**
	 * Returns the degree of this node
	 * @return the degree
	 */
	public short getDegree (){return deg;}
	/**Returns some hash code*/
	public int hashCode(){return index;}
	/**
	 * Sets the degree of this node
	 * to <tt>deg</tt>
	 * @param deg the new degree
	 */
	public void setDegree(short deg){
		this.deg = deg;
	}
}
