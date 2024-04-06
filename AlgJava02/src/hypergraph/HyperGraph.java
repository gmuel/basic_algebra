package hypergraph;

import group.NNegInt;

import java.util.HashSet;
import java.util.Iterator;

import module.poly.AbstractPolyModule;

import ring.UnitaryCommRing;
import ring.grading.GradedIndexing;
import ring.poly.MonoPoly;

public class HyperGraph<U extends UnitaryCommRing<U>> {
	private HashSet<HyperEdge<U>> edgeSet;
	private HashSet<HyperNode>    nodeSet;
	
	public HyperGraph (){
		edgeSet = new HashSet<HyperEdge<U>> ();
		nodeSet = new HashSet<HyperNode> ();
	}
	public boolean addEdge (HyperEdge<U> edge){
		if(edgeSet.add(edge)){
			for (HyperNode node : edge) nodeSet.add(node);
			return true;
		}
		return false;
	}
	public boolean addNode (HyperNode node){
		return nodeSet.add(node);
	}
	public AbstractPolyModule<GradedIndexing<NNegInt>,U> getGraphPoly (){
		AbstractPolyModule<GradedIndexing<NNegInt>,U> grPolyMod =
				new AbstractPolyModule<GradedIndexing<NNegInt>,U> ();
		//MonoPoly<GradedIndexing<NNegInt>,U> grPoly = new MonoPoly<GradedIndexing<NNegInt>,U> ();
		for (HyperEdge<U> edge:edgeSet){
			MonoPoly<GradedIndexing<NNegInt>,U> edgePoly = edge.getEdgePoly();
			for (HyperNode nodeInEdge:edge){
				int hash = nodeInEdge.hashCode();
				MonoPoly<GradedIndexing<NNegInt>,U> p;
				NNegInt index = new NNegInt(hash);
				if ((p = grPolyMod.getValue(index))==null) p = new MonoPoly<GradedIndexing<NNegInt>,U>();
				
				if(nodeInEdge.getDegree()<0) grPolyMod.setEntry(index, p.add(edgePoly.addInverse()));
				else grPolyMod.setEntry(index, p.add(edgePoly));
				
			}
		}
		
		return grPolyMod;
	}
	public boolean removeEdge(HyperEdge<U> edge){
		return edgeSet.remove(edge);
	}
	public boolean removeNode(HyperNode node){
		if(nodeSet.remove(node)){
			Iterator<HyperEdge<U>> it = edgeSet.iterator();
			while (it.hasNext()){
				HyperEdge<U> edge = it.next();
				if(edge.contains(node)) it.remove();
			}
			return true;
		}
		return false;
	}
}
