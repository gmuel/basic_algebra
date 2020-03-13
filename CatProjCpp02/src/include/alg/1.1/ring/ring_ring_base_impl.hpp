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
	cyc_rng_obj(int i = 0):_base(i){}
	cyc_rng_obj(const _base& o):_base(o){}
	cyc_rng_obj& operator=(int i){_base::operator=(i);return *this;}
	cyc_rng_obj& operator=(const _base& o){_base::operator=(o);return *this;}
};

template<unsigned int N >
struct cyc_rng_mul : public binary< cyc_rng_obj<N >, cyc_rng_mul<N > > {
	cyclic_wrp<N > operator()(const cyc_rng_obj<N >& c1, const cyc_rng_obj<N >& c2) const {
		return cyc_rng_obj<N >((*c1)*(*c2));
	}
};
/*
template<unsigned int N >
const cyc_rng_mul<N >& binary<cyclic_wrp<N >, cyc_rng_mul<N > >::OPERATION = cyc_rng_mul<N >();
*/
template<unsigned int N >
struct cyc_unit : public unit<cyc_rng_obj<N > > {
	typedef unit<cyclic_wrp<N > > _base;
	template<typename U >
	const cyc_rng_obj<N >& operator()(const U& u) const {static cyc_rng_obj<N > one(1);return one;}
	friend struct unit<cyclic<N > >;
private:
	cyc_unit():_base(){}
	cyc_unit(const cyc_unit<N >& o);
};

template<unsigned int N >
struct cyclics {
	typedef uring<cyclic_wrp<N >, cyclic_add<N >,cyclic_add_anti<N >,cyc_rng_mul<N >,cyc_unit<N > > _urng_bind;
};


template<
typename URNG,
typename ABL_BIN,
typename ABL_ANT,
typename MND_BIN,
typename MND_UNI,
typename ABL_UNI = unit<URNG >
>
struct initial {
	typedef initial<
			URNG,
			ABL_BIN,
			ABL_ANT,
			MND_BIN,
			MND_UNI,
			ABL_UNI>
						_ths;
	typedef cyclic_wrp<0 > _urng_obj;

	typedef cyclic_add<0 > _urng_bin;
	typedef cyclic_add_anti<0 > _urng_aant;
	typedef cyclic_add_unit<0 > _urng_auni;
	typedef cyc_rng_mul<0 > _urng_mbin;
	typedef cyc_unit<0 > _urng_muni;
	typedef cyclics<0 > _urng_bind1;
	typedef uring<URNG,ABL_BIN,ABL_ANT,MND_BIN,MND_UNI,ABL_UNI > _urng_bind2;
	typedef cat::morph<
			_urng_obj,
			_urng_bind2,
			_ths
			>			_mor_bind;
};
template< unsigned int M, unsigned int N , bool B = (M%N == 0)>
struct cinitial /*<
N, cyclic_wrp<M >, cyclic_add<M >,cyclic_add_anti<M >,cyc_rng_mul<M >,cyc_unit<M >, cyclic_add_unit<M >
>*/
{
	typedef cinitial<M, N, B >
						_ths;
	typedef cyclics<N > _pre_image;
	typedef uring< cyclic_wrp<M >, cyclic_add<M >,cyclic_add_anti<M >,
			cyc_rng_mul<M >,cyc_unit<M > > _urng_bind;
	typedef cat::morph<
			_pre_image,
			_urng_bind,
			_ths
			>			_mor_bind;
};


} /*alg*/





#endif /* INCLUDE_ALG_1_1_RING_RING_RING_BASE_IMPL_HPP_ */
