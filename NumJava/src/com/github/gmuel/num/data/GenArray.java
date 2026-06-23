package com.github.gmuel.num.data;

import java.util.AbstractList;
import java.util.Map;
import java.util.TreeMap;

import com.github.gmuel.num.fct.Functional;

public class GenArray<X> extends AbstractList<X> {

	public static class Acceptor<X> implements Functional<X, Boolean> {
		public Boolean f(X arg) {
			return arg==null?Boolean.FALSE:Boolean.TRUE;
		}
		public boolean accept(X x) {
			return f(x).booleanValue();
		}
	}
	public GenArray() {
		this((Map<Integer,X>) null);
	}
	public GenArray(int i, X val) {
		this();
		add(i,val);
	}
	public GenArray(Iterable<X> i) {
		this();
		Integer ii = Integer.valueOf(0);
		for(X val:i) {
			if(val!=null) {
				coeffs.put(ii, val);
				ii = Integer.valueOf(ii+1);
			}
		}
	}
	public GenArray(Map<Integer, X> coeffMap) {
		super();
		coeffs = coeffMap==null?new TreeMap<>():
			new TreeMap<>(coeffMap);
		acceptor = getAcceptor();
	}
	public void add(int i, X val) {
		if(acceptor.accept(val)) coeffs.put(i, val);
	}
	public Acceptor<X> getAcceptor(){return new Acceptor<X>();}
	public X get(int index) {
		return coeffs.get(index);
	}
	public String getNullStr() {
		return "null";
	}
	public int size() {
		if(coeffs.isEmpty())
			return 0;
		int idx1 = coeffs.lastKey(), idx0 = coeffs.firstKey();
		return idx1!=idx0?idx1-idx0+1:idx1+1;
	}
	public String toString() {
		if(coeffs.isEmpty()) return "[0]";
		StringBuilder sb = new StringBuilder("[");
		for(int i = 0, sz = size(); i < sz; ++i) {
			X val = get(i);
			sb.append(val==null?getNullStr():val.toString());
			if(i<sz-1) sb.append(",");
		}
		
		return sb.append("]").toString();
	}
	protected TreeMap<Integer, X> coeffs;
	
	protected Acceptor<X> acceptor;
}
