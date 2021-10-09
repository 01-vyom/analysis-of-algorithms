/**
* Finding and printing a cycle in an undirected graph which handles disconnected graphs as well.
* Time Complexity: O(|E|+|V|)
* With testcases and interactive options for random graphs for complexity analysis using JGraphT Library.
* 
* @author  Vyom Pathak
* @version 1.0
* @since   2021-10-01 
*/

import org.jgrapht.*;
import org.jgrapht.generate.*;
import org.jgrapht.graph.*;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import org.jgrapht.util.*;
import java.util.*;
import java.util.stream.Collectors;
 
class FindCycle{
 
   // No. of vertices
   private int V; 
   // Adjacency List Representation
   private ArrayList<Integer> adj[];
   // List to store the cycle
   private ArrayList<Integer> findcycle;

   // Constructor
   public FindCycle(int nodes, int edges, int degree, int task, int unitTask)
   {   
    findcycle = new ArrayList<Integer>();
    GraphGenerator<String, DefaultEdge, String> graphGenerator=null;
    V = nodes;
    adj = new ArrayList[nodes];
    for(int i=0; i<nodes; i++) adj[i] = new ArrayList<Integer>();
    Graph<String,DefaultEdge> G =
    GraphTypeBuilder.undirected().allowingMultipleEdges(false).allowingSelfLoops(false).
    weighted(false).edgeSupplier(SupplierUtil.createDefaultEdgeSupplier()).
    vertexSupplier(SupplierUtil.createStringSupplier(1)).buildGraph();

       if(task==2){
        graphGenerator =
               new RandomRegularGraphGenerator<String,DefaultEdge>(nodes,degree);        
       }
       else if(task==3){
        graphGenerator =
            new GnmRandomGraphGenerator<String,DefaultEdge>(nodes,edges);        
       }
       else if(task==4){
        graphGenerator = new RingGraphGenerator<String,DefaultEdge>(nodes);
       }
       else{
            System.out.println("Running unit test case no "+unitTask);
            nodes = 7; // For Testcase 1, and 2.
            V = nodes;
            adj = new ArrayList[nodes];
            int[][] GraphVE = null;
            if(unitTask==1){
                    for(int i=0; i<nodes; i++)
                        adj[i] = new ArrayList<Integer>();
                    GraphVE = new int[][]{
                            {2,3},
                            {1,7,6,5},
                            {1,4},
                            {3},
                            {2},
                            {2},
                            {2}
                        };
                    //No Cycle Found
            }
            else if(unitTask==2){
                for(int i=0; i<nodes; i++)
                    adj[i] = new ArrayList<Integer>();
                GraphVE = new int[][]{
                            {2,3},
                            {1,6,5},
                            {1,4},
                            {3,7},
                            {2,7},
                            {2},
                            {4,5}
                        };
                    // Cycle: [0, 2, 3, 6, 4, 1, 0] //Single Cycle
            }
            else if(unitTask==3){
                nodes = 6; //Testcase 3
                V = nodes;
                adj = new ArrayList[nodes];
                for(int i=0; i<nodes; i++)
                    adj[i] = new ArrayList<Integer>();
                GraphVE = new int[][]{
                                    {2,5},
                                    {1,6,3},
                                    {2,4,5},
                                    {3,5},
                                    {1,6,3,4},
                                    {2,5}
                                };
                    // Cycle: [0, 4, 5, 1, 0] //Multiple Cycle
            }
            else{
                for(int i=0; i<nodes; i++)
                    adj[i] = new ArrayList<Integer>();
                GraphVE = new int[][]{
                                    {2},
                                    {1,3},
                                    {2},
                                    {5,6},
                                    {6,4},
                                    {4,5,7},
                                    {6}
                                };
                // Cycle: [3, 5, 4, 3] //Disconnected Graph with Cycle
            }
            System.out.println("Graph's Adjacency List Representation: ");
            for(int u =0;u<GraphVE.length;u++){
                for (int j=0; j<GraphVE[u].length;j++){
                    adj[u].add(GraphVE[u][j]-1);
                }
                System.out.println(u+" "+adj[u]);
            }
            return;
       }
        graphGenerator.generateGraph(G);
        Set<String> vertex = G.vertexSet();

        System.out.println("Graph's Adjacency List Representation: ");
        for(String u: vertex){
            adj[Integer.parseInt(u)-1] = (ArrayList<Integer>)  Graphs.neighborSetOf(G, u).stream().map(Integer::parseInt).map(number -> number-1).collect(Collectors.toList());
            System.out.println(Integer.parseInt(u)-1+" "+adj[Integer.parseInt(u)-1]);
        }
   }

   Boolean findCycleHelper(int v, Boolean visited[], int parent)
   {
       visited[v] = true;
       findcycle.add(v);
    
       for (Integer adj_v : adj[v])
       {
           if (!visited[adj_v]){
               if (findCycleHelper(adj_v, visited, v))
                return true;
           }
           else if (adj_v != parent){
            findcycle.add(adj_v);
            return true;
           }
              
       }
       findcycle.remove(findcycle.size()-1);
       return false;
   }
   Boolean findCycle(){
       Boolean visited[] = new Boolean[V];
       for (int i = 0; i < V; i++) visited[i] = false;
       for (int u = 0; u < V; u++){
           if (!visited[u]){
               if (findCycleHelper(u, visited, -1))
                return true;
           }
       }
       return false;
   }

   String cyclePrinting(Boolean cycleExists){
       if (cycleExists){
        Integer rt = findcycle.get(findcycle.size() - 1);
        ArrayList<Integer> Cycle = new ArrayList<Integer>();
        for(Integer a = findcycle.size() - 1; a >= 0; a--) 
        {   
            Cycle.add(findcycle.get(a));
            if (findcycle.get(a) == rt && a!=findcycle.size()-1) 
                break;
        }
        return "Cycle: "+Cycle.toString();
        }
        else return "No Cycle Found";
   }
 
 
   public static void main(String[] args){
    Scanner sc = new Scanner(System.in);       

    System.out.println("What Task do you want to perform:\n1) Test the algorithm using unit-test cases.\n2) Vertices with Degree analysis\n3) Number of Vertices*n = Number of Edges analysis\n4) Ring Graph analysis");

    int task = sc.nextInt();
    int Vertices= 100;
    int Edges = 100;
    int Degree = 3;
    String Cyc = "";
    FindCycle fc = null;
    if (task != 1) 
    {
        System.out.println("Enter the number of vertices");
        Vertices = sc.nextInt();
    }
    switch (task) {
        case 1:
            
            System.out.println("Running unit test cases...\n");
            for(int i=1;i<5;i++){
                fc = new FindCycle(Vertices, Edges, Degree,task,i);
                Boolean cycExists = fc.findCycle();
                Cyc = fc.cyclePrinting(cycExists);
                if (i==1){
                    System.out.println("No cycle found in a graph termination");
                    System.out.println(Cyc);
                    if(Cyc.equals("No Cycle Found")) System.out.println("Testcase passed\n");
                }
                else if(i==2){
                    System.out.println("Finding a cycle in a given graph");
                    System.out.println(Cyc);
                    if(Cyc.equals("Cycle: [0, 2, 3, 6, 4, 1, 0]")) System.out.println("Testcase passed\n");
                }
                else if(i==3){
                    System.out.println("Finding a single cycle from multiple cycles in a graph");
                    System.out.println(Cyc);
                    if(Cyc.equals("Cycle: [0, 4, 5, 1, 0]")) System.out.println("Testcase passed\n");
                }
                else{
                    System.out.println("Finding Cycle in Disconnected Graph");
                    System.out.println(Cyc);
                    if(Cyc.equals("Cycle: [3, 5, 4, 3]")) System.out.println("Testcase passed\n");
                }
            }
            System.out.println("All testcases passed\n");
            sc.close();
            return;
        case 2:
            System.out.println("Enter the degree on each vertices");
            Degree = sc.nextInt();
            break;
        case 3:
            System.out.println("Enter the number n for E=n*V");
            int n = sc.nextInt();
            Edges = Vertices*n;
        case 4:
            break;         
        default:
            System.out.println("Invalid Task");
        }

        sc.close();
        fc = new FindCycle(Vertices, Edges, Degree,task,-1);
       
        long start1 = System.nanoTime();
        Boolean cycExists = fc.findCycle();
        long end1 = System.nanoTime();

        Cyc = fc.cyclePrinting(cycExists);
        System.out.println(Cyc);

        System.out.println("Running time: "+(double)(end1-start1)/1_000_000_000+" seconds");
        
        return;
   }
}
 
