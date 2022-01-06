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
namespace alg {

template<unsigned int N>
class symmetric {

public:
	typedef alg::cyclic_wrp<N >				_cyclic;
	typedef std::map<_cyclic,cyclic >		_map;
	typedef alg::cyclic_add_unit			_cyc_add_unit;
	typedef typename _map::const_iterator 	_c_iter;
	typedef typename _map::iterator			_iter;
	static const _cyc_add_unit CYCLIC_ADD_UNIT;
	symmetric():symMap(){
		symMap[CYCLIC_ADD_UNIT()] = CYCLIC_ADD_UNIT();
	}
	template<typename ITER_TYPE>
	symmetric(ITER_TYPE i, ITER_TYPE e):symMap(){
		if(i!=e) symMap[*i] = *i;
		++i;
		while(i!=e){

			++i;
		}
	}

	bool contains(const _cyclic& i) const {
		return symMap.find(i)!=symMap.end();
	}
	const _cyclic& firstElement() const {
			return symMap.begin()->first;
	}

	void insert(const _cyclic& match, const _cyclic& newElement) {
		if(symMap.find(newElement)!=symMap.end()) return;
		_iter it = symMap.find(match);
		if (it==symMap.end()) return;
		const _cyclic& tmp = it->second;
		it->second = newElement;
		symMap[newElement] = tmp;
	}
	unsigned int length() const {
		return symMap.size();
	}

	friend struct sym_unit {
		static const symmetric<N >& UNIT;
		const symmetric<N>& operator()() const {
			return UNIT;
		}
	};
	friend struct sym_anti {
		symmetric<N > operator()(const symmetric<N>& arg) const {
			return arg.reverse();
		}
	};

	friend struct sym_multi {
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
				for(_iter i = prod.symMap.begin();i!=prod.symMap.end();++i){
					if(i->first==i->second) prod.symMap.erase(i);
				}
				clean(prod);
			}
			}
		}
		void setNonEqual(_c_iter& i2, _c_iter& ii, symmetric<N>& prod) {
			if(i2->first!=ii->second) prod.symMap[i2->first] = ii->second;
		}
	};

	/**
	 * @brief map def
	 */
	const _cyclic& operator()(const _cyclic& i) const {
		_c_iter c = symMap.find(i);
		return c==symMap.end()?
				i:
					c->second;
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

}



#endif /* INCLUDE_ALG_1_1_GROUP_GROUP_SYMMETRIC_GROUP_HPP_ */
