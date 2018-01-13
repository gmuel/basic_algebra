
#define PAIRED_TYPE_HPP_USING_STD_PAIR
#include "../include/set_base.hpp"
#include <iostream>

using namespace std;
using namespace cat;


const empty& empty::EMPTY_SET = empty();//TODO in compi-unit
ostream& operator<<(ostream& o, const empty& e){
	return o << "O = {}";
}
int main() {
	const empty& e = empty::EMPTY_SET;
	cout << e;

}
