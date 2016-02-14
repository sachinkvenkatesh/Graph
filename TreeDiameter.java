import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.Scanner;

public class TreeDiameter {

	/**
	 * Method to find diameter of a tree
	 * 
	 * @param t:
	 *            Graph
	 * @return: Diameter of the tree
	 * @throws CyclicGraphException
	 */
	static int diameter(Graph t) {
		// Graph is not connected if DFS doesn't return the stack having all the
		// vertices - implies graph is not a tree
		ArrayDeque<Vertex> stack = new ArrayDeque<>();
		Vertex u = t.verts.get(1);
		Graph.DFSVisit(u, stack, false, false);
		if (stack.size() != t.numNodes) {
			return -1;
		}

		Graph.BFS(t);
		Vertex z = maxDisVertex(t);
		for (Vertex v : t)
			v.seen = false;
		Graph.BFS(t, z);
		z = maxDisVertex(t);
		return z.distance;
	}
	
	/**
	 * Internal method to find a vertex with maximum distance
	 * 
	 * @param g:
	 *            Graph
	 * @return: Vertex - vertex with maximum(farthest) distance
	 */
	private static Vertex maxDisVertex(Graph g) {
		Vertex u = new Vertex(0);
		for (Vertex v : g)
			if (v.distance > u.distance)
				u = v;
		return u;
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		Scanner in;
		if (args.length > 0) {
			File inputFile = new File(args[0]);
			in = new Scanner(inputFile);
		} else {
			in = new Scanner(System.in);
		}
		
		Graph g = Graph.readGraph(in, false);
		int d = diameter(g);
		if (d == -1)
			System.out.println("Input is not a Tree");
		else
			System.out.println("The Diameter of the tree: " + d);

	}

}
