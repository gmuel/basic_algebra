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
	_uint idx = s.rfind(sub), lng = s.length();
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
	_uint idx = cp.str().rfind(sub), lng = sub.length(), last = s.length();
	while(idx>=0) {
		string tmp = cp.str();
		lst.push_back(tmp.substr(idx+lng,last));
		last = idx;
		cp.str(tmp.substr(0,last));
		idx=cp.str().rfind(sub);
	}
	return lst;
}


bool cycle_parser::parse() {

	int commDelIdx = input.rfind(COMMA_DEL), semiDelIdx = input.rfind(SEMICOLON_DEL),
		length = input.length(), st0 = length;
	bool t1 = commDelIdx>=0, t2 = semiDelIdx>=0;
	if (t1!=t2) {
		cyc = cycle();
		std::string del;
		_uint idx;
		if(t1){
			del = COMMA_DEL;
			idx = commDelIdx;
		}
		else {
			del = SEMICOLON_DEL;
			idx = semiDelIdx;
		}
		std::list<std::string > lst = alg::splitString(input,del);
		_uint curr , next;
		std::stringstream is;
		bool firstSeen=false;
		for(std::list<std::string>::const_iterator i = lst.cbegin(); i!=lst.cend(); ++i){
			if(!firstSeen) {
				is.str(*i);
				is >> next;
				if(is.failbit){
					cyc = cycle::EMPTY_CYCLE;
					return false;
				}
				cyc.start = next;
				firstSeen = true;
				continue;
			}
			curr = next;
			is.str(*i);

			if(is.failbit){
				cyc = cycle::EMPTY_CYCLE;
				return false;
			}
			is >> next;
			if(cyc.map.find(next)!=cyc.map.end()) {
				break;
			}
			cyc.map[curr] = next;
		}
		return true;
	}
	return false;
}

cycle cycle::getCycle(const std::string& s, cycle_parser& cp){
	cp.setInput(s);
	return cp.parse()?cp.getCycle():EMPTY_CYCLE;
}
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
} /*alg*/

