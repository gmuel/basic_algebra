//#include <iostream>
#include "../include/cyclics.hpp"
//extern class std::ostream o;
namespace alg_fun {
const cyclic<1u >& cyclic<1u >::VALS = cyclic<1u >();

std::ostream& operator<<(std::ostream& o, const cyclic<1u >& c){
   return o << "0 mod 1";

}


CYCLICS_HPP_TYPE_DEF(2);

cyclic<0u>::cyclic(int i):val(i){}
cyclic<0u>::cyclic(const cyclic<0u>& o):val(o.val){}
cyclic<0u>::~cyclic(){}

unsigned int gcd(unsigned int c1, unsigned int c2){
	bool a[] = {c1==1||c2==1,c1==0,c2==0};
	if(a[0]) return 1u;
	if(a[1]||a[2]) return a[1]&&a[2]?0:a[1]?c2:c1;
	return c1>=c2?gcd(c2,c1%c2):gcd(c1,c2%c1);
}



unsigned int 		cyclic<1u >::value		(const cyclic<1u >& c)	 {return 0u;}
const cyclic<1u >& 	cyclic<1u >::operator[] (unsigned int) const {return VALS;}
const cyclic<1u >& 	cyclic<1u >::operator[] (int ) const 		 {return VALS;}

const cyclic<1u >& operator+(const cyclic<1u >& c1, const cyclic<1u >& c2) {return cyclic<1u >::VALS;}

const cyclic<1u >& operator-(const cyclic<1u >& c1, const cyclic<1u >& c2) {return cyclic<1u >::VALS;}

const cyclic<1u >& operator*(const cyclic<1u >& c1, const cyclic<1u >& c2) {return cyclic<1u >::VALS;}




#ifdef _H_CYCLICS_HPP_FORW_DELC
const cyclic<2u >& operator+(const cyclic<2u >& c1, const cyclic<2u >& c2) {return c_2::VALS[&c1==&c2?0:1];}
const cyclic<2u >& operator-(const cyclic<2u >& c1, const cyclic<2u >& c2) {return c1+c2;}
const cyclic<2u >& operator*(const cyclic<2u >& c1, const cyclic<2u >& c2) {
	return c_2::VALS[(&c1==&c2)&&(&c1!=c_2::VALS)?1:0];
}
#endif /* _H_CYCLICS_HPP_FORW_DELC */


} /*alg_fun*/
