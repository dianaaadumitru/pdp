import coloring.GraphColoringThreads;
import graph.ColorsGraph;
import graph.DirectedGraph;
import utils.Generator;

public class MainThreads {
    public static final int THREADS_NO = 5;

    public static void main(String[] args) {
        DirectedGraph graph = Generator.generateGraph();
        ColorsGraph colorsGraph = Generator.generateColors();

        System.out.println(graph);

        var graphColoring = GraphColoringThreads.getGraphColoring(THREADS_NO, graph, colorsGraph);
        System.out.println("n-coloring: " + graphColoring);
    }
}
