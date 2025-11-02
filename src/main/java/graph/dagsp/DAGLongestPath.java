package graph.dagsp;
import graph.*;
import graph.topo.TopoSort;
import java.util.*;



public class DAGLongestPath {
    public static double[] longest(Graph g, int src) {
        List<Integer> topo = TopoSort.sort(g);
        double[] dist = new double[g.n];
        Arrays.fill(dist, Double.NEGATIVE_INFINITY);
        dist[src] = 0;
        for (int u : topo)
            if (dist[u] != Double.NEGATIVE_INFINITY)
                for (Edge e : g.adj.get(u))
                    dist[e.to] = Math.max(dist[e.to], dist[u] + e.weight);
        return dist;
    }
}