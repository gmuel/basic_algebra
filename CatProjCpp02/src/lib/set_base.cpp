#include "set_base.hpp"

namespace cat {

empty::empty(){}

const empty& empty::EMPTY_SET = empty();//TODO in compi-unit

/**
 * @brief The empty set morphism template
 */
template<typename SET_TYPE >
const empty& morph<SET_TYPE,const empty& >::operator()(const SET_TYPE& arg) const {
	return empty::EMPTY_SET;
}


} /* cat */
