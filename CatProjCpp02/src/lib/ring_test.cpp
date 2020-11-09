/*
 * ring_test.cpp
 *
 *  Created on: 11.04.2020
 *      Author: lmmac
 */




#include "../include/ring_base_impl.hpp"

#include <iostream>

using namespace std;
using namespace alg;


typedef cyc_rng_obj<2u > _c2;
typedef cyc_rng_obj<3u > _c3;

template<unsigned int N>
ostream& operator<<(ostream& o, const cyc_rng_obj<N > c){
	return o << cyclic<2u >::value((*c)) << " mod " << N;
}

_c2 operator+(const _c2& cc0, const _c2& cc1){
	return alg::operator +<cyclic_wrp<2u > >(cc0,cc1);
}

int main (){
	_c2 c02(1), c12(0);
	cout << c02 << ", "<<c12;
	cout <<"\n"<<c02 << " + "<<c12 << " = " << (c02+c12);
}
