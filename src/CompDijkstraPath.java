import java.util.*;

public class CompDijkstraPath {

    private CompDijkstraPath() {
    }

    private static <E extends Edge> List<QueueElement<E>> getAdjacent(List<QueueElement<E>> elements, int from) {
        List<QueueElement<E>> adjacents = new ArrayList<>();
        for (QueueElement<E> queueElement : elements) {
            if (queueElement.getPath().get(queueElement.getPath().size() - 1).getSource() == from)
                adjacents.add(queueElement);
        }
        return adjacents;
    }

    private static <E extends Edge> List<QueueElement<E>> generateQueueElements(List<E> graph, int from) {
        List<QueueElement<E>> elements = new ArrayList<>();
        for (E edge : graph) {
            if (edge.to != from)
                elements.add(new QueueElement<>(edge.to, edge.getWeight(), edge));
        }
        return elements;
    }

    public static <E extends Edge> Iterator<E> dijkstra(List<E> graph, int start, int stop) {
        List<QueueElement<E>> elements = generateQueueElements(graph, start);
        Queue<QueueElement<E>> pQueue = new PriorityQueue<>();
        pQueue.add(new QueueElement<>(start, 0, new ArrayList<>()));
        while (!pQueue.isEmpty()) {
            QueueElement<E> qe = pQueue.poll();
            //if (!qe.isVisited()) {
                if (qe.getNode() == stop)
                    return qe.getPath().iterator();
                //qe.visit();
                int prev = qe.getPath().size() == 0 ? -1 : qe.getPath().get(qe.getPath().size() - 1).getSource();
                Stack<QueueElement<E>> rStack = new Stack<>();
                for (QueueElement<E> adjacent : elements) {
                    E lastEdge = adjacent.getPath().get(adjacent.getPath().size() - 1);
                    if (lastEdge.getSource() == qe.getNode() && adjacent.getNode() != prev) {
                        adjacent.getPath().clear();
                        adjacent.getPath().addAll(qe.getPath());
                        adjacent.getPath().add(lastEdge);
                        adjacent.setCost(qe.getCost() + adjacent.getCost());
                        pQueue.add(adjacent);
                        rStack.push(adjacent);
                    }
                }
                for (QueueElement<E> rElement : rStack)
                    elements.remove(rElement);
            //}
        }
        return null;
    }

    private static class QueueElement<E extends Edge> implements Comparable<QueueElement> {

        private int node;
        private double cost;
        private List<E> path;

        //private boolean visited;

        /**
         * Initializes a QueueElement.
         *
         * @param node The node that has been reached.
         * @param cost The cost of getting here from the start node.
         * @param path The path from the start node.
         */
        QueueElement(int node, double cost, List<E> path) {
            this.node = node;
            this.cost = cost;
            this.path = path;
            //visited = false;
        }

        QueueElement(int node, double cost, E edge) {
            this.node = node;
            this.cost = cost;
            path = new ArrayList<>();
            path.add(edge);
        }

        public void setCost(double cost) {
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

        /*boolean isVisited() {
            return visited;
        }

        void visit() {
            visited = true;
        }*/

        @Override
        public int compareTo(QueueElement queueElement) {
            return Double.compare(cost, queueElement.getCost());
        }
    }

}
