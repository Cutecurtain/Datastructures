import java.util.*;

public class CompDijkstraPath {

    private CompDijkstraPath() {
    }

    /**
     * Generates a list of queue element from a list of edges given from the user.
     * All edges will be turned into queue element except the edges leading to the start.
     * @param graph The graph containing edges to create queue elements.
     * @param start Where the path starts.
     * @return The list with the queue elements generated from the graph.
     */
    private static <E extends Edge> List<QueueElement<E>> generateQueueElements(List<E> graph, int start) {
        List<QueueElement<E>> elements = new ArrayList<>();
        for (E edge : graph) {
            if (edge.to != start)
                elements.add(new QueueElement<>(edge.to, edge.getWeight(), edge));
        }
        return elements;
    }

    /**
     * The Dijkstra algorithm given by the class notes of class 11 from the course website.
     * @param elements The previously generated queue elements.
     * @param pq The priority queue that the current paths will be placed in.
     * @param stop The node that is to be reached.
     * @return An iterator containing the path from 'start' to 'stop'.
     */
    private static <E extends Edge> Iterator<E> findShortestPath(List<QueueElement<E>> elements,
                                                                 Queue<QueueElement<E>> pq, int stop) {
        while (!pq.isEmpty()) {
            QueueElement<E> qe = pq.poll();
            if (qe.getNode() == stop)
                return qe.getPath().iterator();

            // The previously reached node in this path.
            int prev = qe.getPath().size() == 0 ? -1 : qe.getPath().get(qe.getPath().size() - 1).getSource();

            /* A remove list. Will be filled with the queue elements that have been reached.
             * Basically to make sure we don't visit any already visited elements.
             */
            List<QueueElement<E>> rl = new ArrayList<>();

            // Find the neighboring queue elements and add them to the path and add them to the remove list.
            for (QueueElement<E> v : elements) {
                E lastEdge = v.getPath().get(v.getPath().size() - 1);
                if (lastEdge.getSource() == qe.getNode() && v.getNode() != prev) {
                    v.getPath().clear();
                    v.getPath().addAll(qe.getPath());
                    v.getPath().add(lastEdge);
                    v.setCost(qe.getCost() + v.getCost());
                    pq.add(v);
                    rl.add(v);
                }
            }

            // Remove the neighboring queue elements from the element list,
            // so that they are not compared to again.
            for (QueueElement<E> re : rl)
                elements.remove(re);
        }
        return null;
    }

    /**
     * Initializes the list of queue elements and the priority queue.
     * Adds the start node to the priority queue.
     * Then call and return iterator from the method findShortestPath().
     * @param graph The graph of edges the user gives.
     * @param start The node where the path starts.
     * @param stop The node that is to be reached.
     * @return An iterator containing the path from 'start' to 'stop'.
     */
    public static <E extends Edge> Iterator<E> dijkstra(List<E> graph, int start, int stop) {
        if (graph == null)
            return null;

        // Generate a list of queue elements from the graph's edges.
        List<QueueElement<E>> elements = generateQueueElements(graph, start);

        // Create and add the start node to a priority list.
        Queue<QueueElement<E>> pq = new PriorityQueue<>();
        pq.add(new QueueElement<>(start, 0, new ArrayList<>()));

        // Find the shortest path and return the iterator.
        return findShortestPath(elements, pq, stop);
    }


    private static class QueueElement<E extends Edge> implements Comparable<QueueElement> {

        // The actual node the path leads to.
        private int node;

        // The cost to go to the node according to the path.
        private double cost;

        // The path to reach the node.
        private List<E> path;

        /**
         * Initializes a QueueElement.
         * @param node The node that has been reached.
         * @param cost The cost of getting here from the start node.
         * @param path The path from the start node.
         */
        QueueElement(int node, double cost, List<E> path) {
            this.node = node;
            this.cost = cost;
            this.path = path;
        }

        /**
         * Initializes a QueueElement.
         * @param node The node that has been reached.
         * @param cost The cost of getting here from the start node.
         * @param edge The edge to the node.
         */
        QueueElement(int node, double cost, E edge) {
            this.node = node;
            this.cost = cost;
            this.path = new ArrayList<>();
            this.path.add(edge);
        }

        void setCost(double cost) {
            this.cost = cost;
        }

        int getNode() {
            return this.node;
        }

        double getCost() {
            return this.cost;
        }

        List<E> getPath() {
            return this.path;
        }

        /**
         * Sending the request to Double.compare().
         * If the cost is the same for both queue elements, then the method
         * will compare the amount of edges in the paths instead.
         * @param queueElement The element to compare to.
         * @return -1 if smaller cost. 0 if equal cost. 1 if bigger cost.
         */
        @Override
        public int compareTo(QueueElement queueElement) {
            int result = Double.compare(this.cost, queueElement.getCost());
            if (result == 0) {
                int size = this.path.size();
                int qSize = queueElement.getPath().size();
                if (size != qSize)
                    return size < qSize ? -1 : 1;
            }
            return result;
        }
    }

}
