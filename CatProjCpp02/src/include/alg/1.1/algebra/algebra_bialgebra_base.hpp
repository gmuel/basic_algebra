/*
 * algebra_bialgebra_base.hpp
 *
 *  Created on: 19.02.2023
 *      Author: stream_vid
 */

#ifndef INCLUDE_ALG_1_1_ALGEBRA_ALGEBRA_BIALGEBRA_BASE_HPP_
#define INCLUDE_ALG_1_1_ALGEBRA_ALGEBRA_BIALGEBRA_BASE_HPP_
#include "algebra_algebra_base.hpp"
namespace alg {

template<typename C>
struct counit {
	static const C COUNIT;
	template<typename T>
	C operator () (const T& t) const{
		return COUNIT;
	}
};


template<typename C
	,template<class > class T
>
struct coproduct {
	T<T1> operator()(const C& c) const {
		return T<T1>(c);
	}
};


template<typename C,
typename BASE_RNG,
typename MOD_BINARY,
typename ANTI,
typename LSCAL,
typename RSCAL,
typename ALG_COBINARY,
typename COUNIT =counit<C >
>
struct coalgebra : public alg::mod<> {

};
}




#endif /* INCLUDE_ALG_1_1_ALGEBRA_ALGEBRA_BIALGEBRA_BASE_HPP_ */
