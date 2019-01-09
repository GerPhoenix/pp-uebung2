package io.dama.par.hoh;

public interface NewList<T> {
	T get(int i);
	
	void add(T e);
	
	void mod(int i, T e);
	
	default T inspect(T e) {
		try {
			Thread.sleep(10);
		} catch (InterruptedException ex) {
			// ... nothing ...
		}
		return e;
	}
}
