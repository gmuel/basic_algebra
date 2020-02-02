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

template<
typename RNG
>
class matrix {//:public ...


public:
	typedef std::map<unsigned int,RNG > _map;
	typedef typename _map::const_iterator _citer;
	typedef typename _map::iterator _iter;

	struct matArray {
		friend class matrix<RNG >;
		friend class mat_add;
		friend class mat_mul;
		matArray getColumn(unsigned int i) const {
			matArray cols;
			for (unsigned int j=0;j<row;++j){
				cols.coeffs[col*j] = coeffs[col*j+i];
			}
			cols.col=1;
			cols.row=row;
			return cols;
		}
		matArray getRow(unsigned int i) const {
			matArray rows;
			for (unsigned int j=0;j<row;++j){
				rows.coeffs[j] = coeffs[col*i+j];
			}
			rows.col=col;
			rows.row=1;
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
		matArray(const matArray& o):
			coeffs(o.coeffs),
			col(o.col),
			row(o.row){}

	private:
		_map coeffs;
		unsigned int row, col;
		void setCoeff(unsigned int rowId, unsigned int colId, const RNG& scl){
			if(rowId<row&&colId<col){
				_citer ii = coeffs.find(rowId*col+colId);
				if(scl!=0){
					if(ii!=coeffs.cend()) ii->second = scl;
					else coeffs[rowId*col+colId] = scl;
				}
				else if(ii!=coeffs.cend()){
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
					& s2 = tst?m1:m2;
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

	friend struct mat_mul;
	/**
	 * @brief diagonal constructor
	 *
	 * Constructs diagonal matrix $\fscl\cdot I_{\math{szz}}$\f
	 */
	matrix(const RNG& scl, unsigned int szz):
		mat(szz,szz+1),
		sz(szz),
		detPtr(0){

	}
	matrix(unsigned int i, unsigned int j, const RNG& scl):
		mat(i>j?i+1:j+1,i>j?j+1:i+1),
		sz(),
		detPtr(0){

	}
	matrix(const matArray& mArr):matArray(mArr),sz(mArr.col*mArr.row),detPtr(0){}
	matrix(const matrix<RNG >& o):
		matArray(o.matArray),
		sz(o.sz),
		detPtr(o.detPtr==0?0:new RNG(*o.detPtr)){}
	~matrix(){
		if(detPtr!=0){delete detPtr; detPtr = 0;}
	}
	void addRowMultiple(unsigned int row, const RNG& scl) {
		const matArray& rowMat = mat.getRow(row);
		matrix<RNG > imm;
		imm.
	}
	const RNG& det() const {
		if(detPtr==0) setDet();
		return *detPtr;
	}
	void set(unsigned int i, unsigned int j, const RNG& scl) {
		mat.setCoeff(i,j,scl);
	}
	void set(unsigned int i, unsigned int j, const matrix<RNG >& matr) {
		mat.setCoeff(i,j,matr);
	}
	matrix<RNG > subMat(unsigned int i, unsigned int j) const {
		return matrix<RNG >(mat.submat(i,j));
	}
	matrix<RNG > subMat(unsigned int i0, unsigned int i1, unsigned int j0, unsigned int j1) const {
		return matrix<RNG >(mat.submat(i0,i1,j0,j1));
	}

private:
	matArray mat;
	mutable RNG* detPtr;

	unsigned int sz;
	void setDet() const{
		unsigned int row = mat.rowMinSupport();

		matArray sub = mat.getRow(row);

		RNG detVal;
		for(_citer i = sub.coeffs.cbegin(); i!=sub.coeffs.cend();++i){
			if(row+(i->first)%2==0) detVal += (i->second)*submat(row,i->first).det();
			else detVal -= (i->second)*submat(row,i->first).det();
		}
		detPtr = new RNG(detVal);
	}
};

}


#endif /* INCLUDE_ALG_1_1_ALGEBRA_ALGEBRA_MATRIX_BASE_HPP_ */
