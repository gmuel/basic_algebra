package module.integer;


import java.util.HashSet;
import java.util.Map.Entry;
import java.util.TreeMap;

import module.AbstractFiniteModule;
import ring.integer.IntRing;
import util.ComposedObject01;
import group.NNegInt;
import homomorphism.group.GroupHomo;
/**
 * The 
 * @author adin
 *
 * @param <C>
 */
public class AbIntHomo extends AbstractFiniteModule<IntRing> implements
		GroupHomo<AbIntHomo,AbstractFiniteModule<IntRing>,AbstractFiniteModule<IntRing>> {
	//private HashMap<Integer,IntEndos<C>> endosScalarMap;
	//private FIntM dualEl;
	private FIntM arg;
	private FIntM val;
	private TreeMap<NNegInt,ComposedObject01<FIntD,FIntM>> endoGens;
	private NNegInt argRank;
	private NNegInt valRank;
	
	public AbIntHomo() {
		super();
		endoGens = new TreeMap<NNegInt,ComposedObject01<FIntD,FIntM>> ();
	}

	public AbIntHomo(FIntM[] vec){
		this();
		int length = vec.length;
		for (int i = 0; i < length; i++) {
			if(vec[i]!=null&&!vec[i].isZero()) {
				FIntD dual = new FIntD(vec[i]);
				for (int j = 0; j < length; j++){
					if(vec[j]!=null&&!vec[j].isZero()){
						dual.f(vec[j]);
						if(!dual.getValue().isZero()){
							ComposedObject01<FIntD,FIntM> comp = new ComposedObject01<FIntD,FIntM> (dual,vec[j]);
							
						}
					}
				}
			}
		}
	}
	public void f() {
		if(arg==null) return;
		if(val==null) val = new FIntM();
		else val.clear();
		for (Entry<NNegInt,IntRing> entry:this){
			NNegInt index = entry.getKey();
			NNegInt row   = index.mod(argRank);
		}
		
	}

	
	public void setArgument(AbstractFiniteModule<IntRing> arg) {
		// TODO Auto-generated method stub
		
	}

	
	public void f(AbstractFiniteModule<IntRing> arg) {
		// TODO Auto-generated method stub
		
	}

	
	public AbstractFiniteModule<IntRing> getArgument() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public AbstractFiniteModule<IntRing> getValue() {
		// TODO Auto-generated method stub
		return null;
	}
	private void setTree(FIntM[] array){
		int length = array.length;
		HashSet<FIntD> duals = new HashSet<FIntD> ();
		for (int i = 0; i < length; i++){
			duals.add(new FIntD(array[i]));
		}
	}
}
