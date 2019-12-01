/*
 * ring_ring_base.hpp
 *
 *  Created on: 01.12.2019
 *      Author: lmmac
 */

#ifndef INCLUDE_ALG_1_1_RING_RING_RING_BASE_HPP_
#define INCLUDE_ALG_1_1_RING_RING_RING_BASE_HPP_


#include "group_base.hpp"


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

} /*alg*/



#endif /* INCLUDE_ALG_1_1_RING_RING_RING_BASE_HPP_ */
