/*
 * algebra_matrix_base.hpp
 *
 *  Created on: 02.02.2020
 *      Author: lmmac
 */

#ifndef INCLUDE_ALG_1_1_ALGEBRA_ALGEBRA_MATRIX_BASE_HPP_
#define INCLUDE_ALG_1_1_ALGEBRA_ALGEBRA_MATRIX_BASE_HPP_
#include <map>
#include "algebra_algebra_base.hpp"

namespace alg {

template<typename RNG >
class matrix;
template<typename RNG >
struct mat_add;
template<typename RNG >
struct mat_mul;
/**
 * @brief Matrix array class template
 *
 *
 */
template<typename RNG >
struct matArray {
	typedef std::map<unsigned int,RNG > _map;
	typedef typename _map::const_iterator _citer;
	typedef typename _map::iterator _iter;

	struct rowIter : public _citer {
		friend struct matArray<RNG >;
		friend class  matrix<RNG >;
		rowIter& operator++(){
			while(*(this)!=endIt && (this->operator->()->first)/ptr->col!=rowIdx)
				_citer::operator ++();
			_citer::operator ++();
			return *this;
		}
		rowIter operator++(int) {
			rowIter cp (*this);
			this->operator ++();
			return cp;
		}

		~rowIter(){ptr=0;}
		rowIter& operator=(const rowIter& o){
			ptr = o.ptr;
			endIt = o.endIt;
			rowIdx = o.rowIdx;
			return *this;
		}
		rowIter(const rowIter& o):_citer(o),ptr(o.ptr),endIt(o.endIt),rowIdx(o.rowIdx){}

	private:
		rowIter(const matArray& mat, unsigned int rowId):_citer(),ptr(&mat),endIt(mat.coeffs.end()),rowIdx(rowId){}
		const matArray* ptr;
		_citer endIt;
		unsigned int rowIdx;

	};
	friend struct rowIter;
	friend class matrix<RNG >;
	friend struct mat_add<RNG >;
	friend struct mat_mul<RNG >;
	matArray getColumn(unsigned int i) const {
		matArray cols(row,1);
		for (_citer ii = coeffs.cbegin(); ii != coeffs.cend(); ++ii){
			unsigned cl = ii->first%col, rw = (ii->first - cl)/col;
			if(cl==i) cols.coeffs[rw*col] = ii->second;
		}
		return cols;
	}
	matArray getRow(unsigned int i) const {
		matArray rows(1,col);
		for (_citer ii = coeffs.cbegin(); ii != coeffs.cend(); ++ii){
			unsigned cl = ii->first%col, rw = (ii->first - cl)/col;
			if(rw==i) rows.coeffs[cl] = ii->second;
		}
		return rows;
	}
	_citer operator[](unsigned int i) const {
		return coeffs.find(i);
	}
	_iter operator[](unsigned int i){
		return coeffs.find(i);
	}
	matArray& operator=(const matArray&  o){
		coeffs = o.coeffs;
		col = o.col;
		row = o.row;
	}
	rowIter begin() const {
		return rowIter(*this,0);
	}
	rowIter end() const {
		return rowIter(*this,row+1);
	}
	unsigned int colMinSupport() const {
		unsigned int supp(0),cnt(0),last(-1),row0;
		for(unsigned int i = 0; i<col;++i){
			cnt=0;
			for(unsigned int j =0; j<row;++j){
				_citer ii = coeffs.find(j*col+i);
				if(ii!=coeffs.cend()&&ii->second!=0){
					++cnt;
				}

			}
			if(cnt==0) return i;
			if(cnt<last) {last=cnt;supp=i;}
		}
		return supp;
	}
	unsigned int rowMinSupport() const {
		unsigned int supp(0),cnt(0),last(-1),row0;
		for(_citer i = coeffs.cbegin(); i!=coeffs.cend();++i){
			unsigned int col_val=i->first%col, row_val=(i->first-col_val)/col;

			if(i->second==0) {
				if (cnt==0&&col_val==col-1) return row_val;

				continue;
			}

			if(row_val-row0>1) return row0+1;
			if(last<cnt) continue;
			if(row_val-row0!=1) {
				++cnt;
				if(col_val==col-1 && cnt<last){
					last=cnt;
					supp=row_val;
				}
			}
			else {
				if(cnt<last) {last=cnt+1;supp=row_val;}
				cnt=1;
				row0=row_val;
			}
		}

		return supp;
	}
	matArray submat(unsigned int i0, unsigned int i1, unsigned int j0, unsigned int j1) const {
		matArray sub;
		sub.col=j1-j0;
		sub.row=i1-i0;
		for (_citer i = coeffs.cbegin();i!=coeffs.cend(); ++i){
			unsigned int col_val=i->first%col, row_val=(i->first-col_val)/col;
			if(row_val>=i0&&i1>row_val) {
				if(col_val>=j0&&j1>col_val) sub.setCoeff(row_val-i0,col_val-j0,i->second);
			}
		}
		return sub;
	}
	matArray submat(unsigned int i, unsigned int j) const {
		matArray sub;
		sub.col=col-1;
		sub.row=row-1;
		for (_citer i = coeffs.cbegin();i!=coeffs.cend(); ++i){
			unsigned int col_val=i->first%col, row_val=(i->first-col_val)/col;
			if(row_val<i) {
				if(col_val<j) sub.setCoeff(row_val,col_val,i->second);
				else if(col_val>j){
					sub.setCoeff(row_val,col_val-1,i->second);
				}
			}
			else if(row_val>i){
				if(col_val<j) sub.setCoeff(row_val-1,col_val,i->second);
				else if(col_val>j){
					sub.setCoeff(row_val-1,col_val-1,i->second);
				}
			}
		}
		return sub;
	}
	matArray transpose() const {
		matArray trans;
		trans.col=row;
		trans.row=col;
		for (_citer i = coeffs.cbegin();i!=coeffs.cend(); ++i){
			unsigned int col_val=i->first%col, row_val=(i->first-col_val)/col;
			trans.setCoeff(col_val,row_val,i->second);
		}
		return trans;
	}
	matArray():coeffs(),col(),row(){}
	matArray(const matArray& o):
		coeffs(o.coeffs),
		col(o.col),
		row(o.row){}

	private:
	_map coeffs;
	unsigned int row, col;
	void setCoeff(unsigned int rowId, unsigned int colId, const RNG& scl){
		if(rowId<row&&colId<col){
			_iter ii = coeffs.find(rowId*col+colId);
			if(scl!=0){
				if(ii!=coeffs.end()) ii->second = scl;
				else coeffs[rowId*col+colId] = scl;
			}
			else if(ii!=coeffs.end()){
				coeffs.erase(ii);
			}
		}
	}
	void setCoeff(unsigned int rowId, unsigned int colId, const matArray& m){

		if(col>=m.col+colId && row>=m.row+rowId){
			for (_citer i = m.coeffs.cbegin(); i!= m.coeffs.cend();++i){
				unsigned int col0 = i->first/m.col, row0 = (i->first - col0)/m.col;
				setCoeff(rowId+row0,colId+col0,i->second);
			}
		}
	}
	matArray(unsigned int rw, unsigned int cl):
		coeffs(),
		col(cl),
		row(rw){}
};

template<
typename RNG
>
class matrix {//:public ...


public:
	typedef std::map<unsigned int,RNG > _map;
	typedef matArray<RNG >	_mArray;
	typedef typename _map::const_iterator _citer;
	typedef typename _map::iterator _iter;
	typedef typename _mArray::rowIter _riter;


	struct mat_add {
		matrix<RNG > operator()(const matrix<RNG >& m1, const matrix<RNG >& m2) const {
			matrix<RNG > sum(m1.sz>m2.sz?m1:m2);
			const matrix<RNG >& s1 = m1.sz>m2.sz?m2:m1;
			for (_citer i = s1.mat.coeffs.cbegin(); i!=s1.mat.coeffs.cend();++i){
				_iter ii = sum. mat.find(i->first);
				if(ii==sum.mat.coeffs.end()) sum.mat.coeffs[i->first] = i->second;
				else {
					const RNG& slsum = i->second + ii->second;
					if(slsum!=0) {
						ii->second = slsum;
					}
					else ii.erase();
				}
			}
			return sum;
		}
	};
	struct mat_mul {
		matrix<RNG > operator()(const matrix<RNG >& m1, const matrix<RNG >& m2) const {
			matrix<RNG > prd;
			bool tst=m1.sz>m2.sz;
			const matrix<RNG >& s1 = tst?m2:m1,
					& s2 = tst?matrx<RNG >(m1).resize(m1.sz).fillWithDiag(m1.sz-m2.sz):m2;
			prd.sz = tst?m1.sz:m2.sz;
			for (_citer i = s1.mat.coeffs.cbegin(); i!=s1.mat.coeffs.cend();++i){
				unsigned int col1=i->first%s1.mat.col, row1=(i->first-col1)/s1.mat.col;
				const RNG& scl1 = i->second;
				for (_citer j = s2.mat.coeffs.cbegin(); j!=s2.mat.coeffs.cend();++j){
					unsigned int col2=j->first%s2.mat.col, row2=(j->first-col2)/s2.mat.col;
					const RNG& scl2 = j->second;
					if(col1!=row2) continue;
					const RNG& scl = scl1*scl2;
					if(scl!=0) {
						unsigned int idx = row1*prd.mat.col+col2;
						_iter ii = prd.mat.coeffs.find(idx);
						if(ii==prd.mat.coeffs.end()) {
							prd.set(row1,col2,scl);
						}
						else {

							prd.set(row1,col2,scl + ii->second);
						}
					}
				}

			}
			return prd;
		}

	};
	struct mat_anti {
		matrix<RNG > operator()(const matrix<RNG >& m) const {
			matrix<RNG > cp(m);
			_map& mCp = cp.mat.coeffs;
			for(_iter i = mCp.begin();mCp.end();++i){
				i->second = -(i->second);
			}
			return cp;
		}
	};
	struct mat_unit {
		template<typename T>
		const matrix<RNG >& operator()(const T& arg) const {
			static matrix<RNG > zero;
			return zero;
		}
	};
	struct mat_lscl {
		matrix<RNG > operator()(const RNG& scl, const matrix<RNG >& m) const {
			matrix<RNG > cp(m);
			_map& mCp = cp.mat.coeffs;
			for(_iter i = mCp.begin();mCp.end();++i){
				i->second = scl*(i->second);
			}
			return cp;
		}
	};
	struct mat_rscl {
		matrix<RNG > operator()(const matrix<RNG >& m, const RNG& scl) const {
			matrix<RNG > cp(m);
			_map& mCp = cp.mat.coeffs;
			for(_iter i = mCp.begin();mCp.end();++i){
				i->second = (i->second)*scl;
			}
			return cp;
		}
	};

	struct mat_id{
		matrix<RNG > operator()(const matrix<RNG >& m1, const matrix<RNG >& m2) const {

		}
	};

	typedef uassoc_alg<matrix<RNG >, RNG, mat_add,mat_anti,mat_lscl,mat_rscl,mat_mul,mat_id,mat_unit>
		_uassoc;
	//A,BASE_RNG,MOD_BINARY,ANTI,LSCAL,RSCAL,ALG_BINARY,ALG_UNIT,UNIT>
	matrix():mat(),sz(0),detPtr(0){}
	/**
	 * @brief diagonal constructor
	 *
	 * Constructs diagonal matrix $\fscl\cdot I_{\math{szz}}$\f
	 */
	matrix(const RNG& scl, unsigned int szz):
		mat(szz+1,szz+1),
		sz(szz),
		detPtr(0){

	}
	/**
	 * @brief constructs scalar multiple $\fscl \cdot e_{ij}\$f
	 */
	matrix(unsigned int i, unsigned int j, const RNG& scl):
		mat(i>j?i+1:j+1,i>j?j+1:i+1),
		sz(mat.col),
		detPtr(0){

	}
	matrix(const _mArray& mArr):mat(mArr),sz(mArr.col),detPtr(0){}
	matrix(const matrix<RNG >& o):
		mat(o.mat),
		sz(o.sz),
		detPtr(o.detPtr==0?0:new const RNG(*o.detPtr)){}
	~matrix(){
		delDet();
	}
	void fillWithDiag(unsigned int diff) {
		if(detPtr!=0){

		}
		resize(sz+diff);
		for (unsigned int i = 0; i < diff; ++i){

		}
	}
	void resize(unsigned int new_sz) {
		delDet();
		_mArray newMat;

		for (_citer it = mat.coeffs.begin(); it != mat.coeffs.end(); ++it){

			unsigned int j = (it->first)%sz, i = ((it->first)-j)/sz;
			if(i>=sz||i>=sz) break;
			newMat[(i+j/new_sz)*new_sz+j%new_sz]=it->second;
		}

	}
	void addLeftRowMultiple(unsigned int row, const RNG& scl) {
		const _mArray& rowMat = mat.getRow(row);
		for (_citer i = rowMat.coeffs.cbegin(); i!=rowMat.coeffs.cend();++i){
			unsigned int j1 = i->first%rowMat.col;
			_iter ii = mat.coeffs.find(row*mat.row+j1);
			const RNG& scl0 = scl*i->second;
			if(ii==mat.coeffs.cend()){
				if(scl0!=0) mat.coeffs[row*mat.row+j1]=scl0;
			}
			else{
				const RNG& sum = ii->second + scl0;
				if(sum==0) mat.coeffs.erase(ii);
				else ii->second = sum;
			}
		}

	}
	void addRightRowMultiple(unsigned int row, const RNG& scl) {
		const _mArray& rowMat = mat.getRow(row);
		for (_citer i = rowMat.coeffs.cbegin(); i!=rowMat.coeffs.cend();++i){
			unsigned int j1 = i->first%rowMat.col;
			_iter ii = mat.coeffs.find(row*mat.row+j1);
			const RNG& scl0 = i->second*scl;
			if(ii==mat.coeffs.cend()){
				if(scl0!=0) mat.coeffs[row*mat.row+j1]=scl0;
			}
			else{
				const RNG& sum = ii->second + scl0;
				if(sum==0) mat.coeffs.erase(ii);
				else ii->second = sum;
			}
		}

	}
	const RNG& det() const {
		if(detPtr==0) setDet();
		return *detPtr;
	}
	_riter operator[](unsigned int i) const {
		return _riter(mat,i);
	}
	void set(unsigned int i, unsigned int j, const RNG& scl) {
		mat.setCoeff(i,j,scl);
		if(detPtr!=0){
			delete detPtr;
			detPtr=0;

		}
	}
	void set(unsigned int i, unsigned int j, const matrix<RNG >& matr) {
		mat.setCoeff(i,j,matr);
		if(detPtr!=0){
			delete detPtr;
			detPtr=0;

		}
	}
	unsigned int size() const {return sz;}
	matrix<RNG > subMat(unsigned int i, unsigned int j) const {
		return matrix<RNG >(mat.submat(i,j));
	}
	matrix<RNG > subMat(unsigned int i0, unsigned int i1, unsigned int j0, unsigned int j1) const {
		return matrix<RNG >(mat.submat(i0,i1,j0,j1));
	}

private:
	_mArray mat;
	mutable const RNG* detPtr;

	unsigned int sz;
	void setDet() const{
		if(detPtr!=0) return;
		if(sz==1) {
			_iter i = mat.coeffs.find(0);
			detPtr  = i!=mat.coeffs.end()?new RNG(i->second):
					new RNG;
			return;
		}
		if(sz==2) {
			_citer i00 = mat.coeffs.find(0), i01 = mat.coeffs.find(0),
					i10 = mat.coeffs.find(2), i11 = mat.coeffs.find(3),
					e = mat.coeffs.cend();
			RNG val;
			if(i00!=e && i11!=e) val = i00->second * i11->second;

			if(i01!=e&&i10 ) val -=i01->second * i10 -> second;
			detPtr = new RNG(val);
			return;
		}
		unsigned int row = mat.rowMinSupport();

		_mArray sub = mat.getRow(row);

		RNG detVal;
		for(_citer i = sub.coeffs.cbegin(); i!=sub.coeffs.cend();++i){
			if(row+(i->first)%2==0) detVal += (i->second)*submat(row,i->first).det();
			else detVal -= (i->second)*submat(row,i->first).det();
		}
		detPtr = new RNG(detVal);
	}
	void delDet(){if(detPtr!=0) {delete detPtr; detPtr = 0;}}
};

}


#endif /* INCLUDE_ALG_1_1_ALGEBRA_ALGEBRA_MATRIX_BASE_HPP_ */
