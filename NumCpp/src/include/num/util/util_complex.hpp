/*
 * util_complex.hpp
 *
 *  Created on: 20.06.2026
 *      Author: stream
 */

#ifndef NUM_UTIL_UTIL_COMPLEX_HPP_
#define NUM_UTIL_UTIL_COMPLEX_HPP_
#include "../math/math_fun.hpp"

namespace num {

template<typename _FLOAT_TYPE >
struct complex {
	typedef _FLOAT_TYPE _flt;
	_flt real, image;
	friend complex<_FLOAT_TYPE > operator+(const complex<_FLOAT_TYPE >& c1, const complex<_FLOAT_TYPE >& c2){
		return complex<_FLOAT_TYPE >{c1.real+c2.real,c1.image+c2.image};
	}
	friend complex<_FLOAT_TYPE > operator+(const _FLOAT_TYPE& c1, const complex<_FLOAT_TYPE >& c2){
		return complex<_FLOAT_TYPE >{c1+c2.real,c2.image};
	}
	friend complex<_FLOAT_TYPE > operator+( const complex<_FLOAT_TYPE >& c1,const _FLOAT_TYPE& c2){
		return complex<_FLOAT_TYPE >{c1.real+c2,c1.image};
	}

	friend complex<_FLOAT_TYPE > operator*(const complex<_FLOAT_TYPE >& c1, const complex<_FLOAT_TYPE >& c2){
		return complex<_FLOAT_TYPE >{c1.real*c2.real-c1.image*c2.image,c2.real*c1.image+c1.real*c2.image};
	}
	friend complex<_FLOAT_TYPE > operator*(const _FLOAT_TYPE& c1, const complex<_FLOAT_TYPE >& c2){
		return complex<_FLOAT_TYPE >{c1*c2.real,c1*c2.image};
	}
	friend complex<_FLOAT_TYPE > operator*( const complex<_FLOAT_TYPE >& c1,const _FLOAT_TYPE& c2){
		return complex<_FLOAT_TYPE >{c1.real*c2,c1.image*c2};
	}
	friend bool operator==(const complex<_FLOAT_TYPE >& c1, const complex<_FLOAT_TYPE >& c2){
		return c1.real==c2.real && c1.image==c2.image;
	}
	friend _FLOAT_TYPE abs(const complex<_FLOAT_TYPE >& val){
		return sqrt(val.real*val.real+val.image*val.image);
	}
};



}


#endif /* NUM_UTIL_UTIL_COMPLEX_HPP_ */
