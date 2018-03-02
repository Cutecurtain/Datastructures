import java.util.*;

public class CompKruskalEdge {

    private CompKruskalEdge() {}

    /**
     * Generates a field full with empty lists.
     * @param noOfNodes The size the requested field.
     * @return The field.
     */
    private static <E extends Edge> List<E>[] generateEdgeField(int noOfNodes) {
        List<E>[] cc = new List[noOfNodes];
        for (int i = 0; i < noOfNodes; i++)
            cc[i] = new LinkedList<>();
        return cc;
    }

    /**
     * Adds all element from cc[from] to cc[to] and makes all the nodes
     * in the list of node 'from' to reference the list of node 'to'.
     * Then makes the node 'from' reference the list of node 'to'.
     * @param cc The field with all the list references for every node.
     * @param to The list that the affected nodes should reference.
     * @param from The list with all the nodes that is affected.
     */
    private static <E extends Edge> void rePoint(List<E>[] cc, int to, int from) {
        cc[to].addAll(cc[from]);
        for (E edge : cc[from]) {
            cc[edge.getSource()] = cc[to];
            cc[edge.getDest()] = cc[to];
        }
        cc[from] = cc[to];
    }

    /**
     * The kruskal algorithm given by the class notes of class 11 from the course website.
     * @param cc The field of all list references for each of the nodes.
     * @param pq The priority queue that contains all the edges from the graph.
     */
    private static <E extends Edge> void findMinimumSpanningTree(List<E>[] cc, Queue<E> pq) {
        for (int length = cc.length; !pq.isEmpty() && length > 1;) {
            E e = pq.poll();
            int source = e.getSource(), dest = e.getDest();
            if (cc[source] != cc[dest]) {
                // Choose the smaller list.
                int min = source < dest ? source : dest;

                // Choose the bigger list.
                int max = source + dest - min;

                // Adjust the references for all the affected nodes.
                rePoint(cc, max, min);

                // Add the next edge to the current list.
                cc[max].add(e);

                length--;
            }
        }
    }

    /**
     * Initialize fields and priority queue. Then call and return iterator from
     * the method findMinimumSpanningTree().
     * @param graph The graph of edges the user gives.
     * @param noOfNodes The number of nodes in the graph.
     * @return An iterator containing the minimum spanning tree.
     */
    public static <E extends Edge> Iterator<E> kruskal(List<E> graph, int noOfNodes) {
        if (graph == null || noOfNodes <= 0)
            return null;

        // Generate a field of lists for each node.
        List<E>[] cc = generateEdgeField(noOfNodes);

        // Add all edges to a priority queue.
        Queue<E> pq = new PriorityQueue<>(new EdgeComparator<E>());
        pq.addAll(graph);

        // Find the minimum spanning tree.
        findMinimumSpanningTree(cc, pq);

        // Return the iterator for the minimum spanning tree.
        return cc[0].iterator();
    }

    private static class EdgeComparator<E extends Edge> implements Comparator<E> {

        /**
         * A simple compare method, just sending the request to Double.compare().
         * @param e An edge.
         * @param t1 An edge to compare the edge 'e' to.
         * @return -1 if e has less weight. 0 if the weights are equal. 1 if e has more weight.
         */
        @Override
        public int compare(E e, E t1) {
            return Double.compare(e.getWeight(), t1.getWeight());
        }

    }

}
