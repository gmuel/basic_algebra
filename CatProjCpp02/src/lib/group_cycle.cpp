/*
 * group_cycle.cpp
 *
 *  Created on: 26.07.2021
 *      Author: iworker
 */



#include "../include/cycle.hpp"
#include <sstream>
namespace alg {
//using namespace std;

typedef std::string string;
typedef std::stringstream stringstream;
const cycle& cycle::EMPTY_CYCLE = cycle();

const string& cycle_parser::COMMA_DEL		= ",";
const string& cycle_parser::SEMICOLON_DEL 	= ";";

typedef typename std::list<string>::const_iterator _lciter;
string replaceAll(const string& s, const string& sub,const string& repl) {
	stringstream ss(s);
	int idx = s.rfind(sub), lng = s.length();
	while (idx>=0) {
		ss.str(ss.str().replace(idx,lng,repl));
		idx=ss.str().rfind(sub);
	}
	return ss.str();
}

string stripBraces(const string& s){
	return replaceAll(replaceAll(replaceAll(replaceAll(replaceAll(replaceAll(s,"{",""),"}",""),"(",""),")",""),"[",""),"]","");
}

std::list<string> splitString(const string& s, const string sub) {
	stringstream cp(s);
	std::list<string> lst;
	int idx = cp.str().rfind(sub), lng = sub.length(), last = s.length();
	while(true) {
		string tmp = cp.str();
		lst.push_back(tmp.substr(idx+lng,last));
		last = idx;
		cp.str(tmp.substr(0,last));
		idx=cp.str().rfind(sub);
		if(idx<0) {
			lst.push_back(cp.str());
			break;
		}
	}
	return lst;
}

bool getDelimiterAndLastIndex (const std::string& input, std::string& del, _uint& idx) {
	int commDelIdx = input.rfind(cycle_parser::COMMA_DEL), semiDelIdx = input.rfind(cycle_parser::SEMICOLON_DEL),
			length = input.length(), st0 = length;
	bool t1 = commDelIdx>=0, t2 = semiDelIdx>=0;
	if (t1!=t2) {
		if(t1){
			del = cycle_parser::COMMA_DEL;
			idx = commDelIdx;
		}
		else {
			del = cycle_parser::SEMICOLON_DEL;
			idx = semiDelIdx;
		}
		return true;
	}
	return false;
}

bool cycle_parser::parse() {
	std::string del;
	_uint idx;
	if(!getDelimiterAndLastIndex(input,del,idx)) return false;

	std::list<std::string > lst = alg::splitString(stripBraces(input),del);
	_uint curr , next;
	std::stringstream is;
	bool firstSeen=false;
	for(std::list<std::string>::const_iterator i = lst.cbegin(); i!=lst.cend(); ++i){
		if(!firstSeen) {
			is.str(*i);
			is >> next;
			//				if(is.failbit){
			//					return false;
			//				}
			cyc.start = next;
			firstSeen = true;
			continue;
		}
		curr = next;
		is.str(*i);
		is >> next;
		//			if(is.failbit){
		//				return false;
		//			}

		if(!cyc.add(curr,next)) {
			break;
		}
	}
	cyc.add(next,cyc.start);
	cyc.fin = next;
	return true;
}
//cycle_iterator cycle::end() const {return cycle(cycle_iterator(cycle::EMPTY_CYCLE));}
cycle cycle::getCycle(const std::string& s, cycle_parser& cp){
	cp.setInput(s);
	return cp.parse()?cp.getCycle():EMPTY_CYCLE;
}
cycle::cycle(const cycle& o):map(o.map),start(o.start),fin(o.fin){}
bool operator==(const cycle& c1, const cycle& c2){
	return c1.length()==c2.length()?c1.map==c2.map:false;
}
bool operator==(const cycle_iterator& i1, const cycle_iterator& i2){
	_uint f1 =i1.ref.first(), f2 = i2.ref.first();
	if(i1.ref==i2.ref){

		return (i1.curr!=f1 && i2.curr!=f2 &&
			i1.curr == i2.curr) || (i1.curr==f1 && i1.started && i2.curr==f2 && i2.started);
	}
	return false;

}
bool cycle::add(_uint i, uint j) {
	if(i==j) return false;
	bool t1 = i == fin, t2 = map.find(i)==map.end();
	if(t1 || t2) {
		map[i] = j;
		return true;
	}
	return false;
}
cycle_iterator& cycle_iterator::operator+(const _uint& i) {
	if(ref.map.find(i)!=ref.map.end()) return *this;
	ref.map[ref.fin] = i;
	ref.fin = i;
	curr = i;
	ref.map[i] = ref.start;
	return *this;
}
std::ostream& operator<<(std::ostream& o, const cycle& c){
	o << "(";
	_uint lst = c.fin, frst = c.start;
	while(c.map.size()>1) {
		o << lst;
		if(lst!=frst) {
			o << ",";
		}
		else {
			break;
		}
		lst = c(lst);
	}
	o << ")";
	return o;
}
} /*alg*/

