/*
 * group_cyclic.hpp
 *
 *  Created on: 26.07.2021
 *      Author: iworker
 */

#ifndef INCLUDE_ALG_1_1_GROUP_GROUP_CYCLIC_HPP_
#define INCLUDE_ALG_1_1_GROUP_GROUP_CYCLIC_HPP_
#include <map>
#include <list>
#include <iostream>
#include <string>
namespace alg {
typedef unsigned int _uint;
class cycle_iterator;
class cycle_parser;
typedef std::map<_uint,_uint> _map;
typedef typename _map::iterator _iter;
typedef typename _map::const_iterator _citer;
class cycle {

	_map map;
	_uint start, fin;
	cycle():map(),start(),fin(){}
	bool add(_uint i, _uint j);
public:
	static cycle getCycle(const std::string& s, cycle_parser& cp ) ;
	cycle(const cycle& o);
	cycle& operator=(const cycle& o){
		start = o.start;
		map = o.map;
		fin = o.fin;
		return *this;
	}
	_uint operator()(_uint i) const {_citer it = map.find(i); return map.size()==0||it==map.end()?i:it->second;}
	static const cycle& EMPTY_CYCLE;
	friend class cycle_iterator ;
	friend class cycle_parser;
	_uint length() const {return map.size();}
	_uint first() const {return start;}
	cycle_iterator end() const ;
	std::list<_uint> orbit() const ;
	bool in(_uint i) const {return map.find(i)==map.end();}
	friend bool operator==(const cycle&,const cycle&);
	friend std::ostream& operator<<(std::ostream& o, const cycle& c);

};
bool operator!=(const cycle& c1, const cycle& c2) {
	return !(c1==c2);
}
class cycle_parser {
	std::string input;
	cycle cyc;
public:
	static const std::string& COMMA_DEL;
	static const std::string& SEMICOLON_DEL;
	cycle_parser():input(),cyc(){}
	cycle_parser(const std::string& s):input(s),cyc(){}
	cycle_parser(const cycle_parser& o):input(o.input),cyc(o.cyc){}
	cycle_parser& operator=(const cycle_parser& o);
	bool parse();
	cycle getCycle() const {return cyc;}
	void setInput(const std::string& s){input = s;}
};
class cycle_iterator {
	cycle& ref;
	_uint curr;
	bool started;
public:
	cycle_iterator(cycle& o):ref(o),curr(o.start),started(false){}
	cycle_iterator(const cycle_iterator& o): ref(o.ref),curr(o.curr),started(o.started){}

	cycle_iterator& operator++() {
		if(!started) started = true;
		curr = ref(curr);
		return *this;
	}
	const _uint& operator->() const {return curr;}
	//_uint& operator->() ;
	cycle_iterator& operator+(const _uint& i) ;
	bool isCycleCompleted() const {return started?curr==ref.start:false;}
	friend bool operator==(const cycle_iterator& i1, const cycle_iterator& i2);
};


} /*alg*/


#endif /* INCLUDE_ALG_1_1_GROUP_GROUP_CYCLIC_HPP_ */
