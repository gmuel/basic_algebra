#include "hyperbools.hpp"

#include <iostream>

using namespace cat;
using namespace std;

template<unsigned int N >
ostream& operator<<(ostream& strm, const n_bool<N >& t){
	strm << t.value() << " of " << N << "-th truth";

}
template<unsigned int N >
void printOR(const n_bool<N >& t1, const n_bool<N >& t2){
	cout << t1 << " || " << t2 << " = " << (t1||t2) <<"\n";
}

template<unsigned int N >
void printAND(const n_bool<N >& t1, const n_bool<N >& t2){
	cout << t1 << " && " << t2 << " = " << (t1&&t2) <<"\n";
}


template<unsigned int N >
void print(){
	n_bool<N > t[N+1];
	for (int i = 0; i < N + 1; ++i){t[i] = n_bool<N >(i);}
	
	for (unsigned int i = 0; i < N + 1; ++i){
		const n_bool<N>& t1 = t[i];
		for (unsigned int j = 0; j < N + 1; ++j) {
			const n_bool<N>& t2 = t[j];
			printOR<N>(t1,t2);
			printAND<N>(t1,t2);
			cout << "---------------------------------------------------------\n";
		}
	
	}
	
}

int main (){
	print<1>();
	cout << "*************************************************\n";
	print<2>();
	cout << "*************************************************\n";
	print<3>();

}
