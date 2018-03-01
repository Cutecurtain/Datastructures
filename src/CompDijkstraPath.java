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
     * @param pq
     * @param stop
     * @param <E>
     * @return
     */
    private static <E extends Edge> Iterator<E> findShortestPath(List<QueueElement<E>> elements, Queue<QueueElement<E>> pq, int stop) {
        while (!pq.isEmpty()) {
            QueueElement<E> qe = pq.poll();
            if (qe.getNode() == stop)
                return qe.getPath().iterator();
            int prev = qe.getPath().size() == 0 ? -1 : qe.getPath().get(qe.getPath().size() - 1).getSource();
            Stack<QueueElement<E>> rs = new Stack<>();
            for (QueueElement<E> v : elements) {
                E lastEdge = v.getPath().get(v.getPath().size() - 1);
                if (lastEdge.getSource() == qe.getNode() && v.getNode() != prev) {
                    v.getPath().clear();
                    v.getPath().addAll(qe.getPath());
                    v.getPath().add(lastEdge);
                    v.setCost(qe.getCost() + v.getCost());
                    pq.add(v);
                    rs.push(v);
                }
            }
            for (QueueElement<E> re : rs)
                elements.remove(re);
        }
        return null;
    }


    public static <E extends Edge> Iterator<E> dijkstra(List<E> graph, int start, int stop) {
        List<QueueElement<E>> elements = generateQueueElements(graph, start);
        Queue<QueueElement<E>> pq = new PriorityQueue<>();
        pq.add(new QueueElement<>(start, 0, new ArrayList<>()));
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
            path = new ArrayList<>();
            path.add(edge);
        }

        void setCost(double cost) {
            this.cost = cost;
        }

        int getNode() {
            return node;
        }

        double getCost() {
            return cost;
        }

        List<E> getPath() {
            return path;
        }

        /**
         * A simple compareTo method, just sending the request to Double.compare().
         * @param queueElement
         * @return -1 if smaller cost. 0 if equal cost. 1 if bigger cost.
         */
        @Override
        public int compareTo(QueueElement queueElement) {
            return Double.compare(cost, queueElement.getCost());
        }
    }

}
