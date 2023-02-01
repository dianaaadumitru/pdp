import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Matrix {
    public int[][] computeProduct(int n1, int m1, int[][] a, int n2, int m2, int[][] b, int noThreads) throws Exception {
        if (n1 != m2) {
            throw new Exception("not possible!");
        }

        int[][] c = new int[n1][m2];
        ExecutorService executor = Executors.newFixedThreadPool(noThreads);

        int step = n1 / noThreads;
        for (int i = 0; i < n1; i +=step) {
            int end = i + step;
            int finalI = i;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    for (int rowa = finalI; rowa < end; rowa++) {
                        for (int cola = 0; cola < m2; cola ++) {
                            for (int rowb = 0; rowb < n2; rowb++) {
                                c[rowa][cola] += a[rowa][rowb] * b[rowb][cola];
                            }
                        }
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
        return c;
    }
}
