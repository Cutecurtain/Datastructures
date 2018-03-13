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
		return this.dijkstra(edges, from, to);
	}
		
	public Iterator<E> minimumSpanningTree() {
		return DirectedGraph.kruskal(edges, noOfNodes);
	}



	// -------------- DIJKSTRA --------------


	/**
	 * Adds the from node to the priority queue.
	 * Then call and return iterator from the method findShortestPath().
	 * @param graph The graph of edges the user gives.
	 * @param from The node where the path starts.
	 * @param to The node that is to be reached.
	 * @return An iterator containing the path from 'from' to 'to'.
	 */
	private Iterator<E> dijkstra(List<E> graph, int from, int to) {
		if (graph == null)
			throw new NullPointerException("No graph given!");
		else if (from < 0 || from > this.noOfNodes - 1)
			throw new IndexOutOfBoundsException("Node " + from + " does not exist!");
		else if (to < 0 || to > this.noOfNodes - 1)
			throw new IndexOutOfBoundsException("Node " + to + " does not exist!");

		// Create and add the from node to a priority queue.
		Queue<DijkstraPath<E>> pq = new PriorityQueue<>();
		pq.add(new DijkstraPath<>(from, 0, new ArrayList<>()));

		// Find the shortest path and return the iterator.
		return findShortestPath(graph, pq, to);
	}

	/**
	 * The Dijkstra algorithm given by the class notes of class 11 from the course website.
	 * @param graph The entire graph.
	 * @param pq The priority queue that the current paths will be placed in.
	 * @param stop The node that is to be reached.
	 * @return An iterator containing the path from 'start' to 'stop'.
	 */
	private Iterator<E> findShortestPath(List<E> graph, Queue<DijkstraPath<E>> pq, int stop) {
		// To be filled with the nodes we will visit.
		Set<Integer> visited = new HashSet<>();

		while (!pq.isEmpty()) {
			// Get next shortest path from the priority queue.
			DijkstraPath<E> qe = pq.poll();

			// Just in case something somehow went wrong.
			while (visited.contains(qe.getNode()))
				qe = pq.poll();

			// If we have reached the goal node. Then return the path.
			if (qe.getNode() == stop)
				return qe.getPath().iterator();

			// Visit this node.
			visited.add(qe.getNode());

			// Find the neighboring queue elements and add them to the path.
			for (E nextEdge : graph) {

				// If the source in this edge is the same as the node we are currently on and its
				// destination has not been visited.
				if (nextEdge.getSource() == qe.getNode() && !visited.contains(nextEdge.getDest())) {

					// Create a new path to the next node.
					List<E> newPath = new ArrayList<>(qe.getPath());
					newPath.add(nextEdge);

					// Create a new DijkstraPath to the next node.
					DijkstraPath<E> newDijkstraPath = new DijkstraPath<>(nextEdge.getDest(),
																		qe.getCost() + nextEdge.getWeight(),
																		newPath);

					// Add to the priority queue as a potential path.
					pq.add(newDijkstraPath);
				}
			}
		}
		return null;
	}



	// -------------- KRUSKAL --------------


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

	/**
	 * The kruskal algorithm given by the class notes of class 11 from the course website.
	 * @param cc The field of all list references for each of the nodes.
	 * @param pq The priority queue that contains all the edges from the graph.
	 */
	private static <E extends Edge> void findMinimumSpanningTree(List<E>[] cc, Queue<E> pq) {

		for (int length = cc.length; !pq.isEmpty() && length > 1;) {

			// Grab the next cheapest edge from the priority queue.
			E e = pq.poll();

			// Collect the source and destination.
			int source = e.getSource();
			int dest = e.getDest();

			// If the source and destination nodes does not reference the same list.
			if (cc[source] != cc[dest]) {

				// Choose the smaller list.
				int minIndex = source < dest ? source : dest;

				// Choose the bigger list.
				int maxIndex = source + dest - minIndex;

				// Adjust the references for all the affected nodes.
				rePoint(cc, maxIndex, minIndex);

				// Add the next edge to the current list.
				cc[maxIndex].add(e);

				length--;
			}
		}
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
		// Append elements from the list in 'from' to the list in 'to'.
		cc[to].addAll(cc[from]);

		// Make every affected node reference the 'to' list.
		for (E edge : cc[from]) {
			cc[edge.getSource()] = cc[to];
			cc[edge.getDest()] = cc[to];
		}
		cc[from] = cc[to];
	}

	/**
	 * Generates a field full with empty lists.
	 * @param noOfNodes The size the requested field.
	 * @return The field.
	 */
	private static <E extends Edge> List<E>[] generateEdgeField(int noOfNodes) {
		// Create a array with lists for every node.
		List<E>[] cc = new List[noOfNodes];
		for (int i = 0; i < noOfNodes; i++)
			cc[i] = new LinkedList<>();
		return cc;
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
  
