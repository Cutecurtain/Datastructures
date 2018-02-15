import java.util.Iterator;

public class SLCWithGet <E extends Comparable<? super E>> extends LinkedCollection<E> implements CollectionWithGet<E> {

    /**
     * Inserts a given element to the linked list.
     * @param e The element to insert.
     */
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

    /**
     * Tries to add an element to the linked list.
     * @param e The element to add.
     * @return true if the element was added to the linked list.
     */
    @Override
    public boolean add(E e) {
        if (e == null)
            throw new NullPointerException();
        insert(e);
        return true;
    }

    /**
     * Tries to get an element from the linked list matching the dummy element.
     * @param e The dummy element to compare to.
     * @return The matching element from the linked list.
     */
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
