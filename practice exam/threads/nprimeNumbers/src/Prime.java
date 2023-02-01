import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Prime {

    public List<Integer> primeNumbers(List<Integer> firstPrimes, int n, int startPosition, int endPosition) throws ExecutionException, InterruptedException {
        List<Integer> primes = new ArrayList<>();
        if (endPosition - startPosition <= 1) {
            if (isDivisible(firstPrimes, startPosition)) {
                primes.add(startPosition);
            }
            return primes;
        }

        ExecutorService executor = Executors.newFixedThreadPool(4);
        int middle = (startPosition + endPosition) / 2;
        Future<List<Integer>> l = executor.submit(() -> primeNumbers(firstPrimes, n, startPosition, middle));
        Future<List<Integer>> r = executor.submit(() -> primeNumbers(firstPrimes, n, middle, endPosition));
        var left = l.get();
        var right = r.get();
        executor.shutdown();
        executor.awaitTermination(600, TimeUnit.MILLISECONDS);
        executor.shutdownNow();
        primes.addAll(left);
        primes.addAll(right);
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
