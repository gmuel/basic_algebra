/*
 * lie_base_impl.hpp
 *
 *  Created on: 26.07.2019
 *      Author: stream_vid
 */

#ifndef INCLUDE_LIE_BASE_IMPL_HPP_
#define INCLUDE_LIE_BASE_IMPL_HPP_

#include "lie_base.hpp"
#include <map>


namespace alg {

template<
typename BASE_RNG
>
class sl2_el {
public:
	typedef sl2_el<BASE_RNG > _ths;
	//typedef std::map<root_coeff,BA>
	const static struct root {
		~root(){}
		friend class sl2_el;
		friend bool operator==(const root& r1, const root& r2) {return &r1==&r2;}
		friend bool operator!=(const root& r1, const root& r2) {return &r1!=&r2;}
	private:
		root(){}
		root(const root& o);
		root& operator=(const root& o);

	}& X, & Y, & H;
	struct root_coeff {
		root_coeff(const root& rt):r(rt),coeff(){}
		root_coeff(const root& rt, const BASE_RNG coef):r(rt),coeff(coef){}
		root_coeff(const root_coeff& o):r(o.r),coeff(o.coeff){}
		~root_coeff(){}
		root_coeff& operator=(const root_coeff& o) {
			coeff = o.coeff;
			return *this;
		}
		const root& r;
		BASE_RNG coeff;
	};
	const BASE_RNG& operator()(const root& r) const {
		return r==x.r?x.coeff:
				r==y.r?y.coeff:
						h.coeff;
	}
	BASE_RNG& operator()(const root& r) {
		return r==x.r?x.coeff:
				r==y.r?y.coeff:
						h.coeff;
	}
	friend struct sl2_el_add {
		_ths operator()(const _ths& e1, const _ths& e2) const {
			return _ths(
						root_coeff(X,e1.x.coeff+e2.x.coeff),
						root_coeff(Y,e1.y.coeff+e2.y.coeff),
						root_coeff(H,e1.h.coeff+e2.h.coeff)
						);
		}
	};
	friend struct sl2_el_anti {
			_ths operator()(const _ths& e) const {
				return _ths(
							root_coeff(X,-e.x.coeff),
							root_coeff(Y,-e.y.coeff),
							root_coeff(H,-e.h.coeff)
							);
			}
		};
	friend struct sl2_el_equi {
			bool operator()(const _ths& e1, const _ths& e2) const {
				return e1.x.coeff==e2.x.coeff &&
						e1.y.coeff==e2.y.coeff &&
							e1.h.coeff==e2.h.coeff
							;
			}
		};
	friend struct sl2_el_mul {
			_ths operator()(const _ths& e1, const _ths& e2) const {
				return _ths(
							root_coeff(X,e1.h.coeff*(e2.x.coeff+e2.x.coeff)-(e1.x.coeff+e1.x.coeff)*e2.h.coeff),
							root_coeff(Y,(e1.y.coeff+e1.y.coeff)*e2.h.coeff-e1.h.coeff*(e2.y.coeff+e2.y.coeff)),
							root_coeff(H,e1.x.coeff*e2.y.coeff-e1.y.coeff*e2.x.coeff)
							);
			}
		};
	friend struct sl2_el_lscl {
		_ths operator()(const BASE_RNG& scl, const _ths& e) const {
			return _ths(
					root_coeff(X,scl*e.x.coeff),
					root_coeff(Y,scl*e.y.coeff),
					root_coeff(H,scl*e.h.coeff)
			);

		}
	};
	friend struct sl2_el_rscl {
		_ths operator()(const _ths& e, const BASE_RNG& scl) const {
			return _ths(
					root_coeff(X,e.x.coeff*scl),
					root_coeff(Y,e.y.coeff*scl),
					root_coeff(H,e.h.coeff*scl)
			);

		}
	};
	friend struct sl2_el_unit {
		template<typename U >
				const _ths& operator()(const U& u) const {
					return ZERO;
				}
			};
	sl2_el():x(X),y(Y),h(H){}
	sl2_el(const root& rt, const BASE_RNG coeff):x(X),y(Y),h(H){rt==X?x.coeff = coeff:rt==Y?y.coeff=coeff:h.coeff = coeff;}
	sl2_el(const root_coeff& xx, const root_coeff& yy, const root_coeff& hh):x(xx),y(yy),h(hh){}
	static const sl2_el& ZERO;
	typedef lie_alg<_ths,BASE_RNG, sl2_el_add ,sl2_el_anti , sl2_el_lscl , sl2_el_rscl , sl2_el_mul, sl2_el_unit > _lie_diag;
private:
	root_coeff x, y, h;
};



}
#endif /* INCLUDE_LIE_BASE_IMPL_HPP_ */
