import com.sun.xml.internal.messaging.saaj.soap.impl.TreeException;

import java.util.Stack;

public class SplayTreeWithGet<E extends Comparable<? super E>> extends BinarySearchTree<E> implements CollectionWithGet<E> {

    public SplayTreeWithGet() {
        super();
    }

    /* Rotera 1 steg i hogervarv, dvs
          x'                 y'
         / \                / \
        y'  C   -->        A   x'
       / \                    / \
      A   B                  B   C
*/
    private void zig(Entry x) {
        Entry y = x.left;
        E temp = x.element;
        x.element = y.element;
        y.element = temp;
        x.left = y.left;
        if (x.left != null)
            x.left.parent = x;
        y.left = y.right;
        y.right = x.right;
        if (y.right != null)
            y.right.parent = y;
        x.right = y;
    } //   rotateRight
    // ========== ========== ========== ==========

    /* Rotera 1 steg i vanstervarv, dvs
              x'                 y'
             / \                / \
            A   y'  -->        x'  C
               / \            / \
              B   C          A   B
    */
    private void zag(Entry x) {
        Entry y = x.right;
        E temp = x.element;
        x.element = y.element;
        y.element = temp;
        x.right = y.right;
        if (x.right != null)
            x.right.parent = x;
        y.right = y.left;
        y.left = x.left;
        if (y.left != null)
            y.left.parent = y;
        x.left = y;
    } //   rotateLeft
    // ========== ========== ========== ==========

    /* Rotera 2 steg i hogervarv, dvs
              x'                  z'
             / \                /   \
            y'  D   -->        y'    x'
           / \                / \   / \
          A   z'             A   B C   D
             / \
            B   C
    */
    private void zigzag(Entry x) {
        Entry y = x.left,
                z = x.left.right;
        E e = x.element;
        x.element = z.element;
        z.element = e;
        y.right = z.left;
        if (y.right != null)
            y.right.parent = y;
        z.left = z.right;
        z.right = x.right;
        if (z.right != null)
            z.right.parent = z;
        x.right = z;
        z.parent = x;
    }  //  doubleRotateRight
    // ========== ========== ========== ==========

    /* Rotera 2 steg i vanstervarv, dvs
               x'                  z'
              / \                /   \
             A   y'   -->       x'    y'
                / \            / \   / \
               z   D          A   B C   D
              / \
             B   C
     */
    private void zagzig(Entry x) {
        Entry y = x.right,
                z = x.right.left;
        E e = x.element;
        x.element = z.element;
        z.element = e;
        y.left = z.right;
        if (y.left != null)
            y.left.parent = y;
        z.right = z.left;
        z.left = x.left;
        if (z.left != null)
            z.left.parent = z;
        x.left = z;
        z.parent = x;
    } //  doubleRotateLeft
    // ========== ========== ========== ==========


    /*
               x'            z'
              / \           / \
             y'  A   -->   D   y'
            / \               / \
           z'  B             C   x'
          / \                   / \
         D   C                 B   A
    */
    private void zigzig(Entry x) {
        Entry y = x.left;
        Entry z = y.left;
        E e = x.element;
        x.element = z.element;
        z.element = e;
        y.left = z.right;
        if (y.left != null)
            y.left.parent = y;
        x.left = z.left;
        if (x.left != null)
            x.left.parent = x;
        z.left = y.right;
        if (z.left != null)
            z.left.parent = z;
        z.right = x.right;
        if (z.right != null)
            z.right.parent = z;
        x.right = y;
        y.right = z;
    }

    /*
        x'                  z'
       / \                 / \
      A   y'     -->      y'  D
         / \             / \
        B   z'          x'  C
           / \         / \
          C   D       A   B
     */
    private void zagzag(Entry x) {
        Entry y = x.right;
        Entry z = y.right;
        E e = x.element;
        x.element = z.element;
        z.element = e;
        y.right = z.left;
        if (y.right != null)
            y.right.parent = y;
        x.right = z.left;
        if (x.right != null)
            x.right.parent = x;
        z.left = x.left;
        if (z.left != null)
            z.left.parent = z;
        z.right = y.left;
        if (z.right != null)
            z.right.parent = z;
        x.left = y;
        y.left = z;
    }

    /**
     * Balances a selected part of the tree. Making the entry node reach the targeted level.
     * @param entry The entry that is suppose to become root of part tree.
     * @param target The target root of the part of the tree that is to be balanced.
     */
    private void balance(Entry entry, Entry target) {
        Entry next;
        while (entry != null && entry.parent != null && entry != target) {
            if (entry.parent.left == entry) {
                if (entry.parent.parent != null && entry.parent != target) {
                    next = entry.parent.parent;
                    if (next.left == entry.parent)
                        zigzig(next);
                    else
                        zagzig(next);
                } else {
                    next = entry.parent;
                    zig(next);
                }
            } else {
                if (entry.parent.parent != null && entry.parent != target) {
                    next = entry.parent.parent;
                    if (next.right == entry.parent)
                        zagzag(next);
                    else
                        zigzag(next);
                } else {
                    next = entry.parent;
                    zag(next);
                }
            }
            entry = next;
        }
    }

    /**
     * Tries to find the given element from the given entry and balancing the tree in the process.
     * @param e The element to find.
     * @param t The root which to search from.
     * @return
     */
    @Override
    protected Entry find(E e, Entry t) {
        Entry entry = super.find(e, t);
        this.balance(entry, t);
        return entry == null ? null : t;
    }

    /**
     * Tries to add an element.
     * @param e The element to add to the tree.
     * @return true if the element is added to the tree.
     */
    @Override
    public boolean add(E e) {
        if (e == null)
            throw new NullPointerException("Can not add null as element");
        return super.add(e);
    }

    /**
     * Returns an element from the tree matching the dummy element.
     * @param e The dummy element to compare to.
     * @return The matching tree element.
     */
    @Override
    public E get(E e) {
        Entry entry = this.find(e, super.root);
        return entry == null ? null : entry.element;
    }
}