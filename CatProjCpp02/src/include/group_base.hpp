#ifndef H_GROUP_BASE_HPP
#define H_GROUP_BASE_HPP
#include "cat_base.hpp"
namespace alg {

using namespace cat;

template<typename T , typename BI_OPER>
struct binary {
	const BI_OPER& OPERATION;
};
template<typename T , typename TER_OPER>
struct teriary {
	const TER_OPER& OPERATION;
	T operator()(const T& a, const T& b, const T& c) const ;
};
template<typename T , typename BINARY>
struct left_hand : public teriary<T, left_hand<T, BINARY > > {
	T operator()(const T& a, const T& b, const T& c) const {
		return binary(binary(a,b),c);
	}
	left_hand(const BINARY& bin):binary(bin){}
	left_hand(const left_hand<T, BINARY >& o):binary(o.binary){}
	left_hand& operator=(const BINARY& bin){
		binary = bin;

		return *this;
	}
	~left_hand(){}
protected:
	BINARY binary;
};
template<typename T , typename BINARY>
struct right_hand : public teriary<T, right_hand<T,BINARY > > {
	T operator()(const T& a, const T& b, const T& c) const {
		return binary(a,binary(b,c));
	}
	right_hand(const BINARY& bin):binary(bin){}
	right_hand(const right_hand<T, BINARY >& o):binary(o.binary){}
	right_hand& operator=(const BINARY& bin){
		binary = bin;

		return *this;
	}
	~right_hand(){}
protected:

	BINARY binary;
};
template<typename T , typename BINARY>
struct assoc : public eq_diag<teriary<T, left_hand<T,BINARY > > > {
	assoc(const BINARY& bin):operation(bin),left(bin),right(bin){}
private:
	const BINARY operation;
	const left_hand<T,BINARY> 	left;
	const right_hand<T,BINARY > right;

};



} /* alg */

#endif /*  H_GROUP_BASE_HPP */
