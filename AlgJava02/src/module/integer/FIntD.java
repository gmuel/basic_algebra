package module.integer;

import java.util.Map.Entry;

import group.NNegInt;
import homomorphism.group.GroupHomo;
import module.AbstractFiniteModule;
import ring.integer.IntRing;
/**
 * Dual module class of finitely generated integer modules - 
 * the <tt>Z</tt> linear mappings on a integer module
 * @author adin
 *
 */
public class FIntD extends AbstractFiniteModule<IntRing> implements GroupHomo<FIntD,AbstractFiniteModule<IntRing>, IntRing>{
	/**the zero dual element: mapping all vector elements
	 * to {@link IntRing#ZERO}*/
	public static final  FIntD ZERO = new FIntD();
	/**the argument*/
	private FIntM arg;
	/**the value*/
	private IntRing val;
	/**
	 * Constructs the zero dual element
	 */
	public FIntD (){super();}
	/**
	 * Constructs the dual element
	 * specified by the argument <tt>array</tt>
	 * @param array the argument
	 */
	public FIntD (IntRing[] array){
		super(array);
	}
	public FIntD(AbstractFiniteModule<IntRing> element){
		super(element);
	}
	/**
	 * Computes the image of the current argument
	 * only if the current argument is not null
	 */
	public void f() {
		if(arg!=null){
			val = IntRing.ZERO;
			for (Entry<NNegInt,IntRing> en:this){
				NNegInt index = en.getKey();
				IntRing val1   = en.getValue(), val2 = null;
				if((val2 = arg.getValue(index))!=null){
					val = val.add(val1.multiply(val2));
				}
			}
		}
		
	}
	/**
	 * Computes the of the given argument
	 * <tt>arg</tt> 
	 */
	public void f(AbstractFiniteModule<IntRing> arg) {
		setArgument(arg);
		f();
	}
	/**
	 * Returns the current argument, possibly null
	 */
	public AbstractFiniteModule<IntRing> getArgument() {return arg==null?null:arg;}
	/**
	 * Returns the current image
	 */
	public IntRing getValue() {
		if(val==null||val.isZero()){
			f();
		}
		return val==null?null:val;
	}
	
	/**
	 * Returns the scalar multiple of this element
	 * @param scalar the scalar
	 * @return the scalar multiple
	 */
	public FIntD multiply(IntRing scalar) {return new FIntD(ringAct(scalar));}
	/**
	 * Sets the argument
	 */
	public void setArgument(AbstractFiniteModule<IntRing> arg) {
		if(arg!=null) this.arg = new FIntM(arg);
		
	}

}
