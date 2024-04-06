package homomorphism.ring;

import java.util.Map.Entry;

import group.NNegInt;
import ring.UnitaryCommRing;
import ring.extension.UnitaryExten;
import ring.poly.MonoPoly;
import topo.AbstractElFct;
/**
 * The polynomial embedding class - maps
 * each polynomial of type <tt>U</tt> to
 * its counterpart in some finite extension.
 * In this sense, any instance is the extension
 * of the underlying ring homomorphism to the
 * polynomial rings
 * @author adin
 *
 * @param <U>
 */
public class PolyExtenEmbed<U extends UnitaryCommRing<U>> extends
		AbstractElFct<MonoPoly<NNegInt, U>, MonoPoly<NNegInt, UnitaryExten<U>>>
		implements
		RingHomo<PolyExtenEmbed<U>, MonoPoly<NNegInt, U>, MonoPoly<NNegInt, UnitaryExten<U>>> {
	/*----------------------statics----------------------*/
	/*-------------------class methods-------------------*/
	/**
	 * Returns the embedding homomorphism
	 * <p><tt>f : U -&gt U[X]/(poly), u |-&gt u.1</tt>
	 * @param poly the modulo operator
	 * @return the embedder
	 */
	public static final <U extends UnitaryCommRing<U>> EmbedHomo<U> getEmbedder (MonoPoly<NNegInt,U> poly){
		return new EmbedHomo<U> (poly);
	}
	/*-------------------nested classes------------------*/
	/**
	 * The embedding class - maps each element of some sub-class of
	 * <code>UnitaryCommRing</tt> to its corresponding element in
	 * <tt>U.X^0</tt>
	 * @author adin
	 *
	 * @param <U> type of the underlying ring
	 */
	public static class EmbedHomo<U extends UnitaryCommRing<U>> extends AbstractElFct<U,UnitaryExten<U>>
	implements RingHomo<EmbedHomo<U>,U,UnitaryExten<U>> {
		/**the modulo operator*/
		private final MonoPoly<NNegInt,U> poly;
		/**
		 * Constructs an embedding setting
		 * the modulo operator to <tt>poly</tt>
		 * @param poly the modulo polynomial
		 */
		private EmbedHomo (MonoPoly<NNegInt,U> poly){
			this.poly = new MonoPoly<NNegInt,U> (poly);
		}
		/**
		 * Computes the image for the current
		 * argument if it is not null
		 */
		public void f() {
			if(arg!=null) {
				val = new UnitaryExten<U> (new MonoPoly<NNegInt,U> (NNegInt.ZERO,arg),poly);
			}
		}

		/**
		 * Returns true only if <tt>isZero()</tt> returns true
		 */
		public boolean isKernel(U arg) {return arg.isZero();}
		
	}
	/*--------------------non statics--------------------*/
	/*----------------instance variables-----------------*/
	/**the underlying ring homomorphism, mapping
	 * each ring element in <tt>U</tt> to its
	 * counterpart in <tt>U[X]/(modulator)</tt>*/
	private final EmbedHomo<U> embedder;
	/*-------------------constructors-------------------*/
	/**
	 * Constructs an embedder object - any polynomial
	 * <tt>p</tt> of type <code>MonoPoly</code> is mapped
	 * to its equivalence class <tt>p mod poly</tt>
	 * @param poly the modulo operating polynomial (modulator)
	 */
	public PolyExtenEmbed (MonoPoly<NNegInt,U> poly) {
		this(UnitaryExten.getEmbedder(poly));
	}
	/**
	 * Constructs an embedder object specified
	 * by the ring homomorphism <tt>embedder</tt>
	 * @param embedder the ring homomorphism
	 */
	public PolyExtenEmbed (EmbedHomo<U> embedder){
		super();
		if(embedder!=null) this.embedder = embedder;
		else throw new NullPointerException ("\nEmbedder mustnever be null!");
	}
	/**
	 * Computes the image polynomial
	 * for any non-null argument
	 */
	public void f() {
		if(arg!=null){
			val = new MonoPoly<NNegInt,UnitaryExten<U>> ();
			for (Entry<NNegInt,U> coeffEntry:arg){
				embedder.f(coeffEntry.getValue());
				val.setCoefficient(coeffEntry.getKey(), embedder.getValue());
			}
		}
		
	}
	/**
	 * Returns true only if
	 * <tt>arg.isZero(9</tt> returns true
	 */
	public boolean isKernel(MonoPoly<NNegInt, U> arg) {return arg.isZero();}

}
