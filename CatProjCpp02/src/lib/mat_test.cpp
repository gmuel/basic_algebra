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
typedef cyclic_wrp<2 > c2;
static c2 ONE(1);
int main () {
	matrix<c2 > e0(0,0,ONE), e1 (0,1,ONE),
			e2(1,0,ONE),e3(1,1,ONE);

}

