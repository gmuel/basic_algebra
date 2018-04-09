/*
 * test_util.hpp
 *
 *  Created on: 10.03.2018
 *      Author: stream_vid
 */

#ifndef TEST_UTIL_HPP_
#define TEST_UTIL_HPP_
#include "fct_wrapper.hpp"
#include <typeinfo>

namespace test_util {
/**
 * @brief Generic test structure template
 *
 * Should implement a function operator:
 * <tt>bool operator()(const T&,const T&) const;</tt>
 * All derived classes should at least provide a default constructor
 * @tparam T type
 * @tparam TEST_TYPE_OF_T type of test class, must implement a function operator
 * 		   <p><tt>bool operator()(const T&, const T&) const;</tt> or</p>
 * 		   <p><tt>bool operator()(T&,T&) const;</tt>.</p>
 * 		   <p>this type may be a derived type:
 * 		   <p><tt>//..<br/>
 * 		   class some_test : public test<type_to_test, some_test > {<br/>
 * 		   //test spec interface..
 * 		   bool operator()(const T& t1, const T& t2) const {//either this member or<br/>
 * 		   }<br/>
 * 		   bool operator()(T& t1, T& t2) const {//this member<br/>
 * 		   }<br/></tt>
 *
 *
 */
template <typename T, typename TEST_TYPE_OF_T >
struct test {
	typedef TEST_TYPE_OF_T _test;
	/**@brief class ref*/
	static const T& CLASS_REP;
	/**@brief a static reference to to underlying test class/type*/
	static const TEST_TYPE_OF_T& TEST_REF;
	/**
	 * @brief Test flag - failure or success
	 *
	 * Only two instances provided
	 * <ol><li><tt>test&ltT,TEST_TYPE_OF_T &gt::FAIL</tt> failure flag</li>
	 * <li><tt>test&ltT,TEST_TYPE_OF_T &gt::SUCC</tt> success flag</li></ol>
	 * <
	 */
	struct test_flag {
		/**@brief CP-constr*/
		test_flag(const test_flag& o):flag(o.flag){}
		/**@brief Destr*/
		~test_flag(){}

		friend struct test<T, TEST_TYPE_OF_T >;
	private:
		/**@brief failure string*/
		static const char* FAIL_STR;
		/**@brief success string*/
		static const char* SUCC_STR;
		/**@brief sole constr, accepting chars*/
		test_flag(const char flg):flag(flg){}
		char flag;
	};
	/**@brief failure flag*/
	static const test_flag& FAIL;
	/**@brief success flag*/
	static const test_flag& SUCC;
	/**@brief destr, TODO has to be virtual?!*/
	virtual ~test(){}
	/**@brief each instance is a pointer on booleans*/
	bool operator->()const {return _flag==!0?false:_flag!=test_flag::FAIL_STR;}
	/**@brief each instance is a reference to a flag strings*/
	const char* operator*()const {return _flag==0?test_flag::FAIL_STR:test_flag::SUCC_STR;}
	/**@brief function operator calling <tt>bool TEST_TYPE_OF_T::operator()(const T&,const T&) const</tt>*/
	bool operator()(const T& t1, const T& t2) const {
		return TEST_REF(t1,t2);
	}
	/**@brief */
	bool operator()(T& t1, T& t2) const {
		return TEST_REF(t1,t2);
	}
protected:
	test():_flag(0){}
	mutable const test_flag* _flag;
	void set_failure() const {_flag ='0';}

};
template <typename TEST_TYPE_OF_T >
struct test<void,TEST_TYPE_OF_T > {
	typedef TEST_TYPE_OF_T _test;
	/**
	 * @brief Test flag - failure or success
	 *
	 * Only two instances provided
	 * <ol><li><tt>test&ltT,TEST_TYPE_OF_T &gt::FAIL</tt> failure flag</li>
	 * <li><tt>test&ltT,TEST_TYPE_OF_T &gt::SUCC</tt> success flag</li></ol>
	 * <
	 */
	struct test_flag {
		friend struct test<void, TEST_TYPE_OF_T >;
	private:
		/**@brief failure string*/
		static const char* FAIL_STR;
		/**@brief sole constr, accepting chars*/
		test_flag(const char flg):flag(flg){}
		char flag;
	};
	test():_flag(&FAIL){}
	/**@brief failure flag*/
	static const test_flag& FAIL;
	/**@brief destr, TODO has to be virtual?!*/
	virtual ~test(){_flag = 0;}
	/**@brief each instance is a pointer on booleans*/
	bool operator->()const {return false;}
	/**@brief each instance is a reference to a flag strings*/
	const char* operator*()const {return test_flag::FAIL_STR;}
	/**@brief function operator calling <tt>bool TEST_TYPE_OF_T::operator()(const T&,const T&) const</tt>*/
	bool operator()() const {
		return false;
	}
protected:
	const test_flag* _flag;

};
/*******************************************************************************************
 * 																						   *
 * 							Class statics def.											   *
 * 																						   *
 *******************************************************************************************/
template<typename T, typename TEST_TYPE_OF_T >
const char* test<T,TEST_TYPE_OF_T >::test_flag::FAIL_STR = "Test failed...";
template<typename T, typename TEST_TYPE_OF_T >
const char* test<T,TEST_TYPE_OF_T >::test_flag::SUCC_STR = "Test succeeded!";
template<typename T, typename TEST_TYPE_OF_T >
const typename test<T,TEST_TYPE_OF_T >::test_flag& test<T,TEST_TYPE_OF_T >::FAIL =
		test<T,TEST_TYPE_OF_T >::test_flag('0');
template<typename T, typename TEST_TYPE_OF_T >
const typename test<T,TEST_TYPE_OF_T >::test_flag& test<T,TEST_TYPE_OF_T >::SUCC =
		test<T,TEST_TYPE_OF_T >::test_flag('1');
/**
 * Structure template for data member tests
 * @tparam T type to test
 * @tparam TEST_TYPE_OF_T type implementing test interface
 */
template<typename T , typename TEST_TYPE_OF_T >
struct data_test : public test<T,TEST_TYPE_OF_T > {

};

template<typename T , typename TEST_TYPE_OF_T >
const T& test<T , TEST_TYPE_OF_T >::CLASS_REP = T();
template<typename T , typename TEST_TYPE_OF_T >
const TEST_TYPE_OF_T& test<T , TEST_TYPE_OF_T >::TEST_REF = test<T ,TEST_TYPE_OF_T >::_test();

/**
 * @brief First data member test template - equality testing
 *
 * Relies on equality operator of type <tt>T</tt>
 */
template<typename T >
struct data_equality : public data_test<T, data_equality<T > > {
	/**Handy tings*/
	typedef data_test<T, data_equality<T > > _base;
	/**Mandatory constructor*/
	data_equality():_base(){}
	/**Mandatory function operator*/
	bool operator()(const T& t1, const T& t2) const {
		return t1==t2;
	}
	/**Test equality member - true for same types*/
	template<typename T1 , typename T2>
	friend bool operator==(const data_equality<T1 >& dat1, const data_equality<T2 >& dat2){
		return typeid(dat1)==typeid(dat2);
	}
	/**Test equality member - true for same types*/
	template<typename T1>
	friend bool operator== (const data_equality<T1 >& dat1, const data_equality<T1 >& dat2){
		return true;
	}
	/**Test inequality member - true for different types*/
	template<typename T1 , typename T2>
	friend bool operator!=(const data_equality<T1 >& dat1, const data_equality<T2 >& dat2){
		return !(dat1==dat2);
	}
};
/**
 * Void specialization of data member equality
 *
 * Intended to be used in test iterations with
 * an instance of this type to represent 'past-the-end' iterator
 * Its function member always returns false
 * E.g.:<br/>
 * <tt>
 */
template< >
struct data_equality<void > : public data_test<void, data_equality<void > > {
	typedef data_test<void, data_equality<void > > _base;
	data_equality():_base(){}
	bool operator()() const {
		return false;
	}
	template<typename T1 >
	friend bool operator==(const data_equality<T1 >& dat1, const data_equality<void >& dat2){
		return false;
	}
	template<typename T1>
	friend bool operator== (const data_equality<void >& dat1, const data_equality<T1 >& dat2){
		return false;
	}
	friend bool operator==(const data_equality<void >& dat1, const data_equality<void >& dat2){
		return true;
	}
};

template<typename T >
struct data_inequality : public data_test<T, data_inequality<T > > {
	typedef data_test<T, data_inequality<T > > _base;
	data_inequality():_base(){}
	bool operator()(const T& t1, const T& t2) const {
		return t1!=t2;
	}
};
template< >
struct data_inequality<void > : public data_test<void, data_inequality<void > > {
	typedef data_test<void, data_inequality<void > > _base;
	data_inequality():_base(){}
	bool operator()() const {
		return false;
	}
	template<typename T1 >
	friend bool operator==(const data_inequality<T1 >& dat1, const data_inequality<void >& dat2){
		return false;
	}
	template<typename T1>
	friend bool operator== (const data_inequality<void >& dat1, const data_inequality<T1 >& dat2){
		return false;
	}
	friend bool operator==(const data_inequality<void >& dat1, const data_inequality<void >& dat2){
		return true;
	}
};

template<typename T, typename FCT_TYPE_TEST >
struct function_test : public test<T, FCT_TYPE_TEST >{
};

template<typename T, typename ARG_TYPE >
struct setter_test_equality : public function_test<T,setter_test_equality<T,ARG_TYPE>  > {

	bool operator()(T& t1, T& t2) const {
		(t1._fct)(arg);
		(t2._fct)(arg);
		return t1==t2;
	}
	setter_test_equality(ARG_TYPE ar, unary_fct_wrapper<T,ARG_TYPE,void >& fct):arg(ar),_fct(&fct) {}
private:
	unary_fct_wrapper<T,ARG_TYPE,void > _fct;
	ARG_TYPE arg;
};
template<typename T, typename ARG_TYPE, typename RET_TYPE >
struct unary_getter_equality : public function_test<T, unary_getter_equality<T, ARG_TYPE, RET_TYPE > > {

	bool operator()(T& t1, T& t2) const {

		return (t1._fct)(arg)==(t2._fct)(arg);
	}
	unary_getter_equality(ARG_TYPE ar, unary_fct_wrapper<T,ARG_TYPE,void >& fct):arg(ar),_fct(&fct) {}
private:
	unary_fct_wrapper<T,ARG_TYPE,RET_TYPE > _fct;
	ARG_TYPE arg;

};
template<typename T, typename ARG_TYPE >
struct unary_getter_equality<T,ARG_TYPE, void > : public setter_test_equality<T,ARG_TYPE > {
	typedef setter_test_equality<T,ARG_TYPE > _base;
	unary_getter_equality(ARG_TYPE ar, unary_fct_wrapper<T,ARG_TYPE,void >& fct) :_base(ar,fct){}
};

template<typename T >
struct exception_test {

};
/**
 * @brief Type macro
 * <p>Use<br/>
 * <tt>MACRO(T1,M,M1);</tt> defines a class template <tt>M</tt> with type parameter <tt>T1</tt>
 * and as a child of class template <tt>M&ltT1&gt</tt><br/>
 * It acts as an iterator that may </p>
 */
#define TEST_UTIL_HPP_DERIVED_FROM1(T1,M,M1) \
template<typename T1 > \
struct M1 : public M<T1 > { \
	~M1(){delete var_ptr;var_ptr =0;} \
	template<typename NEXT > \
	M1<NEXT > operator++(){ \
		return M1<NEXT >::TEST;} \
	template<typename PREV > \
	M1<PREV > operator--(){ \
		return M1<PREV >::NEXT;} \
	static const M1<T1 >& TEST; \
private: \
	M1<T1 >& operator=(const M1<T1>& o); \
	M1():var_ptr(new T1()){} \
	M1(const M1<T1 >& o); \
	T1* var_ptr; \
}; \
template<typename T1 > \
const M1<T1 >& M1<T1 >::TEST;
#define TEST_UTIL_HPP_DERIVED_FROM2(M,M1) \
template< > \
struct M1<void> : public M<void> { \
	M1<void > operator++(){return *this;} \
	M1<void > operator--(){return *this;} \
};
TEST_UTIL_HPP_DERIVED_FROM1(T1,data_equality,data_equal);
	TEST_UTIL_HPP_DERIVED_FROM2(data_equality,data_equal);
	TEST_UTIL_HPP_DERIVED_FROM1(T1,data_inequality,data_inequal);
	TEST_UTIL_HPP_DERIVED_FROM2(data_inequality,data_inequal);
template<typename T >
class test_suite {
public:
	static const T& CLASS_INST_REF;
	template<typename U >
	data_equal<U > begin_eq() const {
		return data_equal<U >();
	}
	data_equal<void > begin_eq() const {return end_eq();}

	data_equal<void > end_eq() const {return data_equal<void >();}
	bool run() {
		return true;
	}
private:

};
template<typename T >
const T& test_suite<T >::CLASS_INST_REF = test<T, data_equality<T > >::CLASS_REP;

} /*test_util*/



#endif /* TEST_UTIL_HPP_ */
/**@brief Template destr*/
/**@brief Previous data equality test<br/>@tparam PREV1 grandparent's test type*/
/**@brief Next data equality test<br/>@tparam NEXT1 grandchild's test type*/
/*~M1(){ \
		if(M1<NEXT,T1,NEXT1>::next_ptr!=0){ \
			delete M1<NEXT,T1,NEXT1>::next_ptr; \
			M1<NEXT,T1,NEXT1>::next_ptr = 0; \
		} \
		if(var_ptr!=0)var_ptr = 0; \
		if(M1<PREV,PREV1,T1>::prev_ptr!=0){ \
			M1<PREV,PREV1,T1>::prev_ptr->M1<T1,PREV,NEXT>::next_ptr = 0; \
			M1<PREV,PREV1,T1>::prev_ptr = 0; \
		} \
	}
	template<typename PREV1 > \
	M1<PREV,PREV1,T1 >* prev_ptr; \
	template<typename NEXT1 > \
	M1<NEXT,T1, NEXT1 >* next_ptr; \*/
