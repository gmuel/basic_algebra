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
	_EXPR_TYPE val;
	void eval(){}
};

template<typename _DUAL_TYPE >
struct dual : public expr<_DUAL_TYPE > {
	typedef _DUAL_TYPE _dt;
	typedef dual<_dt > _tc;

	const static _dt	EPS;
	_dt del;

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
	_tc& operator+=(const _tc& d){
		val = val + d.val;
		del = del + d.del;
		return *this;
	}
	_tc& operator-=(const _tc& d){
		val = val - d.val;
		del = del - d.del;
		return *this;
	}
	_tc& operator*=(const _tc& d){
		val = val * d.val;
		del = del * d.val + val * d.del;
		return *this;
	}
	_tc& operator/=(const _tc& d){
		_dt scl = 1/d.val, scl1 = val*scl;
		val = scl1;
		del = (s1.del-scl1*s2.del)*scl;
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
	friend _dt abs(const _tc& s){
		return std::abs(s.val);
	}
	friend _tc dabs(const _tc& s){
		return _tc{std::abs(s.val),s.val<0?-s.del:s.del};
	}
	friend _tc sqrt(const _tc& s){
		_dt sqr = std::sqrt(s.val);
		return _tc{sqr,_dt(.5)*s.del/sqr};
	}
	friend _tc pow(const _tc& s, int i){
		if(i==0) return _tc(s.val==0?0:1);
		if(i==1) return s;
		if(i<0) return pow(1/s,-i);
		_dt pw = i==2?s.val:std::pow(s.val, i-1);
		return _tc{pw*s.val,i*pw*s.del};
	}
	friend _tc log(const _tc& s){
		return _tc{std::log(s.val),s.del/s.val};
	}

	friend _tc exp(const _tc& s){
		auto val = std::exp(s.val);
		return _tc{val,val*s.del};
	}
	friend _tc sin(const _tc& s){
		return _tc{std::sin(s.val),s.del*std::cos(s.val)};
	}
	friend _tc cos(const _tc& s){
		return _tc{std::cos(s.val),-s.del*std::sin(s.val)};
	}
	friend _tc sinh(const _tc& s){
		return _tc{std::sinh(s.val),std::cosh(s.val)*s.del};
	}
	friend _tc cosh(const _tc& s){
		return _tc{std::cosh(s.val),-std::sinh(s.val)*s.del};
	}
};

typedef dual<float > 		_fdual;
typedef dual<double >		_ddual;
typedef dual<long double >	_ldual;

template< >
const float dual<float >::EPS = 1e-8;

template< >
const double dual<double >::EPS = 1e-15;

template< >
const long double dual<long double >::EPS = 1e-28;
}

#endif /* INCLUDE_AUTO_DIFF_HPP_ */
