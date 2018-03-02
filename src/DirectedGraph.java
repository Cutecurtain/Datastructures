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
		return DirectedGraph.dijkstra(edges, from, to);
	}
		
	public Iterator<E> minimumSpanningTree() {
		return DirectedGraph.kruskal(edges, noOfNodes);
	}



	// -------------- DIJKSTRA --------------


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
		List<DijkstraPath<E>> elements = generateQueueElements(graph, start);

		// Create and add the start node to a priority list.
		Queue<DijkstraPath<E>> pq = new PriorityQueue<>();
		pq.add(new DijkstraPath<>(start, 0, new ArrayList<>()));

		// Find the shortest path and return the iterator.
		return findShortestPath(elements, pq, stop);
	}

	/**
	 * The Dijkstra algorithm given by the class notes of class 11 from the course website.
	 * @param elements The previously generated queue elements.
	 * @param pq The priority queue that the current paths will be placed in.
	 * @param stop The node that is to be reached.
	 * @return An iterator containing the path from 'start' to 'stop'.
	 */
	private static <E extends Edge> Iterator<E> findShortestPath(List<DijkstraPath<E>> elements,
																 Queue<DijkstraPath<E>> pq, int stop) {
		while (!pq.isEmpty()) {

			// Get next shortest path from the priority queue.
			DijkstraPath<E> qe = pq.poll();

			// If we have reached the goal node. Then return the path.
			if (qe.getNode() == stop)
				return qe.getPath().iterator();

			// The previous node in this path.
			int prev = -1;
			int size = qe.getPath().size();
			if (size != 0)
				prev = qe.getPath().get(size - 1).getSource();

			/* A remove list. Will be filled with the queue elements that have been reached.
			 * Basically to make sure we don't visit any already visited elements.
			 */
			List<DijkstraPath<E>> visited = new ArrayList<>();

			// Find the neighboring queue elements and add them to the path and add them to the remove list.
			for (DijkstraPath<E> nextNode : elements) {

				// The edge that leads to the next node.
				E lastEdge = nextNode.getPath().get(nextNode.getPath().size() - 1);

				// If the last node in this edge is the same as the one we are currently on (i.e this edge is connected
				// to the node we are on) and it is not the previous node that lead us here.
				if (lastEdge.getSource() == qe.getNode() && nextNode.getNode() != prev) {

					// Remove the previous way to the found node.
					nextNode.getPath().clear();

					// Give it the path to the node we are on.
					nextNode.getPath().addAll(qe.getPath());

					// Add the new edge, so that the path leads to the new node.
					nextNode.getPath().add(lastEdge);

					// Update the cost of traveling to the new node.
					nextNode.setCost(qe.getCost() + nextNode.getCost());

					// Add to the priority queue as a potential path.
					pq.add(nextNode);

					// Add to the visited list. We have now visited this node.
					visited.add(nextNode);
				}
			}

			// Remove the visited queue elements from the element list,
			// so that they are not compared to again.
			for (DijkstraPath<E> re : visited)
				elements.remove(re);
		}
		return null;
	}

	/**
	 * Generates a list of queue element from a list of edges given from the user.
	 * All edges will be turned into queue element except the edges leading to the start.
	 * @param graph The graph containing edges to create queue elements.
	 * @param start Where the path starts.
	 * @return The list with the queue elements generated from the graph.
	 */
	private static <E extends Edge> List<DijkstraPath<E>> generateQueueElements(List<E> graph, int start) {
		List<DijkstraPath<E>> elements = new ArrayList<>();
		for (E edge : graph) {
			if (edge.to != start)
				elements.add(new DijkstraPath<>(edge.to, edge.getWeight(), edge));
		}
		return elements;
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
		cc[to].addAll(cc[from]);
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
  
