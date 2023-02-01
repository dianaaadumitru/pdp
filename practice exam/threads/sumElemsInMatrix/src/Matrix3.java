import java.util.concurrent.*;

public class Matrix3 {
    public int sumMatrix(int noRows, int noColumns, int[][] matrix, int noThreads) throws ExecutionException, InterruptedException {
//        final int[] sum = {0};

        int step = noRows / noThreads;
        if (step == 0) {
            step = 1;
        }
        ExecutorService service = Executors.newFixedThreadPool(noThreads);
        Future<Integer>[] futures = new Future[noThreads];
        int index = 0;
        for (int i = 0; i < noRows; i += step) {
            int end = i + step;
            int finalI = i;
            futures[index] = service.submit(() -> {
                int sum = 0;
                for (int row = finalI; row < end; row++) {
                    for (int col = 0; col < noColumns; col++) {
                        sum += matrix[row][col];
                    }
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
                newFutures[i] = service.submit(() ->
                        finalFutures[j].get() + finalFutures[j + 1].get()
                );
            }
            futures = newFutures;
        }

        service.shutdown();
        try {
            if (!service.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                service.shutdownNow();
            }
        } catch (InterruptedException ex) {
            service.shutdownNow();
        }

        return futures[0].get();
    }
}
