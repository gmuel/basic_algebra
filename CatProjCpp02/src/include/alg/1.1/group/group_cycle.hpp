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


#define CYW_TYPE(N) \
	typedef alg::cyclic_wrp<N>	_cyw##N

template<unsigned int N >
struct c_pair : public std::pair<alg::cyclic_wrp<N>, alg::cyclic_wrp<N> >{
	CYW_TYPE(N);
	typedef std::pair<alg::cyclic_wrp<N>, alg::cyclic_wrp<N> > _base;
	c_pair<N >* prev,* next;
	c_pair(const _cywN& c1, const _cywN& c2):_base(c1,c2),prev(0),next(0){}
	c_pair(_cywN&& c1, _cywN&& c2):_base(c1,c2),prev(0),next(0){}
	c_pair(const _base& o):_base(o),prev(0),next(0){}
	c_pair(_base&& o):     _base(o),prev(0),next(0){}
	explicit c_pair(const c_pair<N>& o):_base(o),prev(0),next(0){
		if(o.next!=0) {
			next = new c_pair<N> (*o.next);
			next->prev = this;
		}
	}
	explicit c_pair(c_pair<N>&& o):     _base(o),prev(o.prev),next(o.next){}
	~c_pair(){
		if(next!=0) {
			delete next;
			next = 0;
		}
		prev = 0;
	}
	using _base::first;
	using _base::second;
};

template<unsigned int N >
c_pair<N> make_cpair(const alg::cyclic_wrp<N>& c1, const alg::cyclic_wrp<N>& c2){
	return c_pair<N>(c1,c2);
}
template<unsigned int N >
c_pair<N> make_cpair(alg::cyclic_wrp<N>&& c1, alg::cyclic_wrp<N>&& c2){
	return c_pair<N>(c1,c2);
}
template<unsigned int N >
bool operator<(const alg::cyclic_wrp<N>& c1, const alg::cyclic_wrp<N>& c2){
	 return *(c1)<*(c2);
}
template<unsigned int N >
struct less : public std::less<alg::cyclic_wrp<N> > {
	bool operator()(const alg::cyclic_wrp<N>& c1, const alg::cyclic_wrp<N>& c2) const {
		return c1 < c2;
	}
};
template<unsigned int N >
class cycle {
public:
	typedef alg::cyclic_wrp<N> 			_cyc;
	typedef c_pair<N>					_cpa;
	typedef less<N>						_lss;
	typedef std::map<_cyc,_cpa*,_lss>	_cma;
	typedef typename _cma::const_iterator _c_it;
	typedef typename _cma::iterator 		_it;
	friend struct const_iterator;
	struct const_iterator{
		const _cpa* curr 	= 0;
		const_iterator& operator++() {
			if(curr!=0){
				curr = curr->next;
			}
			return *this;
		}
		const_iterator& operator--() {
			if(curr!=0) curr = curr->prev;
			return *this;
		}
		const _cpa* operator->() const {
			return curr;
		}
		friend bool operator==(const const_iterator& i1, const const_iterator& i2){
			return i1.curr==i2.curr;
		}
		friend bool operator!=(const const_iterator& i1, const const_iterator& i2){
			return i1.curr!=i2.curr;
		}
	};
	friend struct iterator;
	struct iterator {
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
	cycle(const _cyc& c1, const _cyc& c2):map(),cpa(0),first(0),_last(0){
		cpa = new _cpa(make_cpair<N>(c1, c2));
		map[c1] = cpa;
		_last = first = cpa;
		if(c1!=c2) {
			cpa->next = new _cpa(make_cpair<N>(c2, c1));
			cpa->next->prev = this;
			_last = map[c2] = cpa->next;
		}
	}
	cycle(_cyc&& c1, _cyc&& c2):map(),cpa(0),first(0),_last(0){
		cpa = new _cpa(make_cpair<N>(c1, c2));
		map[c1] = cpa;
		_last = first = cpa;
		if(c1!=c2) {
			create_pair(c2, c1, cpa);
			_last = map[c2] = cpa->next;
		}
	}
	template<typename IT >
	cycle(IT i, IT e):cpa(),map(),first(0),_last(0){
		insert(i,e);
	}
	cycle(const cycle<N>& o):cpa(0),map(o.map),first(0),_last(0){
		if(o.cpa!=0){
			cpa = new _cyc(*o.cpa);
			auto i = map.begin();
			if(i!=map.end()) {
				first = i->second;
			}
			set_last();
		}
	}
	cycle(cycle<N>&& o):cpa(o.cpa),map(o.map),first(o.first),_last(o._last){}
	~cycle(){
		map.clear();
		first = _last = 0;
		if(cpa!=0){
			delete cpa;
			cpa = 0;
		}
	}
	iterator begin() {
		return iterator {cpa};
	}
	const_iterator cbegin() const {
		return const_iterator {cpa};
	}
	const_iterator cend() const {return const_iterator();}
	/**
	 * @return last valid iterator (i.e. calling operator++ will invalidate)
	 * Useful for reverse traversal of cycle
	 * Usage see cbegin()
	 */
	const_iterator clast() const {return const_iterator{_last};}
	bool contains(const _cyc& c) const {return find(c)!=cend();}
	iterator end() {return iterator();}
	const_iterator find(const _cyc& c) const {
		_c_it i = map.find(c);
		return i==map.cend()?cend():const_iterator{i->second};
	}
	iterator find(const _cyc& c) {
		_it i = map.find(c);
		return i==map.end()?end():iterator{i->second};
	}
	template<typename IT >
	void insert(IT i, IT e){
		iterator ii = last();
		if(ii!=end()) return insert(i,e,ii);
		if(i!=e){
			_cyc frst = *i;
			_cyc lst = (++i!=e)?*i:frst;
			if(frst!=lst){
				cpa = new _cpa(frst,lst);
				map[frst] = lst;
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
		_cpa* _crr = hint.operator->(),* _nxt = _crr->next;
		_cyc _frst = _crr->first, _lst = _crr->second;
		while(i!=e) {
			_cyc _snd = *i;
			if(create_pair(_frst, _snd, _crr)){
				_crr = _crr->next;
				_frst = _snd;
			}
			++i;
		}
		if(_crr!=hint.operator ->()&&create_pair(_frst, _lst, _crr)) {
			_crr = _crr->next;
			if(_nxt!=0){
				_crr->next = _nxt;
				_nxt->prev  = _crr;
			}
			else _last = _crr;
		}
		set_last();
	}
	iterator last() {return iterator{_last};}
	std::size_t length() const {std::size_t sz = map.size(); return sz==0?1:sz;}
	cycle<N > reverse() const {
		cycle<N>c;
		auto i = clast(), e = cend();
		std::size_t lng = length();
		if(lng==1 || lng==2) return *this;
		if(i!=e){
			_cyc pre = first->first, im;
			_cpa* curr (0);
			while(i!=e) {
				im = i->first;
				if(c.cpa==0) {
					c.cpa = new _cpa(pre,im);
					curr = c.cpa;
				}
				else {
					c.create_pair(pre, im, curr);
					curr = curr->next;
				}
				pre = im;
				--i;
			}

		}
		return c;
	}
	const _cyc& operator[](const _cyc& c) const {
		const_iterator i = find(c);
		return i!=cend()?i.operator ->()->second:c;
	}
	_cyc& operator[](_cyc& c) {
		iterator i = find(c);
		return i!=end() ?i.operator ->()->second:c;
	}
	friend bool operator==(const cycle<N>& c1, const cycle<N>& c2){
		std::size_t sz1 = c1.length(), sz2 = c2.length();
		if((sz1==0&&sz2==1)
				||(sz1==1&&sz2==0)) return true;
		if(sz1!=sz2) return false;
		auto i1 = c1.cbegin(), i2 = c2.cbegin(),
				e1 = c1.cend(), e2 = c2.cend();
		while(i1!=e1&&i2!=e2) {
			if(i1->first!=i2->first||i1->second!=i2->second) return false;
			++i1;
			++i2;
		}
		return i1==e1&&i2==e2;
	}
	friend bool operator!=(const cycle<N>& c1, const cycle<N>& c2){
		return !(c1==c2);
	}
	friend bool operator<(const cycle<N>& c1, const cycle<N>& c2){
		std::size_t sz1 = c1.length(), sz2 = c2.length();
		if(sz1!=sz2) return sz1<sz2?true:false;
		auto i1 = c1.cbegin(), i2 = c2.cbegin(),
				e1 = c1.cend(), e2 = c2.cend();
		while(i1!=e1&&i2!=e2) {
			if(i1->first<i2->first) return true;
			if(i2->first<i1->first) return false;
			++i1;
			++i2;
		}
		return false;
	}
private:
	_cma map;
	_cpa* cpa,* first,* _last;
	bool create_pair(const _cyc& _frst, const _cyc& _snd, _cpa* curr){
		if(map.find(_snd)==map.end()){
			auto* tmp = new _cpa(_frst,_snd);
			map[_frst] = tmp;
			if(curr!=0){
				tmp->prev = curr;
				curr->next = tmp;
			}
			else {
				insertOrAppend(tmp);
			}
			return true;
		}
		return false;
	}
	bool create_pair(_cyc&& _frst, _cyc&& _snd, _cpa* curr){
		if(map.find(_snd)==map.end()){
			auto* tmp = new _cpa(_frst,_snd);
			map[_frst] = tmp;
			if(curr!=0){
				tmp->prev = curr;
				curr->next = tmp;
			}
			else {
				insertOrAppend(tmp);
			}
			return true;
		}
		return false;
	}
	void insertOrAppend(_cpa* tmp){
		if(cpa==0) {
			cpa = tmp;
		}
		else if (_last!=0) {
			_last->next = tmp;
			tmp->prev   = _last;
			_last = tmp;
		}
	}
	void set_last(){
		_cyc* curr = first;
		if(curr==0) return;
		while(curr->next!=0) {
			if(curr->next->prev == 0) curr->next->prev = curr;
			curr = curr->next;
		}
		if(_last!=curr) _last = curr;
	}
};
#define CYC_TYPE(N) \
	typedef cycle<N>		_cyc##N

template<>
class cycle<1u > {
public:
	~cycle(){}
	static const cycle<1u>& ID;
	friend const cycle<1u>& operator*(const cycle<1u>& s1, const cycle<1u>& s2) {return ID;}
	friend bool operator==(const cycle<1u>& c1, const cycle<1u>& c2) {
		return true;
	}
protected:
	cycle(){}
private:
	cycle(const cycle<1u>& o){}
	cycle(cycle<1u>&& o){}
	cycle<1u>& operator=(const cycle<1u>& s){return *this;}
};

const cycle<1u>& cycle<1u>::ID = cycle();
template<>
class cycle<0u> : public cycle<1u > {
public:
	using cycle<1u>::ID;
};
template<>
class cycle<2u> {
public:
	CYW_TYPE(2u);
	CYC_TYPE(2u);
	static const _cyw2u& ZERO;
	static const _cyw2u& ONE;
	const _cyw2u& operator[](const _cyw2u& c) const {
		return c==ZERO?ONE:ZERO;
	}
};
}


#endif /* INCLUDE_CYCLE_HPP_ */
