import java.math.BigInteger;
import java.util.concurrent.*;

public class Karatsuba {
    private final int NR_THREADS = 4;


    public BigInteger productParallelized(BigInteger x, BigInteger y) throws ExecutionException, InterruptedException {
        if (x.toString().length() < 3 || y.toString().length() < 3)
            return x.multiply(y);

        int n = Math.max(x.toString().length(), y.toString().length());
        int len = (n / 2) + (n % 2);

        // Multiplier
        long maxLenTen = (long) Math.pow(10, len);
        BigInteger a = x.divide(new BigInteger(String.valueOf(maxLenTen)));
        BigInteger b = x.mod(new BigInteger(String.valueOf(maxLenTen)));
        BigInteger c = y.divide(new BigInteger(String.valueOf(maxLenTen)));
        BigInteger d = y.mod(new BigInteger(String.valueOf(maxLenTen)));

        ExecutorService executor = Executors.newFixedThreadPool(NR_THREADS);

        Future<BigInteger> f1 = executor.submit(() -> productParallelized(a, c));
        Future<BigInteger> f2 = executor.submit(() -> productParallelized(b, d));
        Future<BigInteger> f3 = executor.submit(() -> productParallelized(a.add(b), c.add(d)));

        executor.shutdown();

        BigInteger p1 = f1.get();
        BigInteger p2 = f2.get();
        BigInteger p3 = f3.get();

        executor.awaitTermination(60, TimeUnit.SECONDS);

        BigInteger q1 = addZeros(p1, 2 * len);
        BigInteger q2 = addZeros(p3.subtract(p2).subtract(p1), len);

        return p2.add(q1.add(q2));
    }

    private BigInteger addZeros(BigInteger x, int noZeros) {
        while (noZeros > 0) {
            x = x.multiply(new BigInteger("10"));
            noZeros--;
        }
        return x;
    }
}


