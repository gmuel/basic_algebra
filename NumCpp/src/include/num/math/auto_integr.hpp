/*
 * auto_integr.hpp
 *
 *  Created on: 25.08.2026
 *      Author: fun_gab
 */

#ifndef INCLUDE_NUM_MATH_AUTO_INTEGR_HPP_
#define INCLUDE_NUM_MATH_AUTO_INTEGR_HPP_
#include "auto_vec.hpp"
#include <utility>


namespace num {

template<typename _DUAL >
struct rk4_fctl : public fct<std::pair<v_fct<_DUAL>, _DUAL >, v_fct<_DUAL > > {
	v_fct<_DUAL > operator()(const std::pair<v_fct<_DUAL >, _DUAL >& arg) const {
		return operator()(arg.first,arg.second);
	}
	v_fct<_DUAL > operator()(const v_fct<_DUAL >& f, const _DUAL& s) const {
		return [=] (const vec<_DUAL >& arg){
			_DUAL scl1 = s/6, scl2 = .5 * s;
			const vec<_DUAL >& k1 = f(arg),& k2 = f(arg + scl2 * k1),& k3 = f(arg + scl2 * k2),
					& k4 = f(arg + s * k3);

			return arg + scl1 * (k1 + 2 * (k2 + k3) + k4);
		};
	}
};
template<typename _DUAL >
class rk4 : public v_fct<_DUAL > {
public:

};

template<typename _DUAL >
struct rk38_fctl : public fct<std::pair<v_fct<_DUAL>, _DUAL >, v_fct<_DUAL > > {

	v_fct<_DUAL > operator()(const std::pair<v_fct<_DUAL >, _DUAL >& arg) const {
		return operator()(arg.first,arg.second);
	}
	v_fct<_DUAL > operator()(const v_fct<_DUAL >& f, const _DUAL& s) const {
		return [=] (const vec<_DUAL >& arg){
			_DUAL scl1 = s/8, scl2 = s/3;
			const vec<_DUAL >& k1 = f(arg),& k2 = f(arg + scl2 * k1),& k3 = f(arg + s * k2 - scl2 * k1),
					& k4 = f(arg + s * (k1 - k2 + k3));

			return arg + scl1 * (k1 + 3 * (k2 + k3) + k4);
		};
	}
};
}



#endif /* INCLUDE_NUM_MATH_AUTO_INTEGR_HPP_ */
