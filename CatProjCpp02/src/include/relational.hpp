#ifndef RELATIONAL_HPP
#define RELATIONAL_HPP

namespace cat {

/*
 * Reflexive type
 * requires boolean type
 */
template<typename REFL_TYPE, typename BOOLEAN >
struct reflexive {
	struct diagonal {
		virtual BOOLEAN operator()(const REFL_TYPE& t1, const REFL_TYPE& t2) const =0;
	};
	
	
};

}

#endif /*RELATIONAL_HPP*/
