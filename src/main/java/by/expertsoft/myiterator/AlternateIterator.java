package by.expertsoft.myiterator;

import java.util.Iterator;

public class AlternateIterator<T extends Comparable> implements Iterator<T> {

    private Iterator<T> iter1;
    private Iterator<T> iter2;
    private boolean useStream1;

    public AlternateIterator(Iterator<T> iter1, Iterator<T> iter2) {
        useStream1 = true;
        this.iter1 = iter1;
        this.iter2 = iter2;
    }

    @Override
    public boolean hasNext() {
        if (useStream1) {
            return iter1.hasNext();
        } else {
            return iter2.hasNext();
        }
    }

    @Override
    public T next() {
        if(!hasNext()) {
            return null;
        }

        try {
            return useStream1 ? iter1.next() : iter2.next();
        } finally {
            useStream1 = !useStream1;
        }
    }
}
