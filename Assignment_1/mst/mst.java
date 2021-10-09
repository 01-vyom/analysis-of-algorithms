
/**
* Building a Minimum Spanning Tree in an undirected sparse graph [|E|=O(|V|+8)].
* Time Complexity: O(|E|)
* With testcases and interactive options for random graphs for complexity analysis using JGraphT Library.
* 
* @author  Vyom Pathak
* @version 1.0
* @since   2021-10-05
*/

import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.SpanningTreeAlgorithm.SpanningTree;
import org.jgrapht.alg.spanning.KruskalMinimumSpanningTree;

import java.util.*;

import org.jgrapht.*;
import org.jgrapht.generate.*;
import org.jgrapht.graph.*;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import org.jgrapht.util.*;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

class Key {

    private final int x;
    private final int y;

    public Key(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Key))
            return false;
        Key key = (Key) o;
        return x == key.x && y == key.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString() {
        return String.valueOf(x) + "," + String.valueOf(y);
    }

}

class MST {

    // Cycle
    private int V;
    private ArrayList<Integer> adj[];
    private ArrayList<Integer> findcycle;
    Graph<String, DefaultEdge> g;
    Map<Key, Double> edge_map;
    // Cycle

    // Cycle
    Boolean findCycleHelper(int v, Boolean visited[], int parent) {
        visited[v] = true;
        findcycle.add(v);

        for (Integer adj_v : adj[v]) {
            if (!visited[adj_v]) {
                if (findCycleHelper(adj_v, visited, v))
                    return true;
            } else if (adj_v != parent) {
                findcycle.add(adj_v);
                return true;
            }

        }
        findcycle.remove(findcycle.size() - 1);
        return false;
    }

    void removeCycle() {
        Boolean visited[] = new Boolean[V];
        for (int u = 0; u < V; u++)
            visited[u] = false;
        Boolean flg = false;
        for (int u = 0; u < V; u++) {
            if (!visited[u]) {
                if (findCycleHelper(u, visited, -1)) {
                    flg = true;
                    break;
                }
            }
        }
        if (flg) {
            double max = -1E29;
            Integer rt = findcycle.get(findcycle.size() - 1);
            ArrayList<Integer> Cycle = new ArrayList<Integer>();
            int[] mx_ky = { 0, 0 };
            for (Integer a = findcycle.size() - 1; a >= 0; a--) {
                Cycle.add(findcycle.get(a));
                if (findcycle.get(a) == rt && a != findcycle.size() - 1)
                    break;
            }
            // System.out.print(Cycle.toString());
            for (int i = 0; i < Cycle.size() - 1; i++) {
                Key tp_ky_o1 = new Key(Cycle.get(i), Cycle.get(i + 1));
                Key tp_ky_o2 = new Key(Cycle.get(i + 1), Cycle.get(i));
                if (edge_map.containsKey(tp_ky_o1)) {
                    if (max < edge_map.get(tp_ky_o1)) {
                        max = edge_map.get(tp_ky_o1);
                        mx_ky[0] = Cycle.get(i);
                        mx_ky[1] = Cycle.get(i + 1);
                    }
                } else if (edge_map.containsKey(tp_ky_o2)) {
                    if (max < edge_map.get(tp_ky_o2)) {
                        max = edge_map.get(tp_ky_o2);
                        mx_ky[0] = Cycle.get(i + 1);
                        mx_ky[1] = Cycle.get(i);
                    }
                }
            }
            // System.out.println(mx_ky[0] + " " + mx_ky[1]);
            adj[mx_ky[0]].remove(Integer.valueOf(mx_ky[1]));
            adj[mx_ky[1]].remove(Integer.valueOf(mx_ky[0]));
            edge_map.remove(new Key(mx_ky[0], mx_ky[1]));
        }

    }

    public double displayMST() {
        double Cost = 0.0;
        int cnt = 0;
        System.out.println("Edges of Minimum Spanning Tree: ");
        for (Map.Entry<Key, Double> set : edge_map.entrySet()) {
            Key tp_ky = set.getKey();
            System.out.print("[" + (tp_ky.getX() + 1) + "-" + (tp_ky.getY() + 1) + "]->(" + set.getValue() + ") ");
            Cost += set.getValue();
            cnt += 1;
        }
        System.out.println("\nCost of Minimum Spanning Tree: " + Cost);
        return Cost;
    }
    // Cycle

    public MST(int nodes, int edges, int[] weight_range, int task) {
        V = nodes;
        edge_map = new HashMap<Key, Double>();
        // Cycle
        findcycle = new ArrayList<Integer>();
        // Cycle

        g = GraphTypeBuilder.undirected().allowingMultipleEdges(false).allowingSelfLoops(false).weighted(true)
                .edgeClass(DefaultEdge.class).vertexSupplier(SupplierUtil.createStringSupplier(1)).buildGraph();
        if (task == 1) {
            nodes = 7;
            edges = 8;
            V = nodes;
            adj = new ArrayList[nodes];
            int[][] GraphVE = null;
            for (int i = 0; i < nodes; i++)
                adj[i] = new ArrayList<Integer>();
            GraphVE = new int[][] { { 2, 5 }, { 1, 6, 3 }, { 2, 4, 5 }, { 3, 5 }, { 1, 6, 3, 4 }, { 2, 5 } };
            System.out.println("\nGraph's Adjacency List Representation: ");
            System.out
                    .println(Arrays.deepToString(GraphVE).replace("], ", "]\n").replace("[[", "[").replace("]]", "]"));
            for (int u = 0; u < GraphVE.length; u++) {
                for (int j = 0; j < GraphVE[u].length; j++) {
                    adj[u].add(GraphVE[u][j] - 1);
                }
            }
            edge_map.put(new Key(0, 1), 1.0);
            edge_map.put(new Key(0, 4), 2.0);
            edge_map.put(new Key(1, 2), 3.0);
            edge_map.put(new Key(1, 5), 10.0);
            edge_map.put(new Key(4, 5), 5.0);
            edge_map.put(new Key(2, 4), 15.0);
            edge_map.put(new Key(3, 4), 3.0);
            edge_map.put(new Key(2, 3), 20.0);
            System.out.println("Graph Edge Weights: ");
            for (Map.Entry<Key, Double> set : edge_map.entrySet()) {
                Key tp_ky = set.getKey();
                System.out
                        .println("[" + (tp_ky.getX() + 1) + "-" + (tp_ky.getY() + 1) + "]->(" + set.getValue() + ") ");
            }
            System.out.println("");
            return;
        } else if (task == 2) {
            adj = new ArrayList[nodes];
            for (int i = 0; i < nodes; i++)
                adj[i] = new ArrayList<Integer>();
            LinearGraphGenerator<String, DefaultEdge> graphGenerator = new LinearGraphGenerator<String, DefaultEdge>(
                    nodes); // Vertex
            graphGenerator.generateGraph(g);

            for (int edges_add = 0; edges_add < edges - nodes + 1; edges_add++) {
                String x, y;
                do {
                    x = String.valueOf(ThreadLocalRandom.current().nextInt(1, nodes / 2));
                    y = String.valueOf(ThreadLocalRandom.current().nextInt((nodes / 2) + 1, nodes));
                } while (g.containsEdge(x, y));
                g.addEdge(x, y, new DefaultEdge());
                g.setEdgeWeight(x, y, 0.0);
            }

            List<Double> weights = new ArrayList<Double>();
            for (int i = 0; i < edges; i++)
                weights.add((double) ThreadLocalRandom.current().nextInt(weight_range[0], weight_range[1]));

            Collections.shuffle(weights);
            Set<String> vertex = g.vertexSet();
            Set<DefaultEdge> edge = g.edgeSet();

            System.out.println("\nGraph's Adjacency List Representation: ");
            for (String v : vertex) {
                System.out.println(v + " " + Graphs.successorListOf(g, v));

                // Cycle
                adj[Integer.parseInt(v) - 1] = (ArrayList<Integer>) Graphs.neighborListOf(g, v).stream()
                        .map(Integer::parseInt).map(number -> number - 1).collect(Collectors.toList());
                // Cycle
            }
            System.out.println("");
            int i = 0;

            System.out.println("Graph Edge Weights: ");
            for (DefaultEdge e : edge) {
                g.setEdgeWeight(e, weights.get(i));
                i += 1;
                Key tp_ky = new Key(Integer.parseInt(g.getEdgeSource(e)) - 1, Integer.parseInt(g.getEdgeTarget(e)) - 1);
                edge_map.put(tp_ky, g.getEdgeWeight(e));
                System.out.println(
                        "[" + g.getEdgeSource(e) + "-" + g.getEdgeTarget(e) + "]->(" + g.getEdgeWeight(e) + ") ");
            }

            /**
             * Uncomment this to compare the output of the MST with inbuilt Kruskhal MST
             * System.out.println(""); KruskalMinimumSpanningTree<String, DefaultEdge> kmst
             * = new KruskalMinimumSpanningTree<String, DefaultEdge>( g);
             * SpanningTree<DefaultEdge> ST = kmst.getSpanningTree();
             * System.out.println("Edges of Minimum Spanning Tree: "); int cnt = 0; for
             * (DefaultEdge e : ST.getEdges()) { System.out.print( "[" + g.getEdgeSource(e)
             * + "-" + g.getEdgeTarget(e) + "]->(" + g.getEdgeWeight(e) + ") "); cnt += 1; }
             * System.out.println("\nCost of Minimum Spanning Tree: " + ST.getWeight());
             */
        }
        return;
    };

    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);

        System.out.println(
                "What Task do you want to perform:\n1) Test the algorithm using unit-test cases.\n2) Edges and Weight analysis\n");

        int task = sc.nextInt();

        int Vertices = 9000;
        int randnum = ThreadLocalRandom.current().nextInt(0, 8 + 1); // At-Most 8 edges
        int Edges = Vertices + randnum;
        int[] weight_range = new int[] { 100, 500 };
        MST mst = null;
        double Cost = 0.0;

        switch (task) {
            case 1:
                System.out.println("Running unit test cases...\n");
                mst = new MST(Vertices, Edges, weight_range, task);
                for (int times = 0; times < 2; times++)
                    mst.removeCycle();

                System.out.println("MST Unit-Test Case");
                Cost = mst.displayMST();
                if (Cost == 29.0)
                    System.out.println("Testcase passed\n");
                sc.close();
                return;
            case 2:
                System.out.println("Enter the number of vertices");
                Vertices = sc.nextInt();
                System.out.println("Enter the number of edges in the range of |V| and |V|+8");
                Edges = sc.nextInt();
                System.out.println("Enter a large weight range separated by spaces");
                weight_range[0] = sc.nextInt();
                weight_range[1] = sc.nextInt();
                System.out.println(Vertices + " " + weight_range[0] + " " + weight_range[1]);
                break;
            default:
                System.out.println("Invalid Task");
        }
        sc.close();

        mst = new MST(Vertices, Edges, weight_range, task);

        long start1 = System.nanoTime();
        for (int times = 0; times < Edges - Vertices + 1; times++)
            mst.removeCycle();
        long end1 = System.nanoTime();
        mst.displayMST();

        System.out.println("Running time: " + (double) (end1 - start1) / 1_000_000_000 + " seconds");

    }

}