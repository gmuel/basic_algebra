/*
 * ring_ring_base_impl.hpp
 *
 *  Created on: 01.12.2019
 *      Author: lmmac
 */

#ifndef INCLUDE_ALG_1_1_RING_RING_RING_BASE_IMPL_HPP_
#define INCLUDE_ALG_1_1_RING_RING_RING_BASE_IMPL_HPP_


#include "ring_ring_base.hpp"
#include "../group/group_group_base_impl.hpp"


namespace alg {



template<unsigned int N >
struct cyc_rng_obj : public cyclic_wrp<N > {
	typedef cyclic_wrp<N > _base;
	cyc_rng_obj(int i = 0):_base(0){}
	cyc_rng_obj(const _base& o):_base(o){}
	cyc_rng_obj& operator=(int i){_base::operator=(i);return *this;}
	cyc_rng_obj& operator=(explicit const _base& o){_base::operator=(o);return *this;}
};

template<unsigned int N >
struct cyc_rng_mul : public binary< cyclic_wrp<N >, cyc_rng_mul<N > > {
	cyclic_wrp<N > operator()(const cyclic_wrp<N >& c1, const cyclic_wrp<N >& c2) const {
		return cyclic_wrp<N >((*c1)*(*c2));
	}
};
/*
template<unsigned int N >
const cyc_rng_mul<N >& binary<cyclic_wrp<N >, cyc_rng_mul<N > >::OPERATION = cyc_rng_mul<N >();
*/
template<unsigned int N >
struct cyc_unit : public unit<cyclic_wrp<N > > {
	typedef unit<cyclic_wrp<N > > _base;
	template<typename U >
	const cyclic_wrp<N >& operator()(const U& u) const {static cyclic_wrp<N > one(1);return one;}
	friend struct unit<cyclic<N > >;
private:
	cyc_unit():_base(){}
	cyc_unit(const cyc_unit<N >& o);
};

} /*alg*/





#endif /* INCLUDE_ALG_1_1_RING_RING_RING_BASE_IMPL_HPP_ */
