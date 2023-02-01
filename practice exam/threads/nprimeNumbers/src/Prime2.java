import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Prime2 {

    public List<Integer> getPrimes(List<Integer> firstPrimes, int n, int noThreads) {
        List<Integer> primes = new ArrayList<>();

        ExecutorService executor = Executors.newFixedThreadPool(noThreads);
        int step = (int) (n - Math.sqrt(n)) / noThreads;

        int i;
        for (i = 0; i < n - (int) (Math.sqrt(n)); i += step) {
            int end = i + step;
            if (end > n - (int) (Math.sqrt(n))) {
                end = n - (int) (Math.sqrt(n));
            }
            int finalI = i;
            int finalEnd = end;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    for (int j = finalI; j < finalEnd; j++) {
                        if (isDivisible(firstPrimes, (int) (j + Math.sqrt(n)))) {
                            primes.add(j + (int) (Math.sqrt(n)));
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

        return primes;
    }

    private boolean isDivisible(List<Integer> firstPrimes, int n) {
        for (int prime: firstPrimes) {
            if (n % prime == 0)
                return false;
        }
        return true;
    }
}
