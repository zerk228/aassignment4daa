import graph.*;
import graph.topo.TopoSort;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;


public class TopoTest {
    @Test
    void testTopoOrder() {
        Graph g = new Graph(4);
        g.addEdge(0,1,1);
        g.addEdge(1,2,1);
        g.addEdge(0,3,1);
        var order = TopoSort.sort(g);
        assertTrue(order.indexOf(0) < order.indexOf(1));
        assertTrue(order.indexOf(1) < order.indexOf(2));
    }
}

