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

template<typename _iter
		,typename _ctype
		,typename _dtype>
struct fct_iter {
	typedef fct<_ctype,_dtype > 			_fct;
	typedef fct_iter<_iter,_ctype,_dtype >	_tc;
	_fct fct;
	_iter it;
	_tc& operator++(){
		++it;
		return *this;
	}
	_tc& operator--(){
		--it;
		return *this;
	}
	_dtype operator->() const {
		const _ctype* ptr = it.operator->();
		return ptr!=0?fct(*ptr):_dtype();
	}
	friend bool operator==(const _tc& i1, const _tc& i2){
		return i1.it==i2.it;
	}
	friend bool operator!=(const _tc& i1, const _tc& i2){
		return i1.it!=i2.it;
	}
};

template<typename _co_dom1
		,typename _co_dom2
		,typename _dom>
struct pfct : public fct <fct<std::pair<_co_dom1,_co_dom2 >, _dom>, fct<_co_dom1,fct<_co_dom2,_dom > > > {
	fct<_co_dom2, _dom > operator()(const fct<std::pair<_co_dom1,_co_dom2 >, _dom >& f) const {
		return [=] (const _co_dom1& arg1){
			return [=] (const _co_dom2& arg2){
				return f(std::pair<_co_dom1,_co_dom2 >::pair(arg1, arg2));
			};
		};
	}
};
}


#endif /* NUM_UTIL_UTIL_FCT_HPP_ */
