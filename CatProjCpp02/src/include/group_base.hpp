#ifndef H_GROUP_BASE_HPP
#define H_GROUP_BASE_HPP
#include "cat_base.hpp"
namespace alg {

//using namespace cat;

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
struct assoc : public cat::eq_diag<teriary<T, left_hand<T,BINARY > > > {
	assoc(const BINARY& bin):operation(bin),left(bin),right(bin){}
	bool operator()(const T& a, const T& b, const T& c) const {
		return left(a,b,c)==right(a,b,c);
	}
private:
	const BINARY operation;
	const left_hand<T,BINARY> 	left;
	const right_hand<T,BINARY > right;

};
template<typename T >
struct unit {
	static const T& UNIT;
	template<typename U >
	const T& operator()(const U& e) const {return UNIT;}
};
template<typename T >
const T& unit<T>::UNIT = T();
template<typename SEMI_GROUP, typename BINARY >
class semi_group : public cat::cat<assoc<SEMI_GROUP,BINARY > > {
public:
	static const BINARY& 					OPERATION;
	static const assoc<SEMI_GROUP,BINARY >&	ASS_DIAGRAM;


};
template<typename T, typename U >
const U& semi_group<T,U>::OPERATION = U();
template<typename T, typename U >
const assoc<T,U >& semi_group<T,U >::ASS_DIAGRAM = assoc<T,U>(semi_group<T,U>::OPERATION);
template<typename MONOID , typename BINARY >
struct unital_associative : public assoc<MONOID, BINARY > {
	typedef assoc<MONOID,BINARY > _base;
	static const unit<MONOID >& UNIT;

	unital_associative(const BINARY& bin):_base(bin){}

};
template<typename MONOID , typename BINARY >
class monoid : public cat::cat<unital_associative<MONOID, BINARY > > {
public:
	static const unital_associative<MONOID, BINARY >& MONOID_DIAGRAM;
};
template<typename MONOID, typename BINARY >
const unital_associative<MONOID,BINARY >& monoid<MONOID,BINARY >::MONOID_DIAGRAM =
	unital_associative<MONOID,BINARY >(BINARY());
template<typename MONOID, typename BINARY >
class mon_2_semi {

};
} /* alg */

#endif /*  H_GROUP_BASE_HPP */
