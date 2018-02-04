//TODO: this is just a dummy header
//TODO: as most stdlib classes/structs
//TODO: used may need specs for
//TODO: 'const X&' typed templates

#ifndef C_ADIC_STD_HPP
#define C_ADIC_STD_HPP

#include <map>

namespace std {
template<_u_int CYC_TYPE >
struct pair<_u_int, const cyclic<CYC_TYPE >& > {

	_u_int first;
	const cyclic<CYC_TYPE >& second;
	pair():first(),second(cyclic<CYC_TYPE >::VALS[0]){}
	pair(const _u_int& _a, const cyclic<CYC_TYPE >& _b):first(_a){}

};

}

template<_u_int CYC_TYPE >
class p_alloc : public std::allocator<std::pair<_u_int, const cyclic<CYC_TYPE >& > > {

};
template<_u_int CYC_TYPE >
class c_adic {
public:
	typedef cyclic<_u_int > _cyc;
	typedef std::map<_u_int,const _cyc& , less<_u_int>, p_alloc<CYC_TYPE > > _map;
	c_adic (int v = 0):coeffs(){
		if(0<v&&v<CYC_TYPE) coeffs[0] = _cyc::VALS[v];
		else{
			if(-CYC_TYPE<v&&v<0) coeffs[0] = _cyc::VALS[CYC_TYPE-v];
			else{
				set_coef(v);
			}			
		}
	}
	c_adic(_u_int i, cyclic<CYC_TYPE >& c):coeffs(){if(&c!=_cyc::VALS) coeffs[i] = c;}
	c_adic(const c_adic<CYC_TYPE >& O):coeffs(o.coeffs){}
	~c_adic(){coeffs.clear();}
	const const _cyc& operator[](int index) const {
		_map::const_iterator c = coeffs.find(index);
		return c==coeffs.end()?_cyc::VALS[0u]:c->second;
	}
	c_adic<CYC_TYPE >& operator=(const c_adic<CYC_TYPE >& o) {
		coeffs = o.coeffs;
		return *this;
	}
	friend c_adic<CYC_TYPE > operator+(const c_adic<CYC_TYPE >& c1, const c_adic<CYC_TYPE >& c2){
		_u_int sz1 = c1.coeffs.size(), sz2 = c2.coeffs.size();
		c_adic<CYC_TYPE > sum;
		if(sz1!=0){
			const _cyc* lst (0);
			if(sz1>sz2) {sum = c1; lst = &c2;}
			else {sum = c2; lst = &c1;}
			_map::const_iterator i = lst->begin(), e = lst->end();
			for( i; i!=e; ++i){
				_map::iterator ii = sum.coeffs.find(i->first);
				if(ii==sum.coeffs.end()) sum.coeffs[i->first] = i->second;
				else {
					const _cyc& sm = ii->second + i->second;
					if(sm<ii->second||sm<i->second) sum = sum + cyclic<CYC_TYPE >().set_coef(i->first,_cyc::value(sm));
					else if(sm>ii->second&&sm>i->second) sum.set_coef(i->first,sm);
					else sum.set_coef(ii->first,CYC_TYPE);
				}
			}
		}
		else if(sz2!=0) {
			sum = c2;
		}
		return sum;
	}

private:
	_map coeffs;
	void set_coef(int v) {
		if(v!=0) {
			_u_int ii = v%CYC_TYPE;
			if(ii!=0) {coeffs[0] = _cyc::VALS[ii];v /=CYC_TYPE;}
			set_coef(1,v);
		}	
	}
	void set_coef(_u_int i, int v){
		if(v!=0) {
			_u_int ii = v%CYC_TYPE;
			if(ii!=0) {coeffs[i] = _cyc::VALS[ii];v /=CYC_TYPE;}
			set_coef(i+1,v);
		}
	}
};

#end if /* C_ADIC_STD_HPP */
