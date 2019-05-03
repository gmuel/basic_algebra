/*
 * lie_base.hpp
 *
 *  Created on: 03.05.2019
 *      Author: stream_vid
 */

#ifndef INCLUDE_LIE_BASE_HPP_
#define INCLUDE_LIE_BASE_HPP_

#include "algebra_base.hpp"

namespace alg {

template<typename g,
typename BASE_RNG,
typename MOD_BINARY,
typename ANTI,
typename LSCAL,
typename RSCAL,
typename LIE_BINARY,
typename UNIT = unit<g >
>
struct lie_diag : public alg_diag<g,BASE_RNG,MOD_BINARY, ANTI,LSCAL,RSCAL,LIE_BINARY,UNIT > {
	typedef alg_diag<g,BASE_RNG,MOD_BINARY,ANTI,LSCAL,RSCAL,LIE_BINARY,UNIT>	_alg_bind;
	static const LIE_BINARY& LIE;
	struct jacobian_id  {
		bool operator()(const g& g1, const g& g2, const g& g3) const{return LIE(LIE(-g1,g2),g3)==LIE(LIE(g2,g3),g1)+LIE(LIE(g3,g1),g2);}
	};
	struct anti_symm {
		bool operator()(const g& g1, const g& g2) const{return LIE(g1,g2)==-LIE(g2,g1);}
	};


};

template<typename T, typename BINARY, typename BINARY2, typename ANTI >
struct commutator  {
	typedef binary<T,BINARY >		_bin_bind;
	using _bin_bind::OPERATION;
	typedef binary<T,BINARY2 >	_bin2;
	typedef antipode<T, ANTI >		_anti;
	T operator()(const T& t1, const T& t2) const {
		return _bin2::OPERATION(OPERATION(t1,t2),_anti::ANTIPODE(OPERATION(t2,t1)));
	}
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
struct assc_alg_2_lie : public cat::functor<
	assc_alg_diag<A,BASE_RNG,MOD_BINARY, ANTI,LSCAL,RSCAL,ALG_BINARY,UNIT > ,
	lie_diag<A,BASE_RNG,MOD_BINARY,ANTI,LSCAL,RSCAL,commutator<A,ALG_BINARY,MOD_BINARY,ANTI > > > {
	typedef cat::functor<
		assc_alg_diag<A,BASE_RNG,MOD_BINARY, ANTI,LSCAL,RSCAL,ALG_BINARY,UNIT > ,
		lie_diag<A,BASE_RNG,MOD_BINARY,ANTI,LSCAL,RSCAL,commutator<A,ALG_BINARY,MOD_BINARY,ANTI > > >
		_fct_bind;
	using _fct_bind::_pre;
	using _fct_bind::_im;

};




} /*alg*/


#endif /* INCLUDE_LIE_BASE_HPP_ */
