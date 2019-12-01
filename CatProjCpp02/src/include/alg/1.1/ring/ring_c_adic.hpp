/*
 * ring_c_adic.hpp
 *
 *  Created on: 01.12.2019
 *      Author: lmmac
 */

#ifndef INCLUDE_ALG_1_1_RING_RING_C_ADIC_HPP_
#define INCLUDE_ALG_1_1_RING_RING_C_ADIC_HPP_


/*
 * Comes in 2 flavors:
 * 1) using standard lib containers (use macro '_H_C_ADIC_USE_STD'
 * 2) using local header for doubly linked list 'd_link_list.h'
 *
 * Currently, only option 2 is supported as the author
 * has no way of deal with const refs in std::(map, pair, ...)
 * as type parameters. A const pointer version might follow sometime...
 * TODO ->
 */
#include "cyclics.hpp"
#ifndef _H_C_ADIC_USE_STD
#ifndef D_LINK_LIST_H01_UNSIZED_D_LINKS
#define D_LINK_LIST_H01_UNSIZED_D_LINKS
#endif
#ifndef D_LINK_LIST_H01_CONST_REF_SPEC
#define D_LINK_LIST_H01_CONST_REF_SPEC
#endif
#include "d_link_list01.hpp"
#else
#include <map>
#include <utility>
#endif

typedef unsigned int _u_int;





//Currently not supported, as constant refs collide
//with std::map and its functions
#ifndef _H_C_ADIC_USE_STD
/**
 * @brief C-adic integer class templ.
 *
 * Implemented as spec of <code>util::d_linked_exp_list<const cyclic<CYC_TYPE >& ></code>
 *
 * @tparam CYC_TYPE integer parameter for series expansion
 */
template<_u_int CYC_TYPE >
class c_adic {
public:
	typedef alg_fun::cyclic<CYC_TYPE > _cyc;
	typedef const _cyc&			  _cyc_cref;
	typedef c_adic<CYC_TYPE > 		   _cad;
	typedef util::d_linked_exp_list<_cyc_cref > _glist;
	/**
	 * @brief List class
	 *
	 * Spec of 'util::d_linked_exp_list<const cyclic<CYC_TYPE >& >'
	 */
	typedef class c_list : public _glist {
	public:
		typedef _glist 	_base;
		typedef typename _base::const_iterator					  _c_iter;
		typedef typename _base::iterator						    _iter;
		//TODO might need special iterators capable of ignoring/skipping zero values
		c_list(const _base& o):_base(){
			init(o.begin(), o.end(),0);
		}
		~c_list(){}
	private:
		friend class c_adic<CYC_TYPE >;
		void init(_c_iter i, _c_iter e, _u_int idx_hint){
			_iter ii = begin();_u_int idx(idx_hint);
			while(i!=e) {
				const _cyc& x = i->operator*();
				if(&x!=_cyc::VALS) ii.insert(x,idx_hint+i->i,true);
				++i; ++idx;
			}
			_base::set_array();
		}
		c_list(int v = 0):_base(){
			_iter i = begin();
			_u_int idx(0);
			while(v!=0){
				int r = v>0?v%CYC_TYPE:CYC+(v%CYC);
				if(r!=0) i.insert(_cyc::VALS[r],idx,true);
				v/=CYC;++idx;
			}
			_base::set_array();
		}
		/**
		 * @brief Range c_tor template
		 *
		 * Constructs a c-adic by taking all non-zero entries as
		 * coefficients
		 * @tparam INPUT_ITER range iter type
		 * @param i iterator
		 * @param e past-the-end iterator
		 * @param idx_hint index hint, defaults to zero
		 */
		template<typename INPUT_ITER >
		c_list(INPUT_ITER i, INPUT_ITER e, _u_int idx_hint = 0):_base(){
			_iter ii = begin();_u_int idx(idx_hint);
			while(i!=e) {
				const _cyc& x = *i;
				if(x!=_cyc::VALS[0]) ii.insert(x,idx,true);
				++i; ++idx;
			}
			_base::set_array();
		}
		/**
		 * @brief Range c_tor
		 *
		 * A specialiszation of the templated c_tor
		 * @param i iterator
		 * @param e past-the-end iterator
		 * @param idx_hint index hint, defaults to zero
		 */
		c_list(_c_iter i, _c_iter e, _u_int idx_hint = 0):_base(){
			init(i,e,idx_hint);
		}
		void clear(){typename _base::iterator i = _base::begin(), e = _base::end(); while(i!=e) i.erase();}
		void set_array(){_base::set_array();}
	} _list;
	typedef typename _list::_c_iter   _c_iter;
	typedef typename _list::_iter	  _iter;
	static const c_adic<CYC_TYPE >& ZERO,& ONE;
	c_adic (int v = 0):coeffs(v){
		/*if(0<v&&v<CYC_TYPE) coeffs.begin().insert(_cyc::VALS[v],0);
		else{
			if(-((int )CYC_TYPE)<v&&v<0) coeffs.begin().insert(_cyc::VALS[CYC_TYPE-v],0);
			else{
				set_coef(v);
			}
		}*/
	}
	c_adic(_u_int i, _cyc_cref c):coeffs(){if(&c!=_cyc::VALS) coeffs.begin().insert(c,i);}
	template<typename INPUT_ITER >
	c_adic (INPUT_ITER i, INPUT_ITER e, _u_int idx_hint = 0):coeffs(i,e,idx_hint){}
	c_adic(const c_adic<CYC_TYPE >& o):coeffs(o.coeffs){}
	~c_adic(){coeffs.clear();}
	const _cyc& operator[](int index) const {
		_c_iter c = coeffs.find(index);
		return c==coeffs.end()?_cyc::VALS[0u]:c->operator*();
	}
	c_adic<CYC_TYPE >& operator=(const c_adic<CYC_TYPE >& o) {
		coeffs = o.coeffs;
		return *this;
	}
	friend c_adic<CYC_TYPE > operator+(const c_adic<CYC_TYPE >& c1, const c_adic<CYC_TYPE >& c2){
		_u_int sz1 = c1.coeffs.size(), sz2 = c2.coeffs.size();
		c_adic<CYC_TYPE > sum;
		if(sz1!=0){
			const _cyc* lst (0);
			if(sz1>sz2) {sum = c1; lst = &c2;}
			else {sum = c2; lst = &c1;}
			_c_iter i = lst->coeffs.begin(), e = lst->coeffs.end();
			for( ; i!=e; ++i){
				_iter ii = sum.coeffs.find(i.id());
				if(ii==sum.coeffs.end()) sum.coeffs.begin().insert(*i,i.idx());
				else {
					const _cyc& sm = (*ii) + (*i);
					if(sm<*ii||sm<*i) sum = sum + c_adic<CYC_TYPE >(i.idx()+1,1);
					else if(sm>*ii&&sm>*i) sum.set_coef(i.idx(),sm);
					else sum.set_coef(ii.idx(),CYC_TYPE);
				}
			}
		}
		else if(sz2!=0) {
			sum = c2;
		}
		return sum;
	}
	//iterators
	_c_iter begin() const {return coeffs.begin();}
	_iter   begin() {return coeffs.begin();}
	_c_iter   end() const {return coeffs.end();}
	_iter     end() {return coeffs.end();}

	_c_iter  find(_u_int idx) const {return coeffs.find(idx);}
	_c_iter  find(const _cyc& c) const {return coeffs.find(c);}
	_iter    find(_u_int idx) {return coeffs.find(idx);}
	_iter    find(const _cyc& c) {return coeffs.find(c);}
	/*
	friend _cad operator+(const _cad& c1, const _cad& c2) {
		const _u_int& sz1 = c1.coeffs.size(), & sz2 = c2.coeffs.size();
		const _cad* mn = 0;
		_cad sum;
		if(sz1>sz2) {mn = &c2;sum = c1;}
		else {mn = & c1; sum = c2;}
		_c_iter i = mn->begin(), e = mn->end();


	}*/
	friend _cad operator*(const _cad& c1, const _cad& c2) {
		return _cad();
	}
	friend std::ostream& operator<<(std::ostream& o, const _cad& c){
		_c_iter i = c.begin(), e = c.end();
		if(i==e) return o << "0_" << CYC_TYPE;
		while(true) {
			o << _cyc::value(i->operator*());
			_u_int id(i->idx());
			if(id>=1) {
				o << "." << CYC_TYPE;
				if(id>=2) o<< "^" << id;
			}
			++i;
			if(i!=e) o << " + ";
			else break;
		}
		return o;
	}
private:
	_list coeffs;
	void set_coef(int v) {
		if(v!=0) {
			_iter iii = coeffs.begin();
			int ii = v>=0?v%CYC:(CYC+(v%CYC));
			std::cout << "v = " << v <<"\tr = " <<ii<<"\n";
			if(ii!=0) {
				iii.insert(_cyc::VALS[ii],0,true);
				//v = (v>0?v-ii:v+ii)/CYC;
				//std::cout << "v = " << v <<"\tr = " <<ii<<"\n";
				//set_coef(1,v,iii);
			}
			//else
			set_coef(1,v/CYC,iii);
			coeffs.set_array();
		}
	}
	void set_coef(_u_int i, _cyc_cref val){
		if(val!=_cyc::VALS[0]) {
			coeffs.insert(i,val);
		}
	}
	/*
	void set_coef(_u_int i, int v, _iter& iii){
		if(v!=0) {
			int ii = v>=0?v%CYC:(CYC+(v%CYC));
			std::cout << "v = " << v <<"\tr = " <<ii<<"\n";
			if(ii!=0) {iii.insert(_cyc::VALS[ii],i,true);}//int w =(v>0?v-ii:v+ii)/CYC;std::cout << "v = " << w <<"\tr = " <<ii<<"\n";set_coef(i+1,w,iii);}
			//return;
			set_coef(i+1,v/CYC,iii);
		}
	}*/
	static const int CYC;
};
template<_u_int CYC_TYPE >
const int c_adic<CYC_TYPE >::CYC = (int) CYC_TYPE;
template<_u_int CYC_TYPE >
const c_adic<CYC_TYPE >& c_adic<CYC_TYPE >::ZERO = c_adic<CYC_TYPE >();
template<_u_int CYC_TYPE >
const c_adic<CYC_TYPE >& c_adic<CYC_TYPE >::ONE  = c_adic<CYC_TYPE >(1);
//namespace util {
//template<_u_int CYC_TYPE >
//const cyclic<CYC_TYPE >& util::d_link_exp<const cyclic<CYC_TYPE >& >::init_DEF(){return cyclic<CYC_TYPE >::VALS[0];}
//}
#else /* _H_C_ADIC_USE_STD*/
#include "c_adic_std.hpp"
#endif /* _H_C_ADIC_USE_STD */



#endif /* INCLUDE_ALG_1_1_RING_RING_C_ADIC_HPP_ */
