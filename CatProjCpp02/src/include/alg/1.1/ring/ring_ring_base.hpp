/*
 * ring_ring_base.hpp
 *
 *  Created on: 01.12.2019
 *      Author: lmmac
 */

#ifndef INCLUDE_ALG_1_1_RING_RING_RING_BASE_HPP_
#define INCLUDE_ALG_1_1_RING_RING_RING_BASE_HPP_


#include "../group/group_group_base.hpp"


namespace alg {

template<typename RNG,
typename ABL_BINARY,
typename ABL_ANTI,
typename SMG_BINARY,
typename ABL_UNIT = unit<RNG > >
struct ring_diag : public abel_diag<RNG, ABL_BINARY, ABL_ANTI, ABL_UNIT > {
	typedef abel_diag<RNG, ABL_BINARY, ABL_ANTI, ABL_UNIT > 	_base;
	typedef semi_group<RNG,SMG_BINARY > 						_multi_bind;
};

template<typename RNG,
typename ABL_BINARY,
typename ABL_ANTI,
typename SMG_BINARY,
typename ABL_UNIT = unit<RNG > >
struct ring : public cat::cat<ring_diag<RNG, ABL_BINARY, ABL_ANTI, ABL_UNIT > >{
	typedef ring_diag<RNG, ABL_BINARY, ABL_ANTI, ABL_UNIT > 	_rng_bind;
};

template<typename RNG,
typename ABL_BINARY,
typename ABL_ANTI,
typename MND_BINARY,
typename MND_UNIT,
typename ABL_UNIT = unit<RNG > >
struct uring_diag : public abel_diag<RNG, ABL_BINARY, ABL_ANTI, ABL_UNIT > {
	typedef abel_diag<RNG, ABL_BINARY, ABL_ANTI, ABL_UNIT > 	_base;
	typedef unital_associative<RNG,MND_BINARY,MND_UNIT >		_utass_bind;
};

template<typename RNG,
typename ABL_BINARY,
typename ABL_ANTI,
typename MND_BINARY,
typename MND_UNIT,
typename ABL_UNIT = unit<RNG > >
struct uring : public cat::cat<uring_diag<RNG, ABL_BINARY, ABL_ANTI, MND_BINARY, MND_UNIT , ABL_UNIT > >{
	typedef uring_diag<RNG, ABL_BINARY, ABL_ANTI, MND_BINARY, MND_UNIT , ABL_UNIT >	_urng_bing;
	typedef ring_diag<RNG, ABL_BINARY, ABL_ANTI, ABL_UNIT > 						_rng_bind;
};


struct zero {
	static const zero& ZERO;
	struct add {zero operator()(const zero& z1, const zero& z2) const {return ZERO;}};
	struct mul {zero operator()(const zero& z1, const zero& z2) const {return ZERO;}};
	struct eq {bool operator()(const zero& z1, const zero& z2) const {return true;}};
	struct unit {
		template<typename U >
		zero operator()(const U& u) const {return ZERO;}
	};
	struct anti {

			zero operator()(const zero& z) const {return ZERO;}
		};
	typedef ring<zero,add,anti,mul,unit >		_rng_bind;
	typedef uring<zero,add,anti,mul,unit,unit >	_urng_bind;
};

template<
typename RNG,
typename ABL_BINARY,
typename ABL_ANTI,
typename SMG_BINARY,
typename ABL_UNIT = unit<RNG >
>
struct terminal_map {
	typedef ring<RNG,ABL_BINARY,ABL_ANTI,ABL_UNIT > _rng_bind;
	typedef cat::morph<_rng_bind,zero::_rng_bind,terminal_map<RNG,ABL_BINARY,ABL_ANTI,ABL_UNIT >  >
												_mor_bind;
};



} /*alg*/



#endif /* INCLUDE_ALG_1_1_RING_RING_RING_BASE_HPP_ */
