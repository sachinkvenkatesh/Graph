import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class TopologicalOrder {

	/**
	 * Method for Topological order using Queue
	 * 
	 * @param g:
	 *            Graph
	 * @return: ArrayList - containing the vertices in topological order
	 *
	 */
	static List<Vertex> topologicalOrder1(Graph g) {
		List<Vertex> topOrder = new ArrayList<>();
		// Vertices with indegree 0 are stored in the queue
		Queue<Vertex> queue = new LinkedList<>();
		int count = 0; // to keep the count of number of nodes visited
		g.calcIndegree(); // Calculating indegree of all vertices
		// Adding vertices of indegree 0 to queue
		for (Vertex u : g) {
			if (u.indegree == 0)
				queue.add(u);
		}
		Vertex u, v;
		// Remove a vertex (this will have indegree 0). Add this vertex to the
		// final topological order.
		while (!queue.isEmpty()) {
			u = queue.remove();
			topOrder.add(u);
			++count;
			// delete the edges - this is done by decrementing the indegree of
			// the vertex at the other end of the edge. if the indegree of the
			// vertex at the other end is 0, add it to the queue.
			for (Edge e : u.Adj) {
				v = e.otherEnd(u);
				v.indegree--;
				if (v.indegree == 0)
					queue.add(v);
			}
		}
		// to detect cycle - if the number of vertices != count
		if (count == g.numNodes)
			return topOrder;
		else
			return null;
	}
	
	/**
	 * Method for Topological order using DFS
	 * 
	 * @param g:
	 *            Graph
	 * @return:ArrayDeque - vertices in topological order
	 */
	static ArrayDeque<Vertex> topologicalOrder2(Graph g) {
		// true - if graph is undirected or no cycle detection in directed
		// graphs
		// false - if cycle has to be detected in directed graphs
		return Graph.DFSTop(g, false, true);
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		Scanner in;
		List<Vertex> lst;
		Deque<Vertex> lst2;
		
		if (args.length > 0) {
			File inputFile = new File(args[0]);
			in = new Scanner(inputFile);
		} else {
			in = new Scanner(System.in);
		}

		Graph g = Graph.readGraph(in, true);
		System.out.println("Topological order1");
		lst = topologicalOrder1(g);
		System.out.println(lst);

		System.out.println("Topological order2");
		lst2 = topologicalOrder2(g);
		System.out.println(lst2);
	}

}
