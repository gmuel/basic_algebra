package com.github.gmuel.num.fct;

@FunctionalInterface
public interface Functional<X, Y> {

	public Y f(X x);
	
	default Functional<X,X> codomId(){
		return (X x) -> x;
	}
	default Functional<Y,Y> domId(){
		return (Y y) -> y;
	}
	default <W> Functional<W,Y> o(Functional<W,X> f){
		return (W w) -> f(f.f(w));
	}
	@FunctionalInterface
	public static interface Endo<X> extends Functional<X,X> {
		default Endo<X> o(Endo<X> f) {return (X x) -> f(f.f(x));}
	}
}
