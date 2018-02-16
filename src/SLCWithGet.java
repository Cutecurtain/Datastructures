import java.util.Iterator;

public class SLCWithGet <E extends Comparable<? super E>> extends LinkedCollection<E> implements CollectionWithGet<E> {

    Entry middle;

    public SLCWithGet() {
        super();
        middle = null;
    }

    /**
     * Inserts a given element to the linked list.
     * @param e The element to insert.
     */
    private void insert(E e) {
        if (head == null) {
            super.add(e);
            middle = head;
        } else {
            Entry temp;
            int cmp = e.compareTo(middle.element);
            if (cmp < 0) {
                temp = head;
            } else if (cmp > 0) {
                temp = middle;
            } else {
                return;
            }
            while (temp.next != null && e.compareTo(temp.element) > 0)
                temp = temp.next;
            temp.next = new Entry(e, temp.next);
        }

        int half = super.size() / 2;
        Entry newMiddle = head;
        for (int i = 0; i < half; i++) {
            newMiddle = newMiddle.next;
        }
        middle = newMiddle;
    }

    /**
     * Tries to add an element to the linked list.
     * @param e The element to add.
     * @return true if the element was added to the linked list.
     */
    @Override
    public boolean add(E e) {
        if (e == null)
            throw new NullPointerException("Can not add null as element");
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
        /*Entry entry = super.head;
        while (entry != null) {
            if (entry.element.compareTo(e) == 0)
                return entry.element;
            entry = entry.next;
        }
        return null;*/

        Iterator<E> iterator = super.iterator();
        while (iterator.hasNext()) {
            E rv = iterator.next();
            if (e.compareTo(rv) == 0)
                return rv;
        }
        return null;
    }

}
