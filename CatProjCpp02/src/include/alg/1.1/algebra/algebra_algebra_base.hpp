/*
 * algebra_algebra_base.hpp
 *
 *  Created on: 01.12.2019
 *      Author: lmmac
 */

#ifndef INCLUDE_ALG_1_1_ALGEBRA_ALGEBRA_ALGEBRA_BASE_HPP_
#define INCLUDE_ALG_1_1_ALGEBRA_ALGEBRA_ALGEBRA_BASE_HPP_


#include "../modules/modules_module.hpp"

namespace alg {

template<typename A,
typename BASE_RNG,
typename MOD_BINARY,
typename ANTI,
typename LSCAL,
typename RSCAL,
typename ALG_BINARY,
typename UNIT = unit<A >
>
struct alg_diag : public mod_diag<A,BASE_RNG,MOD_BINARY, ANTI,LSCAL,RSCAL,UNIT> {
	typedef mod_diag<A,BASE_RNG,MOD_BINARY,ANTI,LSCAL,RSCAL,UNIT>	_mod_bind;
	typedef mod_diag<A,A,MOD_BINARY,ANTI,ALG_BINARY,UNIT >			_alg_mod_bind;

};

template<typename A,
typename BASE_RNG,
typename MOD_BINARY,
typename ANTI,
typename LSCAL,
typename RSCAL,
typename ALG_BINARY,
typename UNIT = unit<A >
>
struct assc_alg_diag : public alg_diag<A,BASE_RNG,MOD_BINARY, ANTI,LSCAL,RSCAL,ALG_BINARY,UNIT> {
	typedef alg_diag<A,BASE_RNG,MOD_BINARY,ANTI,LSCAL,RSCAL,ALG_BINARY,UNIT>	_alg_bind;
	typedef alg::ring_diag<A,MOD_BINARY,ANTI,ALG_BINARY,UNIT >					_rng_bing;
};

template<typename A,
typename BASE_RNG,
typename MOD_BINARY,
typename ANTI,
typename LSCAL,
typename RSCAL,
typename ALG_BINARY,
typename ALG_UNIT,
typename UNIT = unit<A >
>
struct uassc_alg_diag : public assc_alg_diag<A,BASE_RNG,MOD_BINARY, ANTI,LSCAL,RSCAL,ALG_BINARY,UNIT> {
	typedef assc_alg_diag<A,BASE_RNG,MOD_BINARY,ANTI,LSCAL,RSCAL,ALG_BINARY,UNIT>	_alg_bind;
	typedef alg::uring_diag<A,MOD_BINARY,ANTI,ALG_BINARY,ALG_UNIT,UNIT >			_urng_bing;
};






} /*alg*/





#endif /* INCLUDE_ALG_1_1_ALGEBRA_ALGEBRA_ALGEBRA_BASE_HPP_ */
