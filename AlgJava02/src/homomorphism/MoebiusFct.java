package homomorphism;

import java.util.Map.Entry;
import java.util.TreeMap;

import ring.integer.IntRing;
import group.NNegInt;
/**
 * The Moebius-function: a monoid homomorphism of the multiplicative
 * monoid of natural numbers <tt>N</tt> and the multiplicative monoid
 * of <tt>{-1, 0, 1}</tt>
 * @author adin
 *
 */
public final class MoebiusFct implements MonoidHomo<MoebiusFct, NNegInt, IntRing> {
	public static final MoebiusFct MOEBIUS = new MoebiusFct();
	private NNegInt arg;
	private IntRing val;
	private MoebiusFct() {}

	
	public void f() {
		if(arg==null) return;
		if(arg.equals(NNegInt.ONE)) val = IntRing.ONE;
		else {
			TreeMap<IntRing, NNegInt> primeDiv = IntRing.primeDivisors(arg.getValue());
			for (Entry<IntRing,NNegInt> entry:primeDiv.entrySet()){
				if(entry.getValue().getValue().mod(IntRing.TWO).equals(IntRing.ZERO)) {val = IntRing.ZERO;return;}
			}
			if(primeDiv.size()%2==0) val = IntRing.ONE;
			else val = IntRing.M_ONE;
		}
	}

	
	public void f(NNegInt arg) {
		if(arg==null) return;
		this.arg = arg;
		f();
	}

	
	public NNegInt getArgument() {return arg==null?null:arg;}

	
	public IntRing getValue() {
		if(val==null){
			if(arg==null) return null;
			f();
		}
		return val;
	}

	
	public void setArgument(NNegInt arg) {
		this.arg = arg;
	}

}
