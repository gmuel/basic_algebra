/*
 * group_group_base_impl.hpp
 *
 *  Created on: 01.12.2019
 *      Author: lmmac
 */

#ifndef INCLUDE_ALG_1_1_GROUP_GROUP_GROUP_BASE_IMPL_HPP_
#define INCLUDE_ALG_1_1_GROUP_GROUP_GROUP_BASE_IMPL_HPP_



#include "group_group_base.hpp"
#include "group_cyclics.hpp"
//test implementation
namespace alg {


using namespace alg_fun;
/**
 * @brief Wrapper structure template for <tt>alg_fun::cyclic&ltN &gt</tt>
 * @p Each instance is zero const-able, copy-const-able, etc.
 * @tparam N group order
 */
template<unsigned int N >
struct cyclic_wrp {
	typedef cyclic<N > _cyclic;
	cyclic_wrp(int val = 0):ptr(&cyclic<N>::VALS[val]){}
	cyclic_wrp(const cyclic<N>& val):ptr(&val){}
	cyclic_wrp(const cyclic_wrp<N >& o):ptr(o.ptr){}
	~cyclic_wrp(){ptr=0;}
	cyclic_wrp<N >& operator=(int val);
	cyclic_wrp<N >& operator=(const cyclic<N>& val){

		return *this;
	}
	cyclic_wrp<N >& operator=(const cyclic_wrp<N>& val){
		val.ptr==ptr?:ptr=val.ptr;
		return *this;
	}
	const cyclic<N>& operator*() const {return *ptr;}
private:
	const cyclic<N>* ptr;
};
template<unsigned int N>
struct cyclic_add : public binary<cyclic_wrp<N >, cyclic_add<N > > {
	typedef cyclic_wrp<N > _cyclic;
	_cyclic operator()(const _cyclic& c1, const _cyclic& c2) const {
		return cyclic_wrp<N >((*c1)+(*c2));
	}
};
template<unsigned int N>
struct cyclic_add_unit : public unit<cyclic_wrp<N > > {
	typedef cyclic_wrp<N > _cyclic;
	template<typename U >
	const _cyclic& operator()(const U& u) const {
		static _cyclic zero;
		return zero;
	}
};
template<unsigned int N >
struct cyclic_add_anti : public antipode<cyclic_wrp<N >, cyclic_add_anti<N > > {
	typedef cyclic_wrp<N > _cyclic;
	_cyclic operator()(const _cyclic& c1) const {
		return _cyclic(-(*c1));
	}
};
template<unsigned int N >
struct cyclic_group_diag : public alg::group_diag<cyclic_wrp<N >,
	cyclic_add<N >, cyclic_add_anti<N >,cyclic_add_unit<N > > {

};

template<unsigned int N >
struct cyclic_group : public group<cyclic_wrp<N >,
cyclic_add<N >, cyclic_add_anti<N >,cyclic_add_unit<N > > {
	typedef cyclic_wrp<N >	_cyc_el;
	typedef cyclic_add<N >		_cyc_ad;
	typedef cyclic_add_anti<N >	_cyc_at;
	INCLUDE_ALG_1_1_GROUP_BASE_HPP_LOCAL_ABEL_FRNDS(cyc_el, cycl_ad, cyclic_add_anti<N > )
};



} /*alg*/




#endif /* INCLUDE_ALG_1_1_GROUP_GROUP_GROUP_BASE_IMPL_HPP_ */
