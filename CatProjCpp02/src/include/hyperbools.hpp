#ifndef HYPERBOOLS_HPP
#define HYPERBOOLS_HPP

namespace cat {

/**
 * @brief Hyper boolean struct temp
 *
 * Intended to extend boolean type in
 * a boolean conforming fashion
 * This definition is equivalent to Goedelian logic
 * except for negation
 * @tparam N (N+1)-valued boolean type
 */
template<unsigned int N>
struct n_bool {
	/**Number of distinct instances*/
	static const unsigned int MOD = N+1;
	/**
	 * @brief Constructs N-bool from int via modules
	 * MOD, defaults to zero (false)
	 * @param tt
	 */
	n_bool(int tt = 0):val(tt%MOD==0?0:
		tt>0?tt%MOD:MOD+tt%MOD){}
	/**
	 * @brief Bool constructor
	 */
	n_bool(bool tt):val(tt?N:0){}
	/**
	 * @brief Copy constructor
	 */
	n_bool(const n_bool<N >& tt):val(tt.val){}
	/**@brief destructor*/
	~n_bool(){}
	/**@brief Value member returns unsigned int in 
	 * integer interval [0,N]
	 */
	unsigned int value()const {return val;}
	/**@brief Assignment operator*/
	n_bool<N>& operator=(const n_bool<N>& t) {val = t.val;return *this;}
	/**@brief Intersection operator*/
	friend n_bool<N > operator&&(const n_bool<N>& t1, const n_bool<N >& t2)
	{
		return t1.val<t2.val?t1:t2;
	}
	/**@brief Union operator*/
	friend n_bool<N > operator||(const n_bool<N>& t1, const n_bool<N >& t2)
	{
		return t1.val<t2.val?t2:t1;
	}
	/**@brief Negation operator*/
	friend n_bool<N > operator!(const n_bool<N >& t){
		return n_bool<N >(MOD-t.val);
	}
	/**@brief Equality operator*/
	friend bool operator==(const n_bool<N >& t1, const n_bool<N >& t2)
	{
		return t1.val==t2.val;
	}
	/**@brief Inequality operator*/
	friend bool operator!=(const n_bool<N >& t1, const n_bool<N >& t2)
	{
		return t1.val!=t2.val;
	}
private:
	unsigned int val;

};

/**@brief Typedef of boolean
 * Only use bool instead of this type
 * if memory usage is of concern
 */
template< >struct n_bool<1 > {
	n_bool(int tt = 0);/*:val(tt==0?false:
		true){}*/
	/**
	 * @brief Bool constructor
	 */
	n_bool(bool tt);/*:val(tt){}*/
	/**
	 * @brief Copy constructor
	 */
	n_bool(const n_bool<1 >& tt);/*:val(tt.val){}*/
	/**@brief destructor*/
	~n_bool();
	/**@brief Value member returns unsigned int in 
	 * integer interval [0,N]
	 */
	bool value()const ;/*{return val;}*/
	/**@brief Assignment operator*/
	n_bool<1>& operator=(const n_bool<1 >& t);/* {val = t.val;return *this;}*/
	/**@brief Intersection operator*/
	friend n_bool<1 > operator&&(const n_bool<1 >& t1, const n_bool<1 >& t2);/*
	{
		return t1.val&&t2.val;
	}*/
	/**@brief Union operator*/
	friend n_bool<1 > operator||(const n_bool<1>& t1, const n_bool<1 >& t2);/*
	{
		return t1.val||t2.val;
	}*/
	/**@brief Negation operator*/
	friend n_bool<1 > operator!(const n_bool<1 >& t);/*{
		return !t.val;
	}*/
	/**@brief Equality operator*/
	friend bool operator==(const n_bool<1 >& t1, const n_bool<1 >& t2);/*
	{
		return t1.val==t2.val;
	}*/
	/**@brief Inequality operator*/
	friend bool operator!=(const n_bool<1 >& t1, const n_bool<1 >& t2);/*
	{
		return t1.val!=t2.val;
	}*/
private:
	bool val;
};
typedef n_bool<1 > Bool;

template<unsigned int N>
struct goedel {
	/**
	 * @brief Constructs N-bool from int via modules
	 * MOD, defaults to zero (false)
	 * @param tt
	 */
	goedel(int tt = 0):val(tt){}
	/**
	 * @brief Bool constructor
	 */
	goedel(bool tt):val(tt){}
	/**
	 * @brief n-boolean constructor
	 */
	goedel(const n_bool<N >& tt):val(tt){}
	/**
	 * @brief Copy constructor
	 */
	goedel(const goedel<N >& tt):val(val.tt){}
	/**@brief destructor*/
	~goedel(){}
	/**@brief Value member returns unsigned int in 
	 * integer interval [0,N]
	 */
	unsigned int value()const {return val.value();}
	/**@brief Assignment operator*/
	goedel<N>& operator=(const n_bool<N>& t) {val = t.val;return *this;}
	/**@brief Intersection operator*/
	friend goedel<N > operator&&(const n_bool<N>& t1, const n_bool<N >& t2)
	{
		return t1.val&&t2.val;
	}
	/**@brief Union operator*/
	friend goedel<N > operator||(const n_bool<N>& t1, const n_bool<N >& t2)
	{
		return t1.val||t2.val;
	}
	/**@brief Negation operator*/
	friend goedel<N > operator!(const goedel<N >& t){
		return goedel<N >(t.value()==0?N:0);
	}
	/**@brief Equality operator*/
	friend bool operator==(const goedel<N >& t1, const goedel<N >& t2)
	{
		return t1.val==t2.val;
	}
	/**@brief Inequality operator*/
	friend bool operator!=(const goedel<N >& t1, const goedel<N >& t2)
	{
		return t1.val!=t2.val;
	}
	

private:
	n_bool<N > val;
};
/*
typedef n_bool<3 > tert;

template< >
struct n_bool<3 > {
	enum terinaries {ZERO,ONE,TWO};
	static const tert& FALSE_,& TRUE_,& INDET_;
	n_bool(int tt = 0);
	n_bool(bool tt);
	n_bool(const terinaries& tt);
	n_bool(const tert& tt);
	~n_bool(){}
	friend tert operator&&(const tert& t1, const tert& t2);
	friend tert operator||(const tert& t1, const tert& t2);
	friend tert operator!(const tert& t);
	friend bool operator==(const tert& t1, const tert& t2);
	friend bool operator!=(const tert& t1, const tert& t2);
private:
	tert& operator=(const tert& tt);
	const terinaries& val;

};
*/

}

#endif /*HYPERBOOLS_HPP*/
