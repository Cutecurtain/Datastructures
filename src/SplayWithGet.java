import java.util.Stack;

public class SplayWithGet<E extends Comparable<? super E>> extends BinarySearchTree<E> implements CollectionWithGet<E> {

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
        Entry B = y.right;
        Entry C = z.right;

        y.right = x;
        z.right = y;
        y.left = C;
        x.left = B;

        z.parent = x.parent;
        x.parent = y;
        y.parent = z;

        E temp = x.element;
        x.element = z.element;
        z.element = temp;
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
        Entry B = y.left;
        Entry C = z.left;

        x.right = B;
        y.right = C;
        y.left = x;
        z.left = y;

        z.parent = x.parent;
        x.parent = y;
        y.parent = z;

        E temp = x.element;
        x.element = z.element;
        z.element = temp;
    }


    private void balance(Entry entry) {
        Entry temp = entry;
        Stack<Integer> directions = new Stack<>();
        while (temp.parent != null) {
            if (temp == temp.parent.left) {
                directions.push(0);
            } else {
                directions.push(1);
            }
            temp = temp.parent;
        }
        while (!directions.empty()) {
            int dir1 = directions.pop();
            entry = entry.parent;
            if (!directions.empty()) {
                entry = entry.parent;
                int dir2 = directions.pop();
                if (dir1 == dir2) {
                    if (dir1 == 0)
                        zigzig(entry.left);
                    else
                        zagzag(entry.right);
                } else {
                    if (dir1 == 0)
                        zagzig(entry.left);
                    else
                        zigzag(entry.right);
                }
            } else {
                if (dir1 == 0)
                    zig(entry.left);
                else
                    zag(entry.right);
            }
        }
    }

    private void insert(E e) {
        Entry entry = super.find(e, super.root);
        if (entry == null)
            super.add(e);
        balance(entry);
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
        return super.find(e, super.root).element;
    }
}
