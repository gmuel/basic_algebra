/*
 * modules_module.hpp
 *
 *  Created on: 01.12.2019
 *      Author: lmmac
 */

#ifndef INCLUDE_ALG_1_1_MODULES_MODULES_MODULE_HPP_
#define INCLUDE_ALG_1_1_MODULES_MODULES_MODULE_HPP_

#include "../ring/ring_ring_base.hpp"
#include <map>
namespace alg {
/**
 * @brief Generic left module diagram structure template
 * @tparam M module type
 * @tparam BASE_RNG base ring
 * @tparam BINARY module addition
 * @tparam SCAL scalar left multiplication (BASE_RNG left action)
 * @tparam UNIT zero element
 */
template<typename M,
typename BASE_RNG,
typename BINARY,
typename ANTI,
typename SCAL,
typename UNIT = unit<M >
>
struct lmod_diag : public abel_diag<M,BINARY,ANTI,UNIT >  {
	static const SCAL& LEFT;
	typedef abel_diag<M,BINARY,ANTI,UNIT > 		_abl_bind;
	/**
	 * @brief Left distributivity tautology
	 */
	struct left_dist {
		bool operator()(const BASE_RNG& r, const M& m1, const M& m2) const {
			return LEFT(r,m1+m2)==(Left(r,m1)+LEFT(r,m2));
		}
	};
	/**
	 * @brief Scalar addition tautology
	 */
	struct left_add {
		bool operator()(const BASE_RNG& r1, const BASE_RNG& r2, const M& m) const {
			return (LEFT(r1,m)+LEFT(r2,m))==LEFT(r1+r2,m);
		}
	};
	/**
	 * @brief Scalar-scalar multiplication tautology
	 */
	struct left_mul {
		bool operator()(const BASE_RNG& r1, const BASE_RNG& r2, const M& m) const {
			return (LEFT(r1,m)*LEFT(r2,m))==LEFT(r1*r2,m);
		}
	};
};
/**
 * @brief Left module structure template
 * @see lmod_diag
 */
template<typename M,
typename BASE_RNG,
typename BINARY,
typename ANTI,
typename SCAL,
typename UNIT = unit<M >
>
struct lmod : public cat::cat<lmod_diag<M, BINARY, ANTI, SCAL, UNIT > > {
	typedef lmod_diag<M, BINARY, ANTI, SCAL, UNIT >		_lmod_bind;
	typedef M											_obj;
	typedef SCAL										_scl;
};
/**
 * @brief Generic right module diagram structure template
 * @tparam M module type
 * @tparam BASE_RNG base ring
 * @tparam BINARY module addition
 * @tparam SCAL scalar right multiplication (BASE_RNG right action)
 * @tparam UNIT zero element
 */
template<typename M,
typename BASE_RNG,
typename BINARY,
typename ANTI,
typename SCAL,
typename UNIT = unit<M >
>
struct rmod_diag : public abel_diag<M,BINARY,ANTI,UNIT >  {
	static const SCAL& RIGHT;
	typedef abel_diag<M,BINARY,ANTI,UNIT > _abl_bind;
	/**
	 * @brief Right distributivity tautology
	 */
	struct right_dist {
		bool operator()(const BASE_RNG& r, const M& m1, const M& m2) const {
			return RIGHT(m1+m2,r)==(RIGHT(m1,r)+RIGHT(m2,r));
		}
	};
	/**
	 * @brief Scalar addition tautology
	 */
	struct right_add {
		bool operator()(const BASE_RNG& r1, const BASE_RNG& r2, const M& m) const {
			return (RIGHT(m,r1)+RIGHT(m,r2))==RIGHT(m,r1+r2);
		}
	};
	/**
	 * @brief Scalar-scalar multiplication tautology
	 */
	struct right_mul {
		bool operator()(const BASE_RNG& r1, const BASE_RNG& r2, const M& m) const {
			return (RIGHT(m,r1)*RIGHT(m,r2))==RIGHT(m,r1*r2);
		}
	};
};
/**
 * @brief Right module structure template
 * @see rmod_diag
 */
template<typename M,
typename BASE_RNG,
typename BINARY,
typename ANTI,
typename SCAL,
typename UNIT = unit<M >
>
struct rmod : public cat::cat<rmod_diag<M, BINARY, ANTI, SCAL, UNIT > > {
	typedef rmod_diag<M, BINARY, ANTI, SCAL, UNIT >		_rmod_bind;
	typedef M											_obj;
	typedef SCAL										_scl;
};
/**
 * @brief Generic 2-sided module diagram structure template
 * @tparam M module type
 * @tparam BASE_RNG base ring
 * @tparam BINARY module addition
 * @tparam LSCAL scalar left multiplication (BASE_RNG left action)
 * @tparam RSCAL scalar right multiplication (BASE_RNG right action)
 * @tparam UNIT zero element
 */
template<typename M,
typename BASE_RNG,
typename BINARY,
typename ANTI,
typename LSCAL,
typename RSCAL,
typename UNIT = unit<M >
>
struct mod_diag : public abel_diag<M,BINARY,ANTI,UNIT >  {
	typedef abel_diag<M,BINARY,ANTI,UNIT >		_abl_bind;
	typedef lmod_diag<M,BINARY,ANTI,LSCAL,UNIT>	_lmod_bind;
	typedef rmod_diag<M,BINARY,ANTI,RSCAL,UNIT>	_rmod_bind;
};
/**
 * @brief 2-sided module structure template
 * @see mod_diag
 * @see lmod
 * @see rmod
 */
template<typename M,
typename BASE_RNG,
typename BINARY,
typename ANTI,
typename LSCAL,
typename RSCAL,
typename UNIT = unit<M >
>
struct mod : public cat::cat<mod_diag<M,BINARY,ANTI,LSCAL,RSCAL,UNIT > > {
	typedef mod_diag<M,BINARY,ANTI,LSCAL,RSCAL,UNIT >		_mod_bind;
	typedef lmod<M,BINARY,ANTI,LSCAL,UNIT>					_lmod;
	typedef	rmod<M,BINARY,ANTI,RSCAL,UNIT>					_rmod;
	typedef M												_obj;
};


template<typename M,
typename BASE_RNG,
typename BINARY,
typename ANTI,
typename LSCAL,
typename GENS,
typename UNIT = unit<M >
>
struct l_noeth_diag : public lmod_diag<M,BASE_RNG,BINARY,ANTI,LSCAL,UNIT >  {

	typedef lmod_diag<M,BASE_RNG,BINARY,ANTI,LSCAL,UNIT >	_lmod_bind;
	using _lmod_bind::LEFT;
	using _lmod_bind::_abl_bind::_grp_bind::_u_assc::UNIT;
	typedef GENS	_gen;
	struct embed {
		embed(const _gen& g, const M& m):_g(g),_m(m){}
		embed(const embed& o):_g(o._g),_m(o._m){}
		~embed(){}
		M operator()(const _gen& g) const {
			return g==_g?_m:_lmod_bind::_abl_bind::_grp_bind::_u_assc::UNIT(_m);
		}
		M operator()(const BASE_RNG& r) const {return _lmod_bin::LEFT(r,this->operator()(_g));}
		const _gen& operator()() const {return _g;}
	private:
		const _gen& _g;
		const M&	_m;
	};
	typedef	std::map<_gen,embed >	_gen_map;


};


template<typename M,
typename BASE_RNG,
typename BINARY,
typename ANTI,
typename LSCAL,
typename GENS,
typename UNIT = unit<M >
>
struct l_noeth : public cat::cat<l_noeth_diag<M,BASE_RNG,BINARY,ANTI,LSCAL,GENS, UNIT > > {
	typedef l_noeth_diag<M,BASE_RNG,BINARY,ANTI,LSCAL,GENS, UNIT >		_lnoeth_bind;
	typedef Lmod<M,BASE_RNG,BINARY,ANTI,LSCAL,UNIT>						_lmod_bind;
};

template<typename M,
typename BASE_RNG,
typename BINARY,
typename ANTI,
typename RSCAL,
typename GENS,
typename UNIT = unit<M >
>
struct r_noeth_diag : public rmod_diag<M,BASE_RNG,BINARY,ANTI,RSCAL,UNIT >  {
	typedef rmod_diag<M,BASE_RNG,BINARY,ANTI,LSCAL,UNIT >	_rmod_bind;
		using _rmod_bind::RIGHT;
		using _rmod_bind::_abl_bind::_grp_bind::_u_assc::UNIT;
		typedef GENS	_gen;
		static const struct embed {
			embed(const _gen& g, const M& m):_g(g),_m(m){}
			embed(const embed& o):_g(o._g),_m(o._m){}
			~embed(){}
			M operator()(const _gen& g) const {
				return g==_g?_m:_rmod_bind::_abl_bind::_grp_bind::_u_assc::UNIT(_m);
			}
			M operator()(const BASE_RNG& r) const {return _rmod_bind::RIGHT(this->operator ()(_g),r);}
			const _gen& operator()() const {return _g;}
		private:
			const _gen& _g;
			const M&	_m;
		};
		typedef	std::map<_gen,embed>	_gen_map;
};


template<typename M,
typename BASE_RNG,
typename BINARY,
typename ANTI,
typename RSCAL,
typename GENS,
typename UNIT = unit<M >
>
struct r_noeth : public cat::cat<r_noeth_diag<M,BASE_RNG,ANTI,RSCAL,GENS, UNIT > > {
	typedef r_noeth_diag<M,BASE_RNG,BINARY,ANTI,RSCAL,GENS,UNIT >		_rnoeth_bind;
	typedef rmod<M,BASE_RNG,BINARY,ANTI,RSCAL,UNIT>						_rmod_bind;
};

template<typename M,
typename BASE_RNG,
typename BINARY,
typename ANTI,
typename LSCAL,
typename RSCAL,
typename GENS,
typename UNIT = unit<M >
>
struct noeth_diag : public mod_diag<M,BASE_RNG,BINARY,ANTI,LSCAL,RSCAL,UNIT >  {
	typedef l_noeth<M,BASE_RNG,BINARY,ANTI,LSCAL,GENS,UNIT >		_lnoeth_bind;
	typedef r_noeth<M,BASE_RNG,BINARY,ANTI,RSCAL,GENS,UNIT >		_rnoeth_bind;

};


template<typename M,
typename BASE_RNG,
typename BINARY,
typename ANTI,
typename LSCAL,
typename RSCAL,
typename GENS,
typename UNIT = unit<M >
>
struct noeth : public cat::cat<noeth_diag<M,BASE_RNG,ANTI,LSCAL,RSCAL,GENS, UNIT > > {
	typedef noeth_diag<M,BASE_RNG,BINARY,ANTI,LSCAL,RSCAL,GENS, UNIT >			_diag_bin;

};
} /*alg*/





#endif /* INCLUDE_ALG_1_1_MODULES_MODULES_MODULE_HPP_ */
