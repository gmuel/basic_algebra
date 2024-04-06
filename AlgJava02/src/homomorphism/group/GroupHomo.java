package homomorphism.group;

import group.Group;
import homomorphism.MonoidHomo;
/**
 * The group homomorphism interface: extends {@link MonoidHomo}
 * interface
 * @author adin
 *
 * @param <X> the type of the implementing group homomorphism class
 * @param <G> the type of the preimage group
 * @param <H> the type of the image group
 */
public interface GroupHomo<X extends GroupHomo<X,G,H>, G extends Group<G>, H extends Group<H>> extends
		MonoidHomo<X,G,H> {

}
