import graph.ColorsGraph;
import graph.DirectedGraph;
import graph.GraphColoring;

public class Main {
    public static final int THREADS_NO = 5;


    public static void main(String[] args) {
//        DirectedGraph graph = DirectedGraph.generateRandomGraph(5, 7);
        DirectedGraph graph = new DirectedGraph(5);
        graph.addEdge(0, 1);
        graph.addEdge(0, 4);
        graph.addEdge(3, 1);
        graph.addEdge(2, 4);
        graph.addEdge(1, 2);
        graph.addEdge(1, 0);
        graph.addEdge(4, 3);

        ColorsGraph colorsGraph = new ColorsGraph(4);
        colorsGraph.addColor(89, "red");
        colorsGraph.addColor(11, "purple");
        colorsGraph.addColor(24, "green");
        colorsGraph.addColor(34, "black");

        System.out.println("graph: " + graph);

        var graphColoring = GraphColoring.getGraphColoring(THREADS_NO, graph, colorsGraph);
        System.out.println("n-coloring: " + graphColoring);
    }
}
