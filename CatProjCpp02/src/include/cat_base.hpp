#ifndef H_CAT_BASE_HPP
#define H_CAT_BASE_HPP

#ifndef H_CAT_BASE_HPP_USING_STD
#include "paired_type.hpp"
#else
#include <utility>
#endif

namespace cat {

#ifndef H_CAT_BASE_HPP_USING_STD
using namespace util;
#else
using namespace std;
#endif


/**
 * @brief Category class templ
 *
 * Accepts a diagram type as type parameter
 * @tparam DIAGRAMS a diagram type
 */
template<	//typename OBJECT_CLASS,
			//typename MORPH_BASE,
			//typename ID_FUNC,
			typename DIAG >
class cat {
	template<typename DIAGRAM >
	cat(const cat<DIAGRAM >& o);
	template<typename DIAGRAM >
	cat<DIAG >& operator=(const cat<DIAGRAM >& o);
protected:
	cat(){}
public:
	~cat(){}
};
/**
 * @brief Relation structure template
 *
 * Note that no definition of static member <code>rel::REL</code> is provided
 * user definitions must provide their own definition of member
 * @tparam REL_TYPE binary boolean valued function type
 */
template<typename TYPE1, typename TYPE2, typename REL_TYPE >
struct rel {
	static const REL_TYPE& REL;
	bool operator()(const TYPE1& t1, const TYPE2& t2) const {return REL(t1,t2);}
};

template<typename TYPE, typename REL_NAME >
struct reflexive;
/**
 * @brief Relation class temp
 *
 * Only incomplete member function - implement function member
 * as needed as a spec
 * @tparam TYPE
 * @tparam REL_TYPE could be implementing subclass

template<typename TYPE , typename REL_TYPE >
struct rel<TYPE, TYPE, REL_TYPE > {
	static const REL_TYPE& REL;
	bool operator()(const TYPE& t1, const TYPE& t2) const {return REL(t1,t2);}
};
*/
/**
 * @brief Reflexive relation class temp
 *
 * @tparam TYPE
 * @tparam REL_TYPE
 */
template<typename TYPE , typename REL_NAME >
struct reflexive : public rel<TYPE, TYPE, REL_NAME > {
	typedef rel<TYPE,TYPE, REL_NAME > _base;

	bool operator()(const TYPE& t) const {return this->_base::operator()(t,t) == true;}
};
/*
template<class TYPE >
struct reflexive<TYPE, reflexive<TYPE > >  : public rel<TYPE, TYPE, reflexive<TYPE > > {
	typedef rel<TYPE,TYPE, reflexive > _base;

	bool operator()(const TYPE& t) const {return this->_base::operator()(t,t) == true;}
};
*/

template<typename TYPE, typename REL_NAME >
struct symmetric : public rel<TYPE, TYPE, REL_NAME > {
	typedef rel<TYPE, TYPE, REL_NAME  > _base;
	bool operator()(const TYPE& t1, const TYPE& t2) const {
		return _base::operator()(t1,t2) && _base::operator()(t2,t1);
	}
};

template<typename TYPE, typename REL_NAME >
struct transitive : public rel<TYPE, TYPE, REL_NAME > {
	typedef rel<TYPE, TYPE, REL_NAME > _base;
	bool operator()(const TYPE& t1, const TYPE& t2, const TYPE& t3) const {
		return _base::operator()(t1,t2) && _base::operator()(t2,t3) && _base::operator()(t1,t3);
	}
};

template<typename TYPE, typename REL_NAME >
struct eq : public rel<TYPE, TYPE, REL_NAME >{
	typedef rel<TYPE, TYPE, REL_NAME  >  	_base;
	typedef reflexive<TYPE, REL_NAME > 		_refl;
	typedef symmetric<TYPE, REL_NAME > 		_symm;
	typedef transitive<TYPE, REL_NAME >		_tran;

	static const _refl& REFLEX;

	static const _symm& SYMMETRIC;

	static const _tran& TRANSITIVE;

	bool operator()(const TYPE& t1, const TYPE& t2, const TYPE& t3) const {
		return REFLEX(t1) && REFLEX(t2) && REFLEX(t3) &&
					SYMMETRIC(t1,t2) && SYMMETRIC(t1,t3) && SYMMETRIC(t2,t3) &&
						TRANSITIVE(t1,t2,t3);
	}
};



/**
 * @brief Equality diagram
 *
 * Defines an equality diagram for some type SET_TYPE
 * It provides two structures:
 * <p><ol>
 * <li><tt>diagonal</tt> is the diagonal map \f[ x \longmapsto (x,x)\f]</li>
 * <li><tt>equi</tt> is the equality type (a function type with function operator
 * returning true on equality and false otherwise)</li></ol>
 * <p><b>Note</b>, that the latter type is incomplete: the user has to provide
 * his/her own specialization
 * @tparam SET_TYPE
 */
template<	typename SET_TYPE >
struct eq_diag {
	typedef SET_TYPE _type;
	typedef pair< _type > _pair;
	struct diagonal {
		_pair operator()(const _type& x) const {return _pair(x,x);}
	};
	struct equi : public eq<_type, equi > {
		bool operator()(const _pair& p) const {return eq<_type, equi >::_base::REL(p[0],p[1]);}
		bool operator()(const _type& s, const _type& t) const {return eq<_type, equi >::_base::REL(s,t);}
	};
	~eq_diag(){}
protected:
	eq_diag(){}
};

/**
 * @brief Generic object template
 */
template <typename _obj >
struct object {
	template<typename _obj2 >
	object(const object<_obj2 >& o){}
	~object(){}
protected:
	object(){}
};
template<typename _pre, typename _im, typename MORPHISM  >
struct morph;
/**
 * @brief Generic morphism template
 */
template<typename _pre, typename _im , typename MORPHISM >
struct morph {
	typedef _im image;
	image operator()(const _pre& arg) const ;//{return IMAGE;}
//	static const _im& IMAGE;
	static const MORPHISM& MORPH;
	~morph(){}
protected:
	morph(){}
	template<typename _pr1, typename _im1 , typename MORPHISM1 >
	morph(const morph<_pr1, _im1, MORPHISM1 >& o){}//;
};
template<typename _pre, typename _im, typename MORPHISM >
const MORPHISM& morph<_pre,_im, MORPHISM >::MORPH = MORPHISM();

template<typename _pre, typename _im , typename MORPHISM>
struct morph<_pre,const _im& , const MORPHISM& > {
	typedef const _im& image;
	image operator()(const _pre& arg) const ;//{return IMAGE;}
//	static const _im& IMAGE;
	static const MORPHISM& MORPH;
	~morph(){}
protected:
	morph(){}
	template<typename _pr1, typename _im1, typename MORPHISM1 >
	morph(const morph<_pr1, const _im1& , const MORPHISM1& >& o){}
};
template<typename _pre, typename _image1, typename _image2 >
struct composite : public morph<_pre, _image2, composite<_pre, _image1, _image2 > > {
	typedef morph<_pre, _image2, composite<_pre,_image1,_image2 > > _base;
	struct first_morph : public morph<_pre, _image1, first_morph > {

	} first_morphism;
	struct second_morph : public morph<_image1, _image2, second_morph > {

	} second_morph;
	composite():first_morphism(),second_morph(){}
	~composite(){}
};

template<typename _pre_image, typename _image, typename _morph>
struct pre_image : public eq_diag<pre_image<_pre_image,_image,_morph > > {


};

template<typename DIAG1, typename DIAG2 >
struct functor {
	typedef struct preimage : public cat<DIAG1 > {

	} _pre;
	typedef struct image : public cat<DIAG2 > {

	} _im;
	template<typename PRE_OBJ, typename IM_OBJ >
	struct morph_im : public morph<PRE_OBJ, IM_OBJ, morph_im<PRE_OBJ, IM_OBJ> > {

	};
};
/*
template<typename _pre, typename _im >
const _im& morph<_pre,_im >::IMAGE = _im();

template<typename _pre, typename _im >
const _im& morph<_pre, const _im& >::IMAGE = _im::DUMMY;
*/

}

#endif /* H_CAT_BASE_HPP */
