/*
 * utils_fct_wrapper.hpp
 *
 *  Created on: 01.12.2019
 *      Author: lmmac
 */

#ifndef INCLUDE_ALG_1_1_UTILS_UTILS_FCT_WRAPPER_HPP_
#define INCLUDE_ALG_1_1_UTILS_UTILS_FCT_WRAPPER_HPP_


namespace test_util {

/**
 * @brief Unary (non-static member) function wrapper template
 *
 * Accepts a function pointer of type
 * <tt>RET_TYPE T::name_of_function(ARG_TYPE arg);</tt>
 * @tparam T type with member to wrap
 * @tparam ARG_TYPE argument type
 * @tparam RET_TYPE return type
 */
template<typename T, typename ARG_TYPE, typename RET_TYPE >
struct unary_fct_wrapper {
	typedef RET_TYPE (T::*_fct_type)(ARG_TYPE );
	unary_fct_wrapper(_fct_type fct): _fct(&T::fct){}
	unary_fct_wrapper(const unary_fct_wrapper<T,ARG_TYPE,RET_TYPE >& u_wrapped):_fct(&u_wrapped.T::_fct){}
	~unary_fct_wrapper(){_fct = 0;}
	unary_fct_wrapper<T, ARG_TYPE, RET_TYPE>& operator=(const unary_fct_wrapper<T, ARG_TYPE, RET_TYPE >& fct){
		_fct = fct._fct;
		return *this;
	}
	RET_TYPE operator()(T t, ARG_TYPE arg) const {return (t.*_fct)(arg);}
private:

	_fct_type _fct;
};
template<typename T, typename ARG_TYPE, typename RET_TYPE >
struct unary_fct_wrapper<const T, ARG_TYPE, RET_TYPE > {
	typedef RET_TYPE (T::*_fct_type)(ARG_TYPE ) const ;
	unary_fct_wrapper(_fct_type fct): _fct(&T::fct){}
	unary_fct_wrapper(const unary_fct_wrapper<const T,ARG_TYPE,RET_TYPE >& u_wrapped):_fct(&u_wrapped.T::_fct){}
	~unary_fct_wrapper(){_fct = 0;}
	unary_fct_wrapper<const T, ARG_TYPE, RET_TYPE>& operator=(const unary_fct_wrapper<const T, ARG_TYPE, RET_TYPE >& fct){
		_fct = fct._fct;
		return *this;
	}
	_fct_type operator&() const {return _fct;}
	RET_TYPE operator()(const T t, ARG_TYPE arg) const {return (t.*_fct)(arg);}
private:

	_fct_type _fct;
};
template<typename T, typename ARG_TYPE >
struct unary_fct_wrapper<T, ARG_TYPE,void > {
	typedef void (T::*_fct_type)(ARG_TYPE );
	unary_fct_wrapper(_fct_type fct): _fct(fct){}
	unary_fct_wrapper(const unary_fct_wrapper<T,ARG_TYPE,void>& u_wrapped):_fct(u_wrapped._fct){}
	~unary_fct_wrapper(){_fct = 0;}
	unary_fct_wrapper<T, ARG_TYPE, void>& operator=(const unary_fct_wrapper<T, ARG_TYPE,void >& fct){
		_fct = fct._fct;
		return *this;
	}
	void operator()(T t, ARG_TYPE arg) const {(t.*_fct)(arg);}
private:
	_fct_type _fct;
};
template<typename T, typename ARG_TYPE >
struct unary_fct_wrapper< const T, ARG_TYPE,void > {
	typedef void (T::*_fct_type) (ARG_TYPE ) const ;
	unary_fct_wrapper(_fct_type fct): _fct(fct){}
	unary_fct_wrapper(const unary_fct_wrapper<T,ARG_TYPE,void>& u_wrapped):_fct(u_wrapped._fct){}
	~unary_fct_wrapper(){_fct = 0;}
	unary_fct_wrapper<const T, ARG_TYPE, void>& operator=(const unary_fct_wrapper<const T, ARG_TYPE,void >& fct){
		_fct = fct._fct;
		return *this;
	}
	void operator()(const T t, ARG_TYPE arg) const {(t.*_fct)(arg);}
private:
	_fct_type _fct;
};
template<typename T, typename RET_TYPE >
struct unary_fct_wrapper<T, void, RET_TYPE > {
	typedef RET_TYPE (T::*_fct_type)( );
	unary_fct_wrapper(_fct_type fct): _fct(fct){}
	unary_fct_wrapper(const unary_fct_wrapper<T, void, RET_TYPE >& u_wrapped):_fct(u_wrapped._fct){}
	~unary_fct_wrapper(){_fct = 0;}
	unary_fct_wrapper<T, void, RET_TYPE >& operator=(const unary_fct_wrapper<T, void, RET_TYPE >& fct){
		_fct = fct._fct;
		return *this;
	}
	void operator()(T t) const {(t.*_fct)();}
private:
	_fct_type _fct;
};
template<typename T, typename RET_TYPE >
struct unary_fct_wrapper<const T, void, RET_TYPE > {
	typedef RET_TYPE (T::*_fct_type)( ) const ;
	unary_fct_wrapper(_fct_type fct): _fct(fct){}
	unary_fct_wrapper(const unary_fct_wrapper<const T, void, RET_TYPE >& u_wrapped):_fct(u_wrapped._fct){}
	~unary_fct_wrapper(){_fct = 0;}
	unary_fct_wrapper<const T, void, RET_TYPE >& operator=(const unary_fct_wrapper<const T, void, RET_TYPE >& fct){
		_fct = fct._fct;
		return *this;
	}
	void operator()(T t) const {(t.*_fct)();}
private:
	_fct_type _fct;
};

template<typename T, typename ARG_TYPE, typename RET_TYPE >
struct unary_fct_wrapper<T*,ARG_TYPE,RET_TYPE > {
	typedef RET_TYPE (T::*_fct_type)(ARG_TYPE );
	unary_fct_wrapper(_fct_type fct): _fct(&T::fct){}
	unary_fct_wrapper(const unary_fct_wrapper<T*,ARG_TYPE,RET_TYPE >& u_wrapped):_fct(&u_wrapped.T::_fct){}
	~unary_fct_wrapper(){_fct = 0;}
	unary_fct_wrapper<T*, ARG_TYPE, RET_TYPE>& operator=(const unary_fct_wrapper<T*, ARG_TYPE, RET_TYPE >& fct){
		_fct = fct._fct;
		return *this;
	}
	RET_TYPE operator()(T* t, ARG_TYPE arg) const {return (t->*_fct)(arg);}
private:

	_fct_type _fct;
};
template<typename T, typename ARG_TYPE, typename RET_TYPE >
struct unary_fct_wrapper<const T*, ARG_TYPE, RET_TYPE > {
	typedef RET_TYPE (T::*_fct_type)(ARG_TYPE ) const ;
	unary_fct_wrapper(_fct_type fct): _fct(&T::fct){}
	unary_fct_wrapper(const unary_fct_wrapper<const T*,ARG_TYPE,RET_TYPE >& u_wrapped):_fct(&u_wrapped.T::_fct){}
	~unary_fct_wrapper(){_fct = 0;}
	unary_fct_wrapper<const T*, ARG_TYPE, RET_TYPE>& operator=(const unary_fct_wrapper<const T*, ARG_TYPE, RET_TYPE >& fct){
		_fct = fct._fct;
		return *this;
	}
	_fct_type operator&() const {return _fct;}
	RET_TYPE operator()(const T t, ARG_TYPE arg) const {return (t.*_fct)(arg);}
private:

	_fct_type _fct;
};
template<typename T, typename ARG_TYPE >
struct unary_fct_wrapper<T*, ARG_TYPE,void > {
	typedef void (T::*_fct_type)(ARG_TYPE );
	unary_fct_wrapper(_fct_type fct): _fct(fct){}
	unary_fct_wrapper(const unary_fct_wrapper<T*,ARG_TYPE,void>& u_wrapped):_fct(u_wrapped._fct){}
	~unary_fct_wrapper(){_fct = 0;}
	unary_fct_wrapper<T*, ARG_TYPE, void>& operator=(const unary_fct_wrapper<T*, ARG_TYPE,void >& fct){
		_fct = fct._fct;
		return *this;
	}
	void operator()(T t, ARG_TYPE arg) const {(t.*_fct)(arg);}
private:
	_fct_type _fct;
};
template<typename T, typename ARG_TYPE >
struct unary_fct_wrapper< const T*, ARG_TYPE,void > {
	typedef void (T::*_fct_type) (ARG_TYPE ) const ;
	unary_fct_wrapper(_fct_type fct): _fct(fct){}
	unary_fct_wrapper(const unary_fct_wrapper<const T*,ARG_TYPE,void>& u_wrapped):_fct(u_wrapped._fct){}
	~unary_fct_wrapper(){_fct = 0;}
	unary_fct_wrapper<const T*, ARG_TYPE, void>& operator=(const unary_fct_wrapper<const T*, ARG_TYPE,void >& fct){
		_fct = fct._fct;
		return *this;
	}
	void operator()(const T t, ARG_TYPE arg) const {(t.*_fct)(arg);}
private:
	_fct_type _fct;
};
template<typename T, typename RET_TYPE >
struct unary_fct_wrapper<T*, void, RET_TYPE > {
	typedef RET_TYPE (T::*_fct_type)( );
	unary_fct_wrapper(_fct_type fct): _fct(fct){}
	unary_fct_wrapper(const unary_fct_wrapper<T*, void, RET_TYPE >& u_wrapped):_fct(u_wrapped._fct){}
	~unary_fct_wrapper(){_fct = 0;}
	unary_fct_wrapper<T*, void, RET_TYPE >& operator=(const unary_fct_wrapper<T*, void, RET_TYPE >& fct){
		_fct = fct._fct;
		return *this;
	}
	void operator()(T t) const {(t.*_fct)();}
private:
	_fct_type _fct;
};
template<typename T, typename RET_TYPE >
struct unary_fct_wrapper<const T*, void, RET_TYPE > {
	typedef RET_TYPE (T::*_fct_type)( ) const ;
	unary_fct_wrapper(_fct_type fct): _fct(fct){}
	unary_fct_wrapper(const unary_fct_wrapper<const T*, void, RET_TYPE >& u_wrapped):_fct(u_wrapped._fct){}
	~unary_fct_wrapper(){_fct = 0;}
	unary_fct_wrapper<const T*, void, RET_TYPE >& operator=(const unary_fct_wrapper<const T*, void, RET_TYPE >& fct){
		_fct = fct._fct;
		return *this;
	}
	void operator()(T t) const {(t.*_fct)();}
private:
	_fct_type _fct;
};

} /*test_util*/





#endif /* INCLUDE_ALG_1_1_UTILS_UTILS_FCT_WRAPPER_HPP_ */
