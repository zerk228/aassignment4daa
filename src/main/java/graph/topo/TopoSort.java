package graph.topo;

import graph.*;
import java.util.*;

public class TopoSort {
    public static List<Integer> sort(Graph g) {
        int[] indeg = new int[g.n];
        for (var edges : g.adj)
            for (Edge e : edges)
                indeg[e.to]++;

        Queue<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < g.n; i++)
            if (indeg[i] == 0) q.add(i);

        List<Integer> order = new ArrayList<>();
        while (!q.isEmpty()) {
            int v = q.poll();
            order.add(v);
            for (Edge e : g.adj.get(v))
                if (--indeg[e.to] == 0) q.add(e.to);
        }
        return order;
    }
}