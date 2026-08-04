/*
 * diff.cpp
 *
 *  Created on: 04.08.2026
 *      Author: fun_gab
 */




#include "include/num/math/auto_diff.hpp"

using namespace std;
using namespace num;

int main(){
	_fdual f1 {1,0}, f2{0,1}, sum = f1 + f2;
	cout << "f1 + f2 = " << sum << "\n";
	cout << "(f1 + f2)f2 = " << sum * f2 << "\n";
	cout << "exp(f1 + f2) = " << exp(sum)<< "\n";
}
