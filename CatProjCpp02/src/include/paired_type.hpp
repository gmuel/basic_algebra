#ifndef PAIRED_TYPE_HPP
#define PAIRED_TYPE_HPP
//currently using own type, may change 2 utility::pair

namespace util {

#ifdef PAIRED_TYPE_HPP_USING_STD_PAIR
#include <utility>

//code for spec for singly typed pair from standard lib goes here
template < typename _type >
struct pair : public std::pair<_type,_type> {//private inheritance???
	typedef std::pair<_type,_type> _base;
	pair():_base(){}
	pair(const _type& t1, const _type& t2):_base(t1,t2){}
	template<typename _other >
	pair(const pair<_other >& o):_base(o){}
	const _type& operator[](int i) const {
		return i>=0?i%2==0?_base::first:_base::second:operator[](-i);
	}
	struct iterator {
	
	private:
		
	};
};

#else


template< typename _type1, typename _type2 = _type1  >
struct pair{


#ifdef PAIRED_TYPE_HPP_USING_HEAP
	pair():first(0),second(0){}
	//pair(const _type& s):first(new _type(s)),second(new _type(s)){}
	static const struct proj1 {
		const _type1& operator()(const pair<_type1,_type2>& arg) const {return arg.first;}
		_type1& operator()(pair<_type1,_type2>& arg) const {return arg.first;}
	}& P1;
	static const struct proj2 {
		const _type2& operator()(const pair<_type1,_type2>& arg) const {return arg.second;}
		_type2& operator()(pair<_type1,_type2>& arg) const {return arg.second;}
	}& P2;
	friend class proj1;
	friend class proj2;
	pair(const _type1& s, const _type2& t):first(new _type(s)),second(new _type(t)){}
	pair(const pair<_type >& o):first(o.first!=0?new _type(*o.first):0),second(o.second!=0?new _type(*o.second):0){}
	~pair(){
		if(first) {delete first; first=0;}
		if(second) {delete second; second =0;}
	}

private:
	_type1 * first;
	_type2 * second;
#else /*PAIRED_TYPE_HPP_USING_HEAP*/

	struct proj1 {
		const _type1& operator()(const pair<_type1,_type2>& arg) const {return arg.first;}
		_type1& operator()(pair<_type1,_type2>& arg) const {return arg.first;}
	};
	static const proj1& P1;
	static const struct proj2 {
		const _type2& operator()(const pair<_type1,_type2>& arg) const {return arg.second;}
		_type2& operator()(pair<_type1,_type2>& arg) const {return arg.second;}
	}& P2;
	friend class proj1;
	friend class proj2;

	pair():first(_type1()),second(_type2()){}
	//pair(const _type& s):first(s),second(s){}
	pair(const _type1& s, const _type2& t):first(s),second(t){}
	pair(const pair<_type1,_type2  >& o):first(o.first),second(o.second){}
	~pair(){}
	friend bool operator==(const pair<_type1,_type2>& p1, const pair<_type1,_type2>& p2) {
		return p1.first==p2.first && p1.second == p2.second;
	}
	friend bool operator!=(const pair<_type1,_type2>& p1, const pair<_type1,_type2>& p2) {
		return !(p1==p2);
	}
private:
	_type1 first;
	_type2 second;

};

#endif /*PAIRED_TYPE_HPP_USING_HEAP*/

template<typename _type1, typename _type2 >
const typename pair<_type1,_type2 >::proj1& pair<_type1,_type2 >::P1 = pair<_type1,_type2 >::proj1(); // @suppress("Type cannot be resolved")

template<typename _type1, typename _type2 >
const typename pair<_type1,_type2 >::proj2& pair<_type1,_type2 >::P2 = pair<_type1,_type2 >::proj2(); // @suppress("Type cannot be resolved")

#endif

} /*util*/

#endif /*PAIRED_TYPE_HPP*/
