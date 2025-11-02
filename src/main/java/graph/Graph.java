package graph;
import java.util.*;



public class Graph {
    public final int n;
    public final List<List<Edge>> adj;

    public Graph(int n) {
        this.n = n;
        adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
    }

    public void addEdge(int u, int v, int w) {
        adj.get(u).add(new Edge(u, v, w));
    }

    public Graph transpose() {
        Graph g = new Graph(n);
        for (int u = 0; u < n; u++)
            for (Edge e : adj.get(u))
                g.addEdge(e.to, e.from, e.weight);
        return g;
    }
}