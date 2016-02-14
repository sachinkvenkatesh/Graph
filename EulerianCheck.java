import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EulerianCheck {
	
	/**
	 * Method to check whether the graph is Eulerian or not. If not to find the
	 * Eulerian path if it exists.
	 * 
	 * @param g:
	 *            Graph
	 * @throws CyclicGraphException
	 */
	static void testEulerian(Graph g) {
		int oddDegreeVertex = 0;
		List<Vertex> eulerPath = new ArrayList<>();

		// Graph is not connected if DFS doesn't return the stack having all the
		// vertices
		ArrayDeque<Vertex> stack = new ArrayDeque<>();
		Vertex u = g.verts.get(1);
		Graph.DFSVisit(u, stack, true, false);
		if (stack.size() != g.numNodes) {
			System.out.println("Graph is not connected");
			return;
		}

		// to calculate the degree of all the vertices
		Graph.calDegree(g);

		for (Vertex v : g) {
			if (v.degree % 2 != 0) { // to check if the vertex has odd degree
				eulerPath.add(v); // if it is odd degree vertex, store the path
				++oddDegreeVertex;
			}
		}

		if (oddDegreeVertex == 0) // no odd degree vertex - Eulerian Graph
			System.out.println("Graph is Eulerian");
		else if (oddDegreeVertex == 2) // 2odd degree vertices,Euler path exist
			System.out.println("Graph has an Eulerian Path between vertices " + eulerPath.get(0).name + " and "
					+ eulerPath.get(1));
		else // more than 2 odd degree vertices, no Euler path
			System.out.println("Graph is not Eulerian. It has " + oddDegreeVertex + " vertices of odd degree");
	}

	public static void main(String[] args) throws FileNotFoundException {
		Scanner in = null;
		if (args.length > 0) {
			File inputFile = new File(args[0]);
			in = new Scanner(inputFile);
		} else {
			in = new Scanner(System.in);
		}

		Graph g = Graph.readGraph(in, false);
		testEulerian(g);

	}

}
