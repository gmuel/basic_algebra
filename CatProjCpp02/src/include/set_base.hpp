#ifndef SET_BASE_HPP
#define SET_BASE_HPP
#include "cat_base.hpp"

namespace cat {

/****************************************************************
 *																*
 *							set cat								*
 *																*
 ****************************************************************/
 
template<typename SET_TYPE >
struct set_obj : public object<SET_TYPE > {};

struct empty : public set_obj<empty > {
	static const empty& EMPTY_SET;
private:
	empty();
	empty(const empty& o);
	empty& operator=(const empty& o);
};


/*
template<typename SET_TYPE >
const empty& morph<SET_TYPE,const empty& >::IMAGE = empty::EMPTY_SET;
*/


template<typename SET_TYPE >
struct set : public cat<eq_diag<SET_TYPE > > {
	struct eq : public eq_diag<SET_TYPE > {
		friend class set<SET_TYPE >;
		~eq(){}
	private:
		eq():eq_diag<SET_TYPE >(){}
		eq(const eq& o);
		eq& operator=(const eq& o);
	};
	static const eq& EQUAL;
	static const morph<SET_TYPE , const empty& >& EMPTY;

};

template <typename SET_TYPE >
const typename set<SET_TYPE >::eq& set<SET_TYPE >::EQUAL = set<SET_TYPE >::eq();

template<typename SET_TYPE >
const morph<SET_TYPE, const empty& >& set<SET_TYPE >::EMPTY = morph<SET_TYPE,const empty& >();

template<typename _type >
bool operator==(const _type& t1, const _type& t2) {
	return set<_type >::EQUAL(t1,t2);
}

/****************************************************************
 *																*
 *					   pointed space cat						*
 *																*
 ****************************************************************/
 
template<typename SPACE_TYPE >
struct pointed_obj : public set_obj<SPACE_TYPE > {
	struct base_pt {
		static const SPACE_TYPE& BASE_POINT;
	};
};

template<typename SPACE_TYPE >
struct pointed : public set<SPACE_TYPE > {
	static const pointed_obj<SPACE_TYPE >& OBJECT;
};

}


#endif /*SET_BASE_HPP*/
