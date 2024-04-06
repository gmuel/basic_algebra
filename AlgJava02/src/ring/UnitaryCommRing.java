package ring;


/**
 * Class of commutative unitary rings - rings with 'one'-element 
 * @author adin
 *
 * @param <U> type of the implementing class
 */
public abstract class UnitaryCommRing<U extends UnitaryCommRing<U>> extends
		AbstractCommRing<U>{
	/**
	 * Default constructor
	 */
	public UnitaryCommRing() {
		super();
	}
	/**
	 * Returns the one element
	 * @return one element
	 */
	public abstract U getOne ();
}
