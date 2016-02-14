
/**
 * Class to represent a graph
 * 
 *
 */

import java.io.*;
import java.util.*;

class Graph implements Iterable<Vertex> {
	public List<Vertex> verts; // array of vertices
	public int numNodes; // number of verices in the graph

	/**
	 * Constructor for Graph
	 * 
	 * @param size
	 *            : int - number of vertices
	 */
	Graph(int size) {
		numNodes = size;
		verts = new ArrayList<>(size + 1);
		verts.add(0, null);
		// create an array of Vertex objects
		for (int i = 1; i <= size; i++)
			verts.add(i, new Vertex(i));
	}

	/**
	 * Method to add an edge to the graph
	 * 
	 * @param a
	 *            : int - one end of edge
	 * @param b
	 *            : int - other end of edge
	 * @param weight
	 *            : int - the weight of the edge
	 */
	void addEdge(int a, int b, int weight) {
		Vertex u = verts.get(a);
		Vertex v = verts.get(b);
		Edge e = new Edge(u, v, weight);
		u.Adj.add(e);
		v.Adj.add(e);
	}

	/**
	 * Method to add an arc (directed edge) to the graph
	 * 
	 * @param a
	 *            : int - the head of the arc
	 * @param b
	 *            : int - the tail of the arc
	 * @param weight
	 *            : int - the weight of the arc
	 */
	void addDirectedEdge(int a, int b, int weight) {
		Vertex head = verts.get(a);
		Vertex tail = verts.get(b);
		Edge e = new Edge(head, tail, weight);
		head.Adj.add(e);
		tail.revAdj.add(e);
	}

	/**
	 * Method to create an instance of VertexIterator
	 */
	public Iterator<Vertex> iterator() {
		return new VertexIterator();
	}

	/**
	 * A Custom Iterator Class for iterating through the vertices in a graph
	 * 
	 *
	 * @param <Vertex>
	 */
	private class VertexIterator implements Iterator<Vertex> {
		private Iterator<Vertex> it;

		/**
		 * Constructor for VertexIterator
		 * 
		 * @param v
		 *            : Array of vertices
		 * @param n
		 *            : int - Size of the graph
		 */
		private VertexIterator() {
			it = verts.iterator();
			it.next(); // Index 0 is not used. Skip it.
		}

		/**
		 * Method to check if there is any vertex left in the iteration
		 * Overrides the default hasNext() method of Iterator Class
		 */
		public boolean hasNext() {
			return it.hasNext();
		}

		/**
		 * Method to return the next Vertex object in the iteration Overrides
		 * the default next() method of Iterator Class
		 */
		public Vertex next() {
			return it.next();
		}

		/**
		 * Throws an error if a vertex is attempted to be removed
		 */
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	public static Graph readGraph(Scanner in, boolean directed) {
		// read the graph related parameters
		int n = in.nextInt(); // number of vertices in the graph
		int m = in.nextInt(); // number of edges in the graph

		// create a graph instance
		Graph g = new Graph(n);
		for (int i = 0; i < m; i++) {
			int u = in.nextInt();
			int v = in.nextInt();
			int w = in.nextInt();
			if (directed) {
				g.addDirectedEdge(u, v, w);
			} else {
				g.addEdge(u, v, w);
			}
		}
		in.close();
		return g;
	}

	void calcIndegree() {
		for (Vertex u : this) {
			u.indegree = 0;
		}

		for (Vertex u : this) {
			for (Edge e : u.Adj) {
				e.otherEnd(u).indegree++;
			}
		}
	}

	/**
	 * Method for Topological order using DFS
	 * 
	 * @param g:
	 *            Graph
	 * @param isCycleAllowed:
	 *            Boolean - to enable cycle detection
	 * @return: ArrayDeque - vertices in topological order
	 */
	static ArrayDeque<Vertex> DFSTop(Graph g, boolean isCycleAllowed, boolean isDirected) {
		ArrayDeque<Vertex> stack = new ArrayDeque<>();

		for (Vertex v : g)
			v.color = Vertex.Color.WHITE;

		for (Vertex v : g) {
			if (v.color == Vertex.Color.WHITE) {
				DFSVisit(v, stack, isCycleAllowed, isDirected);
			}
		}
		if (stack.size() == g.numNodes)
			return stack;
		else
			return null;
	}

	/**
	 * Private method for DFS
	 * 
	 * @param u:
	 *            Vertex - DFS on this vertex
	 * @param stack:
	 *            ArrayDeque<Vertex> - to store the topological order of
	 *            vertices
	 * @throws CyclicGraphException
	 *             exception to be thrown when cycle is encountered in the graph
	 */
	public static void DFSVisit(Vertex u, ArrayDeque<Vertex> stack, boolean isCycleAllowed, boolean isDirected) {
		u.color = Vertex.Color.GRAY; // vertex being processed
		for (Edge e : u.Adj) {
			Vertex v = e.otherEnd(u);
			if (v.color == Vertex.Color.WHITE) {
				v.parent = u;
				DFSVisit(v, stack, isCycleAllowed, isDirected);
			} else if (!isCycleAllowed
					&& ((isDirected && v.color == Vertex.Color.GRAY) || (!isDirected && v != u.parent))) {
				// To detect cycle in DAG
				return;
			}
		}

		u.color = Vertex.Color.BLACK;// vertex processed
		stack.push(u);
	}

	/**
	 * Method for BFS - calls another method that implements BFS from first
	 * vertex
	 * 
	 * @param g:
	 *            Graph
	 */
	public static void BFS(Graph g) {
		for (Vertex v : g)
			v.seen = false;
		for (Vertex v : g)
			if (!v.seen)
				BFS(g, v);
	}

	/**
	 * Method to implement the BFS
	 * 
	 * @param g:
	 *            Graph
	 * @param v:
	 *            vertex - start of BFS
	 */
	public static void BFS(Graph g, Vertex v) {
		Queue<Vertex> vertexVisited = new LinkedList<>();// visited vertices
		v.seen = true;
		v.distance = 0;
		vertexVisited.add(v);

		/*
		 * Remove vertex from the queue and check its adjacent vertices. Mark
		 * the adjacent vertices and add it to the queue. Repeat till the queue
		 * is empty i.e all the vertices are visited.
		 * 
		 * Distance of the vertices is the distance starting vertex v
		 */
		while (!vertexVisited.isEmpty()) {
			Vertex u = vertexVisited.remove();
			for (Edge e : u.Adj) {
				Vertex w = e.otherEnd(u);
				if (!w.seen) {
					w.seen = true;
					w.parent = u;
					w.distance = u.distance + 1;
					vertexVisited.add(w);
				}
			}
		}
	}

	/**
	 * Method to calculate the degree of a vertex in a undirected graph
	 */
	static void calDegree(Graph g) {
		for (Vertex v : g)
			v.degree = 0;
		// size of the adjacency list is the degree of the vertex in undirected
		// graph
		for (Vertex v : g) {
			v.degree = v.Adj.size();
		}
	}
}