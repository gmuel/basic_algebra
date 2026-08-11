/*
 * auto_vec.hpp
 *
 *  Created on: 11.08.2026
 *      Author: fun_gab
 */

#ifndef INCLUDE_NUM_MATH_AUTO_VEC_HPP_
#define INCLUDE_NUM_MATH_AUTO_VEC_HPP_
#include "auto_diff.hpp"
#include <map>
namespace num {

template<typename _DUAL >
class vec {

public:
	typedef _DUAL 								_dt;
	typedef vec<_dt >							_tc;
	typedef std::map<unsigned int,dual<_DUAL> > _map;
	typedef typename _map::const_iterator		_cit;
	typedef typename _map::iterator				_it;
	_dt& operator[](unsigned int i){
		return coefs[i];
	}
	friend vec<_dt > operator+(const vec<_dt >& s1, const vec<_dt >& s2){
		_tc sum;

	}
	unsigned int suppCount() const {
		unsigned int cnt(0);
		for(const auto i = coefs.cbegin();i!=coefs.cend();++i) {
			if(abs(i->second-norm2)>eps) ++cnt;
		}
		return cnt;
	}
private:
	_map coefs;
	_dt norm2,eps;

};

}

#endif /* INCLUDE_NUM_MATH_AUTO_VEC_HPP_ */
