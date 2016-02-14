import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.Scanner;

public class StrglyConnectedComps {

	/**
	 * To calculate the number of strongly connected components in the given
	 * Graph.
	 * 
	 * @param g:
	 *            Graph
	 * @return: Number of Strongly Connected Components if exists else return -1.
	 * @throws CyclicGraphException
	 */
	static int stronglyConnectedComponents(Graph g) {
		// indicates that the graph is not connected
		ArrayDeque<Vertex> stack = new ArrayDeque<>();
		Vertex v = g.verts.get(1);
		Graph.DFSVisit(v, stack, true, true);
		if (stack.size() != g.numNodes) {
			return -1;
		}

		for (Vertex u : g)
			u.seen = false;

		Vertex u;
		int cno = 0;
		// loop through the vertices in stack order obtained from the running
		// DFS on Graph
		while (!stack.isEmpty()) {
			u = stack.pop();
			if (!u.seen)
				DFSVisit(u, ++cno);
		}
		return cno;
	}
	
	/**
	 * DFSVisit to calculate the number of Strongly Connected Components. Here
	 * DFS on Graph with reverse edges.
	 * 
	 * @param u:
	 *            Vertex - DFS on this Vertex
	 * @param cno:
	 *            To store the number of strongly connected graphs
	 */
	static void DFSVisit(Vertex u, int cno) {
		u.seen = true;
		u.cno = cno;

		for (Edge e : u.revAdj) {
			Vertex v = e.otherEnd(u);
			if (!v.seen)
				DFSVisit(v, cno);
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		Scanner in;
		if (args.length > 0) {
			File inputFile = new File(args[0]);
			in = new Scanner(inputFile);
		} else {
			in = new Scanner(System.in);
		}
		
		Graph g = Graph.readGraph(in, true);
		int scc = stronglyConnectedComponents(g);
		if (scc == -1)
			System.out.println("Ggraph is not connected");
		else
			System.out.println("No. of Strongly Connected Components: " + scc);
	}
}
