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
class rk4 : public v_fct<_DUAL > {
public:
	struct _fctl : public fct<std::pair<v_fct<_DUAL>, _DUAL >, v_fct<_DUAL > > {
		v_fct<_DUAL > operator()(const std::pair<v_fct<_DUAL >, _DUAL >& arg) const {
			return operator()(arg.first,arg.second);
		}
		v_fct<_DUAL > operator()(const v_fct<_DUAL >& f, const _DUAL& s){
			return [=] (const vec<_DUAL >& arg){
				const vec<_DUAL >& k1 = f(arg),& k2 = f(arg + .5 * s * k1),& k3 = f(arg + .5 * s * k2),
						& k4 = f(arg + s * k3);
				_DUAL scl = s/6;
				return arg + scl * (k1 + 2 * (k2 + k3) + k4);
			};
		}
	};
};

}



#endif /* INCLUDE_NUM_MATH_AUTO_INTEGR_HPP_ */
