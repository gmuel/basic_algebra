/*
 * cycle.hpp
 *
 *  Created on: 14.05.2026
 *      Author: fun_gab
 */

#ifndef INCLUDE_CYCLE_HPP_
#define INCLUDE_CYCLE_HPP_
#include "group_group_base_impl.hpp"
#include <utility>
#include <map>
namespace sym {

template<unsigned int N >
struct c_pair : public std::pair<alg::cyclic_wrp<N>, alg::cyclic_wrp<N> >{
	typedef alg::cyclic_wrp<N> _cyc;
	typedef std::pair<alg::cyclic_wrp<N>, alg::cyclic_wrp<N> > _base;
	c_pair<N >* prev,* next;
	c_pair(const _cyc& c1, const _cyc& c2):_base(c1,c2),prev(0),next(0){}
	c_pair(_cyc&& c1, _cyc&& c2):_base(c1,c2),prev(0),next(0){}
	c_pair(const _base& o):_base(o),prev(0),next(0){}
	c_pair(_base&& o):     _base(o),prev(0),next(0){}
	c_pair(explicit const c_pair<N>& o):_base(o),prev(0),next(o.next!=0?new c_pair<N> (*o.next):0){}
	c_pair(explicit c_pair<N>&& o):     _base(o),prev(0),next(o.next){}
	~c_pair(){
		if(next!=0) {
			delete next;
			next = 0;
		}
		prev = 0;
	}
};

template<unsigned int N >
c_pair<N> make_cpair(const alg::cyclic_wrp<N>& c1, const alg::cyclic_wrp<N>& c2){
	c_pair<N> cp(c1,c2);
	return cp;
}
template<unsigned int N >
c_pair<N> make_cpair(alg::cyclic_wrp<N>&& c1, alg::cyclic_wrp<N>&& c2){
	c_pair<N> cp(c1,c2);
	return cp;
}

template<unsigned int N >
class cycle {
public:
	typedef alg::cyclic_wrp<N> 		_cyc;
	typedef c_pair<N>				_cpa;
	typedef std::map<_cyc,_cpa*>	_cma;
	typedef typename _cma::const_iterator _c_it;
	typedef typename _cma::iterator 		_it;
	cycle(const _cyc& c1, const _cyc& c2):cpa(0),map(),first(0),last(0){
		cpa = new _cpa(make_cpair<N>(c1, c2));
		map[c1] = cpa;
		last = first = cpa;
		if(c1!=c2) {
			cpa->next = new _cpa(make_cpair<N>(c2, c1));
			cpa->next->prev = this;
			last = map[c2] = cpa->next;
		}
	}
	cycle(_cyc&& c1, _cyc&& c2):cpa(0),map(),first(0),last(0){
		cpa = new _cpa(make_cpair<N>(c1, c2));
		map[c1] = cpa;
		last = first = cpa;
		if(c1!=c2) {
			create_pair(c2, c1, cpa);
			last = map[c2] = cpa->next;
		}
	}
	template<typename IT >
	cycle(IT i, IT e):cpa(),map(),first(0),last(0){
		insert(i,e);
	}
	~cycle(){
		map.clear();
		first = last = 0;
		delete cpa;
		cpa = 0;
	}
	iterator begin() {
		iterator i = {cpa};
		return i;
	}
	const_iterator cbegin() const {
		const_iterator i = {cpa};
		return i;
	}
	const_iterator cend() const {return const_iterator();}
	bool contains(const _cyc& c) const {return find(c)!=cend();}
	iterator end() {return iterator();}
	const_iterator find(const _cyc& c) const {
		_c_it i = map.find(c);
		return i==map.end()?cend():const_iterator{i->second};
	}
	iterator find(const _cyc& c) {
		_it i = map.find(c);
		return i==map.end()?end():iterator{i->second};
	}
	template<typename IT >
	void insert(IT i, IT e){
		if(last!=0){
			iterator ii = {last};
			return insert(i,e,ii);
		}
		if(i!=e){
			_cyc frst = *i, lst = (i!=e)?*(++i):*i;
			if(frst!=lst){
				cpa = new _cpa(frst,lst);
				map[frst] = lst;
				create_pair(lst,frst, cpa);
				insert(i,e,iterator{cpa});
			}
		}
	}
	/**
	 * inserts elements returned by given iterator
	 * iterator has to provide deref-operator returning
	 * <code>alg::cyclic_wrp</code> to be used
	 * @tparam IT iterator type - with defref op
	 */
	template<typename IT >
	void insert(IT i, IT e, iterator hint){
		if(hint==end()||i==e) return;
		_cyc _frst = hint->first, _lst = hint->second;
		_cpa* curr = hint.operator->(),* nxt = hint->next;
		while(i!=e) {
			_cyc snd = *i;
			if(create_pair(_frst, snd, curr)){
				curr = curr->next;
				_frst = snd;
			}
			++i;
		}
		if(curr!=hint.operator ->()&&create_pair(_frst, _lst, curr)) {
			curr = curr->next;
			if(nxt!=0){
				curr->next = nxt;
				nxt->prev  = curr;
			}
			else last = curr;
		}

	}
	friend struct const_iterator {
		const _cpa* ptr = 0;
		const_iterator& operator++() {
			if(ptr!=0) ptr = ptr->next;
			return *this;
		}
		const_iterator& operator--() {
			if(ptr!=0) ptr = ptr->prev;
			return *this;
		}
		const _cpa* operator->() const {
			return ptr;
		}
		friend bool operator==(const const_iterator& i1, const const_iterator& i2){
			return i1.ptr==i2.ptr;
		}
		friend bool operator!=(const const_iterator& i1, const const_iterator& i2){
			return i1.ptr!=i2.ptr;
		}
	};
	friend struct iterator {
		_cpa* ptr = 0;
		iterator& operator++() {
			if(ptr!=0) ptr = ptr->next;
			return *this;
		}
		iterator& operator--() {
			if(ptr!=0) ptr = ptr->prev;
			return *this;
		}
		_cpa* operator->() {
			return ptr;
		}
		friend bool operator==(const iterator& i1, const iterator& i2){
			return i1.ptr==i2.ptr;
		}
		friend bool operator!=(const iterator& i1, const iterator& i2){
			return i1.ptr!=i2.ptr;
		}
	};
	const _cyc& operator[](const _cyc& c) const {
		const_iterator i = find(c);
		return i!=cend()?i->second:c;
	}
	_cyc& operator[](_cyc& c) {
		iterator i = find(c);
		return i!=end()?i->second:c;
	}
private:
	_cpa* cpa,* first,* last;
	_cma map;
	bool create_pair(const _cyc& _frst, const _cyc& _snd, _cpa* curr){
		if(map.find(_snd)==map.end()){
			auto* tmp = new _cpa(_frst,_snd);
			map[_frst] = tmp;
			tmp->prev = curr;
			curr->next = tmp;
			return true;
		}
		return false;
	}
	bool create_pair(_cyc&& _frst, _cyc&& _snd, _cpa* curr){
		if(map.find(_snd)==map.end()){
			auto* tmp = new _cpa(_frst,_snd);
			map[_frst] = tmp;
			tmp->prev = curr;
			curr->next = tmp;
			return true;
		}
		return false;
	}
};

}


#endif /* INCLUDE_CYCLE_HPP_ */
