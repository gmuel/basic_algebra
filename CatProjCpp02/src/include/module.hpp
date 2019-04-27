/*
 * module.hpp
 *
 *  Created on: 14.04.2019
 *      Author: stream_vid
 */

#ifndef INCLUDE_MODULE_HPP_
#define INCLUDE_MODULE_HPP_
#include "ring_base.hpp"

namespace alg {

template<typename M,
typename BASE_RNG,
typename BINARY,
typename ANTI,
typename SCAL,
typename UNIT = unit<M >
>
struct lmod_diag : public abel_diag<M,BINARY,ANTI,UNIT >  {
	static const SCAL& LEFT;
	typedef abel_diag<M,BINARY,ANTI,UNIT > grp_diag_bind;
	struct left_dist {
		bool operator()(const BASE_RNG& r, const M& m1, const M& m2) const {
			return LEFT(r,m1+m2)==(Left(r,m1)+LEFT(r,m2));
		}
	};
	struct left_add {
		bool operator()(const BASE_RNG& r1, const BASE_RNG& r2, const M& m) const {
			return (LEFT(r1,m)+LEFT(r2,m))==LEFT(r1+r2,m);
		}
	};
	struct left_mul {
			bool operator()(const BASE_RNG& r1, const BASE_RNG& r2, const M& m) const {
				return (LEFT(r1,m)*LEFT(r2,m))==LEFT(r1*r2,m);
			}
		};
};

template<typename M,
typename BASE_RNG,
typename BINARY,
typename ANTI,
typename SCAL,
typename UNIT = unit<M >
>
struct rmod_diag : public abel_diag<M,BINARY,ANTI,UNIT >  {
	static const SCAL& RIGHT;
	typedef abel_diag<M,BINARY,ANTI,UNIT > grp_diag_bind;
	struct left_dist {
		bool operator()(const BASE_RNG& r, const M& m1, const M& m2) const {
			return RIGHT(m1+m2,r)==(RIGHT(m1,r)+RIGHT(m2,r));
		}
	};
	struct right_add {
		bool operator()(const BASE_RNG& r1, const BASE_RNG& r2, const M& m) const {
			return (RIGHT(m,r1)+RIGHT(m,r2))==RIGHT(m,r1+r2);
		}
	};
	struct left_mul {
			bool operator()(const BASE_RNG& r1, const BASE_RNG& r2, const M& m) const {
				return (RIGHT(m,r1)*RIGHT(m,r2))==RIGHT(m,r1*r2);
			}
		};
};


} /*alg*/


#endif /* INCLUDE_MODULE_HPP_ */
