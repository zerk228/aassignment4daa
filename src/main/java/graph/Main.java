package graph;

import graph.scc.SCC;
import graph.topo.TopoSort;
import graph.dagsp.*;
import org.json.*;
import java.nio.file.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        String json = Files.readString(Paths.get("src/main/resources/large1.json"));
        JSONObject obj = new JSONObject(json);
        int n = obj.getInt("n");
        JSONArray edges = obj.getJSONArray("edges");
        int source = obj.getInt("source");

        Graph g = new Graph(n);
        for (int i = 0; i < edges.length(); i++) {
            JSONObject e = edges.getJSONObject(i);
            g.addEdge(e.getInt("u"), e.getInt("v"), e.getInt("w"));
        }

        System.out.println("SCC");
        var scc = new SCC(g).run();
        for (int i = 0; i < scc.size(); i++)
            System.out.println("Component " + i + ": " + scc.get(i));

        System.out.println("\nTopological Sort");
        System.out.println(TopoSort.sort(g));

        System.out.println("\nShortest Paths");
        System.out.println(Arrays.toString(DAGShortestPath.shortest(g, source)));

        System.out.println("\nLongest Paths");
        System.out.println(Arrays.toString(DAGLongestPath.longest(g, source)));
    }
}