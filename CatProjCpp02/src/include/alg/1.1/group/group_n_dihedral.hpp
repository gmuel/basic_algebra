/*
 * group_n_dihedral.hpp
 *
 *  Created on: 01.12.2019
 *      Author: lmmac
 */

#ifndef INCLUDE_ALG_1_1_GROUP_GROUP_N_DIHEDRAL_HPP_
#define INCLUDE_ALG_1_1_GROUP_GROUP_N_DIHEDRAL_HPP_


#include "group_base_impl.hpp"

namespace alg {
template<unsigned int N >
struct n_di_mul;
/**
 * @brief N dihedral group class template
 *
 * Modelled via \f[\mathbb{Z}_n \rtimes\mid_\alpha \mathbb{Z}_2\f], i.e.
 * the semi direct product of cyclic group of order \f[n\f], the smallest
 * non-trivial Coxeter group \f[\mathbb{Z}\f] and \f[\alpha = [x \longmapsto [y \longmapsto (-1)^{x} y]]\f]
 * @tparam N order of non-trivial normal divisor
 */
template<unsigned int N >
class n_dihedral {
public:
	/**
	 * @brief
	 * @param arg defaults to <tt>unit&ltcyclic_wrp&ltN &gt &gt::UNIT</tt>
	 * @param _sign
	 */
	n_dihedral(const cyclic_wrp<N >& arg = unit<cyclic_wrp<N > >::UNIT,
		bool _sign = false):
		cycle(arg),sign(_sign){}
	/**@brief Copy constr*/
	n_dihedral(const n_dihedral<N >& o):cycle(o.cycle),sign(o.sign){}
	~n_dihedral(){}
	n_dihedral& operator=(const n_dihedral<N >& o){
		cycle = o.cycle;
		sign  = o.sign;
		return *this;
	}
private:
	friend struct n_di_mul<N >;
	cyclic_wrp<N > cycle;
	bool sign;
};
template<unsigned int N >
struct n_di_mul : public binary<n_dihedral<N >, n_di_mul<N > > {
	n_dihedral<N > operator()(const n_dihedral<N >& m1, const n_dihedral<N >& m2) const {
		return n_dihedral<N >(m1.sign?m1.cycle-m2.cycle:m1.cycle+m2.cycle,
				m1.sign==m2.sign?false:true);
	}
};
template<unsigned int N >
struct n_di_anti: public antipode<n_dihedral<N >, n_di_anti<N > > {
	n_dihedral<N > operator()(const n_dihedral<N >& m) const {
		return n_dihedral<N>(-m.cycle,!m.sign);
	}
};

template<unsigned int N >
class n_dihedral_group : public group<n_dihedral<N >,
									n_di_mul<N >,
									n_di_anti<N > > {

};



} /*alg*/




#endif /* INCLUDE_ALG_1_1_GROUP_GROUP_N_DIHEDRAL_HPP_ */
