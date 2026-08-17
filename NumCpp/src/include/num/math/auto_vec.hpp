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
	vec(unsigned i = 0, const _dt& val = _dt()):coefs(){
		if(val!=0) coefs[i] = val;
	}
	template<typename _ITER >
	vec(_ITER i, _ITER e, unsigned int start = 0):coefs(){
		for(;i!=e;++i) {
			const _dt& val = *i;
			if(val!=0) coefs[start] = val;
			++start;
		}
	}
	vec(const _tc& o):coefs(o.coefs){}
	vec(_tc&& o):coefs(o.coefs){}
	~vec(){}
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
	friend _tc operator+(const vec<_dt >& s1, const vec<_dt >& s2){
		const _tc* mx,* mn;
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
		_dt nrm = sum.norm2();
		nrm *= nrm;
		for(_cit i = mn->coefs.cbegin(); i!=mn->coefs.cend();++i) {
			auto tmp = sum[i->first] + i->second;
			if(dabs(dabs(sum)-nrm)>sum.eps) {
				sum[i->first] = tmp;
				nrm += nrm + tmp * tmp;
			}
			else sum.coefs.erase(i->first);
		}
		return sum;
	}
	friend _tc operator*(const _dt& s1, const _tc& s2){

	}
private:
	_map coefs;
	_dt eps;

};

}

#endif /* INCLUDE_NUM_MATH_AUTO_VEC_HPP_ */
