/*
 * group_group_base.hpp
 *
 *  Created on: 01.12.2019
 *      Author: lmmac
 */

#ifndef INCLUDE_ALG_1_1_GROUP_GROUP_GROUP_BASE_HPP_
#define INCLUDE_ALG_1_1_GROUP_GROUP_GROUP_BASE_HPP_


#include "../cat/cat_cat_base.hpp"
namespace alg {

//using namespace cat;

template<typename T , typename BI_OPER>
struct binary {
	static const BI_OPER& OPERATION;
	T operator()(const T& t1, const T& t2) const {
		return OPERATION(t1,t2);
	}
};
/*
template<typename T, typename BI_OPER >
const BI_OPER& binary<T,BI_OPER >::OPERATION = BI_OPER();
*/

template<typename T , typename TER_OPER>
struct teriary {
	static const TER_OPER& OPERATION;
	T operator()(const T& a, const T& b, const T& c) const {
		return OPERATION(a,b,c);
	}
};
template<typename T, typename TER_OPER >
const TER_OPER& teriary<T,TER_OPER >::OPERATION = TER_OPER();
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
	const BINARY 				operation;
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
	typedef SEMI_GROUP 	_s_group;
	typedef BINARY		_bin;


};
#ifdef _GROUP_BASE_HPP_USE_DEFAULTS
template<typename T, typename U >
const U& semi_group<T,U>::OPERATION = U();
template<typename T, typename U >
const assoc<T,U >& semi_group<T,U >::ASS_DIAGRAM = assoc<T,U>(semi_group<T,U>::OPERATION);
#endif
template<typename MONOID , typename BINARY , typename _UNIT = unit<MONOID> >
struct unital_associative : public assoc<MONOID, BINARY > {
	typedef assoc<MONOID,BINARY > _base;
	typedef semi_group<MONOID,BINARY > _semi_bind;
	typedef _UNIT	_unit;
	static const _unit& UNIT;

	unital_associative(const BINARY& bin):_base(bin){}

};
template<typename MONOID , typename BINARY , typename UNIT = unit<MONOID> >
class monoid : public cat::cat<unital_associative<MONOID, BINARY,UNIT > > {
public:
	static const unital_associative<MONOID, BINARY, UNIT >& MONOID_DIAGRAM;
};
#ifdef _GROUP_BASE_HPP_USE_DEFAULTS
template<typename MONOID, typename BINARY, typename UNIT >
const unital_associative<MONOID,BINARY,UNIT >& monoid<MONOID,BINARY,UNIT >::MONOID_DIAGRAM =
	unital_associative<MONOID,BINARY,UNIT >(BINARY());
#endif
template<typename MONOID, typename BINARY >
class mon_2_semi {

};
template<typename GROUP, typename ANTI >
struct antipode {
	typedef ANTI	_ant;
	static const ANTI& ANTIPODE;
	GROUP operator()(const GROUP& g) const {return ANTIPODE(g);}
};
template<typename GROUP, typename BINARY,
	typename ANTI,
	typename UNIT = unit<GROUP > >
struct group_diag : public unital_associative<GROUP, BINARY, UNIT > {
	bool operator()(const GROUP& g) const {
		return g/g == unit<GROUP >::UNIT;
	}
	typedef unital_associative<GROUP,BINARY,UNIT > 	_u_assc;
	typedef  antipode<GROUP,ANTI >					_ant;
};
template<typename GROUP, typename BINARY >
GROUP operator*(const GROUP& g1, const GROUP& g2) {
	return binary<GROUP,BINARY >::OPERATION(g1,g2);
}
template<typename GROUP, typename ANTI >
GROUP operator/(const GROUP& g1, const GROUP& g2) {
	return g1 * antipode<GROUP,ANTI >::ANTIPODE(g2);
}
template
template<typename GROUP, typename BINARY,
typename ANTI,
typename UNIT = unit<GROUP > >
class group : public cat::cat<group_diag<GROUP,BINARY,UNIT,ANTI > > {
public:
	typedef group_diag<GROUP,BINARY,UNIT,ANTI > 	_grp_diag;
	typedef unital_associative<GROUP,BINARY,UNIT >	_mon_diag;
protected:
	typedef GROUP (BINARY::*multiply)(const GROUP& g1, const GROUP& g2) const;
	static const multiply MUL = BINARY::operator();
	typedef GROUP (UNIT::*unit) () const ;
	static const unit E = UNIT::operator();
};
template<typename GROUP, typename BINARY,
typename ANTI,
typename UNIT = unit<GROUP > >
struct forget : public cat::functor<group_diag<GROUP,BINARY,ANTI,UNIT >, cat::eq_diag<GROUP > > {

};

template<typename DOM1, typename DOM2 >
struct flip : public cat::morph<util::pair<DOM1,DOM2>, util::pair<DOM2,DOM1>,flip<DOM1,DOM2 > > {
	typedef util::pair<DOM1,DOM2 > _pair1;
	typedef util::pair<DOM2,DOM1 > _pair2;
	_pair2 operator()(const _pair1& arg) const {return _pair2(_pair1::P2(arg),_pair1::P1(arg));}
};

template<typename GRP,
typename BINARY,
typename ANTI,
typename UNIT = unit<GRP > >
struct abel_diag : public group_diag<GRP,BINARY,ANTI,UNIT > {
	typedef group_diag<GRP,BINARY,ANTI,UNIT > _grp_bind;
	static const flip<GRP,GRP >& FLIP;
	bool operator()(const GRP& g, const GRP& h) const {return g*h==h*g;}
};
#ifdef _GROUP_BASE_HPP_USE_DEFAULTS
template<typename GRP,
typename BINARY,
typename ANTI,
typename UNIT >
const flip<GRP,GRP >& abel_diag<GRP,BINARY,ANTI,UNIT >::FLIP = flip<GRP,GRP > ();
#endif /*_GROUP_BASE_HPP_USE_DEFAULTS*/
template<typename GRP,
typename BINARY,
typename ANTI,
typename UNIT = unit<GRP > >
struct abel : public cat::cat<abel_diag<GRP,BINARY, ANTI, UNIT > > {
	typedef group<GRP,BINARY,ANTI,UNIT > 			_grp_bind;
	typedef abel_diag<GRP,BINARY, ANTI, UNIT > 		_abl_bind;

};
template<
	typename X,
	>
struct binary
#ifndef INCLUDE_ALG_1_1_GROUP_BASE_HPP_LOCAL_ABEL_FRNDS
#define INCLUDE_ALG_1_1_GROUP_BASE_HPP_LOCAL_ABEL_FRNDS(ABL,BIN,ANT) \
	friend ABL operator+(const ABL& a1, const ABL& a2 ) const {static BIN bin; return bin(a1,a2);} \
	friend ABL operator-(const ABL& a) const {static ANT ant; return ant(a);}
#endif
/*template<typename GROUP, typename BINARY,
typename ANTI,
typename UNIT >
const group<GROUP,BINARY,ANTI,UNIT >::multiply group<GROUP,BINARY,ANTI,UNIT >::MUL
	= BINARY::operator();*/
} /* alg */



#endif /* INCLUDE_ALG_1_1_GROUP_GROUP_GROUP_BASE_HPP_ */
