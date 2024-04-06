package module.poly;

import ring.grading.GradedIndexing;
import ring.poly.GradedMultiPoly;
import field.AbstractField;
import group.NNegInt;

public class GradedPolyMod<F extends AbstractField<F>> extends
		AbstractPolyModule<GradedIndexing<NNegInt>, F> {
	public GradedPolyMod (){super();}
	
}
