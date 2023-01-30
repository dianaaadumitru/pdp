package coloring;

import exception.GraphException;
import graph.ColorsGraph;
import graph.DirectedGraph;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GraphColoringThreads {

    public static Map<Integer, String> getGraphColoring(int threadsNo, DirectedGraph graph, ColorsGraph colors) throws GraphException {
        int codesNo = colors.getColorsNo();
        if (!graph.hasEdges())
            return colors.getIndexesToColors(colors.getCodesFromIndexes(Collections.nCopies(graph.getNodeCount(), 0)));

        Vector<Integer> codes = new Vector<>();

        Vector<Integer> partialCodes = new Vector<>(Collections.nCopies(graph.getNodeCount(), 0));
        Lock lock = new ReentrantLock();

        graphColoringRec(new AtomicInteger(threadsNo), 0, graph, codesNo, partialCodes, lock, codes);

        // no solution
        if (codes.isEmpty()) {
            throw new GraphException("No solution found!");
        }

        //solution
        return colors.getIndexesToColors(colors.getCodesFromIndexes(codes));
    }

    private static void graphColoringRec(AtomicInteger threadsNo, int node, DirectedGraph graph, int codesNo, Vector<Integer> partialCodes, Lock lock, Vector<Integer> codes) {
        //solution already found
        if (!codes.isEmpty())
            return;

        //solution
        if (node + 1 == graph.getNodeCount()) {
            if (isCodeValid(node, partialCodes, graph)) {
                lock.lock();

                if (codes.isEmpty()) {
                    codes.addAll(partialCodes);
                }
                lock.unlock();
            }
            return;
        }

        //candidate codes for next node
        int nextNode = node + 1;

        List<Thread> threads = new ArrayList<>();
        List<Integer> validCodes = new ArrayList<>();

        for (int code = 0; code < codesNo; code++) {
            partialCodes.set(nextNode, code);
            if (isCodeValid(nextNode, partialCodes, graph)) {
                if (threadsNo.getAndDecrement() > 0) {
                    Vector<Integer> nextPartialCodes = new Vector<>(partialCodes);
                    Thread thread = new Thread(() -> graphColoringRec(threadsNo, nextNode, graph, codesNo, nextPartialCodes, lock, codes));
                    thread.start();
                    threads.add(thread);
                } else {
                    validCodes.add(code);
                }
            }
        }

        //join threads
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }

        //sequential
        for (int code : validCodes) {
            partialCodes.set(nextNode, code);
            Vector<Integer> nextPartialCodes = new Vector<>(partialCodes);
            graphColoringRec(threadsNo, nextNode, graph, codesNo, nextPartialCodes, lock, codes);
        }
    }

    private static boolean isCodeValid(int node, Vector<Integer> codes, DirectedGraph graph) {
        for (int current = 0; current < node; current++)
            if ((graph.isEdge(node, current) || graph.isEdge(current, node)) && codes.get(node) == codes.get(current).intValue())
                return false;

        return true;
    }
}