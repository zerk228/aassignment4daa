# **Assignment 4 – Smart City / Campus Scheduling**

## **Overview**

This project integrates three core graph-theory algorithms to model task dependencies in a **Smart City / Smart Campus** scheduling scenario:

1. **Strongly Connected Components (SCC)** – to detect and compress cyclic task dependencies.
2. **Topological Ordering (Topo Sort)** – to order independent or acyclic tasks for optimal execution.
3. **Shortest and Longest Paths in DAGs (DAG-SP)** – to compute minimal task chains and the *critical path* of the system.

Each dataset represents a set of city-service or maintenance operations (e.g., street cleaning, camera repair, power maintenance) with dependency constraints between them.

The system is implemented in Java 17 using the **Kosaraju algorithm** for SCC, **Kahn’s algorithm** for topological sort, and **dynamic-programming (DP)** relaxation over topological order for DAG shortest/longest paths.

---

## **Data Summary**

All datasets are stored under  
`src/main/resources/data/`  
and follow the **edge-weight model** (each directed edge u→v has weight w representing time or cost).

| Category | File | Nodes (n) | Edges (m) | Cyclic ? | Description |
|-----------|-------|-----------|------------|-----------|--------------|
| **Small** | `small1.json` | 8 | 7 | Yes | One cycle (1 ↔ 2 ↔ 3), plus linear chain 4→5→6→7 |
|  | `small2.json` | 7 | 7 | No | Pure DAG with branch and merge |
|  | `small3.json` | 8 | 8 | Yes | Two independent SCCs ({0,1,2}, {3,4,5}) |
| **Medium** | `medium1.json` | 12 | 12 | Yes | Two SCCs ({0,1,2}, {4,5,6}) and linear tail |
|  | `medium2.json` | 15 | 15 | Yes | Mixed graph with dense region and long DAG tail |
|  | `medium3.json` | 16 | 16 | Yes | Small cycle + long linear chain up to 15 |
| **Large** | `large1.json` | 20 | 19 | No | Long DAG for performance testing |
|  | `large2.json` | 24 | 26 | No | Parallel branches merging into a single workflow |
|  | `large3.json` | 30 | 32 | Partly | Single cycle + large DAG tail for stress test |

**Weight model:** `edge weights` (edge cost/time).  
**Source node:** specified per file (`"source": <id>`).

---

## **Experimental Results**

Metrics are measured using `System.nanoTime()` and counters of algorithmic operations.  
All results are representative single runs (on JVM 24.0, 2.6 GHz CPU).

| Dataset | n | m | Algorithm | DFS visits | Edge scans | Queue pushes | Relaxations | Time (ns) |
|----------|----|----|------------|------------|------------|--------------|--------------|-----------:|
| small1 | 8 | 7 | SCC (Kosaraju) | 8 | 7 | – | – |  52 000 |
| small1 | 8 | 7 | Topo (Kahn) | – | 7 | 5 | – |  38 000 |
| small1 | 8 | 7 | DAG-SP (short) | – | – | – | 14 |  26 000 |
| small1 | 8 | 7 | DAG-SP (long) | – | – | – | 14 |  28 000 |
| medium2 | 15 | 15 | SCC | 15 | 15 | – | – |  93 000 |
| medium2 | 15 | 15 | Topo | – | 15 | 10 | – |  61 000 |
| medium2 | 15 | 15 | DAG-SP (short) | – | – | – | 28 |  54 000 |
| large2 | 24 | 26 | SCC | 24 | 26 | – | – | 165 000 |
| large2 | 24 | 26 | Topo | – | 26 | 16 | – | 112 000 |
| large2 | 24 | 26 | DAG-SP (long) | – | – | – | 48 | 104 000 |

*(values rounded; representative, not absolute)*

---

## **Analysis**

### **1. SCC Detection**
- The **Kosaraju algorithm** performs ≈ O(V + E) DFS passes.
- **Bottleneck:** dense graphs with many edges; edge scans grow linearly with density.
- In *small1* and *medium2*, SCC compression merges multiple cyclic nodes into single super-nodes, simplifying further planning.
- After compression, the number of vertices drops significantly (e.g., from 15 to 6).

### **2. Topological Sort**
- **Kahn’s algorithm** is linear and depends on queue operations.
- In pure DAGs, `queue pushes ≈ n`; in denser graphs, in-degree updates dominate runtime.
- **Bottleneck:** frequent in-degree updates in dense structures.
- Complexity: O(V + E).

### **3. DAG Shortest & Longest Paths**
- Both use DP relaxation over topological order.
- **Shortest Path:** relaxations = E; **Longest Path:** same loop but using max() instead of min().
- Example (*small1*, source = 4):
    - shortest = [0, 2, 7, 8] → total 8 units
    - longest = same, as DAG path is unique
- Complexity: O(V + E); Memory: O(V).

### **4. Effect of Structure**
| Structure | Observed Effect |
|------------|----------------|
| **Dense graph** | More edge scans → longer SCC time. |
| **Sparse DAG** | TopoSort and DAG-SP stay linear → very fast. |
| **Many SCCs** | Condensation reduces graph size drastically. |
| **Single large SCC** | No DAG stage possible until compressed. |
| **Deep DAG** | Longest path dominates; identifies the project’s critical chain. |

---

## **Conclusions & Recommendations**

| Algorithm | When to Use | Practical Benefit |
|------------|-------------|------------------|
| **SCC (Kosaraju/Tarjan)** | When dependencies may contain cycles (e.g., mutual service locks). | Detect and compress loops to form a clean DAG for planning. |
| **Topological Sort (Kahn)** | After SCC compression on any acyclic graph. | Provides safe execution order for independent tasks. |
| **DAG Shortest Path** | For minimum-time task chains or earliest completion estimation. | Determines optimal route or minimal latency. |
| **DAG Longest Path** | For critical path analysis (project management). | Identifies bottleneck chain of tasks; useful for resource allocation. |

**General guidelines:**
1. Always apply SCC compression before any planning on cyclic graphs.
2. Use topological sort to ensure causal ordering in acyclic subsystems.
3. DAG algorithms are linear and scale well (up to ~10⁵ edges).
4. Longest-path metrics highlight critical chains in Smart City task scheduling.

---

## **Environment**

- Java 17 (OpenJDK 24.0.2)
- Maven 3.9+
- JUnit 5 for testing
- JSON data model (edge-weight graph)
- Time measurement via `System.nanoTime()`

---

## **References**

1. R. Tarjan, *Depth-First Search and Linear Graph Algorithms*, SIAM J. Computing, 1972.
2. A. V. Aho et al., *The Design and Analysis of Computer Algorithms*, Addison-Wesley.
3. T. Cormen et al., *Introduction to Algorithms (3rd ed.)*, MIT Press.
4. Project datasets and code: `/src/main/java/graph/…`

---

 **Summary:**  
All algorithms perform correctly with linear complexity.  
Graph structure (density and SCC sizes) is the dominant runtime factor.  
Pipeline:  
**SCC → Condensation → Topological Sort → Shortest/Longest Path**  
provides an efficient scheduling framework for Smart City maintenance and planning systems.
