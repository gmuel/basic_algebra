/*
 * test_list.cpp
 *
 *  Created on: 10.03.2018
 *      Author: stream_vid
 */

#include <iostream>
using namespace std;
#ifdef _USE_LISTS_N_TESTS
#include "../include/test_util.hpp"
#define D_LINK_LIST_H01_UNSIZED_D_LINKS
#include "../include/d_link_list01.hpp"
using namespace test_util;
using namespace util;
typedef d_linked_exp_list<int > int_list;
typedef int_list::iterator _iter;
typedef int_list::const_iterator _citer;
int main (){}
#else
#ifdef _USE_CYCLICS
#include "../include/cyclics_fun.hpp"
using namespace alg_fun;using namespace alg;
CYCLICS_HPP_TYPE_DEF_CREF(2u);
CYCLICS_HPP_TYPE_DEF_CREF(5u);
CYCLICS_HPP_TYPE_DEF_CREF(10u);
typedef cyclic_project<10u,2u > _proj1;
typedef cyclic_project<10u,5u > _proj2;
typedef cyclic_project<10u,3u > _proj3;
_proj3 pr3;


int main (){
	_proj1 pr1;
	_proj2 pr2;
	for (unsigned int i = 0; i < 10; ++i){
		cr_10u val = cyclic<10u >::VALS[i];
		cout << i << " % 2 = " << pr1(val);
		cout <<"\n" << i << " % 5 = "<< pr2(val);
		cout <<"\n";

	}
	//pr3(cyclic<10u>::VALS[0]);
	//_proj3::EXISTS;
}
#endif /*_USE_CYCLICS*/
#endif



