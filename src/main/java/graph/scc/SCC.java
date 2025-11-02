package graph.scc;
import graph.*;
import java.util.*;


public class SCC {
    private final Graph g;
    private boolean[] vis;
    private Deque<Integer> order = new ArrayDeque<>();
    public List<List<Integer>> components = new ArrayList<>();

    public SCC(Graph g) {
        this.g = g; }

    public List<List<Integer>> run() {
        vis = new boolean[g.n];
        for (int i = 0; i < g.n; i++)
            if (!vis[i]) dfs1(i);

        Graph gt = g.transpose();
        Arrays.fill(vis, false);
        while (!order.isEmpty()) {
            int v = order.pop();
            if (!vis[v]) {
                List<Integer> comp = new ArrayList<>();
                dfs2(gt, v, comp);
                components.add(comp);
            }
        }
        return components;
    }


    private void dfs1(int vertex) {
        vis[vertex] = true;

        for (Edge edge : g.adj.get(vertex)) {
            int next = edge.to;
            if (!vis[next]) {
                dfs1(next);
            }
        }


        order.push(vertex);
    }


    private void dfs2(Graph transposedGraph, int vertex, List<Integer> component) {
        vis[vertex] = true;
        component.add(vertex);

        for (Edge edge : transposedGraph.adj.get(vertex)) {
            int next = edge.to;
            if (!vis[next]) {
                dfs2(transposedGraph, next, component);
            }
        }
    }

}