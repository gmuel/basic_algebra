/*
 * cat_relational.hpp
 *
 *  Created on: 01.12.2019
 *      Author: lmmac
 */

#ifndef INCLUDE_ALG_1_1_CAT_CAT_RELATIONAL_HPP_
#define INCLUDE_ALG_1_1_CAT_CAT_RELATIONAL_HPP_


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


#endif /* INCLUDE_ALG_1_1_CAT_CAT_RELATIONAL_HPP_ */
