/*
 * util_fct.hpp
 *
 *  Created on: 20.06.2026
 *      Author: stream
 */

#ifndef NUM_UTIL_UTIL_FCT_HPP_
#define NUM_UTIL_UTIL_FCT_HPP_


namespace num {

template<typename _co_dom_type
		,typename _dom_type>
struct fct {
	typedef _co_dom_type _cd;
	typedef _dom_type	 _dm;
	_dm operator()(const _cd& arg) {
		return _dm();
	}
	template<typename _cd1>
	friend fct<_cd1,_dm > operator*(const fct<_cd,_dm>& lf, const fct<_cd1,_cd >& rf){

		return [=] (const _cd1& arg){return lf(rf(arg));};
	}
};

}


#endif /* NUM_UTIL_UTIL_FCT_HPP_ */
