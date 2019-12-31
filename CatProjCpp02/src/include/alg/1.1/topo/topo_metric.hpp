/*
 * topo_metric.hpp
 *
 *  Created on: 14.12.2019
 *      Author: stream_vid
 */

#ifndef INCLUDE_ALG_1_1_TOPO_TOPO_METRIC_HPP_
#define INCLUDE_ALG_1_1_TOPO_TOPO_METRIC_HPP_

#include "topo_topo.hpp"

namespace top{

template <
typename X,
typename R,
typename D
>
struct metric_diagram {
	static const D& d;
	bool operator()(const X& x1, const X& x2) const {
		return 0<=d(x1,x2);
	}
	bool operator()(const X& x) const {
		return 0==d(x,x);
	}
	bool operator()(const X& x1, const X& x2, const X& x3) const {
		return d(x1,x3)<=d(x1,x2)+d(x2,x3);
	}
};


template<
typename X,
typename R = unsigned double >
struct metrix {
	typedef metrix<X, R > _clss;
	typedef metric_diagram<X, R, _clss > _diag_bind;
};

} /*top*/

#endif /* INCLUDE_ALG_1_1_TOPO_TOPO_METRIC_HPP_ */
