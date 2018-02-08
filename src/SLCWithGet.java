import java.util.Iterator;

public class SLCWithGet <E extends Comparable<? super E>> extends LinkedCollection<E> implements CollectionWithGet<E> {

    private void insert(E e) {
        if (head == null) {
            head = new Entry(e, null);
        } else {
            Entry entry = head;
            while (entry.next != null && entry.next.element.compareTo(e) < 0)
                entry = entry.next;
            entry.next = new Entry(e, entry.next);
        }
    }

    @Override
    public boolean add(E e) {
        if (e == null)
            throw new NullPointerException();
        insert(e);
        return true;
    }

    @Override
    public E get(E e) {
        Iterator<E> iterator = super.iterator();
        while (iterator.hasNext()) {
            E rv = iterator.next();
            if (e.compareTo(rv) == 0)
                return rv;
        }
        return null;
    }

}
