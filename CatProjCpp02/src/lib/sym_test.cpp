/*
 * sym_test.cpp
 *
 *  Created on: 09.01.2022
 *      Author: gamer
 */


#include "../include/alg/1.1/group/group_symmetric_group.hpp"
#include <iostream>

using namespace alg;

using namespace std;

typedef cyclic_wrp<3> _z3;
typedef symmetric<3> _sym3;
typedef typename _sym3::const_sym_iterator _c_iter;
typedef typename _sym3::sym_iterator _iter;

struct init_iter123 {
	unsigned int i = 0;
	init_iter123& operator++() {
		if(i<3) ++i;
		return *this;
	}
	_z3 operator*() const {
		return _z3(cyclic<3>::VALS[i]);
	}
	friend bool operator==(const init_iter123& i1, const init_iter123& i2){
		return i1.i== i2.i ?true:false;
	}
	static init_iter123 end() {
		static init_iter123 e;
		e.i=3;
		return e;
	}
};

struct init_iter132 {
	unsigned int i = 0;
	init_iter132& operator++() {
		if(i>0) --i;
		return *this;
	}
	_z3 operator*() const {
		return _z3(cyclic<3>::VALS[i]);
	}
	friend bool operator==(const init_iter132& i1, const init_iter132& i2){
		return i1.i== i2.i ?true:false;
	}
	static init_iter132 end() {
		static init_iter132 e;
		e.i=3;
		return e;
	}
};
template<typename X>
bool operator!=(const X& x1, const X& x2){
	return !(x1==x2);
}

int main () {
	_sym3 s123(init_iter123(),init_iter123::end()),
				s132(init_iter132(),init_iter132::end());

}
