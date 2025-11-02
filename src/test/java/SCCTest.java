import graph.*;
import graph.scc.SCC;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SCCTest {
    @Test
    void testSimpleCycle() {
        Graph g = new Graph(3);
        g.addEdge(0,1,1);
        g.addEdge(1,2,1);
        g.addEdge(2,0,1);
        SCC scc = new SCC(g);
        var comps = scc.run();
        assertEquals(1, comps.size());
    }
}

