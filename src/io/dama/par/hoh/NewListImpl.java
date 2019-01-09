package io.dama.par.hoh;

import java.util.concurrent.locks.ReentrantLock;

public class NewListImpl<T> implements NewList<T> {
    /* Leu'
        Statt *synchronized* als Schlüsselwort an den Methoden wird hier eine private
         Instanzvariable zum Synchronisieren verwendet, damit niemand von außen an
          derselben Variable einen Lock setzen kann, um Verklemmungen zu vermeiden.
     */
	
	private class ListElement<U> {
		private final ReentrantLock lock = new ReentrantLock();
		private U element;
		private ListElement<U> prev;
		private ListElement<U> next;
		
		private ListElement(final U element, final ListElement<U> prev, final ListElement<U> next) {
			this.element = element;
			this.prev = prev;
			this.next = next;
		}
	}
	
	private final ReentrantLock addLock = new ReentrantLock();
	
	private ListElement<T> first;
	
	public NewListImpl() {
		first = null;
	}
	

	@Override
	public T get(final int i) {
		ListElement<T> ptr = first;
		if (ptr == null)
			throw new IndexOutOfBoundsException(i);
		
		ptr.lock.lock();
		try {
			for (int j = 0; j < i; j++) {
				if (ptr.next == null)
					throw new IndexOutOfBoundsException(i);
				
				//critical block
				ptr.next.lock.lock();
				ptr.lock.unlock();
				ptr = ptr.next;
			}
			return inspect(ptr.element);
		} finally {
			ptr.lock.unlock();
		}
	}
	
	@Override
	public void add(final T e) {
		ListElement<T> element = new ListElement<>(e, null, null);
		addLock.lock();
		try {
			if (first == null) {
				first = element;
			} else {
				
				first.lock.lock();
				try {
					element.next = first;
					first.prev = element;
				} finally {
					first.lock.unlock();
				}
				first = element;
			}
		} finally {
			addLock.unlock();
		}
		
	}
	
	@Override
	public void mod(final int i, final T e) {
		ListElement<T> ptr = first;
		if (ptr == null)
			throw new IndexOutOfBoundsException(i);
		
		first.lock.lock();
		try {
			for (int j = 0; j < i; j++) {
				if (ptr.next == null)
					throw new IndexOutOfBoundsException(i);
				
				//critical block
				ptr.next.lock.lock();
				ptr.lock.unlock();
				ptr = ptr.next;
			}
			ptr.element = e;
		} finally {
			ptr.lock.unlock();
		}
		
	}
}
