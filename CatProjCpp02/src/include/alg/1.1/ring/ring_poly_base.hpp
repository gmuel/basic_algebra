/*
 * ring_poly_base.hpp
 *
 *  Created on: 10.04.2020
 *      Author: lmmac
 */

#ifndef INCLUDE_ALG_1_1_RING_RING_POLY_BASE_HPP_
#define INCLUDE_ALG_1_1_RING_RING_POLY_BASE_HPP_


#include "ring_poly.hpp"

namespace alg {

struct numb_mon {
	int operator()(int arg) const {
		typename std::map::const_iterator i = monom.find(arg);
		return i==monom.end()?0:i->first;
	}

private:
	std::map<int,int> monom;
};

struct numb_grade {
	int operator() (const numb_mon& m) const;
private:
	std::map<int,int> lin_form;

};

template<
	typename RNG,
	typename ORD
	>
class graded_poly : public poly<numb_mon,RNG,ORD> {
public:
	typedef poly<numb_mon,RNG,ORD> 		_base;
	template<typename RNG1,typename ORD1 >
	graded_poly(const graded_poly<RNG1,ORD1>& o):
		_base(o){}

};

} /* alg*/

#endif /* INCLUDE_ALG_1_1_RING_RING_POLY_BASE_HPP_ */
