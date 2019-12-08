/*
 * topo_topo.hpp
 *
 *  Created on: 02.12.2019
 *      Author: stream_vid
 */

#ifndef INCLUDE_ALG_1_1_TOPO_TOPO_TOPO_HPP_
#define INCLUDE_ALG_1_1_TOPO_TOPO_TOPO_HPP_

#include "../cat/cat_cat_base.hpp"

namespace top {

using namespace cat;

template<typename X,
typename T>
struct interior {
	typedef interior<X,interior<X,T> > _inner_int;
};



}



#endif /* INCLUDE_ALG_1_1_TOPO_TOPO_TOPO_HPP_ */
