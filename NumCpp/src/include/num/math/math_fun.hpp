/*
 * math_fun.hpp
 *
 *  Created on: 20.06.2026
 *      Author: stream
 */

#ifndef NUM_MATH_MATH_FUN_HPP_
#define NUM_MATH_MATH_FUN_HPP_

namespace num {

float abs(const float& flt){return flt>=0?flt:-flt;}
double abs(const double& dbl) {return dbl>=0?dbl:-dbl;}

const float& ZERO_F=0f;


template<typename _FLOAT_TYPE >
_FLOAT_TYPE sqrt(const _FLOAT_TYPE& flt, const _FLOAT_TYPE& eps, unsigned int max_it = 50){
	_FLOAT_TYPE val (flt);

	unsigned int i (0);
	while (i<max_it&&abs(val*val-flt)>eps) {
		val = .5*(val+flt/val);
		++i;
	}
	return val;
}
template<typename _FLOAT_TYPE >
_FLOAT_TYPE tk(const _FLOAT_TYPE& val, unsigned int max){
	_FLOAT_TYPE tk = 1, sqr = (val*val);
	for(unsigned int i = 0; i <= max; ++i){
		unsigned int idx = max - i, idx4 = idx*4;
		tk = ((idx4+1)*(idx4+5)-4*sqr)*(idx4+3)+(idx4+3)*sqr/(1+sqr*(idx4+9)/tk);
	}
	return tk;
}
template<typename _FLOAT_TYPE >
_FLOAT_TYPE tan(const _FLOAT_TYPE& val, const _FLOAT_TYPE& eps, unsigned int max_it = 12){
	_FLOAT_TYPE tn, tn1(1), sqr (val*val);
	unsigned int cnt(4);
	while(cnt<max_it&&abs(tn1-tn)>eps) {
		tn = tn1;
		_FLOAT_TYPE tmp = tk(val,cnt);
		tn1 = 5*sqr/(5*sqr+tk);
		++cnt;
	}
	return tn1;
}

}

#endif /* NUM_MATH_MATH_FUN_HPP_ */
