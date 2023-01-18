package graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DirectedGraph {
    private final int nodeCount;
    private final List<List<Integer>> container;
    private final List<Integer> nodes;

    public DirectedGraph(int nodeCount) {
        this.nodeCount = nodeCount;
        this.container = new ArrayList<>(nodeCount);
        this.nodes = new ArrayList<>();

        for (int i = 0; i < nodeCount; i++) {
            this.container.add(new ArrayList<>());
            this.nodes.add(i);
        }
    }

    public static DirectedGraph generateRandomGraph(int size, int edgeNumber) {
        DirectedGraph graph = new DirectedGraph(size);
        Random random = new Random();

        for (int i = 0; i < edgeNumber; i++) {
            int nodeA = random.nextInt(size - 1);
            int nodeB = random.nextInt(size - 1);

            if (nodeA == nodeB || graph.container.get(nodeA).contains(nodeB)) {
                i--;
            } else {
                graph.addEdge(nodeA, nodeB);
            }

        }

        System.out.println("nodes: " + graph.nodes);
        System.out.println("container: " + graph.container);
        return graph;
    }

    public int getNodeCount() {
        return nodeCount;
    }

    public void addEdge(int nodeA, int nodeB) {
        this.container.get(nodeA).add(nodeB);
    }

    public List<Integer> neighboursOf(int node) {
        return this.container.get(node);
    }

    public List<Integer> getNodes() {
        return nodes;
    }

    public int size() {
        return this.container.size();
    }

    public boolean isEdge(int nodeA, int nodeB) {
        return container.get(nodeA).contains(nodeB);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nodes.size(); i++) {
            sb.append(nodes.get(i)).append(": ").append(container.get(i)).append("\n");
        }
        return sb.toString();
    }

}