import java.util.concurrent.*;

public class Matrix {
    public static final int N_THREADS = 4;

    public int computeSum(int[][] matrix, int startRow, int endRow) throws ExecutionException, InterruptedException {
        int sum = 0;

        if (endRow - startRow <= 1) {
            for (int i = startRow; i< endRow; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    sum += matrix[i][j];
                }
            }
        } else {
            ExecutorService service = Executors.newFixedThreadPool(N_THREADS);
            int mid = (startRow + endRow) / 2;
            Future<Integer> l = service.submit(() -> computeSum(matrix, startRow, mid));
            Future<Integer> r = service.submit(() -> computeSum(matrix, mid, endRow));
            int left = l.get();
            int right = r.get();
            service.shutdown();
            sum = left + right;
            service.awaitTermination(600, TimeUnit.MILLISECONDS);
        }

        return sum;
    }
}

