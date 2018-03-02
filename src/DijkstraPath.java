import java.util.ArrayList;
import java.util.List;

public class DijkstraPath<E extends Edge> implements Comparable<DijkstraPath> {

    // The actual node the path leads to.
    private int node;

    // The cost to go to the node according to the path.
    private double cost;

    // The path to reach the node.
    private List<E> path;

    /**
     * Initializes a DijkstraPath.
     * @param node The node that has been reached.
     * @param cost The cost of getting here from the start node.
     * @param path The path from the start node.
     */
    DijkstraPath(int node, double cost, List<E> path) {
        this.node = node;
        this.cost = cost;
        this.path = path;
    }

    /**
     * Initializes a DijkstraPath.
     * @param node The node that has been reached.
     * @param cost The cost of getting here from the start node.
     * @param edge The edge to the node.
     */
    DijkstraPath(int node, double cost, E edge) {
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
     * @param t1 The element to compare to.
     * @return -1 if smaller cost. 0 if equal cost. 1 if bigger cost.
     */
    @Override
    public int compareTo(DijkstraPath t1) {
        int result = Double.compare(this.cost, t1.getCost());

        // If the cost is equal, then compare the length of the path instead.
        if (result == 0) {
            int size = this.path.size();
            int qSize = t1.getPath().size();
            if (size != qSize)
                return size < qSize ? -1 : 1;
        }
        return result;
    }
}