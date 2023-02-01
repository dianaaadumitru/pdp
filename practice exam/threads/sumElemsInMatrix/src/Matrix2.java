import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Matrix2 {
    public int sumMatrix(int noRows, int noColumns, int[][] matrix, int noThreads) {
        final int[] sum = {0};

        int step = noRows / noThreads;
        if (step == 0) {
            step = 1;
        }
        ExecutorService service = Executors.newFixedThreadPool(noThreads);
        Future<Integer>[] futures = new Future[noThreads];

        for (int i = 0; i < noRows; i += step) {
            int end = i + step;
            int finalI = i;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    for (int row = finalI; row < end; row++) {
                        for (int col = 0; col < noColumns; col ++) {
                            sum[0] += matrix[row][col];
                        }
                    }
                }
            };
            service.execute(runnable);
        }
        service.shutdown();
        try {
            if (!service.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                service.shutdownNow();
            }
        } catch (InterruptedException ex) {
            service.shutdownNow();
        }

        return sum[0];
    }
}
