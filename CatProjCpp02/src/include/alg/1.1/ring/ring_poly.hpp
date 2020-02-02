/*
 * ring_poly.hpp
 *
 *  Created on: 31.12.2019
 *      Author: lmmac
 */

#ifndef INCLUDE_ALG_1_1_RING_RING_POLY_HPP_
#define INCLUDE_ALG_1_1_RING_RING_POLY_HPP_

#include "ring_ring_base_impl.hpp"
#include <map>
#include <utility>
namespace alg {
template<typename RNG , typename MON>
class poly {
	typedef typename MON::_mon_bind _mon_bind;
	typedef typename RNG::_rng_bind _rng_bing;
	typedef poly<MON,RNG > 			_ths;
	typedef std::map<MON,RNG> 				_map;
	typedef typename _map::iterator 		_iter;
	typedef typename _map::const_iterator 	_citer;
	_map coeffs;
public:
	poly(const MON& i, const RNG& scl = RNG::UNIT()) : coeffs(){
		if(scl!=0) {
			coeffs[i]=scl;
		}

	}
	poly(const _ths& o):coeffs(o.coeffs){}
	poly& operator=(const _ths& o) {
		coeffs = o.coeffs;
		return *this;
	}
	friend struct poly_add {
		poly operator()(const _ths& p1, const _ths& p2) const {
			int sz1=p1.coeffs.size(), sz2 = p2.coeffs.size();
			const _ths& smd = sz1>sz2?p2:p1;
			_ths sum(sz1>sz2?p1:p2);
			for (_citer i = smd.coeffs.cbegin(); i!=smd.coeffs.cend(); ++i){
				const MON& mn = i->first;
				const RNG& sc = i->second;
				_iter ii = sum.coeffs.find(mn);
				if(ii==sum.coeffs.end()) {
					sum[mn]=sc;
				}
				else{
					const RNG& new_scl = ii->second+sc;
					if(new_scl==0) {
						sum.erase(ii);
					}
					else ii->second = new_scl;
				}
			}

			return sum;
		}
	};
	friend struct poly_anti {
		_ths operator()(const _ths& p) const {
			_ths inv;
			for (_citer i = p.coeffs.cbegin();i!=p.coeffs.cend();++i){
				inv[i->first]=-(i->second);
			}
			return inv;
		}
	};
	friend struct poly_mul {
		_ths operator()(const _ths& p1, const _ths& p2) const {
			_ths prod;
			int sz1 = p1.coeffs.size(), sz2=p2.coeffs.size();
			const _ths& inner = sz1>sz2?p2:p1,& outer = sz1>sz2?p1:p2;
			_citer i1 = outer.coeffs.cbegin(), e1= outer.coeffsgit .cend(),
					i2=inner.coeffs.cbegin(), e2 =inner.coeffs.cend();
			for(;i1!=e1;++i1){
				const MON&  mn1 = i1->first;
				const RNG& scl1 = i1->second;
				for(;i2!=e2;++i2) {
					const MON&  mn2 = i2->first, & mn3 = _mon_bind::_semi_bind::OPERATION(mn1,mn2);
					const RNG& scl2 = i2->second, & scl_prd = scl1*scl2;
					if(scl_prd==0) continue;
					_iter ii = prod.coeffs.find(mn3);
					if(ii==prod.coeffs.end()) {
						prod[mn3]=scl_prd;
					}
					else{
						const RNG& sm = ii->second + scl_prd;
						if(sm==0) prod.coeffs.erase(ii);
						else ii->second = sm;
					}
				}
			}

		}
	};
	friend struct poly_eq {
		bool operator()(const _ths& p1, const _ths& p2) const {
			_citer i1 = p1.coeffs.cbegin(), e1 = p1.coeffs.cend(),
					i2 = p2.coeffs.cbegin(), e2 = p2.coeffs.cend();
			while (i1!=e1&&i2!=e2){
				if (i1->first != i2->first) return false;
				if (i1->second !=i2->second) return false;
				++i1;++i2;
			}

			return i1==e1&&i2==e2?true:false;
		}
	};

};

}


#endif /* INCLUDE_ALG_1_1_RING_RING_POLY_HPP_ */
