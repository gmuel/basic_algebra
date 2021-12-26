/*
 * test_cycle.cpp
 *
 *  Created on: 27.07.2021
 *      Author: iworker
 */

#include "../include/cycle.hpp"
//#include <iostream>
using namespace alg;
using namespace std;


int main(){
	string s = "123,43,2";
	cycle_parser cp(s);
	cycle c = cp.parse()?cp.getCycle():cycle::EMPTY_CYCLE;
	cout << c << "\n";
}
