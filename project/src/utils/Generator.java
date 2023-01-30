package utils;

import graph.ColorsGraph;
import graph.DirectedGraph;

public class Generator {
    public static ColorsGraph generateColors() {
        ColorsGraph colorsGraph = new ColorsGraph(3);
        colorsGraph.addColor(0, "red");
        colorsGraph.addColor(1, "purple");
        colorsGraph.addColor(2, "green");
//        colorsGraph.addColor(3, "black");
//        colorsGraph.addColor(4, "white");
        return colorsGraph;
    }

    public static DirectedGraph generateGraph() {
        DirectedGraph graph = new DirectedGraph(5);
//        graph.addEdge(0, 1);
//        graph.addEdge(0, 4);
//        graph.addEdge(3, 1);
//        graph.addEdge(2, 4);
//        graph.addEdge(1, 2);
//        graph.addEdge(1, 0);
//        graph.addEdge(4, 3);
//        graph.addEdge(4, 1);


//        graph.addEdge(0, 1);
//        graph.addEdge(0, 2);
//        graph.addEdge(0, 3);
//        graph.addEdge(0, 4);
//        graph.addEdge(1, 0);
//        graph.addEdge(1, 2);
//        graph.addEdge(1, 3);
//        graph.addEdge(1, 4);
//        graph.addEdge(2, 0);
//        graph.addEdge(2, 1);
//        graph.addEdge(2, 3);
//        graph.addEdge(2, 4);
//        graph.addEdge(3, 0);
//        graph.addEdge(3, 1);
//        graph.addEdge(3, 2);
//        graph.addEdge(3, 4);
//        graph.addEdge(4, 0);
//        graph.addEdge(4, 1);
//        graph.addEdge(4, 2);
//        graph.addEdge(4, 3);

        graph.addEdge(0,1);

        graph.addEdge(1,2);
        graph.addEdge(2,3);
        graph.addEdge(3,4);
//        graph.addEdge(4,1);

        return graph;
    }
}
