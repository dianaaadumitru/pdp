import coloring.GraphColoringMpi;
import exception.GraphException;
import graph.ColorsGraph;
import graph.DirectedGraph;
import mpi.MPI;
import utils.Generator;

public class MainMPI {
    public static final int THREADS_NO = 5;

    public static void main(String[] args) {
        DirectedGraph graph = Generator.generateGraph();
        ColorsGraph colorsGraph = Generator.generateColors();
//
//        System.out.println("graph: " + graph);



        MPI.Init(args);

        int me = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        if (me == 0) { //main process
            System.out.println("MAIN");
            System.out.println("graph: " + graph);

            try {
                System.out.println("n-coloring: " + GraphColoringMpi.graphColoringMain(size, graph, colorsGraph));
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
