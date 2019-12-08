/*
 * group_cyclics.hpp
 *
 *  Created on: 01.12.2019
 *      Author: lmmac
 */

#ifndef INCLUDE_ALG_1_1_GROUP_GROUP_CYCLICS_HPP_
#define INCLUDE_ALG_1_1_GROUP_GROUP_CYCLICS_HPP_


#include <iostream>
#define _H_CYCLICS_HPP_CYC_TYPE(M)\
    typedef M _cyc_

namespace alg_fun {
/*
 * Cyclic ring class template
 * @tparam CYC_TYPE some positive integer as unsigned
 */
template<unsigned int CYC_TYPE >
struct cyclic {
//public:
    _H_CYCLICS_HPP_CYC_TYPE(cyclic< CYC_TYPE>);
	typedef const _cyc_& _c_ref;
	/**
	 * @brief Cyclic array wrapper type
	 *
	 * Acts as a fixed size array of const
	 * cyclics. The implemented subscript
	 * operator computes 'i mod CYC_TYPE' (non-negatively represented)
	 * Thus, any subscript call is range-valid
	 */
	struct cyc_ar {
		/**
		 * @brief Subscript operator
		 *
		 * @param i index
		 * @return i mod CYC_TYPE as const ref
		 */
		_c_ref operator[](int i) const {
			static const int& CYC = static_cast<int>(CYC_TYPE);
			return 0<=i&&i<CYC?VALS[i]:
					i>=CYC?VALS[(i%CYC_TYPE)]:VALS[(CYC_TYPE+(i%CYC_TYPE))];
		}
		/**
		 * @brief Subscript operator
		 *
		 * @param i index
		 * @return i mod CYC_TYPE as const ref
		 */
		_c_ref operator[](unsigned int i) const {
			return 0<=i&&i<CYC_TYPE?VALS[i]:VALS[i%CYC_TYPE];
		}
		/**
		 * @brief Subscript operator
		 *
		 * @param i index
		 * @return i mod CYC_TYPE as const ref
		 */
		_c_ref operator[](unsigned long i) const {
			return this->operator[]((unsigned int) i);
		}
		~cyc_ar(){}
		friend class cyclic<CYC_TYPE >;
	private:
		static const _cyc_ VALS[];
		cyc_ar(){}
		cyc_ar(const cyc_ar& o);
		cyc_ar& operator=(const cyc_ar& o);

	};
	friend class cyc_ar;

	//array of cycs as static const
	//in wrapper type
    static const cyc_ar& VALS;

    //type size
    static const unsigned int SIZE = sizeof(cyclic);
	static unsigned int value(const _cyc_& c){
		return (c.abs() - VALS.VALS->abs())/SIZE;
	}
	static _c_ref def_val(){return VALS[0];}

	//add
	friend _c_ref operator+(_c_ref c1, _c_ref c2){return VALS[(c1.abs()+c2.abs())/SIZE];}
	friend _c_ref operator+(int c1, _c_ref c2){return VALS[c1]+c2;}
	friend _c_ref operator+(_c_ref c1, int c2){return VALS[c2]+c1;}
	friend _c_ref operator+(unsigned int c1, _c_ref c2){return VALS[c1]+c2;}
	friend _c_ref operator+(_c_ref c1, unsigned int c2){return VALS[c2]+c1;}
	//friend _c_ref operator+(unsigned int c1, const _cyc_* c2){return c2==0?VALS[0]:(*c2) + VALS[c1];}
	//friend _c_ref operator+(const _cyc_* c1, unsigned int c2){return c1==0?VALS[0]:VALS[c2]+(*c1);}

	//sub
	friend _c_ref operator-(_c_ref c1) {return VALS[0]-c1;}
	friend _c_ref operator-(_c_ref c1, _c_ref c2){return VALS[((unsigned int) (c1.abs()-c2.abs())/SIZE)];}
	friend _c_ref operator-(int c1, _c_ref c2){return VALS[c1]-c2;}
	friend _c_ref operator-(_c_ref c1, int c2){return c1-VALS[c2];}
	friend _c_ref operator-(unsigned int c1, _c_ref c2){return VALS[c1]-c2;}
	friend _c_ref operator-(_c_ref c1, unsigned int c2){return c1-VALS[c2];}

	//mul
	friend _c_ref operator*(_c_ref c1, _c_ref c2){return VALS[(c1.abs()*c2.abs())/SIZE];}
	friend _c_ref operator*(int c1, _c_ref c2){return VALS[c1]*c2;}
	friend _c_ref operator*(_c_ref c1, int c2){return VALS[c2]*c1;}
	friend _c_ref operator*(unsigned int c1, _c_ref c2){return VALS[c1]*c2;}
	friend _c_ref operator*(_c_ref c1, unsigned int c2){return VALS[c2]*c1;}

	//mod
	//TODO unit as c2 should return zero...
	friend _c_ref operator%(_c_ref c1, _c_ref c2){return &c1>&c2?(c1-c2)%c2:c1;}

	//div
	//TODO: division not well defined in that only units are valid divisors,
	//TODO: zero divisors
	friend _c_ref operator/(_c_ref c1, _c_ref c2){
		return 	value(c2)%CYC_TYPE==0?
					value(c1)%CYC_TYPE==0?VALS[0]:VALS[0]:VALS[0];
	}
	bool is_unit() const {return gcd(CYC_TYPE,value(*this))==1u?true:false;}
private:
    cyclic(){}
    cyclic(const _cyc_& o);
    _cyc_& operator=(const _cyc_& o);
	unsigned long abs() const {return ((unsigned long) this);}
};

/**
 * @brief greatest common divisor of two elements
 * @param c1 first element
 * @param c2 second
 * @return max of all positive divisors dividing both elements == gcd
 */
unsigned int gcd(unsigned int c1, unsigned int c2 );


//Useful short hand macro: 'cyclic<M >' aka c_M,
// NOTE: c_2 and c_2u are not equi. types
// @param M unsigned int compile-t const
#define CYCLICS_HPP_TYPE_DEF(M)\
	typedef cyclic< M > c_##M

#define CYCLICS_HPP_TYPE_DEF_CREF(M)\
	typedef const cyclic< M >& cr_##M
template<unsigned int CYC_TYPE >
const cyclic<CYC_TYPE > cyclic<CYC_TYPE >::cyc_ar::VALS[CYC_TYPE];
template<unsigned int CYC_TYPE >
const typename cyclic<CYC_TYPE >::cyc_ar& cyclic<CYC_TYPE >::VALS = cyc_ar();

//eq:
template<unsigned int CYC_TYPE >
bool operator==(const cyclic<CYC_TYPE >& c1, const cyclic<CYC_TYPE >& c2){return &c1==&c2;}
template<unsigned int CYC_TYPE >
bool operator==(const cyclic<CYC_TYPE >& c1, int c2){return &c1==cyclic<CYC_TYPE>::VALS[c2];}
template<unsigned int CYC_TYPE >
bool operator==(int c1, const cyclic<CYC_TYPE >& c2){return cyclic<CYC_TYPE>::VALS[c1]==&c2;}
template<unsigned int CYC_TYPE >
bool operator==(const cyclic<CYC_TYPE >& c1, unsigned int c2){return &c1==cyclic<CYC_TYPE>::VALS[c2];}
template<unsigned int CYC_TYPE >
bool operator==(unsigned int c1, const cyclic<CYC_TYPE >& c2){return cyclic<CYC_TYPE>::VALS[c1]==&c2;}

//neq:
template<unsigned int CYC_TYPE >
bool operator!=(const cyclic<CYC_TYPE >& c1, const cyclic<CYC_TYPE >& c2){return &c1!=&c2;}
template<unsigned int CYC_TYPE >
bool operator!=(const cyclic<CYC_TYPE >& c1, int c2){return &c1!=cyclic<CYC_TYPE>::VALS[c2];}
template<unsigned int CYC_TYPE >
bool operator!=(int c1, const cyclic<CYC_TYPE >& c2){return cyclic<CYC_TYPE>::VALS[c1]!=&c2;}
template<unsigned int CYC_TYPE >
bool operator!=(const cyclic<CYC_TYPE >& c1, unsigned int c2){return &c1!=cyclic<CYC_TYPE>::VALS[c2];}
template<unsigned int CYC_TYPE >
bool operator!=(unsigned int c1, const cyclic<CYC_TYPE >& c2){return cyclic<CYC_TYPE>::VALS[c1]!=&c2;}

//leq:
template<unsigned int CYC_TYPE >
bool operator<=(const cyclic<CYC_TYPE >& c1, const cyclic<CYC_TYPE >& c2){return &c1<=&c2;}

//geq:
template<unsigned int CYC_TYPE >
bool operator>=(const cyclic<CYC_TYPE >& c1, const cyclic<CYC_TYPE >& c2){return &c1>=&c2;}

//lt:
template<unsigned int CYC_TYPE >
bool operator<(const cyclic<CYC_TYPE >& c1, const cyclic<CYC_TYPE >& c2){return &c1<&c2;}

//gt:
template<unsigned int CYC_TYPE >
bool operator>(const cyclic<CYC_TYPE >& c1, const cyclic<CYC_TYPE >& c2){return &c1>&c2;}

template<unsigned int CYC_TYPE >
std::ostream& operator<<(std::ostream& o, const cyclic<CYC_TYPE >& c){
   return o << cyclic<CYC_TYPE >::value(c) << " mod " << CYC_TYPE;
}

//TODO: should test for gcd(CYC_TYPE1,CYC_TYPE2)==1 or !=1, resp.
//TODO: only valid for the latter (a compi-error would be nice as indication)
template<unsigned int CYC_TYPE1, unsigned int CYC_TYPE2 >
struct cyc_map {
	typedef cyclic<CYC_TYPE1 > arg_type;
	typedef cyclic<CYC_TYPE2 > val_type;
	static const cyc_map<CYC_TYPE1, CYC_TYPE2 >& MAP;
	const val_type& operator()(const arg_type& arg) const {
		return val_type::VALS[0];

	}
private:

};
/***********************************************************************************
 *																				   *
 *								Specs											   *
 *																				   *
 ***********************************************************************************/
 //zero spec is actually an integer
template< >
struct cyclic<0u > {
	cyclic(int i = 0);
	cyclic(const cyclic<0u >& o);
	~cyclic();
	friend cyclic<0u> operator+(const cyclic<0u>& s1, const cyclic<0u>& s2);
	friend cyclic<0u> operator-(const cyclic<0u>& s1, const cyclic<0u>& s2);
	friend cyclic<0u> operator*(const cyclic<0u>& s1, const cyclic<0u>& s2);
	static unsigned int value(const  cyclic<0u >& s);
	template<unsigned int CYC >
	struct project {
		const cyclic<CYC >& operator()(const cyclic<0u>& arg) const {return cyclic<CYC >::VALS[arg];}
	};
private:
	int val;
};
//one spec is the zero ring - i.e. one element ring!!!
template< >
struct cyclic<1u > {
	CYCLICS_HPP_TYPE_DEF_CREF(1u);
    static cr_1u VALS;
	static unsigned int value(cr_1u c);/*{return 0u;}*/
	cr_1u operator[] (unsigned int) const ;/*{return VALS;}*/
	cr_1u operator[] (int ) const ;/*{return VALS;}*/
};
template< >
struct cyclic<2u > {
	CYCLICS_HPP_TYPE_DEF_CREF(2u);
    static const cyclic<2u > VALS[2];
	static unsigned int value(cr_2u c);/*{return 0u;}*/
	cr_2u operator[] (unsigned int) const ;/*{return VALS;}*/
	cr_2u operator[] (int ) const ;/*{return VALS;}*/
private :
	cyclic(){}
	cyclic(const cyclic<2u >& ){}
	template<typename T >
	cyclic<2u >& operator=(T t);
	template<typename T >
	cyclic<2u >& operator=(const T& t){return *this;}
};
const cyclic<1u >& operator+(const cyclic<1u >& c1, const cyclic<1u >& c2) ;//{return cyclic<1u >::VALS;}
const cyclic<1u >& operator-(const cyclic<1u >& c1, const cyclic<1u >& c2) ;//{return cyclic<1u >::VALS;}
const cyclic<1u >& operator*(const cyclic<1u >& c1, const cyclic<1u >& c2) ;//{return cyclic<1u >::VALS;}

//two spec - only operaters specified

const cyclic<2u >& operator+(const cyclic<2u >& c1, const cyclic<2u >& c2) ;//{return cyclic<1u >::VALS;}
const cyclic<2u >& operator-(const cyclic<2u >& c1, const cyclic<2u >& c2) ;//{return cyclic<1u >::VALS;}
const cyclic<2u >& operator*(const cyclic<2u >& c1, const cyclic<2u >& c2) ;//{return cyclic<1u >::VALS;}


std::ostream& operator<<(std::ostream& o, const cyclic<1u >& c);

} /*alg_fun*/


#endif /* INCLUDE_ALG_1_1_GROUP_GROUP_CYCLICS_HPP_ */
