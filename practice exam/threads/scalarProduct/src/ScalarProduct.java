import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ScalarProduct {
    public int computeProduct(List<Integer> a, List<Integer> b, int noThreads) {
        final int[] sum = {0};

        int step = a.size() / noThreads;
        if (step == 0) {
            step = 1;
        }

        ExecutorService executor = Executors.newFixedThreadPool(noThreads);

        for (int i = 0; i < noThreads; i += step) {
            int start = i * step;
            int end = start + step;
            if (i == noThreads - 1) {
                end += a.size() % noThreads;
            }

            int finalEnd = end;
//            Runnable runnable = new Runnable() {
//                @Override
//                public void run() {
//                    for (int j = start; j < finalEnd; j++) {
//                        sum[0] += (a.get(j) * b.get(j));
//                    }
//                }
//            };
            executor.execute(() -> {
                for (int j = start; j < finalEnd; j++) {
                    sum[0] += (a.get(j) * b.get(j));
                }
            });
        }
        executor.shutdown();
        try {
            if (!executor.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException ex) {
            executor.shutdownNow();
        }

        return sum[0];
    }
}
