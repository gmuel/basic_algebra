/*
 * sym.hpp
 *
 *  Created on: 12.05.2026
 *      Author: fun_gab
 */

#ifndef INCLUDE_SYM_HPP_
#define INCLUDE_SYM_HPP_
#include "group_cycle.hpp"
#include <set>
#include <utility>
namespace sym {

using namespace alg;

template<unsigned int N>
std::set<cycle<N> > common_orbit(const cycle<N>& c1, const cycle<N>& c2){
	std::set<cycle<N> > s;
	bool lng = c1.length()<c2.length();
	auto i = lng?c1.cbegin():c2.cbegin(),
			e = lng?c1.cend():c2.cend();
	while(i!=e) {
		if((lng && c2.contains(*i))||c1.contains(*i)) s.insert(*i);
		++i;
	}
	return s;
}

template<unsigned int N>
class sym {
public:
	typedef cyclic_wrp<N > 			_cyc;
	typedef cycle<N >		 		_cye;
	typedef std::map<_cyc, _cye > 	_map;
	sym(const _cyc& x1, const _cyc& x2):cyc_map(){
		if(x1!=x2){
			cyc_map[x1] = cyc_map[x2] = _cye(x1,x2);
		}
	}
	sym(const _cye& c):cyc_map(){
		this->add_cycle(c);
	}
	/**
	 * Construct permutation of all
	 * disjoint cycles returned by given iterator
	 * @tparam IT iter type - deref operator must return a alg::cyclic_wrp&lt;N&gt;
	 */
	template<typename IT>
	sym(IT i, IT e):cyc_map(){
		for(; i!=e;++i) {
			if(is_disjoint(*i))
				add_cycle(*i);
		}
	}
	sym(const sym& s):cyc_map(s.cyc_map){}
	sym(sym&& s):cyc_map(s.cyc_map){}
	~sym(){}
	const _cyc& operator()(const _cyc& c) const {
		auto i = cyc_map.find(c);
		return i==cyc_map.end()?c:i->second(c);
	}
	sym& operator=(const _cye& c) {
		cyc_map.clear();
		add_cycle(c);
		return *this;
	}
private:
	_map cyc_map;
	void add_cycle(const _cye& c) {
		add_cycle(c,cyc_map);
	}
	void add_cycle(const _cye& c, _map& _mp) {
			_cyc curr = c.firstElement();
			do {
				_mp[curr] = c;
				curr = c(curr);
			}while(curr!=c.firstElement());
		}
	bool is_disjoint(const _cye& c) const {
		_cye* last(0);
		for(auto i = cyc_map.begin(); i!=cyc_map.end();++i){
			if(last==&(i->second)) continue;
			last = &(i->second);
			if(common_orbit(i->second,c).size()!=0) return false;
		}
		return true;
	}
	std::pair<_map,_map > joint_orbit(const sym<N>& s) const {
		_map lhs, rhs;

		bool lhs_sz = cyc_map.size()<s.cyc_map.size();
		auto i = lhs_sz?cyc_map.begin():s.cyc_map.begin()
				, e = lhs_sz?cyc_map.end():s.cyc_map.end();
		while (i!=e){
			auto ii = lhs_sz?s.cyc_map.find(i->first):cyc_map.find(i->first),
					ee = lhs_sz?s.cyc_map.end():cyc_map.end();
			if(ii!=ee) {
				add_cycle( i->second, lhs);
				add_cycle(ii->second, rhs);
			}
			++i;
		}
		return std::make_pair(lhs, rhs);
	}
	_map merge(const _cye& lhs, const _cye& rhs){
		std::set<_cyc> orb = common_orbit(rhs, lhs);
		_map comm;
		_cye c;
		for(auto i = orb.begin(); i!=orb.end();++i){

		}
		return comm;
	}
};
template<>
class sym<1u> {
	sym(){}
	sym(const sym<1u>& o){}
	sym(sym<1u>&& o){}
	sym<1u>& operator=(const sym<1u>& s){return *this;}
public:
	~sym(){}
	static const sym<1u>& ID;
	friend const sym<1u>& operator*(const sym<1u>& s1, const sym<1u>& s2) {return ID;}
};

template<>
class sym<0u> : public sym<1u> {};

template<>
class sym<2u> : public cyclic_wrp<2u> {};

}


#endif /* INCLUDE_SYM_HPP_ */
