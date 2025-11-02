package graph.dagsp;
import graph.*;
import graph.topo.TopoSort;
import java.util.*;

public class DAGShortestPath {
    public static double[] shortest(Graph g, int src) {
        List<Integer> topo = TopoSort.sort(g);
        double[] dist = new double[g.n];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        dist[src] = 0;
        for (int u : topo)
            if (dist[u] != Double.POSITIVE_INFINITY)
                for (Edge e : g.adj.get(u))
                    dist[e.to] = Math.min(dist[e.to], dist[u] + e.weight);
        return dist;
    }
}