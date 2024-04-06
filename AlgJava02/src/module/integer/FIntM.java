package module.integer;

import java.util.Map.Entry;

import group.NNegInt;

import ring.integer.IntRing;

import module.AbstractFiniteModule;
/**
 * The free integer modules of finite rank
 * @author adin
 *
 */
public class FIntM extends AbstractFiniteModule<IntRing>  {
	/**the zero vector: only zero entries*/
	public static final FIntM ZERO = new FIntM ();
	/**
	 * Constructs the zero element
	 */
	public FIntM() {
		super();
	}
	/**
	 * Constructs a module element
	 * with all non-negative integer
	 * entries
	 * @param array the argument array
	 */
	public FIntM (NNegInt[] array){
		this();
		int length = array.length;
		for (int i = 0; i < length; i++){
			if(array[i]!=null) setEntry(new NNegInt(i),array[i].getValue());
		}
	}
	/**
	 * Constructs an module element
	 * with entries specified by the
	 * argument <tt>array</tt>
	 * @param array the argument
	 */
	public FIntM(IntRing[] array){
		super(array);
		
	}
	public FIntM(int index, int entry){
		this(new NNegInt(index),new IntRing(entry));
	}
	public FIntM(NNegInt index, IntRing entry){
		this();
		setEntry(new NNegInt(index),new IntRing(entry));
	}
	/**
	 * Constructs a copy of
	 * the module <tt>element</tt>
	 * @param element the element to copy
	 */
	public FIntM(AbstractFiniteModule<IntRing> element){
		super(element);
	}
	public FIntM add(AbstractFiniteModule<IntRing> another){
		return new FIntM(super.add(another));
	}
	/**
	 * Returns the scalar multiple
	 */
	public FIntM ringAct(IntRing scalar) {
		FIntM scl = new FIntM();
		if(scalar.isZero()) return scl;
		for (Entry<NNegInt,IntRing> entry:this)
			scl.setEntry(entry.getKey(),entry.getValue().multiply(scalar));
		return scl;
	}
 
}
