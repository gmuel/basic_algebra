#include "../include/hyperbools.hpp"

namespace cat {
/*
const tert& tert::FALSE_ = n_bool(ZERO);
//const tert& n_bool<3 >::FALSE_ = n_bool(ZERO);
const tert& n_bool<3 >::TRUE_  = n_bool(ONE);
const tert& n_bool<3 >::INDET_ = n_bool(TWO);

//n_bool<3 >::n_bool():val(ZERO){}
n_bool<3 >::n_bool(int tt):val(tt%3==0?ZERO:
		tt>0?tt%3==1?ONE:TWO:
			3+tt%3==1?ONE:TWO){}
n_bool<3 >::n_bool(bool tt):val(tt?ONE:ZERO){}
n_bool<3 >::n_bool(const terinaries& tt):val(tt){}
n_bool<3 >::n_bool(const tert& tt):val(tt.val){}

//tert& tert::operator=(const tert& tt){val = }

tert operator&&(const tert& t1, const tert& t2){
	return t1.val!=2&&t2.val!=2?tert((t1.val*t2.val)%3):
		tert::INDET_;
}
tert operator||(const tert& t1, const tert& t2){
	return t1.val==1||t2.val==1?tert::TRUE_:
		t1.val==0?t2:t2.val==0?t1:
			tert::INDET_;
}
tert operator!(const tert& t){
	return t.val==0?tert::ONE:t.val==1?tert::ZERO:tert::TWO;
}
bool operator==(const tert& t1, const tert& t2){return t1.val==t2.val;}
bool operator!=(const tert& t1, const tert& t2){return t1.val!=t2.val;}
*/
#ifdef HYPERBOOLS_HPP_TYPEDEFFING

#else
Bool::n_bool(int tt):val(tt==0?false:true){}
Bool::n_bool(bool tt):val(tt){}
Bool::n_bool(const n_bool<1 >& tt):val(tt.val){}

Bool::~n_bool(){}
bool Bool::value()const {return val;}

n_bool<1>& Bool::operator=(const n_bool<1 >& t) {val = t.val;return *this;}

n_bool<1 > operator&&(const n_bool<1 >& t1, const n_bool<1 >& t2)
{
	return t1.val&&t2.val;
}

n_bool<1 > operator||(const n_bool<1>& t1, const n_bool<1 >& t2)
{
	return t1.val||t2.val;
}

n_bool<1 > operator!(const n_bool<1 >& t){
	return !t.val;
}
bool operator==(const n_bool<1 >& t1, const n_bool<1 >& t2)
{
	return t1.val==t2.val;
}

bool operator!=(const n_bool<1 >& t1, const n_bool<1 >& t2)
{
	return t1.val!=t2.val;
}

#endif /*HYPERBOOLS_HPP_TYPEDEFFING*/

}
