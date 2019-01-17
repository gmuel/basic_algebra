#ifndef H_RING_BASE_HPP

#define  H_RING_BASE_HPP

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
	typedef ring_diag<RNG, ABL_BINARY, ABL_ANTI, ABL_UNIT > 	_rng_bind;
};

} /*alg*/

#endif /* H_RING_BASE_HPP */
