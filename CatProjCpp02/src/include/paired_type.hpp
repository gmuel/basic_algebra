#ifndef PAIRED_TYPE_HPP
#define PAIRED_TYPE_HPP
//currently using own type, may change 2 utility::pair

#ifdef PAIRED_TYPE_HPP_USING_STD_PAIR
#include <utility>

namespace util {

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
}
#else

namespace util {

template< typename _type >
struct pair{


#ifdef PAIRED_TYPE_HPP_USING_HEAP
	pair():first(0),second(0){}
	//pair(const _type& s):first(new _type(s)),second(new _type(s)){}
	pair(const _type& s, const _type& t):first(new _type(s)),second(new _type(t)){}
	pair(const pair<_type >& o):first(o.first?new _type(*o.first):0),second(o.second?new _type(*o.second):0){}
	~pair(){
		if(first) {delete first; first=0;}
		if(second) {delete second; second =0;}
	}
	const _type& operator[](int i) const {return i>=0?i%2==0?*first:*second:this->operator[](-i);}
	struct iterator {
		iterator& operator++() {
			++i;
			return *this;
		}
		iterator operator++(int) {
			iterator ii(*this);
			this->operator++();
			return ii;
		}
		iterator& operator=(const iterator& ii) {
			ptr = ii.ptr;
			i = ii.i;
			return *this;
		}
		iterator& operator=(const _type& t) {
			if(i<2) {
				if(i==0) {if(first) delete first; first = new _type(t);}
				else {if(second) delete second; second = new _type(t);}
			}
			return *this;
		}
		friend bool operator==(const iterator& i1, const iterator& i2) {
			return i1.ptr==i2.ptr?i1.i==i2.i:false;
		}
	private:
		pair<_type >* ptr;
		unsigned short i;
		iterator(pair<_type >* pr, unsigned short ii = 0):ptr(pr),i(ii){}
	};
	iterator operator[](int i ) {
		return i<0?this->operator[](-i):
			iterator(this,(unsigned short) (i%3));
	}
private:
	_type* first,* second;
#else /*PAIRED_TYPE_HPP_USING_HEAP*/
	pair():first(0),second(0){}
	//pair(const _type& s):first(s),second(s){}
	pair(const _type& s, const _type& t):first(s),second(t){}
	pair(const pair<_type >& o):first(o.first),second(o.second){}
	~pair(){}
	const _type& operator[](int i) const {return i%2==0?first:second;}
	_type& operator[](int i) {return i%2==0?first:second;}
private:
	_type first, second;


#endif /*PAIRED_TYPE_HPP_USING_HEAP*/
};


}

#endif


#endif /*PAIRED_TYPE_HPP*/
