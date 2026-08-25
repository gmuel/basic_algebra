/*
 * auto_vec.hpp
 *
 *  Created on: 11.08.2026
 *      Author: fun_gab
 */

#ifndef INCLUDE_NUM_MATH_AUTO_VEC_HPP_
#define INCLUDE_NUM_MATH_AUTO_VEC_HPP_
#include "auto_diff.hpp"
#include "../util/util_fct.hpp"
#include <map>
namespace num {

template<typename _DUAL >
class vec : public expr<_DUAL >{

public:
	typedef _DUAL 								_dt;
	typedef vec<_dt >							_tc;
	typedef std::map<unsigned int,dual<_DUAL> > _map;
	typedef typename _map::const_iterator		_cit;
	typedef typename _map::iterator				_it;
	vec(const _dt& val = _dt(), unsigned i = 0):coefs(),eps(_dt::EPS){
		if(val!=0) coefs[i] = val;
	}
	template<typename _ITER >
	vec(_ITER i, _ITER e, unsigned int start = 0):coefs(),eps(_dt::EPS){
		for(;i!=e;++i) {
			const _dt& val = *i;
			if(val!=0) coefs[start] = val;
			++start;
		}
	}
	vec(const _tc& o):coefs(o.coefs),eps(_dt::EPS){}
	vec(_tc&& o):coefs(o.coefs),eps(_dt::EPS){}
	~vec(){}
	_tc& operator=(const _tc& o) = default;
	_tc& operator=(_tc&& o) = default;
	_dt& operator[](unsigned int i){
		return coefs[i];
	}
	_dt& operator[](unsigned int i) const {
		return coefs.operator [](i);
	}
	_tc& operator+=(const _tc& s) {
		for(auto i = s.coefs.cbegin(); i != s.coefs.cend(); ++i) {
			auto ii = coefs.find(i->first);
			if(ii!=coefs.end()) {
				_dt tmp = ii->second + i->second;
				if(tmp==0) coefs.erase(ii);
				else ii->second = tmp;
			}
			else coefs[i->first] = i->second;
		}
		return *this;
	}
	_tc& operator*=(const _dt& d) {
		for(auto i = coefs.begin();i!=coefs.end();++i) i->second *= d;
		return *this;
	}
	unsigned int suppCount() const {
		unsigned int cnt(0);
		_dt norm2 = norm2();
		for(const auto i = coefs.cbegin();i!=coefs.cend();++i) {
			if(abs(abs(i->second)-norm2)>eps) ++cnt;
		}
		return cnt;
	}
	_dt norm2() const {
		_dt nrm;
		for(auto i = coefs.cbegin(); i!=coefs.cend();++i) nrm += i->second * i->second;
		return sqrt(nrm);
	}
	friend _tc operator-(const _tc& s1) {
		_tc ng;
		for(auto i = s1.coefs.cbegin(); i!= s1.coefs.cend(); ++i)	ng[i->first] = -(i->second);
		return ng;
	}
	friend _tc operator+(const vec<_dt >& s1, const vec<_dt >& s2){
		const _tc* mx = 0,* mn = 0;
		unsigned int supp1 = s1.suppCount(), supp2 = s2.suppCount();
		if(supp1>supp2){
			mx = &s1;
			mn = &s2;
		}
		else {
			mx = &s2;
			mn = &s1;
		}
		_tc sum(*mx);
		sum += *mn;
		return sum;
	}
	friend _tc operator*(const _dt& s1, const _tc& s2){
		_tc scl(s2);
		for(auto i = scl.coefs.begin(); i!=scl.coefs.end();++i) i->second = s1 * i->second;
		return scl;
	}
	friend _tc operator*(const _tc& s1, const _dt& s2){
		_tc scl(s1);
		for(auto i = scl.coefs.begin(); i!=scl.coefs.end();++i) i->second = i->second * s2;
		return scl;
	}
private:
	_map coefs;
	_dt eps;

};


template<typename _DUAL >
class v_fct : public vec<_DUAL > {
	_fct f;
	vec<_DUAL > arg,
		img;
public:
	typedef _DUAL			_dt;
	typedef	vec<_dt >		_vc;
	typedef v_fct<_dt >		_tc;
	typedef fct<_vc, _vc >	_fc;
	struct _fct : public _fc {

	};
	_dt operator()(const _dt& x) const {
		return f(x);
	}
	_dt operator()(_dt&& x) {
		operator=(x);
		eval();
		return img;
	}
	_tc& operator=(const _tc& x) = default;
	_tc& operator=(_tc&& x) = default;
	_tc& operator=(const _vc& x) {
		arg = x;
		return *this;
	}
	_tc& operator=(_vc&& x) {
		arg = x;
		return *this;
	}
	const _fct* operator->() const {
		return &f;
	}
	void eval(){
		img = operator()(arg);
	}

};


}

#endif /* INCLUDE_NUM_MATH_AUTO_VEC_HPP_ */
