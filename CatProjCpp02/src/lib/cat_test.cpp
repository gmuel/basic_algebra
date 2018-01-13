#include "../include/cat_base.hpp"
#include <iostream>

using namespace std;
using namespace cat;

struct all_int_eq {
	bool operator()(const int& i, const int& j) const {return true;}
} ALL;
struct odd_or_even {
	bool operator()(const int& i, const int& j) const {return (i%2)==(j%2);}
} ODD_EVEN;
namespace cat {
template< >
bool rel<int, all_int_eq >::operator()(const int& i, const int & j) const {return ALL(i,j);}
template<>
bool rel<int, odd_or_even>::operator()(const int& i, const int& j) const {return ODD_EVEN(i,j);}
}

int main (){
	rel<int, all_int_eq > eq1;
	rel<int, odd_or_even> eq2;
	for (int i = 0; i < 10; ++i){for(int j=0; j < 10; ++j) 	{
		cout << i << "==" << j << " ? " << (eq1(i,j)?"true":"false") <<"\t";
		cout << i << "==" << j << " ? " << (eq2(i,j)?"true":"false") <<"\n";
		}
	}

}
