import coloring.GraphColoringMpi;
import exception.GraphException;
import graph.ColorsGraph;
import graph.DirectedGraph;
import mpi.MPI;

public class Main {
    public static final int THREADS_NO = 5;


    public static void main(String[] args) {
        DirectedGraph graph = generateGraph();
//
        ColorsGraph colorsGraph = generateColors();
//
//        System.out.println("graph: " + graph);

//        var graphColoring = GraphColoringThreads.getGraphColoring(THREADS_NO, graph, colorsGraph);
//        System.out.println("n-coloring: " + graphColoring);

        MPI.Init(args);
//
        int me = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
//        System.out.println("Hello world from rank " + myrank + " of " + size);


        if (me == 0) { //main process
            System.out.println("MAIN");

            try {
                System.out.println(GraphColoringMpi.graphColoringMain(size, graph, colorsGraph));
            } catch (GraphException gce) {
                System.out.println(gce);
            }
        } else { //child process
            System.out.println("Process " + me);

            int codesNo = colorsGraph.getColorsNo();
            GraphColoringMpi.graphColoringWorker(me, size, graph, codesNo);
        }
        MPI.Finalize();

    }

    private static ColorsGraph generateColors() {
        ColorsGraph colorsGraph = new ColorsGraph(4);
        colorsGraph.addColor(89, "red");
        colorsGraph.addColor(11, "purple");
        colorsGraph.addColor(24, "green");
        colorsGraph.addColor(34, "black");
        return colorsGraph;
    }

    private static DirectedGraph generateGraph() {
        DirectedGraph graph = new DirectedGraph(5);
        graph.addEdge(0, 1);
        graph.addEdge(0, 4);
        graph.addEdge(3, 1);
        graph.addEdge(2, 4);
        graph.addEdge(1, 2);
        graph.addEdge(1, 0);
        graph.addEdge(4, 3);
        return graph;
    }
}
