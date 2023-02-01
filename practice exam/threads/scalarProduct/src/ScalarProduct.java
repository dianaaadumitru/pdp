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

        for (final int[] i = {0}; i[0] < a.size(); i[0] += step) {
            int end = i[0] + step;

            int finalI = i[0];
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    for (int j = finalI; j < end; j++) {
                        sum[0] += (a.get(j) * b.get(j));
                    }
                }
            };
            executor.execute(runnable);
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
