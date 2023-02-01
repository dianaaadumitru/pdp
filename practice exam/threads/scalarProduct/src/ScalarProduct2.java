import java.util.List;
import java.util.concurrent.*;

public class ScalarProduct2 {
    public int computeProduct(List<Integer> a, List<Integer> b, int noThreads) throws ExecutionException, InterruptedException {
//        final int[] sum = {0};

        int step = a.size() / noThreads;
        if (step == 0) {
            step = 1;
        }

        Future<Integer>[] futures = new Future[noThreads];
        int index = 0;
        ExecutorService executor = Executors.newFixedThreadPool(noThreads);

        for (int i = 0; i < noThreads; i += step) {
            int start = i * step;
            int end = start + step;
            if (index == noThreads - 1) {
                end += a.size() % noThreads;
            }

            int finalEnd = end;
            futures[index] = executor.submit(() -> {
                int sum = 0;
                for (int j = start; j < finalEnd; j++) {
                    sum += (a.get(j) * b.get(j));
                }
                return sum;
            });
            index++;
        }

        while (futures.length > 1) {
            int newSize = (futures.length + 1) / 2;
            Future<Integer>[] newFutures = new Future[newSize];

            for (int i = 0; i < newSize; i++) {
                int j = i * 2;
                Future<Integer>[] finalFutures = futures;
                newFutures[i] = executor.submit(() -> finalFutures[j].get() + finalFutures[j + 1].get());
            }
            futures = newFutures;
        }
        executor.shutdown();
        try {
            if (!executor.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException ex) {
            executor.shutdownNow();
        }

        return futures[0].get();
    }
}
