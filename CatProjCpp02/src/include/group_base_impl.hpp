/*
 * group_base_impl.hpp
 *
 *  Created on: 03.02.2018
 *      Author: stream_vid
 */

#ifndef GROUP_BASE_IMPL_HPP_
#define GROUP_BASE_IMPL_HPP_

#include "group_base.hpp"
#include "cyclics.hpp"
//test implementation
namespace alg {

using namespace alg_fun;

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
struct cyclic_add : public binary<cyclic_wrp<N >, cyclic_add<cyclic_wrp<N> > > {
	typedef cyclic_wrp<N > _cyclic;
	_cyclic operator()(const _cyclic& c1, const _cyclic& c2) const {
		return cyclic_wrp<N >((*c1)+(*c2));
	}
};
template<unsigned int N>
struct cyclic_add_unit : public unit<cyclic_wrp<N > > {
	typedef cyclic_wrp<N > _cyclic;
	template<typename U >
	_cyclic operator()(const U& u) const {
		return _cyclic;
	}
};
template<unsigned int N >
struct cyclic_add_anti : public antipode<cyclic_wrp<N >, cyclic_add_anti<N > > {
	typedef cyclic_wrp<N > _cyclic;
	_cyclic operator()(const _cyclic& c1) const {
		return _cyclic(-(*c1));
	}
};





} /*alg*/

#endif /* GROUP_BASE_IMPL_HPP_ */
