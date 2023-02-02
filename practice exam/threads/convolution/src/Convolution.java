import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Convolution {
    public List<Integer> computeConvolution(List<Integer> a, List<Integer> b, int noThreads) {
        int step = a.size() / noThreads;
        List<Integer> result = new ArrayList<>();
        if (step == 0)
            step = 1;

        for (int i = 0; i < a.size(); i += step) {
            int start = i;
            int end = start + step;

            ExecutorService executorService = Executors.newFixedThreadPool(noThreads);
            int finalEnd = end;
            executorService.execute(() -> {
                System.out.println(start + " " + finalEnd);
                for (int j = start; j < finalEnd; j++) {
                    int sum = 0;
                    for (int k = 0; k < a.size(); k++) {
                        sum += a.get(j) * b.get((j - k + a.size()) % a.size());
                    }
                    result.add(sum);
                }
            });

            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException ex) {
                executorService.shutdownNow();
            }
        }
        return result;
    }
}
