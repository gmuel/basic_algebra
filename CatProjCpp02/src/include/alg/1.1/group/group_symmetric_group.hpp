/*
 * group_symmetric_group.hpp
 *
 *  Created on: 06.01.2022
 *      Author: stream_vid
 */

#ifndef INCLUDE_ALG_1_1_GROUP_GROUP_SYMMETRIC_GROUP_HPP_
#define INCLUDE_ALG_1_1_GROUP_GROUP_SYMMETRIC_GROUP_HPP_
#include "group_cycles.hpp"
#include <map>
#include <set>
namespace alg {

template<unsigned int N>
class symmetric {

public:
	typedef alg::cyclic_wrp<N >				_cyclic;
	struct less_cyclic {
		bool operator()(const _cyclic& c1, const _cyclic& c2) const {
			return c1.operator ->()<c2.operator ->();
		}
	};
	typedef std::map<_cyclic,_cyclic,less_cyclic >		_map;
	typedef alg::cyclic_add_unit<N>			_cyc_add_unit;
	typedef typename _map::const_iterator 	_c_iter;
	typedef typename _map::iterator			_iter;
	static const _cyc_add_unit& CYCLIC_ADD_UNIT;
	symmetric():symMap(){
		symMap[CYCLIC_ADD_UNIT()] = CYCLIC_ADD_UNIT();
	}
	template<typename ITER_TYPE>
	symmetric(ITER_TYPE i, ITER_TYPE e):symMap(){
		if(i!=e) {
			_cyclic first(*i);
			symMap[first] = first;
			++i;
			while(i!=e&&symMap.size()<N){
				insert(first,*i);
				first = *i;
				++i;
			}
			symMap[*first] = symMap.begin()->first;
		}
		else symMap[CYCLIC_ADD_UNIT()] = CYCLIC_ADD_UNIT();
	}

	bool contains(const _cyclic& i) const {
		return symMap.find(i)!=symMap.end();
	}
	const _cyclic& firstElement() const {
			return symMap.begin()->first;
	}

	void insert(const _cyclic& match, const _cyclic& newElement) {

	}
	unsigned int length() const {
		return symMap.size();
	}

	struct sym_unit {
		static const symmetric<N >& UNIT;
		const symmetric<N>& operator()() const {
			return UNIT;
		}
	};
	static const sym_unit& UNIT;
	struct sym_anti {
		symmetric<N > operator()(const symmetric<N>& arg) const {
			return arg.reverse();
		}
	};

	struct sym_multi {
		symmetric<N > operator()(const symmetric<N>& p1, const symmetric<N >& p2) const {
			_c_iter i2 = p2.symMap.begin(), e2 = p2.symMap.end(),
					i1 = p1.symMap.begin(), e1 = p1.symMap.end();
			symmetric<N> prod;
			prod.symMap.erase();
			while(i2!=e2){
				_c_iter ii = p1.symMap.find(i2->second);
				if(ii==e1) prod.symMap[i2->first] = i2->second;
				else {
					setNonEqual(i2,ii,prod);
				}
				++i2;
			}
			while(i1!=e1){
				if(p2.symMap.find(i1->first)==e2) {
					prod.symMap[i1->first] = i1->second;

				}
				++i1;
			}
			clean(prod);
			return prod;
		}
	private:
		void clean(symmetric<N>& prod){
			switch(prod.symMap.size()){
			case 0:{
				prod.symMap[CYCLIC_ADD_UNIT()] = CYCLIC_ADD_UNIT();
				break;
			}
			case 1:{
				break;

			}
			default:{
				_iter i = prod.symMap.begin(), e = prod.symMap.end();
				while(i!=e){

					if(i->first==i->second)
#if __cplusplus <201103L
						prod.symMap.erase(i);
#else
						i = prod.symMap.erase(i);
#endif
				}
				clean(prod);
			}
			}
		}
		void setNonEqual(_c_iter& i2, _c_iter& ii, symmetric<N>& prod) {
			if(i2->first!=ii->second) prod.symMap[i2->first] = ii->second;
		}
	};
	friend struct sym_multi;
	struct sym_eq {
		bool operator()(const symmetric<N>& s1, const symmetric<N>& s2) const {
			return s1.symMap==s2.symMap;
		}
	};
	/**
	 * @brief unidirectional permutation iterator
	 *
	 * Traverses any permutation in cycles order where the cycle with the lowest
	 * entry gets traversed next
	 */
	class const_sym_iterator {
		_map sym;
		_c_iter it,& e;

		//const _cyclic*	currentFirst;
		_cyclic current;
	public:
		const_sym_iterator(const symmetric<N>& o):sym(o.symMap),it(sym.begin())
			,current(),e(sym.end()){

			current = it->first;
		}
		const_sym_iterator(const const_sym_iterator& o):sym(o.sym),it(sym.begin())
			,current(),e(sym.end()){

			current = it->first;
		}
		const_sym_iterator& operator++() {

			_iter removeIt = sym.find(current);
			if(removeIt==e) return *this;
			const _cyclic& img = removeIt->second;
			sym.erase(removeIt);
			if(it->first!=img) {
				current = img;
			}
			else {
				++it;
				if(it!=e){
					current = it->first;
				}
			}


			return *this;
		}
		const _cyclic* operator->() const {return &current;}
		friend bool operator==(const const_sym_iterator& c1, const const_sym_iterator& c2){
			unsigned int sz1 = c1.sym.size(), sz2 = c2.sym.size();
			if(sz1<=1&&sz2<=1) return true;
			return sz1==sz2?c1.sym==c2.sym:false;
		}
		friend bool operator!=(const const_sym_iterator& i1, const const_sym_iterator& i2){
			return !(i1==i2);
		}

	};
	friend class const_sym_iterator;
	/**
	 * @Mutator iterator - not intended for traversal
	 */
	class sym_iterator {
		_map& sym;
		_iter it,& e;

		_cyclic current;
	public:
		sym_iterator(const symmetric<N>& o):sym(o.symMap),it(sym.begin())
			,current(),e(sym.end()){

			current = it->first;
		}
		sym_iterator(const const_sym_iterator& o):sym(o.sym),it(sym.begin())
			,current(),e(sym.end()){

			current = it->first;
		}
		bool insert(const _cyclic& hint, const _cyclic& newElement){
			if(sym.find(newElement)!=e || sym.size()>N+1) return false;
			_iter it = sym.find(hint);
			if (it==e) return false;
			const _cyclic& tmp = it->second;
			it->second = newElement;
			sym[newElement] = tmp;
			return true;
		}
		bool erase(const _cyclic& rm){
			_iter i = sym.find(rm);
			if(i!=e) return false;
			const _cyclic& tmp = i->second;
			sym.erase(i);
			for(_iter ii = sym.begin();ii!=e;++ii){
				if(ii->second == rm) {
					ii->second = tmp;
					break;
				}

			}
			return true;
		}

	};
	friend class sym_iterator;
	const_sym_iterator
#if __cplusplus < 201103L
			end()
#else
			cend()
#endif
			const {
		return const_sym_iterator(UNIT());
	}

	const_sym_iterator
#if __cplusplus < 201103L
			begin()
#else
			cbegin()
#endif
				const {
		return const_sym_iterator(*this);
	}
	sym_iterator find(const _cyclic& c){
		sym_iterator it(*this);
		it.it = symMap.find(c);
		if(it.it==symMap.end())
			return sym_iterator(UNIT());
		it.current = c;
		return it;
	}

#if __cplusplus < 201103L
	void erase(sym_iterator i){

	}
#else
	sym_iterator erase(sym_iterator i){

		return i;
	}
#endif
	/**
	 * @brief map def
	 */
	_cyclic operator()(const _cyclic& i) const {
		_c_iter c = symMap.find(i);
		return c==symMap.end()?
				i:
					c->second;
	}
	sym_iterator operator()(const _cyclic& hint, const _cyclic& newElement) {
		sym_iterator i(*this);
		i.insert(hint, newElement);
		return i;
	}
	sym_iterator operator()(const _cyclic& c) {
		return operator()(firstElement(),c);
	}
	unsigned int operator()(unsigned int i) const {
		return this->operator()(i)->value();
	}


	symmetric<N> reverse() const {
		_c_iter it = symMap.begin(), e = symMap.end();
		symmetric<N> rev;
		for (;it!=e;++it){
			rev.symMap[it->second] = it->first;
		}
		return rev;
	}


private:
	_map symMap;
};


template <unsigned int N >
const cyclic_add_unit<N>& symmetric<N >::CYCLIC_ADD_UNIT = cyclic_add_unit<N> ();
}



#endif /* INCLUDE_ALG_1_1_GROUP_GROUP_SYMMETRIC_GROUP_HPP_ */
