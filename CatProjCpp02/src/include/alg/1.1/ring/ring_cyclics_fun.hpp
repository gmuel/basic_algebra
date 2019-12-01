/*
 * ring_cyclics_fun.hpp
 *
 *  Created on: 01.12.2019
 *      Author: lmmac
 */

#ifndef INCLUDE_ALG_1_1_RING_RING_CYCLICS_FUN_HPP_
#define INCLUDE_ALG_1_1_RING_RING_CYCLICS_FUN_HPP_


#include "cyclics.hpp"
namespace alg {
/**
 * @brief Cyclic number project structure template
 *
 * Acts as a function object mapping elements in \f\mathbb{Z}_N \longrightarrow \mathbb{Z}_D\f
 * <br /><b>Note</b>, only instances with <tt>N % D == 0</tt> returning true are valid function objects,
 * their counterparts will fail to compile if their function operators are called.<br />
 * In particular, an compile error indicating initialization of mapping reference with incorrect type. Those valid
 * function objects are in fact ring homomorphisms.<br />
 * The following specializations are provided:
 * <ol>
 * <li><tt>typedef cyclic_project&lt0u,0u &gt id_int</tt> is the identity map</li>
 * <li><tt>template&ltunsigned N &gt struct cyclic_project&ltN, 0u >;</tt> is an incomplete
 * type</li>
 * </ol>
 * @tparam N some non-negative integer
 * @tparam D some non-negative integer, only valid function objects if \fD \mid N\f
 */
template<unsigned int N, unsigned int D >
struct cyclic_project  {

	typedef alg_fun::cyclic<N > _pr_cyc;
	typedef alg_fun::cyclic<D > _im_cyc;
	/**
	 * @brief The actual mapping template
	 * @tparam dummy parameter, only true version will be defined
	 */
	template<bool B >
	struct cyc_proj {
		const _im_cyc& operator()(const _pr_cyc& arg) const {
			return _im_cyc::VALS[_pr_cyc::value(arg)%D];
		}
		~cyc_proj(){}
		friend struct cyclic_project<N,D >;
	private:
		cyc_proj(){}
	};
	/**@brief Mapping ref - only a true version will be defined*/
	static const cyc_proj<(N%D == 0) >& MAP_REF;
	/**
	 * @brief Function operator
	 * <p>Will not compile as
	 */
	const _im_cyc& operator()(const _pr_cyc& arg) const {
		return MAP_REF(arg);
	}
};
template<unsigned int N, unsigned int D >
const typename cyclic_project<N,D >::template cyc_proj<(N%D == 0) >& cyclic_project<N,D >::MAP_REF = cyclic_project<N,D >::cyc_proj<true >();
template<unsigned int N >
struct cyclic_project <N,0u >;
template< >
struct cyclic_project<0u, 0u > {
	typedef alg_fun::cyclic<0u > _pr_cyc;
	typedef _pr_cyc _im_cyc;
	_im_cyc operator()(const _pr_cyc& arg) const ;
};
typedef cyclic_project<0u,0u > id_int;
} /*alg*/






#endif /* INCLUDE_ALG_1_1_RING_RING_CYCLICS_FUN_HPP_ */
