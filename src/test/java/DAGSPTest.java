import graph.*;
import graph.dagsp.DAGLongestPath;
import graph.dagsp.DAGShortestPath;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;



public class DAGSPTest {
    @Test
    void testShortestAndLongest() {
        Graph g = new Graph(4);
        g.addEdge(0,1,1);
        g.addEdge(1,2,2);
        g.addEdge(0,2,5);
        double[] s = DAGShortestPath.shortest(g,0);
        double[] l = DAGLongestPath.longest(g,0);
        assertEquals(3.0, s[2]);
        assertEquals(5.0, l[2]);
    }
}

