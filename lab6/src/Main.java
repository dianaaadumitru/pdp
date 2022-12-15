import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    private static final int NR_THREADS = 10;

    public static void main(String[] args) throws InterruptedException {
        DirectedGraph graph = DirectedGraph.generateRandomHamiltonian(10);
        long startTime = System.currentTimeMillis();
        findCycle(graph, NR_THREADS);
        long endTime = System.currentTimeMillis();
        System.out.println(graph);
        System.out.println("Execution time: " + (endTime - startTime) + " ms");
    }

    public static void findCycle(DirectedGraph graph, int nrThreads) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(nrThreads);

        List<Integer> result = new ArrayList<>(graph.size());

        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        for (int i = 0; i < graph.size(); i++) { //check from each node
            pool.submit(new Task(graph, i, result, atomicBoolean));
        }

        pool.shutdown();

        pool.awaitTermination(10, TimeUnit.SECONDS);
    }

}