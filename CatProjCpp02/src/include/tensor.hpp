/*
 * tensor.hpp
 *
 *  Created on: 06.05.2019
 *      Author: iworker
 */

#ifndef INCLUDE_TENSOR_HPP_
#define INCLUDE_TENSOR_HPP_
#include "module.hpp"

namespace alg {

template<typename RMOD,
typename BASE_RNG,
typename RBINARY,
typename RANTI,
typename LMOD,
typename LBINARY,
typename RSCAL,
typename LANTI,
typename LSCAL,
typename RGENS,
typename LGENS,
typename RUNIT = unit<RMOD >,
typename LUNIT = unit<LMOD >
>
struct tensor {

	typedef tensor< RMOD, BASE_RNG, RBINARY, RANTI, LMOD, LBINARY, RSCAL, LANTI, LSCAL, RGENS,LGENS, RUNIT, LUNIT> _this;
	typedef r_noeth<RMOD,BASE_RNG,RBINARY,RANTI,RSCAL,RGENS,RUNIT >		_rmod;
	typedef l_noeth<LMOD,BASE_RNG,LBINARY,LANTI,LSCAL,LGENS,LUNIT > _lmod;


	using _rmod::_rmod_bind::RIGHT;
	using _lmod::_lmod_bind::LEFT;



	typedef typename _rmod::_rmod_bind::_abl_bind::_grp_bind::_ant _rant;
	typedef typename _lmod::_lmod_bind::_abl_bind::_grp_bind::_ant _lant;

	using _rant::ANTIPODE;
	using _lant::ANTIPODE;




	struct tens_pair {
		//TODO
		tens_pair():m1(),m2(){}
		tens_pair(const RMOD& r, const LMOD& l):m1(),m2(){
			if(r!=unit<RMOD>::UNIT(r)&&l!=unit<LMOD>::UNIT(l)) {
				m1=r;m2=l;
			}
		}
		tens_pair(const tens_pair& o):m1(o.m1),m2(o.m2){}
		~tens_pair(){}
		RMOD m1;
		LMOD m2;
	};

	typedef std::map<tens_pair,BASE_RNG > 		_map;

	typedef typename _map::const_iterator 	const_iter;
	typedef typename _map::iterator 		iter;

	struct tens_add {
		_this operator()(const _this& t1, const _this& t2) const {
			_this sum(t1);
			for (const_iter i = t2.tens_map.begin(); i!=t2.tens_map.end(); ++i){
				iter ii = sum.tens_map.find(i.first);
				if(ii!=sum.tens_map.end()) {
					BASE_RNG s = i.second+ii.second;
					if(s!=unit<BASE_RNG>::UNIT(s)) *ii.second = s;
					else sum.tens_map.remove(ii);
				}
				else sum.tens_map[i.first] = i.second;
			}
			return sum;
		}

	};
	template<typename RNG_ANT >
	struct tens_anti {
		_this operator()(const _this& t) const {
			_this ant;
			for(const_iter i = t.tens_map.begin(); i!=t.tens_map.end(); ++i)
				ant.tens_map[i.first] = alg::antipode<BASE_RNG,RNG_ANT >::ANTIPODE(i->second);
			return ant;
		}
	};
	struct tens_refl {
		bool operator()(const tens_pair& t, const BASE_RNG& r) const {
			return _this(RIGHT(t.m1,r),t.m2)==_this(t.m1,LEFT(r,t.m2));
		}
		bool operator()(const tens_pair& t) const {
			return _this(_rant::ANTIPODE(t.m1),t.m2)==_this(t.m1,_lant::ANTIPODE(t.m2));
		}
	};
	tensor():tens_map(){}
	tensor(const RMOD& m1, const LMOD& m2) :tens_map(m1,m2){}
	template<typename INPUT >
	tensor(INPUT i, INPUT e):tens_map(){
		for ( ;i!=e;++i) {

		}
	}
	struct tens_unit {
		template<typename U >
		_this operator()(const U& u) const {return tensor();}
	};
	tensor(const _this& o):tens_map(o.tens_map){}
	~tensor(){}
private:
	std::map<tens_pair,BASE_RNG> tens_map;
};

template<
typename RMOD,
typename BASE_RNG,
typename RBINARY,
typename RANTI,
typename LMOD,
typename LBINARY,
typename RSCAL,
typename LANTI,
typename LSCAL,
typename RGENS,
typename LGENS,
typename RUNIT = unit<RMOD >,
typename LUNIT = unit<LMOD >
>
struct tens_diag : public abel_diag<
	tensor< RMOD, BASE_RNG, RBINARY, RANTI, LMOD, LBINARY, RSCAL, LANTI, LSCAL, RGENS,LGENS, RUNIT, LUNIT>,
	tensor< RMOD, BASE_RNG, RBINARY, RANTI, LMOD, LBINARY, RSCAL, LANTI, LSCAL, RGENS,LGENS, RUNIT, LUNIT>::tens_add,
	tensor< RMOD, BASE_RNG, RBINARY, RANTI, LMOD, LBINARY, RSCAL, LANTI, LSCAL, RGENS,LGENS, RUNIT, LUNIT>::tens_anti,
	tensor< RMOD, BASE_RNG, RBINARY, RANTI, LMOD, LBINARY, RSCAL, LANTI, LSCAL, RGENS,LGENS, RUNIT, LUNIT>::tens_unit
	> {

};

} /*alg*/



#endif /* INCLUDE_TENSOR_HPP_ */
