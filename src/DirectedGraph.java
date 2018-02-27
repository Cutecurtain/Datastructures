import java.util.*;

public class DirectedGraph<E extends Edge> {

	private List<E> edges;

	private int noOfNodes;

	public DirectedGraph(int noOfNodes) {
		this.noOfNodes = noOfNodes;
		edges = new ArrayList<>();
	}

	public void addEdge(E e) {
		edges.add(e);
	}

	public Iterator<E> shortestPath(int from, int to) {
		return CompDijkstraPath.dijkstra(edges, from, to);
	}
		
	public Iterator<E> minimumSpanningTree() {
		return null;
	}

}
  
