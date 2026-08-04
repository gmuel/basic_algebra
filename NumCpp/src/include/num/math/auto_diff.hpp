/*
 * auto_diff.hpp
 *
 *  Created on: 03.08.2026
 *      Author: fun_gab
 */

#ifndef INCLUDE_AUTO_DIFF_HPP_
#define INCLUDE_AUTO_DIFF_HPP_

#include <cmath>
#include <iostream>

namespace num {

template<typename _EXPR_TYPE >
struct expr {

};

template<typename _DUAL_TYPE >
struct dual { // : public _DUAL_TYPE {
	typedef _DUAL_TYPE _dt;
	typedef dual<_dt > _tc;
	_dt val, del;

	dual(const _dt& vl = _dt(), const _dt& dl = _dt()):val(vl),del(dl){}
	dual(int vl, int dl = 0):val(vl),del(dl){}
	dual(const _tc& o):val(o.val),del(o.del){}
	dual(_tc&& o):val(o.val),del(o.del){}
	~dual(){}

	_tc& operator=(const _dt& d){
		val = d;
		del = 0;
		return *this;
	}
	_tc& operator=(int d){
		val = d;
		del = 0;
		return *this;
	}
	_tc& operator=(const _tc& d){
		val = d.val;
		del = d.del;
		return *this;
	}

	friend bool operator==(const _tc& s1, const _tc& s2){
		return s1.val==s2.val && s1.del == s2.del;
	}
	friend bool operator!=(const _tc& s1, const _tc& s2){
		return ! (s1==s2);
	}

	friend _tc operator+(const _tc& s1, const _tc& s2) {
		return _tc{s1.val+s2.val,s1.del+s2.del};
	}
	friend _tc operator+(const _tc& s1, const _dt& s2) {
		return _tc{s1.val+s2,s1.del};
	}
	friend _tc operator+(const _dt& s1, const _tc& s2) {
		return _tc{s1+s2.val,s2.del};
	}
	friend _tc operator*(const _tc& s1, const _tc& s2) {
		return _tc{s1.val*s2.val,s1.del*s2.val+s1.val*s2.del};
	}
	friend _tc operator*(const _tc& s1, const _dt& s2) {
		return _tc{s1.val*s2,s1.del*s2};
	}
	friend _tc operator*(const _dt& s1, const _tc& s2) {
		return _tc{s1*s2.val,s1*s2.del};
	}
	friend _tc operator-(const _tc& s){
		return _tc{-s.val,-s.del};
	}
	friend _tc operator-(const _tc& s1, const _tc& s2){
		return _tc{s1.val-s2.val,s1.del-s2.del};
	}
	friend _tc operator-(const _tc& s1, const _dt& s2){
			return _tc{s1.val-s2,s1.del};
	}
	friend _tc operator-(const _dt& s1, const _tc& s2){
			return _tc{s1-s2.val,-s2.del};
	}
	friend _tc operator/(const _tc& s1, const _tc& s2){
		_dt scl = 1/s2.val, scl1 = s1.val*scl;
		return _tc{scl1,(s1.del-scl1*s2.del)*scl};
	}
	friend _tc operator/(const _tc& s1, const _dt& s2){
		return s1 * (1/s2);
	}
	friend _tc operator/(const _dt& s1, const _tc& s2){
		_dt scl = 1/s2.val, scl1 = s1*scl;
		return _tc{scl1,-scl1*s2.del*scl};
	}
	friend std::ostream& operator<<(std::ostream& o, const 	_tc& s){
		return o << s.val << " + " << s.del << " d";
	}
	friend std::istream& operator>>(std::istream& i, 		_tc& s){
		i >> s.val;
		i >> s.del;
		return i;
	}
};

typedef dual<float > 		_fdual;
typedef dual<double >		_ddual;
typedef dual<long double >	_ldual;

template<typename _DUAL >
_DUAL abs(const dual<_DUAL >& s){
	return std::abs(s.val);
}
template<typename _DUAL >
dual<_DUAL > dabs(const dual<_DUAL >& s){
	return dual<_DUAL >{std::abs(s.val),s.val<0?-s.del:s.del};
}
template<typename _DUAL >
dual<_DUAL > sqrt(const dual<_DUAL >& s){
	_DUAL sqr = std::sqrt(s.val);
	return dual<_DUAL >{sqr,_DUAL(.5)*s.del/sqr};
}
template<typename _DUAL >
dual<_DUAL > pow(const dual<_DUAL >& s, int i){
	if(i==0) return dual<_DUAL >(s.val==0?0:1);
	if(i==1) return s;
	if(i<0) return pow(1/s,-i);
	_DUAL pw = i==2?s.val:std::pow(s.val, i-1);
	return dual<_DUAL >{pw*s.val,i*pw*s.del};
}
template<typename _DUAL >
dual<_DUAL > log(const dual<_DUAL >& s){
	return dual<_DUAL >{std::log(s.val),s.del/s.val};
}
template<typename _DUAL >
dual<_DUAL > exp(const dual<_DUAL >& s){
	return std::exp(s.val) * dual<_DUAL >{_DUAL(1),s.del};
}
template<typename _DUAL >
dual<_DUAL > sin(const dual<_DUAL >& s){
	return dual<_DUAL >{std::sin(s.val),s.del*std::cos(s.val)};
}
template<typename _DUAL >
dual<_DUAL > cos(const dual<_DUAL >& s){
	return dual<_DUAL >{std::cos(s.val),-s.del*std::sin(s.val)};
}
template<typename _DUAL >
dual<_DUAL > sinh(const dual<_DUAL >& s){
	return dual<_DUAL >{std::sinh(s.val),std::cosh(s.val)*s.del};
}
template<typename _DUAL >
dual<_DUAL > cosh(const dual<_DUAL >& s){
	return dual<_DUAL >{std::cosh(s.val),-std::sinh(s.val)*s.del};
}
}

#endif /* INCLUDE_AUTO_DIFF_HPP_ */
