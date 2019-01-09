package io.dama.par.hoh;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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

    /* Sko'
        Wichtig für das HOH-Lock ist,
        dass nicht die gesamte Methode gesperrt wird
        sondern bestenfalls nur zwei/drei Elemente der Liste durch den Lock betroffen sind.

        lement.intrinsiclock.lock() bzw lement.lock()
        und / oder
        ptr.next.intrinsiclock.lock() bzw ptr.next.lock()
        mit
        .unlock() des Elements davor.

        s.a.: https://stackoverflow.com/questions/43762688/java-locks-hand-over-hand-locking-through-list
    */
	/* Sko'
	-->	Kann man das Listenobjekt als zu-veränderndes Element *als auch gleichzeitig* als Lock nutzen? <--
	*/
    @Override
    public T get(final int i) {
        ListElement<T> ptr = first;
        //prev: used to ensure correct unlocking
        ListElement<T> prev;
        first.lock.lock();
        try {
            int j = 0;
            while (j++ < i) {
                prev = ptr;
                try {
                    ptr = ptr.next;
                    ptr.lock.lock();
                } finally {
                    prev.lock.unlock();
                }
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
                ListElement<T> oldFirst = first;
                try {
                    element.next = first;
                    first.prev = element;
                    first = element;
                } finally {
                    oldFirst.lock.unlock();
                }
            }
        } finally {
            addLock.unlock();
        }

    }

    @Override
    public void mod(final int i, final T e) {
        ListElement<T> ptr = first;
        ListElement<T> prev;
        first.lock.lock();
        try {
            int j = 0;
            while (j++ < i) {
                prev = ptr;
                try {
                    ptr = ptr.next;
                    ptr.lock.lock();
                } finally {
                    prev.lock.unlock();
                }
            }
            ptr.element = e;
        } finally {
            ptr.lock.unlock();
        }

    }
}
