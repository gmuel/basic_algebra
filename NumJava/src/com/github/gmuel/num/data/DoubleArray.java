package com.github.gmuel.num.data;


import java.util.Map;
import java.util.Map.Entry;

import com.github.gmuel.num.fct.Functional;

public class DoubleArray extends GenArray<Double> {

	
	public static Double ZERO = Double.valueOf(0);
	public static Double ONE  = Double.valueOf(1);
	public static Double EPS  = Double.valueOf(1e-15);
	public static class AcceptNonZ extends GenArray.Acceptor<Double> {
		public Boolean f(Double arg) {
			return super.accept(arg) && !arg.equals(ZERO)?Boolean.TRUE:Boolean.FALSE;
		}
	}
	public static class AcceptNonEPS extends GenArray.Acceptor<Double> {
		public Boolean f(Double arg) {
			return super.accept(arg) && Math.abs(arg-EPS)>EPS?Boolean.TRUE:Boolean.FALSE;
		}
	}
	public DoubleArray() {super();}
	public DoubleArray(int i, Double val) {
		super(i,val);
	}
	public DoubleArray(Iterable<Double> i) {
		super(i);
	}
	public DoubleArray(Map<Integer,Double> coeffMap) {
		super(coeffMap);
	}
	public DoubleArray add(DoubleArray s) {
		int sz1 = coeffs.size(), sz2 = s.coeffs.size();
		DoubleArray sum = new DoubleArray(sz1>sz2?this:s), cp = sz1>sz2?s:this;
		for(Entry<Integer,Double> entry:cp.coeffs.entrySet()) {
			int i = entry.getKey();
			Double tmp = cp.get(i) + sum.get(i);
			if(!acceptor.accept(tmp)) remove(i); 
			else add(i, tmp);
		}
		return sum;
	}
	public DoubleArray mul(double scl) {
		if(scl==0) return new DoubleArray();
		DoubleArray cp = new DoubleArray(this);
		cp.mul(scl);
		return cp;
	}
	public void mulInPlace(double scl) {
		if(scl==0d) {
			coeffs.clear();
			return;
		}
		for(Entry<Integer,Double> en:coeffs.entrySet()) {
			coeffs.put(en.getKey(), en.getValue()*scl);
		}
	}

	public Double get(int index) {
		Double val = super.get(index);
		return val==null?ZERO:val;
	}
	public Acceptor<Double> getAcceptor(){
		return new AcceptNonZ();
	}
	public String getNullStr() {return "0";}
	public Double norm1() {
		Double nrm = ZERO;
		for (Double val:this) {
			nrm = nrm + val;
		}
		return nrm;
	}
	public Double norm2() {
		Double nrm = ZERO;
		for (Double val:this) {
			nrm = nrm + val*val;
		}
		return Math.sqrt(nrm.doubleValue());
	}
	public Double normM() {
		Double nrm = ZERO;
		for (Double val:this) {
			Double abs = Math.abs(val.doubleValue());
			if(abs>nrm) nrm = abs;
		}
		return nrm;
	}
	public static class DCoArray extends DoubleArray implements Functional<DoubleArray, Double> {
		public Double f(DoubleArray d) {
			if(d==null) return ZERO;
			Double val = ZERO;
			for(int i = 0; i < size(); ++i) {
				val = val + get(i) * d.get(i);
			}
			return val;
		}
	}
}
