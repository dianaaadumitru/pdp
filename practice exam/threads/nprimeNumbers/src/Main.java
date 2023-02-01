import java.util.List;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) {
        List<Integer> firstPrimes = List.of(2, 3, 5, 7);
        Prime2 prime = new Prime2();
        //            System.out.println(prime.primeNumbers(firstPrimes, 50, (int) Math.sqrt(50), 50));
        System.out.println(prime.getPrimes(firstPrimes, 50, 4));

    }
}
