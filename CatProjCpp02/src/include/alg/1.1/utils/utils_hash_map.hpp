/*
 * utils_hash_map.hpp
 *
 *  Created on: 21.08.2022
 *      Author: gamer
 */

#ifndef INCLUDE_ALG_1_1_UTILS_UTILS_HASH_MAP_HPP_
#define INCLUDE_ALG_1_1_UTILS_UTILS_HASH_MAP_HPP_
#include <cstddef>
#include <list>
#include <memory>
#include <utility>
namespace util {

template<typename X, typename Y = std::size_t>
struct unique_hash_fct {
Y operator()( X arg) const {
	return Y((unsigned int)&arg);
}
};
template<typename X, typename Y = bool>
struct eq {
	typedef Y bool_type;
	bool_type operator()(const X& arg1, const X& arg2) const ;
};
template<typename X, typename Y = bool >
struct unique_eq {
	typedef Y bool_type;
	static const unique_hash_fct<X,Y >& EQUAL;
	bool_type operator()(const X& arg1, const X& arg2) const {
		return EQUAL(arg1,arg2);
	}
	static const unique_eq<X,Y>& SINGLE;
private:
	unique_eq(){}
};

template<typename X, typename Y>
const unique_hash_fct<X,Y >& unique_eq<X,Y >::EQUAL = unique_hash_fct<X,Y >();
template<typename X, typename Y >
const unique_eq<X,Y>& unique_eq<X,Y >::SINGLE = unique_eq<X,Y >::unique_eq();


template<typename X, typename EQ,typename AL = std::allocator<X > >
class eq_class {

public:
	typedef typename EQ::bool_type _bool;

	typedef std::list<X,AL > _list;
	typedef typename _list::const_iterator _citer;
	typedef typename _list::iterator _iter;
	typedef typename _list::const_reverse_iterator _criter;
	typedef typename _list::reverse_iterator _irter;

	_iter find( X item){
		for(_iter it = lst.begin();it!=lst.end();++it){
			if(EQ::EQUAL(*it,item)) {
				return it;
			}
		}
		lst.push_back(item);
		return find(item);
	}
	_citer find(const X& item) const {
		for(_iter it = lst.begin();it!=lst.end();++it){
			if(EQ::EQUAL(*it,item)) {
				return it;
			}
		}
		return lst.end();

	}
	_citer begin() const {
		return lst.begin();
	}
	_citer end() const {
			return lst.end();
	}
	_iter begin() {
			return lst.begin();
	}
	_iter end() {
			return lst.end();
	}
	size_t size() const {
		return lst.size();
	}
	_iter remove(X item){
		_iter it = find(item);
		if(it==lst.end()) return lst.end();
		lst.remove(it);
	}
private:
	_list lst;

};

template<
	typename key_type,
	typename val_type,
	typename HASH = unique_hash_fct<const key_type& >,
	typename EQ = unique_eq<std::pair<const key_type,val_type> >,
	typename AL = std::allocator<std::pair< const key_type, val_type> >

>
class hash_map {

public:
	typedef hash_map<key_type,val_type,HASH,EQ,AL> _hash;
	typedef std::pair<const key_type,val_type> _pair;
	typedef eq_class<_pair , EQ, AL > _eqcls;
	friend class const_iterator {
		const _hash* obj_ptr;
		const _eqcls* curr_ptr;
		typedef typename _eqcls::_citer _citer;
		typename _citer* it_ptr;
		size_t idx;
		const_iterator(const const_iterator& o):obj_ptr(o.obj_ptr),
					curr_ptr(o.curr_ptr),it_ptr(new _citer(o.it_ptr)),idx(0){}
		~const_iterator(){
			if(it_ptr!=0) {
				delete it_ptr;
				it_ptr=0;
			}
		}
		const_iterator& operator++(){
			if(obj_ptr!=0&& obj_ptr->buckets!=0){
				if(curr_ptr==0){
					curr_ptr = obj_ptr->buckets[idx];
					++idx;
					return sz>idx||(sz==idx&&curr_ptr!=0) ?operator ++():*this;
				}
				if(it_ptr==0) {
					it_ptr = new _citer(curr_ptr->begin());
					return *this;
				}
				it_ptr->operator++();
				if(*it_ptr==curr_ptr->end()) {
					delete it_ptr;
					it_ptr=0;
					curr_ptr=0;
					return operator++();
				}


			}
			return *this;
		}
		const_iterator& operator--(){
			if(obj_ptr!=0&& obj_ptr->buckets!=0){
				if(curr_ptr==0){
					curr_ptr = obj_ptr->buckets[idx];

					return 0<idx || (idx==0 && curr_ptr!=0)?operator --():*this;
				}
				if(it_ptr==0) {
					it_ptr = new _citer(curr_ptr->cbegin());
					return *this;
				}
				it_ptr->operator++();
				if(*it_ptr==curr_ptr->end()) {
					delete it_ptr;
					it_ptr=0;
					curr_ptr=0;
					return operator++();
				}


			}
			return *this;
		}
	};
	friend class iterator {
		const _hash* obj_ptr;
		const _eqcls* curr_ptr;
		typename _eqcls::_iter* it_ptr;
	public:
		iterator(const iterator& o):obj_ptr(o.obj_ptr),
			curr_ptr(o.curr_ptr),it_ptr(o.it_ptr){}
		iterator& operator++() {

			return *this;
		}
	};


	static const HASH& H_FCT;
private:
	_eqcls** buckets;

	size_t sz;
	size_t cp;
};
}


#endif /* INCLUDE_ALG_1_1_UTILS_UTILS_HASH_MAP_HPP_ */
