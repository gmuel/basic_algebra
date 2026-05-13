/*
 * group_cycles.hpp
 *
 *  Created on: 04.01.2022
 *      Author: stream_vid
 */

#ifndef INCLUDE_ALG_1_1_GROUP_GROUP_CYCLES_HPP_
#define INCLUDE_ALG_1_1_GROUP_GROUP_CYCLES_HPP_
#include "group_group_base_impl.hpp"
#include <map>

namespace alg {

template<unsigned int N >

class cycle_element {
public:

	typedef cyclic_wrp<N >					_cyclic;
	typedef struct  {

		bool operator()(const _cyclic& i1, const _cyclic& i2) const {
			return i1->value()<i2->value();
		}
	} 										_less_cyc;
	typedef std::map<_cyclic, _cyclic,
							_less_cyc > 	_map;
	typedef typename _map::const_iterator 	_c_iter;
	typedef typename _map::iterator 		_iter;
	_map map;
	static const _cyclic TRIVIAL_ELEMENT;
	cycle_element ():map(){
		map[TRIVIAL_ELEMENT] = TRIVIAL_ELEMENT;
	}
	cycle_element(const _cyclic& first, const _cyclic& second):map(){

		map[first]  = second;
		map[second] = first;
	}
	template<typename ITER_TYPE >
	cycle_element(ITER_TYPE i, ITER_TYPE e):map(){
		for(;i!=e;++i){
			append(*i);
		}
	}
	cycle_element(const cycle_element& o):map(o.map){}



	void append(const _cyclic& i){
		insert(lastElement(),i);
	}
	template<typename IT >
	void append(IT i, IT e){
		insert(i, e,lastElement());
	}

	bool contains(const _cyclic& i) const {
		return map.find(i)!=map.end();
	}
	const _cyclic& firstElement() const {
		auto i = map.begin();
		return i!=map.end()?i->first:TRIVIAL_ELEMENT;
	}
	void insert(const _cyclic& match, const _cyclic& newElement){
		if(map.find(newElement)!=map.end()) return;
		_iter it = map.find(match);
		if (it==map.end()) return;
		const _cyclic& tmp = it->second;
		it->second = newElement;
		map[newElement] = tmp;
	}
	template<typename IT>
	void insert(IT i, IT e, const _cyclic& match_hint){
		if(i==e) return;
		auto ii = map.find(match_hint);
		_cyclic cp = match_hint, lst;
		if(ii==map.end()) {
			cp = lastElement();
			lst = firstElement();
		}
		else {
			lst = ii->second;
		}
		while(i!=e) {
			cp = map[cp] = *i;
			++i;
		}
		map[cp] = lst;
	}
	const _cyclic& lastElement() const {
		auto i = map.rbegin();
		return i!=map.rend()?i->first:TRIVIAL_ELEMENT;
	}
	unsigned int length() const {
		return map.size();
	}



	/**
	 * @brief map def
	 */
	const _cyclic& operator()(const _cyclic& i) const {
		_c_iter c = map.find(i);
		return c==map.end()?
				i:
					c->second;
	}
	unsigned int operator()(unsigned int i) const {
		return this->operator()(i)->value();
	}


	void remove(const _cyclic& i) {
		_iter it = map.find(i);
		if(it==map.end()) return;
		const _cyclic& tmp = it->second;
		--it;
		if(it==map.end()) {
			_iter it1 = map.find(lastElement());
			it1->second = tmp;
			map.erase(map.begin());
		}
		else {
			it->second = tmp;
			++it;
			map.erase(it);
		}

	}

	struct const_cycle_iter {
		const cycle_element& ref;
		const _cyclic* ptr;
		const_cycle_iter(const cycle_element& el):ref(el),ptr(&ref.firstElement()){

		}
		const_cycle_iter(const const_cycle_iter& o):ref(o.ref),ptr(o.ptr){}
		const_cycle_iter& operator++(){
			ptr = &ref(*ptr);
			return *this;
		}
		const_cycle_iter operator++(int){
			const_cycle_iter it (*this);
			this->operator ++();
			return it;
		}
		const _cyclic* operator->() const {
			return ptr;
		}
	};

	friend struct const_cycle_iter;

	cycle_element reverse() const {
		_c_iter it = map.begin(), e = map.end();
		cycle_element rev;
		for (;it!=e;++it){
			rev.map[it->second] = it->first;
		}
		return rev;
	}


};

}

#endif /* INCLUDE_ALG_1_1_GROUP_GROUP_CYCLES_HPP_ */
