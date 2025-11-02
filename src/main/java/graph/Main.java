package graph;

import graph.scc.SCC;
import graph.topo.TopoSort;
import graph.dagsp.*;
import org.json.*;
import java.nio.file.*;
import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Path dir = Paths.get("src/main/resources");
        var files = Files.list(dir)
                .filter(p -> p.toString().endsWith(".json") && !p.getFileName().toString().contains("_output"))
                .toList();

        for (Path input : files) {
            System.out.println("\n=== Processing " + input.getFileName() + " ===");

            String json = Files.readString(input);
            JSONObject obj = new JSONObject(json);
            int n = obj.getInt("n");
            JSONArray edges = obj.getJSONArray("edges");
            int source = obj.getInt("source");

            Graph g = new Graph(n);
            for (int i = 0; i < edges.length(); i++) {
                JSONObject e = edges.getJSONObject(i);
                g.addEdge(e.getInt("u"), e.getInt("v"), e.getInt("w"));
            }

            var scc = new SCC(g).run();
            var topo = TopoSort.sort(g);
            var shortest = DAGShortestPath.shortest(g, source);
            var longest = DAGLongestPath.longest(g, source);

            JSONObject output = new JSONObject();
            JSONArray sccArray = new JSONArray();
            for (var comp : scc) sccArray.put(new JSONArray(comp));
            output.put("SCC", sccArray);
            output.put("topoSort", new JSONArray(topo));

            JSONArray shortestArray = new JSONArray();
            for (double v : shortest)
                shortestArray.put(Double.isFinite(v) ? v : JSONObject.NULL);
            output.put("shortestPath", shortestArray);

            JSONArray longestArray = new JSONArray();
            for (double v : longest)
                longestArray.put(Double.isFinite(v) ? v : JSONObject.NULL);
            output.put("longestPath", longestArray);

            String outputFile = input.toString().replace(".json", "_output.json");
            Files.writeString(Paths.get(outputFile), output.toString(2));

            System.out.println(output.toString(2));
            System.out.println("Saved to: " + outputFile);
        }
    }
}
