/*
 * mat_test.cpp
 *
 *  Created on: 02.02.2020
 *      Author: lmmac
 */


#include "../include/ring_base_impl.hpp"
#include "../include/alg/1.1/algebra/algebra_matrix_base.hpp"
#include <iostream>
using namespace alg;
using namespace std;
typedef cyc_rng_obj<2 > c2;
static c2 ONE(1);
ostream& operator<<(ostream& o, const c2& c){
	o << (*c) << " mod 2 ";
	return o;
}
ostream& operator<<(ostream& o, const matrix<c2 >& m){
	unsigned int sz = m.size();
	o << "[";
	for (unsigned int i = 0; i < sz; ++i){
		o << "[";
		for (unsigned int j = 0; j < sz; ++j){
			o << m[i][j];
		}
		o <<"]\n";
	}
	return o << "]";
}
int main () {
	matrix<c2 > e0(0,0,ONE), e1 (0,1,ONE),
			e2(1,0,ONE),e3(1,1,ONE);
	matrix<c2 > m1, m2, m3, m4;
	m1 = e1+e2;
	m2 = e0+e3;
	m3 = m1*m1;

}

