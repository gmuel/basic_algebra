/*
 * mat_test.cpp
 *
 *  Created on: 02.02.2020
 *      Author: lmmac
 */


#include "../include/ring_base_impl.hpp"
#include <iostream>

#include "../include/alg/1.1/algebra/algebra_matrix_base.hpp"
using namespace alg;
using namespace std;
typedef cyc_rng_obj<2 > c2;
static c2 ONE(1);
ostream& operator<<(ostream& o, const c2& c){
	o << (*c) << " mod 2 ";
	return o;
}
ostream& operator<<(ostream& o, const matrix_el<c2 >& m){
	unsigned int sz = m.size();
	o << "[";

	for (unsigned int i = 0; i < sz;++i){
		o << "[";
		typename matArray<c2 >::rowIter it = m[i];
		for (unsigned int j = 0;j!=sz;++j){
			o << it[j];
		}
		o <<"]\n";
	}
	return o << "]";
}
//typedef typename matrix_el<c2 >::_uassoc ua;
//static ua A;
/*matrix_el<c2 > operator*(const matrix_el<c2 >& m1, const matrix_el<c2 >& m2){
	return matrix_el<c2 >::_uassoc::operator*<matrix_el<c2 > >(m1,m2);
}*/
int main () {
	matrix_el<c2 > e0(0,0,ONE), e1 (0,1,ONE),
			e2(1,0,ONE),e3(1,1,ONE);
	matrix_el<c2 > m1, m2, m3, m4;
	m1 = operator+<matrix_el<c2 > >(e1,e2);
	m2 = e0+e3;
	m3 = m1*m1;

}

