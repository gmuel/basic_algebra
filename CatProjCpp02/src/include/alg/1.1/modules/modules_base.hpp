/*
 * modules_int.hpp
 *
 *  Created on: 10.04.2020
 *      Author: lmmac
 */

#ifndef INCLUDE_ALG_1_1_MODULES_MODULES_BASE_HPP_
#define INCLUDE_ALG_1_1_MODULES_MODULES_BASE_HPP_

#include "modules_module.hpp"
#include "../ring/ring_ring_base_impl.hpp"

namespace alg {
template<unsigned int N>
class cyclic_mod {
public:
	typedef std::map<unsigned int, cyclic_wrp<N > > _map;
	typedef typename _map::const_iterator 			_citer;
	typedef typename _map::iterator					_iter;
	struct cyclic_order {
		bool operator<(const cyclic_wrp<N >& c1, const cyclic_wrp<N >& c2) const
		{
			return (*c1)<(*c2);
		}
	};
	struct add {

	};
	cyclic_wrp<N >& operator[] (unsigned int idx, const cyclic_wrp<N >& o){
		return coeffs[idx];
	}
private:
	_map coeffs;
};

} /*alg*/

#endif /* INCLUDE_ALG_1_1_MODULES_MODULES_BASE_HPP_ */
