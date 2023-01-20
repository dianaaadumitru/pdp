package coloring;

import exception.GraphException;
import graph.ColorsGraph;
import graph.DirectedGraph;
import mpi.MPI;

import java.util.*;
import java.util.stream.Collectors;


public class GraphColoringMpi {
    public static Map<Integer, String> graphColoringMain(int mpiSize, DirectedGraph graph, ColorsGraph colors) {
        int codesNo = colors.getColorsNo();
        List<Integer> partialCodes = Collections.nCopies(graph.getNodeCount(), 0);
        List<Integer> codes = graphColoringRec(0, graph, codesNo, partialCodes, 0, mpiSize, 0);

        // no solution
        if (codes.get(0) == -1) {
            throw new GraphException("No solution found!");
        }

        //solution
        return colors.getIndexesToColors(colors.getCodesFromIndexes(codes));
    }

    private static List<Integer> graphColoringRec(int node, DirectedGraph graph, int codesNo, List<Integer> codes, int mpiMe, int mpiSize, int power) {
        int nodesNo = graph.getNodeCount();

        //invalid solution
        if (!isCodeValid(node, codes, graph)) {
            return Collections.nCopies(codesNo, -1);
        }

        //valid solution
        if (node + 1 == graph.getNodeCount()) {
            return codes;
        }

        //valid destination processes
        int coefficient = (int) Math.pow(codesNo, power);
        int code = 0;
        int destination = mpiMe + coefficient * (code + 1);

        while (code + 1 < codesNo && destination < mpiSize) {
            code++;
            destination = mpiMe + coefficient * (code + 1);
        }

        //send data to destination processes
        int nextNode = node + 1;
        int nextPower = power + 1;

        for (int currentCode = 1; currentCode < code; currentCode++) {
            destination = mpiMe + coefficient * currentCode;

            int[] data = new int[]{mpiMe, nextNode, nextPower};
            MPI.COMM_WORLD.Send(data, 0, data.length, MPI.INT, destination, 0);

            int[] nextCodes = codes.stream().mapToInt(i -> i).toArray();
            nextCodes[nextNode] = currentCode;


            MPI.COMM_WORLD.Send(nextCodes, 0, nodesNo, MPI.INT, destination, 0);
        }

        //try code 0 for next node on this process
        List<Integer> nextCodes = new ArrayList<>(codes);
        nextCodes.set(nextNode, 0);

        List<Integer> result = graphColoringRec(nextNode, graph, codesNo, nextCodes, mpiMe, mpiSize, nextPower);
        if (result.get(0) != -1) {
            return result;
        }

        //recv data from destination processes
        for (int currentCode = 1; currentCode < code; currentCode++) {
            destination = mpiMe + coefficient * currentCode;

            int[] result2 = new int[nodesNo];
            MPI.COMM_WORLD.Recv(result2, 0, nodesNo, MPI.INT, destination, MPI.ANY_TAG);

            if (result2[0] != -1) {
                return Arrays.stream(result2).boxed().collect(Collectors.toList());
            }
        }

        //try the remaining codes for next node on this process (if any)
        for (int currentCode = code; currentCode < codesNo; currentCode++) {
            nextCodes = new ArrayList<>(codes);
            nextCodes.set(nextNode, currentCode);

            result = graphColoringRec(nextNode, graph, codesNo, nextCodes, mpiMe, mpiSize, nextPower);
            if (result.get(0) != -1) {
                return result;
            }
        }

        //invalid solution (result is an array of -1)
        return result;
    }

    public static void graphColoringWorker(int mpiMe, int mpiSize, DirectedGraph graph, int codesNo) {
        int nodesNo = graph.getNodeCount();

        //recv data
        int[] data = new int[3];
        MPI.COMM_WORLD.Recv(data, 0, data.length, MPI.INT, MPI.ANY_SOURCE, MPI.ANY_TAG);

        int parent = data[0];
        int node = data[1];
        int power = data[2];

        int[] codes = new int[nodesNo];
        MPI.COMM_WORLD.Recv(codes, 0, nodesNo, MPI.INT, MPI.ANY_SOURCE, MPI.ANY_TAG);

        List<Integer> codesList = Arrays.stream(codes).boxed().collect(Collectors.toList());

        //rec. call
        List<Integer> newCodes = graphColoringRec(node, graph, codesNo, codesList, mpiMe, mpiSize, power);

        int[] newCodesInt = newCodes.stream().mapToInt(i -> i).toArray();

        //send data
        MPI.COMM_WORLD.Send(newCodesInt, 0, nodesNo, MPI.INT, parent, 0);
    }

    private static boolean isCodeValid(int node, List<Integer> codes, DirectedGraph graph) {
        for (int currentNode = 0; currentNode < node; currentNode++)
            if ((graph.isEdge(node, currentNode) || graph.isEdge(currentNode, node)) && Objects.equals(codes.get(node), codes.get(currentNode)))
                return false;

        return true;
    }
}
