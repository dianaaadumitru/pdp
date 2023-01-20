import coloring.GraphColoringMpi;
import exception.GraphException;
import graph.ColorsGraph;
import graph.DirectedGraph;
import mpi.MPI;
import utils.Generator;

public class MainMPI {
    public static void main(String[] args) {
        DirectedGraph graph = Generator.generateGraph();
        ColorsGraph colorsGraph = Generator.generateColors();

        MPI.Init(args);

        int me = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        if (size < 2) {
            System.out.println("Need at least 2 MPI tasks. Quitting...");
            return;
        }

        if (me == 0) { //main process
            System.out.println("MAIN");
            System.out.println("graph: " + graph);

            try {
                long startTime = System.currentTimeMillis();
                var result = GraphColoringMpi.graphColoringMain(size, graph, colorsGraph);
                long endTime = System.currentTimeMillis();
                System.out.println("n-coloring: " + result);
                System.out.println("Execution time: " + (endTime - startTime) + " ms");

            } catch (GraphException exception) {
                System.out.println(exception);
            }
        } else { //child process
            System.out.println("Process " + me);

            int codesNo = colorsGraph.getColorsNo();
            GraphColoringMpi.graphColoringWorker(me, size, graph, codesNo);
        }
        MPI.Finalize();
    }
}
