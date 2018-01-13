#ifndef H_GROUP_BASE_HPP
#define H_GROUP_BASE_HPP
#include "cat_base.hpp"
namespace alg {

using namespace cat;

template<typename T >
struct binary {
	T operator()(const T& a, const T& b) const;
};
template<typename T >
struct teriary {
	T operator()(const T& a, const T& b, const T& c) const ;
};
template<typename T >
struct assoc : public eq_diag<teriary<T > > {
	const 
};



} /* alg */

#endif /*  H_GROUP_BASE_HPP */
